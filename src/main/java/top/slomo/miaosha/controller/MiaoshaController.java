package top.slomo.miaosha.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.slomo.miaosha.entity.MiaoshaUser;
import top.slomo.miaosha.rabbitmq.MiaoshaMessage;
import top.slomo.miaosha.rabbitmq.MqSender;
import top.slomo.miaosha.redis.GoodsKeyPrefix;
import top.slomo.miaosha.redis.RedisService;
import top.slomo.miaosha.result.CodeMsg;
import top.slomo.miaosha.result.Result;
import top.slomo.miaosha.service.*;
import top.slomo.miaosha.vo.GoodsVo;

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

    /**
     * 837 QPS, 5000 threads, 10 loop, ERR 0%
     *
     * 1500 QPS
     *
     */
    @PostMapping("do_miaosha")
    public Result<Integer> doMiaosha(@RequestParam Long goodsId, @RequestParam Long miaoshaGoodsId, MiaoshaUser user) {
        if (Objects.isNull(user)) {
            return Result.error(CodeMsg.SESSION_ERROR);
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

        // mq发送秒杀消息
        final MiaoshaMessage mm = new MiaoshaMessage();
        mm.setMiaoshaUser(user);
        mm.setGoodsId(goodsId);
        mm.setMiaoshaGoodsId(miaoshaGoodsId);
        sender.sendMiaoshaMessage(mm);

        return Result.success(0);
        /*
        // 判断商品库存
        GoodsVo goods = goodsService.getGoodsVoByMiaoshaGoodsId(miaoshaGoodsId);
        if (goods.getStockCount() <= 0) {
            return Result.error(CodeMsg.MIAOSHA_FAILED);
        }
        // 有库存，判断是否已经秒杀到了
        MiaoshaOrder miaoshaOrder = orderInfoService.getMiaoshaOrderByUserIdAndGoodsId(user.getId(), goodsId);
        if (Objects.nonNull(miaoshaOrder)) {
            return Result.error(CodeMsg.ALREADY_MIAOSHA);
        }
        // 减库存 下订单 写入秒杀订单
        OrderInfo orderInfo = miaoshaService.miaosha(user, goods);
        return Result.success(orderInfo);
        */
    }

    @GetMapping("result")
    public Result<Long> result(@RequestParam Long goodsId, @RequestParam Long miaoshaGoodsId, MiaoshaUser user) {
        if (Objects.isNull(user)) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        Long result = miaoshaService.getMiaoshaResult(user.getId(), miaoshaGoodsId, goodsId);
        return Result.success(result);
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
