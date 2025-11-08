package co.yixiang.yshop.module.order.api;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.IdUtil;
import co.yixiang.yshop.framework.common.constant.ShopConstants;
import co.yixiang.yshop.framework.common.enums.OrderInfoEnum;
import co.yixiang.yshop.framework.common.enums.PayIdEnum;
import co.yixiang.yshop.framework.common.exception.ErrorCode;
import co.yixiang.yshop.framework.common.util.object.BeanUtils;
import co.yixiang.yshop.framework.mybatis.core.query.LambdaQueryWrapperX;
import co.yixiang.yshop.framework.tenant.core.context.TenantContextHolder;
import co.yixiang.yshop.module.desk.dal.dataobject.shopdesk.ShopDeskDO;
import co.yixiang.yshop.module.desk.dal.mysql.shopdesk.ShopDeskMapper;
import co.yixiang.yshop.module.message.enums.WechatTempateEnum;
import co.yixiang.yshop.module.message.mq.message.WeixinNoticeMessage;
import co.yixiang.yshop.module.message.mq.producer.WeixinNoticeProducer;
import co.yixiang.yshop.module.message.redismq.msg.OrderMsg;
import co.yixiang.yshop.module.order.api.dto.AppShopDueDTO;
import co.yixiang.yshop.module.order.api.dto.AppStoreOrderDTO;
import co.yixiang.yshop.module.order.controller.app.order.vo.message.ShopOrderMsgVo;
import co.yixiang.yshop.module.order.dal.dataobject.storeorder.StoreOrderDO;
import co.yixiang.yshop.module.order.dal.mysql.storeorder.StoreOrderMapper;
import co.yixiang.yshop.module.order.enums.OrderDueStatusEnum;
import co.yixiang.yshop.module.order.enums.OrderLogEnum;
import co.yixiang.yshop.module.order.enums.OrderStatusEnum;
import co.yixiang.yshop.module.order.enums.PayTypeEnum;
import co.yixiang.yshop.module.order.service.storeorder.AsyncStoreOrderService;
import co.yixiang.yshop.module.order.service.storeorderstatus.StoreOrderStatusService;
import co.yixiang.yshop.module.pay.dal.dataobject.merchantdetails.MerchantDetailsDO;
import co.yixiang.yshop.module.pay.service.merchantdetails.MerchantDetailsService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.egzosn.pay.common.bean.TransferOrder;
import com.egzosn.pay.spring.boot.core.PayServiceManager;
import com.egzosn.pay.wx.v3.bean.WxTransferType;
import com.egzosn.pay.wx.v3.bean.transfer.TransferDetail;
import com.egzosn.pay.wx.v3.bean.transfer.WxTransferOrder;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBlockingDeque;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static co.yixiang.yshop.framework.common.exception.util.ServiceExceptionUtil.exception;
import static co.yixiang.yshop.module.order.enums.ErrorCodeConstants.SELECT_ADDRESS;

@Service
@Slf4j
public class OrderApiImpl implements OrderApi{

    @Resource
    private StoreOrderMapper storeOrderMapper;
    @Resource
    private ShopDeskMapper shopDeskMapper;
    @Resource
    private RedissonClient redissonClient;
    @Resource
    private WeixinNoticeProducer weixinNoticeProducer;
    @Resource
    private StoreOrderStatusService storeOrderStatusService;
    @Resource
    private AsyncStoreOrderService asyncStoreOrderService;
    @Resource
    private PayServiceManager manager;
    @Resource
    private MerchantDetailsService merchantDetailsService;

    @Override
    public AppStoreOrderDTO getDueOrderInfo(Long deskId, LocalDateTime time) {
        StoreOrderDO storeOrderDO = storeOrderMapper.selectOne(new LambdaQueryWrapper<StoreOrderDO>()
                .eq(StoreOrderDO::getDeskId,deskId).eq(StoreOrderDO::getDueTime,time)
                .eq(StoreOrderDO::getOrderType, OrderLogEnum.ORDER_TAKE_DUE.getValue())
                .eq(StoreOrderDO::getDueStatus, OrderDueStatusEnum.DUE_STATUS_1.getValue()));
        //查询当前订单是否在扫码就餐中
        if(storeOrderDO == null){
            LocalDateTime beginOfDay = LocalDateTimeUtil.beginOfDay(time);
            LocalDateTime endOfDay = LocalDateTimeUtil.endOfDay(time);
            ShopDeskDO shopDeskDO = shopDeskMapper.selectOne(new LambdaQueryWrapper<ShopDeskDO>()
                    .eq(ShopDeskDO::getId,deskId)
                    .eq(ShopDeskDO::getLastOrderStatus,OrderInfoEnum.DESK_ORDER_STATUS_ING.getValue())
                    .between(ShopDeskDO::getLastOrderTime,beginOfDay,endOfDay));
            if(shopDeskDO != null){
                storeOrderDO = storeOrderMapper.selectOne(new LambdaQueryWrapper<StoreOrderDO>()
                        .eq(StoreOrderDO::getOrderId,shopDeskDO.getLastOrderNo()));
            }
        }
        return BeanUtils.toBean(storeOrderDO,AppStoreOrderDTO.class);
    }

    @Override
    public String addDueOrder(AppShopDueDTO appShopDueDTO) {
        LocalDateTime now = LocalDateTime.now();
        if(appShopDueDTO.getDueTime().compareTo(now) < 0){
            throw exception(new ErrorCode(20246220,"预约时间不能小于当前时间"));
        }
        AppStoreOrderDTO appStoreOrderDTO = getDueOrderInfo(appShopDueDTO.getDeskId(),appShopDueDTO.getDueTime());
        if(appStoreOrderDTO != null){
            throw exception(new ErrorCode(20246221,"当前时间已经被预定"));
        }
        String orderSn = IdUtil.getSnowflake(0, 0).nextIdStr();
        ShopDeskDO shopDeskDO = shopDeskMapper.selectById(appShopDueDTO.getDeskId());

        StoreOrderDO storeOrderDO = BeanUtils.toBean(appShopDueDTO,StoreOrderDO.class);
        storeOrderDO.setUserIds(appShopDueDTO.getUid().toString());
        storeOrderDO.setOrderId(orderSn);
        storeOrderDO.setShopId(shopDeskDO.getShopId());
        storeOrderDO.setDeskNumber(shopDeskDO.getNumber());
        //storeOrderDO.setDeskPeople()
        storeOrderDO.setShopName(shopDeskDO.getShopName());
        storeOrderDO.setDueStatus(OrderDueStatusEnum.DUE_STATUS_1.getValue());
        storeOrderDO.setOrderType(OrderLogEnum.ORDER_TAKE_DUE.getValue());
        storeOrderDO.setPayType("no");
        storeOrderDO.setMark("--");
        storeOrderDO.setCost(BigDecimal.ZERO);

        storeOrderMapper.insert(storeOrderDO);

        //增加状态
        storeOrderStatusService.create(appShopDueDTO.getUid(),storeOrderDO.getId(),
                OrderLogEnum.CREATE_DUE_ORDER_SUCCESS.getValue(),
                OrderLogEnum.CREATE_DUE_ORDER_SUCCESS.getDesc());

        //websocket通信
        asyncStoreOrderService.pubOrderInfo(new ShopOrderMsgVo().setShopId(shopDeskDO.getShopId()));

        //预约时间点之后2个小时自动确认收货
        try {
            RBlockingDeque<Object> blockingDeque = redissonClient.getBlockingDeque(ShopConstants.REDIS_ORDER_DUE_CONFIRM);
            RDelayedQueue<Object> delayedQueue = redissonClient.getDelayedQueue(blockingDeque);
            //计算时间
            Duration duration = Duration.between(now,storeOrderDO.getDueTime());
            Long hours = duration.toHours();
            delayedQueue.offer(OrderMsg.builder().orderId(orderSn).build(), hours+2, TimeUnit.HOURS);
        } catch (Exception e) {
            log.error("添加延时队列失败：{}",e.getMessage());
        }

        //公众号订单通知管理员
        weixinNoticeProducer.sendNoticeMessage( new WeixinNoticeMessage()
                .setOrderId(orderSn)
                .setTempkey(WechatTempateEnum.NEW_ORDER_NOTICE.getValue())
                .setType(WechatTempateEnum.TEMPLATES.getValue()));


        return orderSn;
    }

    @Override
    public AppStoreOrderDTO getOrderInfo(String orderId) {
        StoreOrderDO storeOrderDO = storeOrderMapper.selectOne(new LambdaQueryWrapper<StoreOrderDO>()
                .eq(StoreOrderDO::getOrderId,orderId));
        return BeanUtils.toBean(storeOrderDO,AppStoreOrderDTO.class);
    }


    @Override
    public Map<String, Object> weixinTransfer(String outBillNo,String openid,String amount,String remark, String userName){
//        Long tenantId = TenantContextHolder.getTenantId();
//        String detailsId = PayIdEnum.WX_WECHAT.getValue() + tenantId;
//        MerchantDetailsDO merchantDetailsDO = merchantDetailsService.getMerchantDetails(detailsId);
//        if(merchantDetailsDO == null){
//            detailsId = PayIdEnum.WX_MINIAPP.getValue() + tenantId;
//            merchantDetailsDO = merchantDetailsService.getMerchantDetails(detailsId);
//        }
//        if(merchantDetailsDO == null){
//            throw exception(new ErrorCode(202510270,"未配置微信支付信息"));
//        }
        TransferOrder order = new TransferOrder();
        //todo 2025年1月后这个转账功能不能用了


        return manager.transfer("", order);
    }

}
