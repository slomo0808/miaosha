package top.slomo.miaosha.redis;

/**
 * @description: .
 * @date: 2021-04-07
 * @author: YuBo
 */
public class OrderKeyPrefix extends BasePrefix {
    private OrderKeyPrefix(String prefix) {
        super(prefix);
    }

    // moug - miaosha order uid gid
    public static OrderKeyPrefix GET_BY_UID_GID = new OrderKeyPrefix("moug");
}
