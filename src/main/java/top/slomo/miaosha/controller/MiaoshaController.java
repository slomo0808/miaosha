package top.slomo.miaosha.controller;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.slomo.miaosha.access.AccessLimit;
import top.slomo.miaosha.entity.MiaoshaOrder;
import top.slomo.miaosha.entity.MiaoshaUser;
import top.slomo.miaosha.rabbitmq.MiaoshaMessage;
import top.slomo.miaosha.rabbitmq.MqSender;
import top.slomo.miaosha.redis.AccessKeyPrefix;
import top.slomo.miaosha.redis.GoodsKeyPrefix;
import top.slomo.miaosha.redis.MiaoshaKeyPrefix;
import top.slomo.miaosha.redis.RedisService;
import top.slomo.miaosha.result.CodeMsg;
import top.slomo.miaosha.result.Result;
import top.slomo.miaosha.service.*;
import top.slomo.miaosha.util.MD5Util;
import top.slomo.miaosha.util.UUIDUtil;
import top.slomo.miaosha.vo.GoodsVo;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description: .
 * @date: 2021-04-07
 * @author: YuBo
 */
@RestController
@RequestMapping("miaosha")
public class MiaoshaController implements InitializingBean {
    private static Logger logger = LoggerFactory.getLogger(MiaoshaController.class);

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderInfoService orderInfoService;

    @Autowired
    MiaoshaService miaoshaService;

    @Autowired
    RedisService redisService;

    @Autowired
    MqSender sender;

    // 内存标记判断
    private final ConcurrentHashMap<Long, Boolean> overMap = new ConcurrentHashMap<>();
    private static final int RATE_LIMIT = 5;

    /**
     * 837 QPS, 5000 threads, 10 loop, ERR 0%
     *
     * 1500 QPS
     *
     */
    @PostMapping("{path}/do_miaosha")
    public Result<Integer> doMiaosha(
            @RequestParam Long goodsId, @RequestParam Long miaoshaGoodsId,
            @PathVariable String path, MiaoshaUser user) {
        if (Objects.isNull(user)) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        // path验证
        String localPath = redisService.get(MiaoshaKeyPrefix.MIAOSHA_PATH, user.getId() + "_" + miaoshaGoodsId, String.class);
        logger.info("localPath: {} \n path: {}", localPath, path);
        if (!StringUtils.equals(localPath, path)) {
            return Result.error(CodeMsg.MIAOSHA_FAILED);
        }

        // 内存标记判断
        if (overMap.get(miaoshaGoodsId)) {
            return Result.error(CodeMsg.MIAOSHA_FAILED);
        }
        // 判断商品库存
        final long stockCount = redisService.hdecrByOne(GoodsKeyPrefix.GOODS_STOCK_COUNT, "", String.valueOf(miaoshaGoodsId));

        if (stockCount < 0) {
            overMap.put(miaoshaGoodsId, true);
            return Result.error(CodeMsg.MIAOSHA_FAILED);
        }

        // 有库存，判断是否已经秒杀到了
        MiaoshaOrder miaoshaOrder = orderInfoService.getMiaoshaOrderByUserIdAndGoodsId(user.getId(), goodsId);
        if (Objects.nonNull(miaoshaOrder)) {
            return Result.error(CodeMsg.ALREADY_MIAOSHA);
        }

        // mq发送秒杀消息
        final MiaoshaMessage mm = new MiaoshaMessage();
        mm.setMiaoshaUser(user);
        mm.setGoodsId(goodsId);
        mm.setMiaoshaGoodsId(miaoshaGoodsId);
        sender.sendMiaoshaMessage(mm);

        return Result.success(0);
    }

    @GetMapping("result")
    public Result<Long> result(@RequestParam Long goodsId, @RequestParam Long miaoshaGoodsId, MiaoshaUser user) {
        if (Objects.isNull(user)) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        Long result = miaoshaService.getMiaoshaResult(user.getId(), miaoshaGoodsId, goodsId);
        return Result.success(result);
    }

    @AccessLimit(seconds = 5, maxCount = 5, requireLogin = true)
    @GetMapping("path")
    public Result<String> getMiaoshaPath(@RequestParam Long miaoshaGoodsId, @RequestParam Integer captchaCode,
                                         MiaoshaUser user) {
        if (Objects.isNull(user)) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        // 验证captcha
        if (!miaoshaService.checkCaptcha(miaoshaGoodsId, captchaCode, user)) {
            return Result.error(CodeMsg.WRONG_CAPTCHA);
        }
        String path = MD5Util.md5(UUIDUtil.uuid() + "slomo");
        redisService.set(MiaoshaKeyPrefix.MIAOSHA_PATH, user.getId() + "_" + miaoshaGoodsId, path);
        return Result.success(path);
    }

    @GetMapping("captcha")
    public Object captcha(@RequestParam Long miaoshaGoodsId, MiaoshaUser user, HttpServletResponse response) throws IOException {
        if (Objects.isNull(user)) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        BufferedImage image = miaoshaService.createCaptcha(miaoshaGoodsId, user);
        final ServletOutputStream out = response.getOutputStream();
        ImageIO.write(image, "JPEG", out);
        out.flush();
        out.close();
        return null;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // 查询数据库
        final List<GoodsVo> list = goodsService.selectGoodsVoList();
        Map<String, String> hash = new HashMap<>();
        for (GoodsVo vo : list) {
            hash.put(String.valueOf(vo.getMiaoshaGoodsId()), String.valueOf(vo.getStockCount()));
            overMap.put(vo.getMiaoshaGoodsId(), false);
        }
        // hmset
        redisService.hmset(GoodsKeyPrefix.GOODS_STOCK_COUNT, "", hash);
    }
}
