package top.slomo.miaosha.vo;

import top.slomo.miaosha.entity.MiaoshaUser;
import top.slomo.miaosha.entity.OrderInfo;

/**
 * @description: .
 * @date: 2021-04-15
 * @author: YuBo
 */
public class OrderVo extends OrderInfo {
    private GoodsVo goods;
    private MiaoshaUser user;

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
}
