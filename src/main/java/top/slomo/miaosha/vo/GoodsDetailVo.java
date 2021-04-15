package top.slomo.miaosha.vo;

import top.slomo.miaosha.entity.MiaoshaUser;

/**
 * @description: .
 * @date: 2021-04-13
 * @author: YuBo
 */
public class GoodsDetailVo {
    private GoodsVo goods;
    private MiaoshaUser user;
    private int miaoshaStatus;
    private int remainSeconds;

    public GoodsVo getGoods() {
        return goods;
    }

    public void setGoods(GoodsVo goods) {
        this.goods = goods;
    }

    public MiaoshaUser getUser() {
        return user;
    }

    public void setUser(MiaoshaUser user) {
        this.user = user;
    }

    public int getMiaoshaStatus() {
        return miaoshaStatus;
    }

    public void setMiaoshaStatus(int miaoshaStatus) {
        this.miaoshaStatus = miaoshaStatus;
    }

    public int getRemainSeconds() {
        return remainSeconds;
    }

    public void setRemainSeconds(int remainSeconds) {
        this.remainSeconds = remainSeconds;
    }
}
