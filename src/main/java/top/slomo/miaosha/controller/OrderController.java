package top.slomo.miaosha.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.slomo.miaosha.entity.MiaoshaUser;
import top.slomo.miaosha.entity.OrderInfo;
import top.slomo.miaosha.redis.RedisService;
import top.slomo.miaosha.result.CodeMsg;
import top.slomo.miaosha.result.Result;
import top.slomo.miaosha.service.GoodsService;
import top.slomo.miaosha.service.OrderInfoService;
import top.slomo.miaosha.vo.GoodsVo;
import top.slomo.miaosha.vo.OrderVo;

import java.util.Objects;

/**
 * @description: .
 * @date: 2021-04-15
 * @author: YuBo
 */
@RestController
@RequestMapping("order")
public class OrderController {
    @Autowired
    OrderInfoService orderService;

    @Autowired
    GoodsService goodsService;

    @Autowired
    RedisService redisService;

    @GetMapping("/{id}")
    public Result<OrderVo> getById(@PathVariable Long id, MiaoshaUser user) {
        if (Objects.isNull(user)) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        OrderInfo order = orderService.getById(id);
        if (!user.getId().equals(order.getUserId())) {
            return Result.error(CodeMsg.NOT_ALLOWED_ORDER);
        }
        GoodsVo goods = goodsService.getGoodsVoById(order.getGoodsId());

        OrderVo orderVo = new OrderVo();
        orderVo.setGoods(goods);
        orderVo.setUser(user);
        BeanUtils.copyProperties(order, orderVo);

        return Result.success(orderVo);
    }
}
