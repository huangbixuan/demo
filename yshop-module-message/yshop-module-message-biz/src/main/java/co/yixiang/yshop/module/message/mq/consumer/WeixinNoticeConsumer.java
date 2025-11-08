package co.yixiang.yshop.module.message.mq.consumer;

import co.yixiang.yshop.framework.mq.redis.core.stream.AbstractRedisStreamMessageListener;
import co.yixiang.yshop.module.message.enums.WechatTempateEnum;
import co.yixiang.yshop.module.message.mq.message.WeixinNoticeMessage;
import co.yixiang.yshop.module.message.supply.WeiXinSubscribeService;
import co.yixiang.yshop.module.message.supply.WeixinTemplateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;


/**
 * 消息队列处理消息推送
 */
@Component
@Slf4j
public class WeixinNoticeConsumer extends AbstractRedisStreamMessageListener<WeixinNoticeMessage> {

    @Resource
    private WeiXinSubscribeService weiXinSubscribeService;
    @Resource
    private WeixinTemplateService weixinTemplateService;


    @Override
    public void onMessage(WeixinNoticeMessage message) {
        log.info("[onMessage][消息内容({})]", message);
        //公众号
        if (WechatTempateEnum.TEMPLATES.getValue().equals(message.getType())) {
            if (WechatTempateEnum.ORDER_AFTER_NOTICE.getValue().equals(message.getTempkey())) {
            } else if (WechatTempateEnum.DELIVERY_SUCCESS.getValue().equals(message.getTempkey())) {
            } else if (WechatTempateEnum.NEW_ORDER_NOTICE.getValue().equals(message.getTempkey())) {
                weixinTemplateService.newOrderNotice(message);
            }else if (WechatTempateEnum.PRODUCT_STOCK_WARNING.getValue().equals(message.getTempkey())) {
            }

        } else if (WechatTempateEnum.SUBSCRIBE.getValue().equals(message.getType())) {
            //小程序
            if (WechatTempateEnum.ORDER_AFTER_NOTICE.getValue().equals(message.getTempkey())) {
            } else if (WechatTempateEnum.DELIVERY_SUCCESS.getValue().equals(message.getTempkey())) {
                weiXinSubscribeService.deliverySuccessNotice(message);
            } else if (WechatTempateEnum.DISTRIBUTION_MONEY_SUCCESS.getValue().equals(message.getTempkey())) {
            }
        }


    }
}
