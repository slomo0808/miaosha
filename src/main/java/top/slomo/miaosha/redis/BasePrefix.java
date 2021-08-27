package top.slomo.miaosha.redis;

/**
 * @description: .
 * @date: 2021-04-07
 * @author: YuBo
 */
public abstract class BasePrefix implements KeyPrefix{

    private int expireSeconds;
    private String prefix;

    public BasePrefix(String prefix) {
        this(0, prefix);
    }

    public BasePrefix(int expireSeconds, String prefix) {
        this.expireSeconds = expireSeconds;
        this.prefix = prefix;
    }

    @Override
    public int expireSeconds() {
        return expireSeconds;
    }

    @Override
    public String prefix() {
        String className = this.getClass().getSimpleName();
        return className + ":" + prefix;
    }
}
