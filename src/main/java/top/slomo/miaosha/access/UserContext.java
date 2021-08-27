package top.slomo.miaosha.access;

import top.slomo.miaosha.entity.MiaoshaUser;

/**
 * @description: .
 * @date: 2021-04-19
 * @author: YuBo
 */
public class UserContext {
    private static final ThreadLocal<MiaoshaUser> userHandler = new ThreadLocal<>();

    public static MiaoshaUser get() {
        return userHandler.get();
    }

    public static void set(MiaoshaUser user) {
        userHandler.set(user);
    }

    public static void remove() {
        userHandler.remove();
    }
}
