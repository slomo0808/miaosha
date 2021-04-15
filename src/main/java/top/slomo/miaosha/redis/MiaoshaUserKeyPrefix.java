package top.slomo.miaosha.redis;

import top.slomo.miaosha.service.MiaoshaUserService;

/**
 * @description: .
 * @date: 2021-04-08
 * @author: YuBo
 */
public class MiaoshaUserKeyPrefix extends BasePrefix {

    public static final int TOKEN_EXPIRE = 60 * 60 * 24 * 2;

    private MiaoshaUserKeyPrefix(String prefix) {
        super(prefix);
    }

    private MiaoshaUserKeyPrefix(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static MiaoshaUserKeyPrefix TOKEN = new MiaoshaUserKeyPrefix(TOKEN_EXPIRE, "tk");
    public static MiaoshaUserKeyPrefix GET_BY_ID = new MiaoshaUserKeyPrefix("user");
}
