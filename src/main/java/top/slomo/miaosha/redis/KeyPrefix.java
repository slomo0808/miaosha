package top.slomo.miaosha.redis;

/**
 * @description: .
 * @date: 2021-04-07
 * @author: YuBo
 */
public interface KeyPrefix {

    int expireSeconds();

    String prefix();
}
