package top.slomo.miaosha.rabbitmq;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * @description: .
 * @date: 2021-04-15
 * @author: YuBo
 */
@Configuration
public class MqConfig {

    static final String QUEUE = "miaosha";

    @Bean
    Queue queue() {
        return new Queue(QUEUE);
    }
}
