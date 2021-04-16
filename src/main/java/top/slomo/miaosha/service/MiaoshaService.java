package top.slomo.miaosha.service;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @description: .
 * @date: 2021-04-13
 * @author: YuBo
 */
@Service
public class MiaoshaService {

    @Autowired
    GoodsService goodsService;
    @Autowired
    OrderInfoService orderInfoService;
    @Autowired
    RedisService redisService;

    @Transactional(rollbackFor = Exception.class)
    public OrderInfo miaosha(MiaoshaUser user, GoodsVo goods) {
        // 减库存
        if(!goodsService.reduceStock(goods)) {
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

}
