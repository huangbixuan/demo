package co.yixiang.yshop.module.message.mq.message;

import co.yixiang.yshop.framework.mq.redis.core.stream.AbstractRedisStreamMessage;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class WeixinNoticeMessage extends AbstractRedisStreamMessage {


    /**
     * 模板编号
     */
    private String tempkey;

    //消息类型
    private String type;

    //订单好
    private String orderId;

    //价格
    private String price;

    //用户
    private Long uid;

    //时间
    private String time;

    // 快递公司
    private String deliveryName;

    // 快递单号
    private String deliveryId;

    //申请售后时间
    private LocalDateTime afterTime;

    //分销金额
    private BigDecimal brokeragePrice;

    //商品名称
    private String productTitle;


    @Override
    public String getStreamKey() {
        return "weixin.msg.notice";
    }
}
