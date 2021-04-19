package top.slomo.miaosha.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.slomo.miaosha.dao.GoodsDao;
import top.slomo.miaosha.entity.Goods;
import top.slomo.miaosha.entity.MiaoshaOrder;
import top.slomo.miaosha.entity.MiaoshaUser;
import top.slomo.miaosha.entity.OrderInfo;
import top.slomo.miaosha.redis.GoodsKeyPrefix;
import top.slomo.miaosha.redis.MiaoshaKeyPrefix;
import top.slomo.miaosha.redis.RedisService;
import top.slomo.miaosha.vo.GoodsVo;

import javax.annotation.PostConstruct;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @description: .
 * @date: 2021-04-13
 * @author: YuBo
 */
@Service
public class MiaoshaService {
    private static final Logger log = LoggerFactory.getLogger(MiaoshaService.class);

    @Autowired
    GoodsService goodsService;
    @Autowired
    OrderInfoService orderInfoService;
    @Autowired
    RedisService redisService;

    @Transactional(rollbackFor = Exception.class)
    public OrderInfo miaosha(MiaoshaUser user, GoodsVo goods) {
        // 减库存
        if (!goodsService.reduceStock(goods)) {
            setGoodsOver(goods.getMiaoshaGoodsId());
            return null;
        }
        // 下订单
        // 写入秒杀订单
        return orderInfoService.createOrder(user, goods);
    }

    public long getMiaoshaResult(Long userId, Long miaoshaGoodsId, Long goodsId) {
        final MiaoshaOrder order = orderInfoService.getMiaoshaOrderByUserIdAndGoodsId(userId, goodsId);
        if (Objects.nonNull(order)) {
            return order.getOrderId();
        }
        return getGoodsOver(miaoshaGoodsId) ? -1 : 0;
    }

    private void setGoodsOver(Long miaoshaGoodsId) {
        redisService.set(MiaoshaKeyPrefix.IS_GOODS_OVER, String.valueOf(miaoshaGoodsId), true);
    }

    private boolean getGoodsOver(Long miaoshaGoodsId) {
        return redisService.exists(MiaoshaKeyPrefix.IS_GOODS_OVER, String.valueOf(miaoshaGoodsId));
    }

    /**
     * 生成验证码
     *
     * @param miaoshaGoodsId
     * @param user
     * @return
     */
    public BufferedImage createCaptcha(Long miaoshaGoodsId, MiaoshaUser user) {
        final int width = 80;
        final int height = 32;
        final BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        // 画笔
        final Graphics graphics = image.getGraphics();
        // 背景
        graphics.setColor(new Color(0xDCDCDC));
        graphics.fillRect(0, 0, width, height);
        // 边框
        graphics.setColor(Color.BLACK);
        graphics.drawRect(0, 0, width - 1, height - 1);
        // 干扰线
        final ThreadLocalRandom random = ThreadLocalRandom.current();
        graphics.setColor(Color.PINK);
        for (int i = 0; i < 50; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            graphics.drawOval(x, y, 0, 0);
        }
        // 生成code并且存到redis中
        String[] strs = generateCode(random);
        graphics.setColor(new Color(0, 100, 0));
        graphics.setFont(new Font("Candra", Font.BOLD, 24));
        graphics.drawString(strs[0], 8, 24);
        graphics.dispose();
        redisService.set(
                MiaoshaKeyPrefix.CAPTCHA_CODE,
                user.getId() + "_" + miaoshaGoodsId,
                Integer.parseInt(strs[1]));

        // 返回图片
        return image;
    }

    private static char[] operators = new char[]{'+', '-', '*'};

    /**
     * 只做 + - *
     *
     * @param random
     * @return
     */
    private String[] generateCode(ThreadLocalRandom random) {
        final int num1 = random.nextInt(10) + 1;
        final int num2 = random.nextInt(10) + 1;
        final int num3 = random.nextInt(10) + 1;

        final char op1 = operators[random.nextInt(3)];
        final char op2 = operators[random.nextInt(3)];

        String code = new StringBuilder()
                .append(num1)
                .append(op1)
                .append(num2)
                .append(op2)
                .append(num3)
                .toString();

        int res = 0;
        if (op2 == operators[2]) {
            res = num2 * num3;
            res = calc(num1, res, op1);
        } else {
            res = calc(num1, num2, op1);
            res = calc(res, num3, op2);
        }
        return new String[]{code, String.valueOf(res)};
    }

    private int calc(int x, int y, char op) {
        int ret = 0;
        switch (op) {
            case '+':
                ret = x + y;
                return ret;
            case '-':
                ret = x - y;
                return ret;
            case '*':
                ret = x * y;
                return ret;
            default:
        }
        return ret;
    }

    public boolean checkCaptcha(Long miaoshaGoodsId, Integer captchaCode, MiaoshaUser user) {
        final Integer localCode = redisService.get(MiaoshaKeyPrefix.CAPTCHA_CODE, user.getId() + "_" + miaoshaGoodsId, int.class);
        redisService.del(MiaoshaKeyPrefix.CAPTCHA_CODE, user.getId() + "_" + miaoshaGoodsId);
        log.info("remoteCode: {}, localCode: {}", captchaCode, localCode);
        return Objects.equals(localCode, captchaCode);
    }
}
