package top.slomo.miaosha.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import top.slomo.miaosha.entity.MiaoshaUser;

/**
 * @description: .
 * @date: 2021-04-07
 * @author: YuBo
 */
@Mapper
public interface MiaoshaUserDao {
    @Select("select * from miaosha_user where id = #{id}")
    MiaoshaUser getById(Long id);
}
