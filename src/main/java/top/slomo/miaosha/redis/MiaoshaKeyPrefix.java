package top.slomo.miaosha.redis;

/**
 * @description: .
 * @date: 2021-04-16
 * @author: YuBo
 */
public class MiaoshaKeyPrefix extends BasePrefix{

    public static final MiaoshaKeyPrefix IS_GOODS_OVER = new MiaoshaKeyPrefix("go");

    public MiaoshaKeyPrefix(String prefix) {
        super(prefix);
    }

    public MiaoshaKeyPrefix(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }
}
