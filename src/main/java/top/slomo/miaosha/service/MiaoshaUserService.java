<<<<<<< HEAD
package top.slomo.miaosha.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.slomo.miaosha.dao.MiaoshaUserDao;
import top.slomo.miaosha.entity.MiaoshaUser;
import top.slomo.miaosha.exception.GlobalException;
import top.slomo.miaosha.redis.MiaoshaUserKeyPrefix;
import top.slomo.miaosha.redis.RedisService;
import top.slomo.miaosha.result.CodeMsg;
import top.slomo.miaosha.util.MD5Util;
import top.slomo.miaosha.util.UUIDUtil;
import top.slomo.miaosha.vo.LoginVo;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * @description: .
 * @date: 2021-04-07
 * @author: YuBo
 */
@Service
public class MiaoshaUserService {

    public static final String COOKIE_NAME_TOKEN = "token";

    @Autowired
    MiaoshaUserDao miaoshaUserDao;


    @Autowired
    RedisService redisService;

    public MiaoshaUser getById(Long id) {
        MiaoshaUser user = redisService.get(MiaoshaUserKeyPrefix.GET_BY_ID, String.valueOf(id), MiaoshaUser.class);
        if (!Objects.isNull(user)) {
            return user;
        }
        user = miaoshaUserDao.getById(id);
        if (!Objects.isNull(user)) {
            redisService.set(MiaoshaUserKeyPrefix.GET_BY_ID, String.valueOf(id), user);
        }
        return user;
    }

    public String login(HttpServletResponse response, LoginVo loginVo) {
        if (loginVo == null) {
            throw new GlobalException(CodeMsg.SERVER_ERROR);
        }

        String mobile = loginVo.getMobile();
        String formPassword = loginVo.getPassword();

        MiaoshaUser miaoshaUser = getById(Long.parseLong(mobile));
        if (miaoshaUser == null) {
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        }

        String calcPass = MD5Util.formPassToDBPass(formPassword, miaoshaUser.getSalt());
        if (!StringUtils.equals(miaoshaUser.getPassword(), calcPass)) {
            throw new GlobalException(CodeMsg.PASSWORD_ERROR);
        }

        // 生成token
        String token = UUIDUtil.uuid();

        addCookie(response, token, miaoshaUser);

        return token;
    }

    private void addCookie(HttpServletResponse response, String token, MiaoshaUser miaoshaUser) {

        // 存储到redis中
        redisService.set(MiaoshaUserKeyPrefix.TOKEN, token, miaoshaUser);
        Cookie cookie = new Cookie(COOKIE_NAME_TOKEN, token);
        cookie.setMaxAge(MiaoshaUserKeyPrefix.TOKEN.expireSeconds());
        cookie.setPath("/");
        response.addCookie(cookie);
    }


    public MiaoshaUser getMiaoshaUserByToken(HttpServletResponse response, String token) {
        if (StringUtils.isBlank(token)) {
            throw new GlobalException(CodeMsg.TOKEN_EMPTY);
        }
        MiaoshaUser user = redisService.get(MiaoshaUserKeyPrefix.TOKEN, token, MiaoshaUser.class);
        if (!Objects.isNull(user)) {
            // 延长有效期
            addCookie(response, token, user);
        }
        return user;
    }
}
=======
package top.slomo.miaosha.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.slomo.miaosha.dao.MiaoshaUserDao;
import top.slomo.miaosha.entity.MiaoshaUser;
import top.slomo.miaosha.exception.GlobalException;
import top.slomo.miaosha.redis.MiaoshaUserKeyPrefix;
import top.slomo.miaosha.redis.RedisService;
import top.slomo.miaosha.result.CodeMsg;
import top.slomo.miaosha.util.MD5Util;
import top.slomo.miaosha.util.UUIDUtil;
import top.slomo.miaosha.vo.LoginVo;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * @description: .
 * @date: 2021-04-07
 * @author: YuBo
 */
@Service
public class MiaoshaUserService {

    public static final String COOKIE_NAME_TOKEN = "token";

    @Autowired
    MiaoshaUserDao miaoshaUserDao;


    @Autowired
    RedisService redisService;

    public MiaoshaUser getById(Long id) {
        MiaoshaUser user = redisService.get(MiaoshaUserKeyPrefix.GET_BY_ID, String.valueOf(id), MiaoshaUser.class);
        if (Objects.nonNull(user)) {
            return user;
        }
        user = miaoshaUserDao.getById(id);
        if (Objects.nonNull(user)) {
            redisService.set(MiaoshaUserKeyPrefix.GET_BY_ID, String.valueOf(id), user);
        }
        return user;
    }

    public String login(HttpServletResponse response, LoginVo loginVo) {
        if (loginVo == null) {
            throw new GlobalException(CodeMsg.SERVER_ERROR);
        }

        String mobile = loginVo.getMobile();
        String formPassword = loginVo.getPassword();

        MiaoshaUser miaoshaUser = getById(Long.parseLong(mobile));
        if (miaoshaUser == null) {
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        }

        String calcPass = MD5Util.formPassToDBPass(formPassword, miaoshaUser.getSalt());
        if (!StringUtils.equals(miaoshaUser.getPassword(), calcPass)) {
            throw new GlobalException(CodeMsg.PASSWORD_ERROR);
        }

        // 生成token
        String token = UUIDUtil.uuid();

        addCookie(response, token, miaoshaUser);

        return token;
    }

    private void addCookie(HttpServletResponse response, String token, MiaoshaUser miaoshaUser) {

        // 存储到redis中
        redisService.set(MiaoshaUserKeyPrefix.TOKEN, token, miaoshaUser);
        Cookie cookie = new Cookie(COOKIE_NAME_TOKEN, token);
        cookie.setMaxAge(MiaoshaUserKeyPrefix.TOKEN.expireSeconds());
        cookie.setPath("/");
        response.addCookie(cookie);
    }


    public MiaoshaUser getMiaoshaUserByToken(HttpServletResponse response, String token) {
        if (StringUtils.isBlank(token)) {
            throw new GlobalException(CodeMsg.TOKEN_EMPTY);
        }
        MiaoshaUser user = redisService.get(MiaoshaUserKeyPrefix.TOKEN, token, MiaoshaUser.class);
        if (Objects.nonNull(user)) {
            // 延长有效期
            addCookie(response, token, user);
        }
        return user;
    }
}
>>>>>>> c2fe060f309f6dfbb1b8a9e19eace1eb5f0235d5
