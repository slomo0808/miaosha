package top.slomo.miaosha.util;

import java.util.UUID;

/**
 * @description: .
 * @date: 2021-04-08
 * @author: YuBo
 */
public class UUIDUtil {
    public static String uuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
