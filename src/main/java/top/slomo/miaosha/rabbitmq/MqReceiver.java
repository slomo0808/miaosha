package top.slomo.miaosha.rabbitmq;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.slomo.miaosha.entity.MiaoshaOrder;
import top.slomo.miaosha.entity.MiaoshaUser;
import top.slomo.miaosha.entity.OrderInfo;
import top.slomo.miaosha.redis.RedisService;
import top.slomo.miaosha.service.GoodsService;
import top.slomo.miaosha.service.MiaoshaService;
import top.slomo.miaosha.service.OrderInfoService;
import top.slomo.miaosha.vo.GoodsVo;

import java.util.Objects;


/**
 * @description: .
 * @date: 2021-04-15
 * @author: YuBo
 */
@Component
public class MqReceiver {
    private static Logger log = LoggerFactory.getLogger(MqReceiver.class);

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderInfoService orderInfoService;

    @Autowired
    MiaoshaService miaoshaService;

    @Autowired
    RedisService redisService;

    @RabbitListener(queues = MqConfig.MIAOSHA_QUEUE)
    public void miaoshaMessageReceive(String message) {
        log.info("receive message :" + message);
        final MiaoshaMessage mm = JSON.parseObject(message, MiaoshaMessage.class);
        final MiaoshaUser user = mm.getMiaoshaUser();
        final Long miaoshaGoodsId = mm.getMiaoshaGoodsId();
        final Long goodsId = mm.getGoodsId();
        // 判断商品库存
        GoodsVo goods = goodsService.getGoodsVoByMiaoshaGoodsId(miaoshaGoodsId);
        if (goods.getStockCount() <= 0) {
            return;
        }
        // 有库存，判断是否已经秒杀到了
        MiaoshaOrder miaoshaOrder = orderInfoService.getMiaoshaOrderByUserIdAndGoodsId(user.getId(), goodsId);
        if (Objects.nonNull(miaoshaOrder)) {
            return;
        }
        // 减库存 下订单 写入秒杀订单
        miaoshaService.miaosha(user, goods);
    }

//
//    @RabbitListener(queues = MqConfig.QUEUE)
//    public void receive(String message) {
//        log.info("receive message :" + message);
//    }

//    @RabbitListener(queues = MqConfig.TOPIC_QUEUE1)
//    public void receiveTopicQueue1(String message) {
//        log.info("receive topic queue1 message: {}", message);
//    }
//
//    @RabbitListener(queues = MqConfig.TOPIC_QUEUE2)
//    public void receiveTopicQueue2(String message) {
//        log.info("receive topic queue2 message: {}", message);
//    }
}
