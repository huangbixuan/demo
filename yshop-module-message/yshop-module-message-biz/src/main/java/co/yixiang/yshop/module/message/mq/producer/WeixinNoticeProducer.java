package co.yixiang.yshop.module.message.mq.producer;

import co.yixiang.yshop.framework.mq.redis.core.RedisMQTemplate;
import co.yixiang.yshop.module.message.mq.message.WeixinNoticeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;

@Slf4j
@Component
public class WeixinNoticeProducer {
    @Resource
    private RedisMQTemplate redisMQTemplate;

    /**
     * 发送消息
     *
     * @param weixinNoticeMessage 数据
     */
    public void sendNoticeMessage(WeixinNoticeMessage weixinNoticeMessage) {
        redisMQTemplate.send(weixinNoticeMessage);
    }
}
