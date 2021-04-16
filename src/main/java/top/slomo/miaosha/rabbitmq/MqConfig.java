package top.slomo.miaosha.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * @description: .
 * @date: 2021-04-15
 * @author: YuBo
 */
@Configuration
public class MqConfig {

    static final String MIAOSHA_QUEUE = "miaosha.queue";
    static final String QUEUE = "miaosha";

    static final String TOPIC_EXCHANGE = "topicExchange";
    static final String TOPIC_QUEUE1 = "topic.queue1";
    static final String TOPIC_QUEUE2 = "topic.queue2";

    /**
     * 秒杀消息队列
     */
    @Bean
    Queue miaoshaQueue() {
        return new Queue(MIAOSHA_QUEUE);
    }

    /**
     * 默认点对点 Direct模式
     */
    @Bean
    Queue queue() {
        return new Queue(QUEUE);
    }

    /**
     * topic模式
     */
    @Bean
    TopicExchange topicExchange() {
       return new TopicExchange(TOPIC_EXCHANGE);
    }

    /**
     * 绑定到topicExchange的queue1
     */
    @Bean
    Queue topicQueue1() {
        return new Queue(TOPIC_QUEUE1, true);
    }

    /**
     * 绑定到topicExchange的queue1
     */
    @Bean
    Queue topicQueue2() {
        return new Queue(TOPIC_QUEUE2, true);
    }

    /**
     * 绑定queue1
     */
    @Bean
    Binding bindTopicQueue1() {
        return BindingBuilder.bind(topicQueue1()).to(topicExchange()).with("topic.key1");
    }

    /**
     * 绑定queue2
     * routingKey支持通配符，*代表一个单词  #代表0个或多个单词
     */
    @Bean
    Binding bindTopicQueue2() {
        return BindingBuilder.bind(topicQueue2()).to(topicExchange()).with("topic.#");
    }
}
