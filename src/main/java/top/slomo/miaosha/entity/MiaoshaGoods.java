package top.slomo.miaosha.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * (MiaoshaGoods)实体类
 *
 * @author makejava
 * @since 2021-04-12 17:19:48
 */
public class MiaoshaGoods implements Serializable {
    private static final long serialVersionUID = -96824097822590553L;
    /**
     * id
     */
    private Long id;
    /**
     * 商品id
     */
    private Long goodsId;
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


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
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