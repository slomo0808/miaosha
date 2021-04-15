package top.slomo.miaosha.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import top.slomo.miaosha.entity.MiaoshaOrder;
import top.slomo.miaosha.entity.MiaoshaUser;
import top.slomo.miaosha.entity.OrderInfo;
import top.slomo.miaosha.result.CodeMsg;
import top.slomo.miaosha.result.Result;
import top.slomo.miaosha.service.*;
import top.slomo.miaosha.vo.GoodsVo;

import java.util.Objects;

/**
 * @description: .
 * @date: 2021-04-07
 * @author: YuBo
 */
@Controller
@RequestMapping("miaosha")
public class MiaoshaController {
    private static Logger logger = LoggerFactory.getLogger(MiaoshaController.class);

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderInfoService orderInfoService;

    @Autowired
    MiaoshaService miaoshaService;

    /**
     * QPS 1,435.379  5000*10  ERR 6.18%
     *
     */
    @PostMapping("do_miaosha")
    @ResponseBody
    public Result<OrderInfo> doMiaosha(MiaoshaUser user, Model model, @RequestParam Long miaoshaGoodsId) {
        if (Objects.isNull(user)) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        // 判断商品库存
        GoodsVo goods = goodsService.getGoodsVoByMiaoshaGoodsId(miaoshaGoodsId);

        if (goods.getStockCount() <= 0) {
            model.addAttribute("errMsg", CodeMsg.STOCK_NOT_ENOUGH.getMsg());
            return Result.error(CodeMsg.MIAOSHA_FAILED);
        }
        // 有库存，判断是否已经秒杀到了
        MiaoshaOrder miaoshaOrder = orderInfoService.getMiaoshaOrderByUserIdAndGoodsId(user.getId(), goods.getId());
        if (!Objects.isNull(miaoshaOrder)) {
            model.addAttribute("errMsg", CodeMsg.ALREADY_MIAOSHA.getMsg());
            return Result.error(CodeMsg.ALREADY_MIAOSHA);
        }

        // 减库存 下订单 写入秒杀订单
        OrderInfo orderInfo = miaoshaService.miaosha(user, goods);

        return Result.success(orderInfo);
    }
}
