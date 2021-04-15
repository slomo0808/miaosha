package top.slomo.miaosha.util;

import org.thymeleaf.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @description: .
 * @date: 2021-04-07
 * @author: YuBo
 */
public class ValidatorUtil {
    private static final Pattern MOBILE_PATTERN = Pattern.compile("1\\d{10}");

    public static boolean isMobile(String s) {
        if (StringUtils.isEmpty(s)) {
            return false;
        }
        Matcher matcher = MOBILE_PATTERN.matcher(s);
        return matcher.matches();
    }

    public static void main(String[] args) {
        System.out.println(isMobile("18613230887"));
        System.out.println(isMobile("1861323087"));
    }
}
