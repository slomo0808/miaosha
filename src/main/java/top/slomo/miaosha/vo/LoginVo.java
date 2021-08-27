package top.slomo.miaosha.vo;

import org.hibernate.validator.constraints.Length;
import top.slomo.miaosha.validator.IsMobile;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @description: .
 * @date: 2021-04-07
 * @author: YuBo
 */
public class LoginVo {
    @NotNull
    @IsMobile
    private String mobile;

    @NotNull
    @Length(max = 32)
    private String password;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "LoginVo{" +
                "mobile='" + mobile + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
