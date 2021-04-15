package top.slomo.miaosha.rabbitmq;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @description: .
 * @date: 2021-04-15
 * @author: YuBo
 */
@Component
public class MqSender {
    private static Logger log = LoggerFactory.getLogger(MqSender.class);

    @Autowired
    AmqpTemplate amqpTemplate;

    public void send(Object msg) {
        String message = msg instanceof String ? (String) msg : JSON.toJSONString(msg);
        log.info("send message: " + message);
        amqpTemplate.convertAndSend(MqConfig.QUEUE ,message);
    }
}
