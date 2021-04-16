package top.slomo.miaosha.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.slomo.miaosha.dao.OrderInfoDao;
import top.slomo.miaosha.entity.MiaoshaOrder;
import top.slomo.miaosha.entity.MiaoshaUser;
import top.slomo.miaosha.entity.OrderInfo;
import top.slomo.miaosha.redis.OrderKeyPrefix;
import top.slomo.miaosha.redis.RedisService;
import top.slomo.miaosha.vo.GoodsVo;

import java.util.Date;

/**
 * @description: .
 * @date: 2021-04-13
 * @author: YuBo
 */
@Service
public class OrderInfoService {
    @Autowired
    OrderInfoDao orderInfoDao;
    @Autowired
    RedisService redisService;

    public MiaoshaOrder getMiaoshaOrderByUserIdAndGoodsId(Long userId, Long goodsId) {
        // return orderInfoDao.getMiaoshaOrderByUserIdAndGoodsId(userId, goodsId);
        return redisService.get(OrderKeyPrefix.GET_BY_UID_GID, "" + userId + "-" +goodsId, MiaoshaOrder.class);
    }

    @Transactional(rollbackFor = Exception.class)
    public OrderInfo createOrder(MiaoshaUser user, GoodsVo goods) {
        OrderInfo order = new OrderInfo();
        order.setUserId(user.getId());
        order.setGoodsId(goods.getId());
        order.setGoodsName(goods.getGoodsName());
        order.setGoodsPrice(goods.getMiaoshaPrice());
        order.setOrderChannel(1);
        order.setGoodsCount(1);
        order.setStatus(0);
        order.setCreateDate(new Date());
        orderInfoDao.insertOrderInfo(order);


        MiaoshaOrder miaoshaOrder = new MiaoshaOrder();
        miaoshaOrder.setUserId(user.getId());
        miaoshaOrder.setGoodsId(goods.getId());
        miaoshaOrder.setOrderId(order.getId());

        redisService.set(OrderKeyPrefix.GET_BY_UID_GID, "" + user.getId() + "-" + goods.getId(), miaoshaOrder);

        orderInfoDao.insertMiaoshaOrder(miaoshaOrder);

        return order;
    }

    public OrderInfo getById(Long id) {
        return orderInfoDao.getById(id);
    }
}
