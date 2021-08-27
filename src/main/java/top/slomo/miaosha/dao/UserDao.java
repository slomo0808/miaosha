package top.slomo.miaosha.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import top.slomo.miaosha.entity.User;

/**
 * @description: .
 * @date: 2021-04-07
 * @author: YuBo
 */
@Mapper
public interface UserDao {

    @Select("select * from user where id = #{id}")
    User getById(Integer id);

    @Insert("insert into user (id,name) value (#{id}, #{name}")
    int tx(User user);
}
