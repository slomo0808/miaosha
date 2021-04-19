package top.slomo.miaosha.redis;

/**
 * @description: .
 * @date: 2021-04-19
 * @author: YuBo
 */
public class AccessKeyPrefix extends BasePrefix {

    public static final AccessKeyPrefix RATE_LIMIT = new AccessKeyPrefix(5, "rl");

    public static AccessKeyPrefix rateLimitWithExpire(int expired) {
        return new AccessKeyPrefix(expired, "rl");
    }

    private AccessKeyPrefix(String prefix) {
        super(prefix);
    }

    private AccessKeyPrefix(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }
}
