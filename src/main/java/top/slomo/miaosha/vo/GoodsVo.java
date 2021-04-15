package top.slomo.miaosha.vo;

import top.slomo.miaosha.entity.Goods;
import top.slomo.miaosha.entity.MiaoshaGoods;

import java.util.Date;

/**
 * @description: .
 * @date: 2021-04-12
 * @author: YuBo
 */
public class GoodsVo extends Goods {
    /**
     * 秒杀GoodsId
     */
    private Long miaoshaGoodsId;

    /**
     * 秒杀价格
     */
    private Double miaoshaPrice;

    /**
     * 库存数量
     */
    private Integer stockCount;
    /**
     * 开始时间
     */
    private Date startDate;
    /**
     * 结束时间
     */
    private Date endDate;

    public Long getMiaoshaGoodsId() {
        return miaoshaGoodsId;
    }

    public void setMiaoshaGoodsId(Long miaoshaGoodsId) {
        this.miaoshaGoodsId = miaoshaGoodsId;
    }

    public Double getMiaoshaPrice() {
        return miaoshaPrice;
    }

    public void setMiaoshaPrice(Double miaoshaPrice) {
        this.miaoshaPrice = miaoshaPrice;
    }

    public Integer getStockCount() {
        return stockCount;
    }

    public void setStockCount(Integer stockCount) {
        this.stockCount = stockCount;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
