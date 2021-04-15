package top.slomo.miaosha.rabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;


/**
 * @description: .
 * @date: 2021-04-15
 * @author: YuBo
 */
@Component
public class MqReceiver {
    private static Logger log = LoggerFactory.getLogger(MqReceiver.class);

    @RabbitListener(queues = MqConfig.QUEUE)
    public void receive(String message) {
        log.info("receive message :" + message);
    }

    @RabbitListener(queues = MqConfig.TOPIC_QUEUE1)
    public void receiveTopicQueue1(String message) {
        log.info("receive topic queue1 message: {}", message);
    }

    @RabbitListener(queues = MqConfig.TOPIC_QUEUE2)
    public void receiveTopicQueue2(String message) {
        log.info("receive topic queue2 message: {}", message);
    }
}
