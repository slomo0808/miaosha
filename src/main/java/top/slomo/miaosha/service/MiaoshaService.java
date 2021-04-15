package top.slomo.miaosha.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.slomo.miaosha.dao.GoodsDao;
import top.slomo.miaosha.entity.Goods;
import top.slomo.miaosha.entity.MiaoshaUser;
import top.slomo.miaosha.entity.OrderInfo;
import top.slomo.miaosha.vo.GoodsVo;

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

    @Transactional(rollbackFor = Exception.class)
    public OrderInfo miaosha(MiaoshaUser user, GoodsVo goods) {
        // 减库存
        goodsService.reduceStock(goods);
        // 下订单
        // 写入秒杀订单
        return orderInfoService.createOrder(user, goods);
    }
}
