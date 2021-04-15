package top.slomo.miaosha.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import top.slomo.miaosha.entity.Goods;
import top.slomo.miaosha.entity.MiaoshaGoods;
import top.slomo.miaosha.vo.GoodsVo;

import java.util.List;

/**
 * (Goods)表数据库访问层
 *
 * @author makejava
 * @since 2021-04-12 17:18:13
 */
@Mapper
public interface GoodsDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Select("select * from goods where id = #{id}")
    Goods selectById(Long id);


    /**
     *
     * @return
     */
    @Select("select g.*,mg.id as miaosha_goods_id,mg.stock_count,mg.miaosha_price,mg.start_date,mg.end_date from miaosha_goods mg left join goods g on mg.goods_id = g.id")
    List<GoodsVo> selectGoodsVoList();

    @Select("select g.*,mg.id as miaosha_goods_id,mg.stock_count,mg.miaosha_price,mg.start_date,mg.end_date from miaosha_goods mg left join goods g on mg.goods_id = g.id where g.id = #{id}")
    GoodsVo getGoodsVoById(Long id);

    @Update("update miaosha_goods set stock_count = stock_count - 1 where id = #{id} and stock_count > 0")
    int reduceStock(MiaoshaGoods g);

    @Select("select g.*,mg.id as miaosha_goods_id,mg.stock_count,mg.miaosha_price,mg.start_date,mg.end_date from miaosha_goods mg left join goods g on mg.goods_id = g.id where mg.id = #{miaoshaGoodsId}")
    GoodsVo getGoodsVoByMiaoshaGoodsId(Long miaoshaGoodsId);
}