package top.slomo.miaosha.redis;

/**
 * @description: .
 * @date: 2021-04-07
 * @author: YuBo
 */
public class GoodsKeyPrefix extends BasePrefix {
    private GoodsKeyPrefix(String prefix) {
        super(prefix);
    }

    private GoodsKeyPrefix(int expiredSecond, String prefix) {
        super(expiredSecond, prefix);
    }

    public static GoodsKeyPrefix GET_GOODS_LIST = new GoodsKeyPrefix(60, "gl");
    public static GoodsKeyPrefix GET_GOODS_DETAIL = new GoodsKeyPrefix(60, "gd");
    public static GoodsKeyPrefix GOODS_STOCK_COUNT = new GoodsKeyPrefix(60, "gsc");
}
