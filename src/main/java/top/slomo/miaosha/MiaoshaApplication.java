package top.slomo.miaosha;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * @description: .
 * @date: 2021-04-07
 * @author: YuBo
 */
@SpringBootApplication
@MapperScan(basePackages = "top.slomo.miaosha.dao")
public class MiaoshaApplication extends SpringBootServletInitializer {
    public static void main(String[] args) {
        ConfigurableEnvironment env = SpringApplication.run(MiaoshaApplication.class, args).getEnvironment();
        System.out.println("MiaoshaApplication启动成功,port: " + env.getProperty("server.port"));
    }
}
