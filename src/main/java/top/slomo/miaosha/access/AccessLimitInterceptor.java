package top.slomo.miaosha.access;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import top.slomo.miaosha.entity.MiaoshaUser;
import top.slomo.miaosha.redis.AccessKeyPrefix;
import top.slomo.miaosha.redis.RedisService;
import top.slomo.miaosha.result.CodeMsg;
import top.slomo.miaosha.result.Result;
import top.slomo.miaosha.service.MiaoshaUserService;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * @description: .
 * @date: 2021-04-19
 * @author: YuBo
 */
@Component
public class AccessLimitInterceptor implements HandlerInterceptor {

    @Autowired
    RedisService redisService;
    @Autowired
    MiaoshaUserService miaoshaUserService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod hm = (HandlerMethod) handler;
            final Method method = hm.getMethod();
            // 获取用户
            MiaoshaUser user = getUser(request, response);
            UserContext.set(user);

            if (method.isAnnotationPresent(AccessLimit.class)) {
                final AccessLimit anno = method.getAnnotation(AccessLimit.class);
                if (anno.requireLogin()) {
                    if (Objects.isNull(user)) {
                        render(response, CodeMsg.SESSION_ERROR);
                        return false;
                    }
                }

                final int seconds = anno.seconds();
                final int maxCount = anno.maxCount();

                // 查询访问次数
                final String key = request.getRequestURI() + "_" + user.getId();
                AccessKeyPrefix prefix = AccessKeyPrefix.rateLimitWithExpire(seconds);
                final Integer count = redisService.get(prefix, key, Integer.class);
                if (Objects.isNull(count)) {
                    redisService.set(prefix, key, 1);
                } else if (count < maxCount) {
                    redisService.incr(prefix, key);
                } else {
                    render(response, CodeMsg.RATE_LIMIT);
                    return false;
                }
            }
        }
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    private MiaoshaUser getUser(HttpServletRequest request, HttpServletResponse response) {
        String token = request.getParameter(MiaoshaUserService.COOKIE_NAME_TOKEN);
        if (StringUtils.isEmpty(token)) {
            token = getCookieValue(request, MiaoshaUserService.COOKIE_NAME_TOKEN);
        }

        if (StringUtils.isEmpty(token)) {
            return null;
        }

        return miaoshaUserService.getMiaoshaUserByToken(response, token);
    }

    private String getCookieValue(HttpServletRequest request, String cookieNameToken) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null || cookies.length == 0) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (StringUtils.equals(cookieNameToken, cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }

    public static void render(HttpServletResponse response, CodeMsg cm) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        final ServletOutputStream out = response.getOutputStream();
        final String str = JSON.toJSONString(Result.error(cm));
        out.write(str.getBytes(StandardCharsets.UTF_8));
        out.flush();
        out.close();
    }
}
