package top.slomo.miaosha.rabbitmq;

import top.slomo.miaosha.entity.MiaoshaUser;

/**
 * @description: .
 * @date: 2021-04-16
 * @author: YuBo
 */
public class MiaoshaMessage {
    private MiaoshaUser miaoshaUser;
    private Long goodsId;
    private Long miaoshaGoodsId;

    public MiaoshaUser getMiaoshaUser() {
        return miaoshaUser;
    }

    public void setMiaoshaUser(MiaoshaUser miaoshaUser) {
        this.miaoshaUser = miaoshaUser;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public Long getMiaoshaGoodsId() {
        return miaoshaGoodsId;
    }

    public void setMiaoshaGoodsId(Long miaoshaGoodsId) {
        this.miaoshaGoodsId = miaoshaGoodsId;
    }
}
