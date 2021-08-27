<<<<<<< HEAD:src/main/java/top/slomo/miaosha/Miaosha3Application.java
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
public class Miaosha3Application extends SpringBootServletInitializer {
    public static void main(String[] args) {
        ConfigurableEnvironment env = SpringApplication.run(Miaosha3Application.class, args).getEnvironment();
        System.out.println("MiaoshaApplication启动成功,port: " + env.getProperty("server.port"));
    }
}
=======
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
>>>>>>> c2fe060f309f6dfbb1b8a9e19eace1eb5f0235d5:src/main/java/top/slomo/miaosha/MiaoshaApplication.java
