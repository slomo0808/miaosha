package top.slomo.miaosha.redis;

/**
 * @description: .
 * @date: 2021-04-07
 * @author: YuBo
 */
public class UserKeyPrefix extends BasePrefix {
    private UserKeyPrefix(String prefix) {
        super(prefix);
    }

    public static UserKeyPrefix GET_BY_ID = new UserKeyPrefix("id");
    public static UserKeyPrefix GET_BY_NAME = new UserKeyPrefix("name");
}
