package top.slomo.miaosha.access;

import java.lang.annotation.*;

/**
 * 简单时间窗口限流
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AccessLimit {
    /**
     * 几秒内
     */
    int seconds();

    /**
     * 最大访问次数
     */
    int maxCount();

    boolean requireLogin() default false;
}
