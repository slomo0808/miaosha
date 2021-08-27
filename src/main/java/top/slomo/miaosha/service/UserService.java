package top.slomo.miaosha.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.slomo.miaosha.dao.UserDao;
import top.slomo.miaosha.entity.User;

/**
 * @description: .
 * @date: 2021-04-07
 * @author: YuBo
 */
@Service
public class UserService {

    @Autowired
    UserDao userDao;

    public User getById(Integer id) {
        return userDao.getById(id);
    }

    @Transactional
    public Boolean tx() {
        User user = new User();
        user.setId(2);
        user.setName("hahaha");
        userDao.tx(user);

        User user2 = new User();
        user2.setId(1);
        user2.setName("xixixi");
        return true;
    }
}
