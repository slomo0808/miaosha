package top.slomo.miaosha.util;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * @description: .
 * @date: 2021-04-07
 * @author: YuBo
 */
public class MD5Util {
    public static String md5(String src){
        return DigestUtils.md5Hex(src);
    }

    private static final String SALT = "1a2b3c4d";

    public static  String inputPassToFormPass(String inputPass) {
        String str = "" + SALT.charAt(0) + SALT.charAt(2) + inputPass + SALT.charAt(5) + SALT.charAt(4);
        return md5(str);
    }

    public static String formPassToDBPass(String formPass, String salt) {
        String str = "" + salt.charAt(0) + salt.charAt(2) + formPass + salt.charAt(5) + salt.charAt(4);
        return md5(str);
    }

    public static String inputPassToDBPass(String inputPass, String salt) {
        String formPass = inputPassToFormPass(inputPass);
        return formPassToDBPass(formPass, salt);
    }

    public static void main(String[] args) {
        System.out.println(inputPassToDBPass("123456", "1a2b3c4d"));
    }
}
