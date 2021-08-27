package top.slomo.miaosha.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import top.slomo.miaosha.entity.User;
import top.slomo.miaosha.redis.RedisService;
import top.slomo.miaosha.redis.UserKeyPrefix;
import top.slomo.miaosha.service.UserService;

/**
 * @description: .
 * @date: 2021-04-07
 * @author: YuBo
 */
@Controller
@RequestMapping("/demo")
public class SampleController {

    @Autowired
    UserService userService;

    @Autowired
    RedisService redisService;

    @GetMapping("/demo")
    public String thymeleaf(Model model) {
        model.addAttribute("name", "YuBo");
        return "hello";
    }

    @GetMapping("/db/{id}")
    @ResponseBody
    public User getUserById(@PathVariable Integer id) {
        return userService.getById(id);
    }

    @GetMapping("/tx")
    @ResponseBody
    public Boolean tx() {
        return userService.tx();
    }

    @GetMapping("/redis/get")
    @ResponseBody
    public User redisGet() {
        return redisService.get(UserKeyPrefix.GET_BY_ID, "2", User.class);
    }

    @GetMapping("/redis/set")
    @ResponseBody
    public Boolean redisSet() {
        User user = new User();
        user.setId(2);
        user.setName("alkdjf");
        return redisService.set(UserKeyPrefix.GET_BY_ID, "2", user);
    }
}
