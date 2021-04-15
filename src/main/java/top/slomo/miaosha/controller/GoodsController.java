package top.slomo.miaosha.controller;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.context.Context;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.context.webflux.SpringWebFluxContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import top.slomo.miaosha.entity.MiaoshaUser;
import top.slomo.miaosha.redis.GoodsKeyPrefix;
import top.slomo.miaosha.redis.RedisService;
import top.slomo.miaosha.result.Result;
import top.slomo.miaosha.service.GoodsService;
import top.slomo.miaosha.service.MiaoshaUserService;
import top.slomo.miaosha.vo.GoodsDetailVo;
import top.slomo.miaosha.vo.GoodsVo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @description: .
 * @date: 2021-04-07
 * @author: YuBo
 */
@Controller
@RequestMapping("goods")
public class GoodsController {
    private static Logger logger = LoggerFactory.getLogger(GoodsController.class);

    @Autowired
    RedisService redisService;
    @Autowired
    GoodsService goodsService;
    @Autowired
    MiaoshaUserService miaoshaUserService;
    @Autowired
    ThymeleafViewResolver thymeleafViewResolver;

    @RequestMapping(value = "to_list", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String toList(
            MiaoshaUser miaoshaUser,
            Model model,
            HttpServletRequest request,
            HttpServletResponse response) {


        // return "goods_list";

        // 取缓存
        String html = redisService.get(GoodsKeyPrefix.GET_GOODS_LIST, "", String.class);
        if (StringUtils.isNotBlank(html)) {
            return html;
        }

        // 手动渲染
        List<GoodsVo> goodsList = goodsService.selectGoodsVoList();

        model.addAttribute("user", miaoshaUser);
        model.addAttribute("goodsList", goodsList);

        WebContext ctx = new WebContext(request, response, request.getServletContext(), request.getLocale(), model.asMap());

        html = thymeleafViewResolver
                .getTemplateEngine()
                .process("goods_list", ctx);

        if (StringUtils.isNotBlank(html)) {
            redisService.set(GoodsKeyPrefix.GET_GOODS_LIST, "", html);
        }

        return html;
    }

    @RequestMapping(value = "to_detail2/{id}", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String toDetail2(
            @PathVariable Long id,
            MiaoshaUser miaoshaUser,
            Model model,
            HttpServletRequest request,
            HttpServletResponse response) {

        // 取缓存
        String html = redisService.get(GoodsKeyPrefix.GET_GOODS_DETAIL, String.valueOf(id), String.class);
        if (StringUtils.isNotBlank(html)) {
            return html;
        }

        model.addAttribute("user", miaoshaUser);
        // 商品信息
        GoodsVo goods = goodsService.getGoodsVoById(id);
        model.addAttribute("goods", goods);
        // 计算状态
        long startTime = goods.getStartDate().getTime();
        long endTime = goods.getEndDate().getTime();
        long now = new Date().getTime();
        int remainSeconds = 0;
        int miaoshaStatus = 0;
        if (now < startTime) { // 秒杀没开始，倒计时
            miaoshaStatus = 0;
            remainSeconds = (int) ((startTime - now) / 1000);
        } else if (now > endTime) { // 秒杀已经结束
            miaoshaStatus = 2;
            remainSeconds = -1;
        } else { // 秒杀正在进行
            miaoshaStatus = 1;
            remainSeconds = 0;
        }
        model.addAttribute("miaoshaStatus", miaoshaStatus);
        model.addAttribute("remainSeconds", remainSeconds);
        // return "goods_detail";
        // 上下文对象
        WebContext ctx = new WebContext(request, response, request.getServletContext(), request.getLocale(), model.asMap());
        // 渲染模板
        html = thymeleafViewResolver
                .getTemplateEngine()
                .process("goods_detail", ctx);
        // set缓存
        if (StringUtils.isNotBlank(html)) {
            redisService.set(GoodsKeyPrefix.GET_GOODS_DETAIL, String.valueOf(id), html);
        }
        return html;
    }


    @RequestMapping(value = "detail/{id}")
    @ResponseBody
    public Result<GoodsDetailVo> toDetail(
            @PathVariable Long id,
            MiaoshaUser miaoshaUser) {

        // 商品信息
        GoodsVo goods = goodsService.getGoodsVoById(id);
        // 计算状态
        long startTime = goods.getStartDate().getTime();
        long endTime = goods.getEndDate().getTime();
        long now = new Date().getTime();
        int remainSeconds = 0;
        int miaoshaStatus = 0;
        if (now < startTime) { // 秒杀没开始，倒计时
            miaoshaStatus = 0;
            remainSeconds = (int) ((startTime - now) / 1000);
        } else if (now > endTime) { // 秒杀已经结束
            miaoshaStatus = 2;
            remainSeconds = -1;
        } else { // 秒杀正在进行
            miaoshaStatus = 1;
            remainSeconds = 0;
        }
        GoodsDetailVo goodsDetailVo = new GoodsDetailVo();
        goodsDetailVo.setGoods(goods);
        goodsDetailVo.setMiaoshaStatus(miaoshaStatus);
        goodsDetailVo.setRemainSeconds(remainSeconds);
        goodsDetailVo.setUser(miaoshaUser);
        return Result.success(goodsDetailVo);
    }
}
