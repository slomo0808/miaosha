package top.slomo.miaosha.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import top.slomo.miaosha.result.CodeMsg;
import top.slomo.miaosha.result.Result;
import top.slomo.miaosha.service.MiaoshaUserService;
import top.slomo.miaosha.vo.LoginVo;

import javax.servlet.http.HttpServletResponse;

/**
 * @description: .
 * @date: 2021-04-07
 * @author: YuBo
 */
@Controller
@RequestMapping("login")
public class LoginController {
    private static Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    MiaoshaUserService miaoshaUserService;

    @RequestMapping("to_login")
    public String toLogin() {
        return "login";
    }

    @PostMapping("do_login")
    @ResponseBody
    public Result<String> doLogin(HttpServletResponse response, @Validated LoginVo loginVo) {
        logger.info(loginVo.toString());
        String token = miaoshaUserService.login(response, loginVo);
        return Result.success(token);
    }
}
