package top.slomo.miaosha.dao;

import org.apache.ibatis.annotations.*;
import top.slomo.miaosha.entity.MiaoshaOrder;
import top.slomo.miaosha.entity.OrderInfo;

import java.util.List;

/**
 * (OrderInfo)表数据库访问层
 *
 * @author makejava
 * @since 2021-04-12 17:20:36
 */
@Mapper
public interface OrderInfoDao {
    @Select("select * from miaosha_order where user_id = #{userId} and goods_id = #{goodsId}")
    MiaoshaOrder getMiaoshaOrderByUserIdAndGoodsId(@Param("userId") Long userId, @Param("goodsId") Long goodsId);

    @Insert("insert into order_info " +
            "(user_id, goods_id, delivery_addr_id, goods_name, goods_count, goods_price, order_channel, status, create_date) " +
            "value " +
            "(#{userId}, #{goodsId}, #{deliveryAddrId}, #{goodsName}, #{goodsCount}, #{goodsPrice}, #{orderChannel}, #{status}, #{createDate})")
    @SelectKey(keyColumn = "id", keyProperty = "id", before = false, resultType = Long.class, statement = "select last_insert_id()")
    void insertOrderInfo(OrderInfo order);

    @Insert("insert into miaosha_order " +
            "(user_id, order_id, goods_id) " +
            "value " +
            "(#{userId}, #{orderId}, #{goodsId})")
    void insertMiaoshaOrder(MiaoshaOrder miaoshaOrder);

    @Select("select * from order_info where id = #{id}")
    OrderInfo getById(Long id);
}