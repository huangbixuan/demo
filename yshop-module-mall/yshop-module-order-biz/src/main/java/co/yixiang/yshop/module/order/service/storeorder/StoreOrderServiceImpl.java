package co.yixiang.yshop.module.order.service.storeorder;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.shop.request.shipping.OrderKeyBean;
import cn.binarywang.wx.miniapp.bean.shop.request.shipping.PayerBean;
import cn.binarywang.wx.miniapp.bean.shop.request.shipping.ShippingListBean;
import cn.binarywang.wx.miniapp.bean.shop.request.shipping.WxMaOrderShippingInfoUploadRequest;
import cn.binarywang.wx.miniapp.bean.shop.response.WxMaOrderShippingInfoBaseResponse;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import co.yixiang.yshop.framework.common.constant.ShopConstants;
import co.yixiang.yshop.framework.common.enums.OrderInfoEnum;
import co.yixiang.yshop.framework.common.enums.PayIdEnum;
import co.yixiang.yshop.framework.common.enums.ShopCommonEnum;
import co.yixiang.yshop.framework.common.exception.ErrorCode;
import co.yixiang.yshop.framework.common.pojo.PageResult;
import co.yixiang.yshop.framework.common.util.http.HttpUtils;
import co.yixiang.yshop.framework.common.util.json.JsonUtils;
import co.yixiang.yshop.framework.common.util.object.BeanUtils;
import co.yixiang.yshop.framework.security.core.util.SecurityFrameworkUtils;
import co.yixiang.yshop.framework.tenant.core.context.TenantContextHolder;
import co.yixiang.yshop.module.desk.dal.dataobject.shopdesk.ShopDeskDO;
import co.yixiang.yshop.module.desk.dal.mysql.shopdesk.ShopDeskMapper;
import co.yixiang.yshop.module.desk.enums.DeskStatusEnum;
import co.yixiang.yshop.module.express.dal.dataobject.express.ExpressDO;
import co.yixiang.yshop.module.express.dal.mysql.express.ExpressMapper;
import co.yixiang.yshop.module.express.enums.ExpressTypeEnum;
import co.yixiang.yshop.module.member.controller.admin.user.vo.UserRespVO;
import co.yixiang.yshop.module.member.convert.user.UserConvert;
import co.yixiang.yshop.module.member.dal.dataobject.user.MemberUserDO;
import co.yixiang.yshop.module.member.dal.mysql.user.MemberUserMapper;
import co.yixiang.yshop.module.member.enums.BillDetailEnum;
import co.yixiang.yshop.module.member.service.user.MemberUserService;
import co.yixiang.yshop.module.member.service.userbill.UserBillService;
import co.yixiang.yshop.module.message.mq.producer.WeixinNoticeProducer;
import co.yixiang.yshop.module.message.redismq.msg.OrderMsg;
import co.yixiang.yshop.module.mp.service.account.MpAccountService;
import co.yixiang.yshop.module.order.controller.admin.storeorder.vo.*;
import co.yixiang.yshop.module.order.controller.app.order.vo.AppDeskOrderGoodsVo;
import co.yixiang.yshop.module.order.controller.app.order.vo.AppDeskOrderVo;
import co.yixiang.yshop.module.order.controller.app.order.vo.message.CartMsgVo;
import co.yixiang.yshop.module.order.controller.app.order.vo.message.UserCartMsgVo;
import co.yixiang.yshop.module.order.convert.storeorder.StoreOrderConvert;
import co.yixiang.yshop.module.order.dal.dataobject.storecartshare.StoreCartShareDO;
import co.yixiang.yshop.module.order.dal.dataobject.storeorder.StoreOrderDO;
import co.yixiang.yshop.module.order.dal.dataobject.storeordercartinfo.StoreOrderCartInfoDO;
import co.yixiang.yshop.module.order.dal.mysql.storecartshare.StoreCartShareMapper;
import co.yixiang.yshop.module.order.dal.mysql.storeorder.StoreOrderMapper;
import co.yixiang.yshop.module.order.dal.mysql.storeordercartinfo.StoreOrderCartInfoMapper;
import co.yixiang.yshop.module.order.dal.redis.order.AsyncCountRedisDAO;
import co.yixiang.yshop.module.order.enums.*;
import co.yixiang.yshop.module.order.service.storeorder.dto.OrderTimeDataDto;
import co.yixiang.yshop.module.order.service.storeorderstatus.StoreOrderStatusService;
import co.yixiang.yshop.module.product.dal.dataobject.storeproduct.StoreProductDO;
import co.yixiang.yshop.module.product.service.storeproduct.AppStoreProductService;
import co.yixiang.yshop.module.store.dal.dataobject.storerevenue.StoreRevenueDO;
import co.yixiang.yshop.module.store.dal.mysql.storerevenue.StoreRevenueMapper;
import co.yixiang.yshop.module.store.dal.mysql.storeshop.StoreShopMapper;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.egzosn.pay.common.bean.RefundOrder;
import com.egzosn.pay.common.bean.RefundResult;
import com.egzosn.pay.spring.boot.core.PayServiceManager;
import com.egzosn.pay.wx.v3.bean.response.WxRefundResult;
import com.kuaidi100.sdk.request.PrintReq;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.redisson.api.RBlockingDeque;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static co.yixiang.yshop.framework.common.exception.util.ServiceExceptionUtil.exception;
import static co.yixiang.yshop.module.member.enums.ErrorCodeConstants.USER_NOT_EXISTS;
import static co.yixiang.yshop.module.order.enums.ErrorCodeConstants.*;

/**
 * 订单 Service 实现类
 *
 * @author yshop
 */
@Slf4j
@Service
@Validated
public class StoreOrderServiceImpl implements StoreOrderService {

    @Resource
    private StoreOrderMapper storeOrderMapper;
    @Resource
    private MemberUserMapper memberUserMapper;
    @Resource
    private StoreOrderCartInfoMapper storeOrderCartInfoMapper;
    @Resource
    private StoreOrderStatusService storeOrderStatusService;
    @Resource
    private AppStoreOrderService appStoreOrderService;
    @Resource
    private MemberUserService userService;
    @Resource
    private WeixinNoticeProducer weixinNoticeProducer;
    @Resource
    private RedissonClient redissonClient;
    @Resource
    private UserBillService billService;
    @Resource
    private AppStoreProductService appStoreProductService;
    @Resource
    private PayServiceManager manager;

    @Resource
    private AsyncStoreOrderService asyncStoreOrderService;
    @Resource
    private ShopDeskMapper shopDeskMapper;

    @Resource
    private AsyncCountRedisDAO asyncCountRedisDAO;
    @Resource
    private StoreCartShareMapper storeCartShareMapper;
    @Resource
    private ExpressMapper expressMapper;

    @Resource
    private MpAccountService mpAccountService;
    @Resource
    private StoreRevenueMapper storeRevenueMapper;
    @Resource
    private StoreShopMapper storeShopMapper;


    @Value("${yshop.demo}")
    private Boolean isDemo;


    @Override
    public Long createStoreOrder(StoreOrderCreateReqVO createReqVO) {
        // 插入
        StoreOrderDO storeOrder = StoreOrderConvert.INSTANCE.convert(createReqVO);
        storeOrderMapper.insert(storeOrder);
        // 返回
        return storeOrder.getId();
    }

    @Override
    public void updateStoreOrder(StoreOrderUpdateReqVO updateReqVO) {
        // 校验存在
        // 更新
        StoreOrderDO updateObj = StoreOrderConvert.INSTANCE.convert(updateReqVO);
        StoreOrderDO updateObj2 = storeOrderMapper.selectById(updateReqVO.getId());
        if (updateObj2 == null) {
            throw exception(STORE_ORDER_NOT_EXISTS);
        }

        //发货自取模式 直接收货
        if(UpdateOrderEnum.ORDER_SEND.getValue().equals(updateReqVO.getUpdateType())
                && updateObj.getOrderType().equals(OrderLogEnum.ORDER_TAKE_IN.getValue())){
           this.takeStoreOrder(updateObj.getId());
            //异步打印小票
           if(!isDemo){
               asyncStoreOrderService.print(updateObj2);
           }
            StoreOrderCartInfoDO storeOrderCartInfoDO = new StoreOrderCartInfoDO();
            storeOrderCartInfoDO.setIsOrder(ShopCommonEnum.IS_STATUS_1.getValue());
            storeOrderCartInfoMapper.update(storeOrderCartInfoDO,new LambdaQueryWrapper<StoreOrderCartInfoDO>()
                    .eq(StoreOrderCartInfoDO::getOid,updateObj.getId()));
           return;
        }
        //发货-外卖模式
        if(UpdateOrderEnum.ORDER_SEND.getValue().equals(updateReqVO.getUpdateType())
                && updateObj.getOrderType().equals(OrderLogEnum.ORDER_TAKE_OUT.getValue())){
            updateObj.setStatus(OrderInfoEnum.STATUS_1.getValue());
            //异步打印小票
            if(!isDemo){
                asyncStoreOrderService.print(updateObj2);
            }
        }
        //堂食模式
        if(UpdateOrderEnum.ORDER_SEND.getValue().equals(updateReqVO.getUpdateType())
                && updateObj.getOrderType().equals(OrderLogEnum.ORDER_TAKE_DESK.getValue())){
            updateObj.setStatus(OrderInfoEnum.STATUS_1.getValue());

            //异步打印小票
            if(!isDemo){
                asyncStoreOrderService.print(updateObj2);
            }
        }
        storeOrderMapper.updateById(updateObj);

        if(UpdateOrderEnum.ORDER_SEND.getValue().equals(updateReqVO.getUpdateType())){
            //增加状态
            storeOrderStatusService.create(updateObj.getUid(),updateObj.getId(), OrderLogEnum.DELIVERY_GOODS.getValue(),
                    "已发货 快递公司：" + updateObj.getDeliveryName() + "快递单号：" + updateObj.getDeliveryId());

            StoreOrderCartInfoDO storeOrderCartInfoDO = new StoreOrderCartInfoDO();
            storeOrderCartInfoDO.setIsOrder(ShopCommonEnum.IS_STATUS_1.getValue());
            storeOrderCartInfoMapper.update(storeOrderCartInfoDO,new LambdaQueryWrapper<StoreOrderCartInfoDO>()
                    .eq(StoreOrderCartInfoDO::getOid,updateObj.getId()));

            MemberUserDO userInfo = userService.getUser(updateObj.getUid());

            if (PayTypeEnum.WEIXIN.getValue().equals(updateReqVO.getPayType())) {
                sendAppletOrderDeliveryNotice(updateObj, userInfo);
            }

            //发送消息队列进行推送消息
//            if(userInfo.getLoginType().equals(AppFromEnum.ROUNTINE.getValue())){
//                weixinNoticeProducer.sendNoticeMessage(updateObj.getUid(), WechatTempateEnum.DELIVERY_SUCCESS.getValue(),
//                        WechatTempateEnum.SUBSCRIBE.getValue(),updateObj.getOrderId(),
//                        updateObj.getPayPrice().toString(),"",updateObj.getDeliveryName(),
//                        updateObj.getDeliveryId());
//            }else if(userInfo.getLoginType().equals(AppFromEnum.WECHAT.getValue())){
//                weixinNoticeProducer.sendNoticeMessage(updateObj.getUid(),WechatTempateEnum.PAY_SUCCESS.getValue(),
//                        WechatTempateEnum.TEMPLATES.getValue(),updateObj.getOrderId(),
//                        updateObj.getPayPrice().toString(),"",updateObj.getDeliveryName(),
//                        updateObj.getDeliveryId());
//            }

            //延时队列 1小时候自动收货
            try {
                RBlockingDeque<Object> blockingDeque = redissonClient.getBlockingDeque(ShopConstants.REDIS_ORDER_OUTTIME_UNCONFIRM);
                RDelayedQueue<Object> delayedQueue = redissonClient.getDelayedQueue(blockingDeque);
                delayedQueue.offer(OrderMsg.builder().orderId(updateReqVO.getOrderId()).build(), ShopConstants.ORDER_OUTTIME_UNCONFIRM, TimeUnit.MINUTES);
                String s = TimeUnit.SECONDS.toSeconds(ShopConstants.ORDER_OUTTIME_UNCONFIRM) + "分钟";
                log.info("添加延时队列成功 ，延迟时间：" + s);
            } catch (Exception e) {
                log.error(e.getMessage());
            }

        }

    }

    @Override
    public void deleteStoreOrder(Long id) {
        // 校验存在
        validateStoreOrderExists(id);
        // 删除
        StoreOrderDO storeOrderDO = StoreOrderDO.builder()
                .isSystemDel(ShopCommonEnum.DELETE_1.getValue())
                .id(id)
                .build();
        storeOrderMapper.updateById(storeOrderDO);
    }

    @Override
    public void payStoreOrder(Long id) {
        // 校验存在
        validateStoreOrderExists(id);
        StoreOrderDO storeOrderDO = storeOrderMapper.selectById(id);
        //下线支付
        appStoreOrderService.paySuccess(storeOrderDO.getOrderId(),PayTypeEnum.CASH.getValue());
    }

    @Override
    public void takeStoreOrder(Long id) {
        StoreOrderDO storeOrderDO = storeOrderMapper.selectById(id);
        appStoreOrderService.takeOrder(storeOrderDO.getOrderId(),storeOrderDO.getUid());
    }


    private void validateStoreOrderExists(Long id) {
        if (storeOrderMapper.selectById(id) == null) {
            throw exception(STORE_ORDER_NOT_EXISTS);
        }
    }

    public List<AppDeskOrderVo> doOrderInfo(List<StoreOrderCartInfoDO> storeOrderCartInfoDOList){
        List<AppDeskOrderVo> appDeskOrderVos = new ArrayList<>();
        Integer lastAddProductMark = storeOrderCartInfoDOList.get(0).getAddProductMark();
        while (lastAddProductMark  >= 0){
            List<StoreOrderCartInfoDO> orderCartInfoDOS = appStoreOrderService.streamData(lastAddProductMark,storeOrderCartInfoDOList);
            AppDeskOrderVo appDeskOrderVo = BeanUtils.toBean(orderCartInfoDOS.get(0),AppDeskOrderVo.class);
            List<AppDeskOrderGoodsVo> appDeskOrderGoodsVos = BeanUtils.toBean(orderCartInfoDOS,AppDeskOrderGoodsVo.class);
            appDeskOrderVo.setAppDeskOrderGoodsVos(appDeskOrderGoodsVos);
            appDeskOrderVos.add(appDeskOrderVo);
            lastAddProductMark --;
        }
        return appDeskOrderVos;
    }

    @Override
    public StoreOrderRespVO getStoreOrder(Long id) {
        StoreOrderDO storeOrderDO = storeOrderMapper.selectById(id);
        StoreOrderRespVO storeOrderRespVO = BeanUtils.toBean(storeOrderDO,StoreOrderRespVO.class);
        MemberUserDO memberUserDO = memberUserMapper.selectById(storeOrderRespVO.getUid());
        UserRespVO  userRespVO = UserConvert.INSTANCE.convert4(memberUserDO);
        storeOrderRespVO.setUserRespVO(userRespVO);

        LambdaQueryWrapper<StoreOrderCartInfoDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StoreOrderCartInfoDO::getOid,storeOrderDO.getId()).orderByDesc(StoreOrderCartInfoDO::getId);;
        List<StoreOrderCartInfoDO> storeOrderCartInfoDOList = storeOrderCartInfoMapper.selectList(wrapper);
        storeOrderRespVO.setStoreOrderCartInfoDOList(storeOrderCartInfoDOList);

        storeOrderRespVO.setStatusStr(this.handleOrderStatus(storeOrderRespVO.getPaid()
                ,storeOrderRespVO.getStatus(),storeOrderRespVO.getRefundStatus(),storeOrderRespVO.getIsSystemDel()));
        if(ObjectUtil.isNotNull(storeOrderDO.getDeskId())){
            ShopDeskDO shopDeskDO = shopDeskMapper.selectById(storeOrderDO.getDeskId());
            storeOrderRespVO.setShopDeskDO(shopDeskDO);
        }

        if(!OrderLogEnum.ORDER_TAKE_DUE.getValue().equals(storeOrderDO.getOrderType())  && storeOrderCartInfoDOList != null && !storeOrderCartInfoDOList.isEmpty()){
            storeOrderRespVO.setAppDeskOrderVo(doOrderInfo(storeOrderCartInfoDOList));
        }

        if(StrUtil.isNotEmpty(storeOrderDO.getSameCityOrderId())){
            ExpressDO expressDO = expressMapper.selectOne(new LambdaQueryWrapper<ExpressDO>()
                    .eq(ExpressDO::getType, ExpressTypeEnum.TYPE_1.getValue())
                    .eq(ExpressDO::getIsMain,ShopCommonEnum.DEFAULT_1.getValue()));
            storeOrderRespVO.setExpressName(expressDO.getName());
        }

        return storeOrderRespVO;
    }

    @Override
    public StoreOrderRespVO getStoreOrder(String orderId) {
        if(StrUtil.isBlank(orderId)){
            return new StoreOrderRespVO();
        }

        StoreOrderDO storeOrderDO = storeOrderMapper.selectOne(new LambdaQueryWrapper<StoreOrderDO>()
                .eq(StoreOrderDO::getOrderId,orderId).ne(StoreOrderDO::getStatus,OrderInfoEnum.STATUS_3.getValue()));
        if(storeOrderDO == null) {
            return new StoreOrderRespVO();
        }
        StoreOrderRespVO storeOrderRespVO = StoreOrderConvert.INSTANCE.convert(storeOrderDO);
        MemberUserDO memberUserDO = memberUserMapper.selectById(storeOrderRespVO.getUid());
        UserRespVO  userRespVO = UserConvert.INSTANCE.convert4(memberUserDO);
        storeOrderRespVO.setUserRespVO(userRespVO);

        LambdaQueryWrapper<StoreOrderCartInfoDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StoreOrderCartInfoDO::getOid,storeOrderDO.getId()).orderByDesc(StoreOrderCartInfoDO::getId);;
        List<StoreOrderCartInfoDO> storeOrderCartInfoDOList = storeOrderCartInfoMapper.selectList(wrapper);
        storeOrderRespVO.setStoreOrderCartInfoDOList(storeOrderCartInfoDOList);

        storeOrderRespVO.setStatusStr(this.handleOrderStatus(storeOrderRespVO.getPaid()
                ,storeOrderRespVO.getStatus(),storeOrderRespVO.getRefundStatus(),storeOrderRespVO.getIsSystemDel()));
        if(ObjectUtil.isNotNull(storeOrderDO.getDeskId())){
            ShopDeskDO shopDeskDO = shopDeskMapper.selectById(storeOrderDO.getDeskId());
            storeOrderRespVO.setShopDeskDO(shopDeskDO);
        }
        if(!OrderLogEnum.ORDER_TAKE_DUE.getValue().equals(storeOrderDO.getOrderType())  && storeOrderCartInfoDOList != null && !storeOrderCartInfoDOList.isEmpty()){
            storeOrderRespVO.setAppDeskOrderVo(doOrderInfo(storeOrderCartInfoDOList));
        }

        return storeOrderRespVO;
    }

    @Override
    public List<StoreOrderDO> getStoreOrderList(Long deskId) {
        return storeOrderMapper.selectList(new LambdaQueryWrapper<StoreOrderDO>()
                .eq(StoreOrderDO::getOrderType,OrderLogEnum.ORDER_TAKE_DUE.getValue())
                .eq(StoreOrderDO::getDeskId,deskId)
                .eq(StoreOrderDO::getDueStatus,OrderDueStatusEnum.DUE_STATUS_1.getValue())
                .orderByAsc(StoreOrderDO::getDueTime));
    }

    /**
     * 订单查询
     * @param pageReqVO 分页查询
     * @return
     */
    @Override
    public PageResult<StoreOrderRespVO> getStoreOrderPage(StoreOrderPageReqVO pageReqVO) {
        PageResult<StoreOrderDO> pageResult =  storeOrderMapper.selectPage(pageReqVO);
        PageResult<StoreOrderRespVO> storeOrderRespVO =  StoreOrderConvert.INSTANCE.convertPage(pageResult);
        for (StoreOrderRespVO storeOrderRespVO1 : storeOrderRespVO.getList()) {
            LambdaQueryWrapper<StoreOrderCartInfoDO> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(StoreOrderCartInfoDO::getOid,storeOrderRespVO1.getId());
            List<StoreOrderCartInfoDO> storeOrderCartInfoDOList = storeOrderCartInfoMapper.selectList(wrapper);
            MemberUserDO memberUserDO = memberUserMapper.selectById(storeOrderRespVO1.getUid());
            UserRespVO  userRespVO = UserConvert.INSTANCE.convert4(memberUserDO);
            storeOrderRespVO1.setStoreOrderCartInfoDOList(storeOrderCartInfoDOList);
            storeOrderRespVO1.setUserRespVO(userRespVO);
            storeOrderRespVO1.setStatusStr(this.handleOrderStatus(storeOrderRespVO1.getPaid()
                    ,storeOrderRespVO1.getStatus(),storeOrderRespVO1.getRefundStatus(),storeOrderRespVO1.getIsSystemDel()));
            if(ObjectUtil.isNotNull(storeOrderRespVO1.getDeskId())){
                ShopDeskDO shopDeskDO = shopDeskMapper.selectById(storeOrderRespVO1.getDeskId());
                storeOrderRespVO1.setShopDeskDO(shopDeskDO);
            }
        }
        return storeOrderRespVO;
    }

    @Override
    public List<StoreOrderDO> getStoreOrderList(StoreOrderExportReqVO exportReqVO) {
        return storeOrderMapper.selectList(exportReqVO);
    }

    /**
     * 确认订单退款
     *
     * @param id 单号
     * @param price   金额
     * @param type    ShopCommonEnum
     * @param salesId 售后id
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void orderRefund(Long id, BigDecimal price, Integer type, Long salesId) {

        StoreOrderDO storeOrderDO = storeOrderMapper.selectById(id);
        if (storeOrderDO == null) {
            throw exception(STORE_ORDER_NOT_EXISTS);
        }

        MemberUserDO userQueryVo = userService.getById(storeOrderDO.getUid());
        if (ObjectUtil.isNull(userQueryVo)) {
            throw exception(USER_NOT_EXISTS);
        }

        if (OrderInfoEnum.REFUND_STATUS_2.getValue().equals(storeOrderDO.getRefundStatus())) {
            throw exception(ORDER_REFUNDED);
        }

        if (storeOrderDO.getPayPrice().compareTo(price) < 0) {
            throw exception(ORDER_PRICE_ERROR);
        }

        storeOrderDO.setRefundStatus(OrderInfoEnum.REFUND_STATUS_2.getValue());
        storeOrderDO.setRefundPrice(price);
        storeOrderMapper.updateById(storeOrderDO);

        //生成分布式唯一值用于退款订单
        String orderSn = IdUtil.getSnowflake(0, 0).nextIdStr();
        BigDecimal balance = userQueryVo.getNowMoney();
        //根据支付类型不同退款不同
        if (PayTypeEnum.YUE.getValue().equals(storeOrderDO.getPayType())) {
            //退款到余额
            userService.incMoney(storeOrderDO.getUid(), price);
            balance = balance.add(price);

        } else if (PayTypeEnum.WEIXIN.getValue().equals(storeOrderDO.getPayType())) {
            if(isDemo){
                throw exception(new ErrorCode(888888,"演示模式没有配置证书无法微信退款的哦！"));
            }
            log.error("{},{},{},{}",orderSn,storeOrderDO.getOrderId(),price,storeOrderDO.getPayPrice());
            RefundOrder refundOrder = new RefundOrder(orderSn,"",storeOrderDO.getOrderId(),price,storeOrderDO.getPayPrice());
            Long tenantId = TenantContextHolder.getTenantId();
            WxRefundResult refundResult = null;
            try {
                refundResult = (WxRefundResult)manager.refund(PayIdEnum.WX_MINIAPP.getValue() + tenantId, refundOrder);
            }catch (Exception e){
                throw exception(new ErrorCode(202510220,e.getMessage()));
            }
           // WxRefundResult refundResult = (WxRefundResult)manager.refund(PayIdEnum.WX_MINIAPP.getValue() + tenantId, refundOrder);
            log.info("微信退款结果：{}",refundResult);
            log.info("微信退款结果：{}",refundResult.getStatus());
            log.info("微信退款结果：{}",refundResult.getOutTradeNo());
            //return;
            if(!refundResult.getStatus().equals("SUCCESS") && !refundResult.getStatus().equals("PROCESSING")){
                throw exception(new ErrorCode(202506281,"退款失败,请联系管理员处理"));
            }
        }else if (PayTypeEnum.ALI.getValue().equals(storeOrderDO.getPayType())){
            RefundOrder refundOrder = new RefundOrder(orderSn,"",storeOrderDO.getOrderId(),price,storeOrderDO.getPayPrice());
            Long tenantId = TenantContextHolder.getTenantId();
            RefundResult refundResult = manager.refund(PayIdEnum.ALI_H5.getValue() + tenantId, refundOrder);
            if(!refundResult.getMsg().equals("Success")){
                log.error("支付退款错误：{}",refundResult.getMsg());
                throw exception(new ErrorCode(202506280,refundResult.getMsg()));
            }
           // throw exception(new ErrorCode(999997,"支付宝暂时不支持退款"));
        }

        //退款成功处理门店流水问题，如果当前订单流水未结算，直接进行删除，如果已经结算，则增进支出流水减去门店账号余额
        StoreRevenueDO storeRevenueDO = storeRevenueMapper.selectOne(new LambdaQueryWrapper<StoreRevenueDO>()
                .eq(StoreRevenueDO::getOrderId,storeOrderDO.getOrderId())
                .eq(StoreRevenueDO::getType,ShopCommonEnum.ADD_1.getValue()));
        if(storeRevenueDO != null){
            if(ShopCommonEnum.IS_FINISH_0.getValue().equals(storeRevenueDO.getIsFinish())){
                storeRevenueMapper.deleteById(storeRevenueDO);
            }else {
               int result = storeShopMapper.decMoney(storeRevenueDO.getShopId(),price);
               Integer isFinish = 1;
               if(result == 0){//扣款失败
                   //门店账号余额不足，提现了 会出现，增进未结算的待支出流水,到时候会通过定时任务去结算
                   isFinish = 0;

               }
               //增加支出流水
               StoreRevenueDO storeRevenueDO2 = StoreRevenueDO.builder()
                        .orderId(storeOrderDO.getOrderId())
                        .shopId(storeOrderDO.getShopId())
                        .shopName(storeOrderDO.getShopName())
                        .uid(storeOrderDO.getUid())
                        .amount(price)
                        .isFinish(isFinish)
                        .type(ShopCommonEnum.ADD_2.getValue())
                        .build();
                storeRevenueMapper.insert(storeRevenueDO2);
            }
        }

        //增加流水
        billService.income(storeOrderDO.getUid(), "商品退款", BillDetailEnum.CATEGORY_1.getValue(),
                BillDetailEnum.TYPE_5.getValue(),
                price.doubleValue(),
                balance.doubleValue(),
                "订单退款到余额" + price + "元", storeOrderDO.getId().toString(),orderSn,null);
        storeOrderStatusService.create(storeOrderDO.getUid(), storeOrderDO.getId(),
                OrderLogEnum.REFUND_ORDER_SUCCESS.getValue(), "退款给用户：" + price + "元");
        //如果是外卖同城配送 取消配送
        //appStoreOrderService.sameCityCancelOrder(id);

        //退库存
        this.regressionStock(storeOrderDO,0);

    }

    /**
     * 订单30s内通知
     * @return
     */
    @Override
    public Long orderNotice() {
        LambdaQueryWrapper<StoreOrderDO> wrapper = new LambdaQueryWrapper();
        Long shopId = SecurityFrameworkUtils.getLoginUser().getShopId();
        if(shopId > 0) {
            wrapper.eq(StoreOrderDO::getShopId,shopId);
        }
        wrapper.eq(StoreOrderDO::getPaid, OrderInfoEnum.PAY_STATUS_1.getValue());
        LocalDateTime nowTime = LocalDateTime.now();
        wrapper.ge(StoreOrderDO::getCreateTime,nowTime.minusSeconds(30));

        return  storeOrderMapper.selectCount(wrapper);

    }

    @Override
    public void cancelDueOrder(Long id) {
        StoreOrderDO storeOrderDO  = storeOrderMapper.selectById(id);
        if(!OrderDueStatusEnum.DUE_STATUS_1.getValue().equals(storeOrderDO.getDueStatus())){
            throw exception(new ErrorCode(20246240,"改订单不是预约中，不可以取消"));
        }
        storeOrderDO.setDueStatus(OrderDueStatusEnum.DUE_STATUS_2.getValue());
        storeOrderMapper.updateById(storeOrderDO);
        //如果当前桌面没有预约订单了，桌面预约状态回复默认
        Long count = storeOrderMapper.selectCount(new LambdaQueryWrapper<StoreOrderDO>()
                .eq(StoreOrderDO::getOrderType,OrderLogEnum.ORDER_TAKE_DUE.getValue())
                .eq(StoreOrderDO::getDeskId,storeOrderDO.getDeskId())
                .eq(StoreOrderDO::getDueStatus,OrderDueStatusEnum.DUE_STATUS_1.getValue()));
        if(count == 0){
            shopDeskMapper.update(new ShopDeskDO().setBookStatus(ShopCommonEnum.IS_STATUS_0.getValue()),
                    new LambdaQueryWrapper<ShopDeskDO>()
                            .eq(ShopDeskDO::getId,storeOrderDO.getDeskId()));
        }
    }

    @Override
    public OrderTimeDataDto orderCount() {
        asyncStoreOrderService.getOrderTimeData();
        OrderTimeDataDto orderTimeDataDto = asyncCountRedisDAO.get();
        Long shopId = SecurityFrameworkUtils.getLoginUser().getShopId();
        List<ProductTopVO> productTopVO = storeOrderMapper.getGoodsTopList((new LambdaQueryWrapper<StoreProductDO>()
                .eq(shopId > 0,StoreProductDO::getShopId,shopId)));
        List<UserTopVO> userTopVOS = storeOrderMapper.getUserTopList();
        if(orderTimeDataDto != null && productTopVO != null){
            orderTimeDataDto.setProductTopVO(productTopVO);
        }

        if(orderTimeDataDto != null && userTopVOS != null){
            orderTimeDataDto.setUserTopVO(userTopVOS);
        }


        return orderTimeDataDto;
    }

    @Override
    public void syncCartInfo(UserCartMsgVo userCartMsgVo) {
        Long uid = SecurityFrameworkUtils.getLoginUserId();
        userCartMsgVo.setUid(uid);
        MemberUserDO memberUserDO = memberUserMapper.selectById(uid);
        userCartMsgVo.setUName(memberUserDO.getNickname());
        StoreCartShareDO storeCartShareDO = BeanUtils.toBean(userCartMsgVo,StoreCartShareDO.class);
        storeCartShareDO.setContent(JSONArray.parseArray(JSON.toJSONString(userCartMsgVo.getContent())));
        StoreCartShareDO storeCartShareDO1 =  storeCartShareMapper.selectOne(new LambdaQueryWrapper<StoreCartShareDO>()
                .eq(StoreCartShareDO::getShopId,userCartMsgVo.getShopId())
                .eq(StoreCartShareDO::getDeskId,userCartMsgVo.getDeskId())
                .eq(StoreCartShareDO::getUid,uid));
        if(storeCartShareDO1 == null){
            storeCartShareMapper.insert(storeCartShareDO);
        }else{
            storeCartShareDO1.setContent(JSONArray.parseArray(JSON.toJSONString(userCartMsgVo.getContent())));
            storeCartShareMapper.updateById(storeCartShareDO1);
        }
        //异步发布websocket广播消息
        asyncStoreOrderService.pubCartInfo(userCartMsgVo);
    }

    @Override
    public List<StoreCartShareDO> getShareCart(Long shopId, Long deskId) {
        Long uid = SecurityFrameworkUtils.getLoginUserId();
        return storeCartShareMapper.selectList(new LambdaQueryWrapper<StoreCartShareDO>()
                .eq(StoreCartShareDO::getShopId,shopId)
                .eq(StoreCartShareDO::getDeskId,deskId)
                .ne(StoreCartShareDO::getUid,uid));
    }

    @Override
    public void printOrder(Long id) {
       StoreOrderDO storeOrderDO = storeOrderMapper.selectById(id);
       asyncStoreOrderService.print(storeOrderDO);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void claerDesk(Long id) {
        ShopDeskDO shopDeskDO = shopDeskMapper.selectById(id);
        //清空桌面
        shopDeskDO.setLastOrderStatus(OrderInfoEnum.DESK_ORDER_STATUS_CONFIRM.getValue());
        shopDeskMapper.updateById(shopDeskDO);

        //删除当前桌面单共享菜单（如果有）
        storeCartShareMapper.delete(new LambdaQueryWrapper<StoreCartShareDO>()
                .eq(StoreCartShareDO::getShopId,shopDeskDO.getShopId())
                .eq(StoreCartShareDO::getDeskId,id));
        //如果有订单删除
        if(StrUtil.isNotBlank(shopDeskDO.getLastOrderNo())){
            StoreOrderDO storeOrderDO = storeOrderMapper.selectOne(new LambdaQueryWrapper<StoreOrderDO>()
                    .eq(StoreOrderDO::getOrderId,shopDeskDO.getLastOrderNo()));
            if(storeOrderDO != null && !storeOrderDO.getPaid().equals(OrderInfoEnum.PAY_STATUS_1.getValue())){
                storeOrderDO.setIsSystemDel(ShopCommonEnum.DELETE_1.getValue());
                storeOrderMapper.deleteById(storeOrderDO.getId());
                //增加状态
                storeOrderStatusService.create(storeOrderDO.getUid(),storeOrderDO.getId(), OrderLogEnum.REMOVE_ORDER.getValue(),
                        OrderLogEnum.OFFLINE_PAY.getDesc());
            }
        }

    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void changeDesk(Long sourceId,Long targetId) {
        //清空原桌台
        ShopDeskDO shopDeskDO = shopDeskMapper.selectById(sourceId);
        shopDeskDO.setLastOrderStatus(OrderInfoEnum.DESK_ORDER_STATUS_CONFIRM.getValue());
        //目前桌台
        ShopDeskDO shopDeskDO2 = shopDeskMapper.selectById(targetId);
        if(OrderInfoEnum.DESK_ORDER_STATUS_ING.getValue().equals(shopDeskDO2.getLastOrderStatus())){
            throw exception(new ErrorCode(202507250,"当前目标桌台正在就餐中,请重新选择"));
        }
        //查询订单
        if(StrUtil.isNotBlank(shopDeskDO.getLastOrderNo())){
            StoreOrderDO storeOrderDO = storeOrderMapper.selectOne(new LambdaQueryWrapper<StoreOrderDO>()
                    .eq(StoreOrderDO::getOrderId,shopDeskDO.getLastOrderNo()));
            if(storeOrderDO != null && !storeOrderDO.getPaid().equals(OrderInfoEnum.PAY_STATUS_1.getValue())){
                //更换目标桌台信息
                storeOrderDO.setDeskNumber(shopDeskDO2.getNumber());
                storeOrderDO.setDeskId(shopDeskDO2.getId());
                storeOrderMapper.updateById(storeOrderDO);

                //目前桌台更换原有桌台订单编号
                shopDeskDO2.setLastOrderNo(shopDeskDO.getLastOrderNo());
                shopDeskDO2.setLastOrderTime(shopDeskDO.getLastOrderTime());
                shopDeskDO.setLastOrderNo(null);
                shopDeskDO.setLastOrderTime(null);
            }
        }else{
            shopDeskDO2.setLastOrderNo(null);
        }
        //设置就餐中
        shopDeskDO2.setLastOrderStatus(OrderInfoEnum.DESK_ORDER_STATUS_ING.getValue());
        //更新目标桌台
        shopDeskMapper.updateById(shopDeskDO2);
        //更新原来桌台
        shopDeskMapper.updateById(shopDeskDO);

    }

    @Override
    public Long getDueOrderCount(Long deskId) {
        //获取当天时间单预约订单
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime begin = now.withHour(0).withMinute(0).withSecond(0);
        LocalDateTime end = now.withHour(23).withMinute(59).withSecond(59);
        return storeOrderMapper.selectCount(new LambdaQueryWrapper<StoreOrderDO>()
                .eq(StoreOrderDO::getOrderType,OrderLogEnum.ORDER_TAKE_DUE.getValue())
                .eq(StoreOrderDO::getDeskId,deskId)
                .between(StoreOrderDO::getDueTime,begin,end)
                .eq(StoreOrderDO::getDueStatus,OrderDueStatusEnum.DUE_STATUS_1.getValue()));
    }

    @Override
    public StoreOrderDO getDueOrder(Long deskId,Long uid) {
        return storeOrderMapper.selectOne(new LambdaQueryWrapper<StoreOrderDO>()
                .eq(StoreOrderDO::getOrderType,OrderLogEnum.ORDER_TAKE_DUE.getValue())
                .eq(StoreOrderDO::getDeskId,deskId)
                .eq(StoreOrderDO::getUid,uid)
                .eq(StoreOrderDO::getDueStatus,OrderDueStatusEnum.DUE_STATUS_1.getValue()));
    }

    @Override
    public boolean todayDueOrder(String orderId){
        //获取当天时间单预约订单
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime begin = now.withHour(0).withMinute(0).withSecond(0);
        LocalDateTime end = now.withHour(23).withMinute(59).withSecond(59);
        return storeOrderMapper.selectCount(new LambdaQueryWrapper<StoreOrderDO>()
                .eq(StoreOrderDO::getOrderType,OrderLogEnum.ORDER_TAKE_DUE.getValue())
                .eq(StoreOrderDO::getOrderId,orderId)
                .between(StoreOrderDO::getDueTime,begin,end)
                .eq(StoreOrderDO::getDueStatus,OrderDueStatusEnum.DUE_STATUS_1.getValue())) > 0;
    }


    private void sendAppletOrderDeliveryNotice(StoreOrderDO orderDO, MemberUserDO memberUserDO) {
        if (isDemo) return;
        log.info("===发货开始=====");
        WxMaService wxMaService = mpAccountService.getMaService();
        WxMaOrderShippingInfoUploadRequest infoUploadRequest = new WxMaOrderShippingInfoUploadRequest();
        OrderKeyBean orderKeyBean = new OrderKeyBean();
        orderKeyBean.setOrderNumberType(1);
        orderKeyBean.setOutTradeNo(orderDO.getOrderId());
        infoUploadRequest.setOrderKey(orderKeyBean);
        infoUploadRequest.setLogisticsType(4);
        infoUploadRequest.setDeliveryMode(1);
        ShippingListBean shippingListBean = new ShippingListBean();
        LambdaQueryWrapper<StoreOrderCartInfoDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StoreOrderCartInfoDO::getOid, orderDO.getId());
        List<StoreOrderCartInfoDO> storeOrderCartInfoDOList = storeOrderCartInfoMapper.selectList(wrapper);
        StoreOrderCartInfoDO storeOrderCartInfoDO = storeOrderCartInfoDOList.get(0);
        String desc = "";
        String sku = storeOrderCartInfoDO.getSpec();
        String name = storeOrderCartInfoDO.getTitle();
        if (storeOrderCartInfoDOList.size() <= 1) {
            desc = name + "|" + sku;
        } else {
            desc = name + "|" + sku + "等";
        }
        shippingListBean.setItemDesc(desc);
        List<ShippingListBean> shippingListBeans = new ArrayList<>();
        shippingListBeans.add(shippingListBean);
        infoUploadRequest.setShippingList(shippingListBeans);
        infoUploadRequest.setPayer(new PayerBean(memberUserDO.getRoutineOpenid()));
        try {
            WxMaOrderShippingInfoBaseResponse baseResponse = wxMaService.getWxMaOrderShippingService()
                    .upload(infoUploadRequest);
            log.error("小程序发货info:{}",baseResponse);
        } catch (WxErrorException e) {
            log.error("小程序发货error:{}",e.getMessage());
        }

    }


    /**
     * 退回库存
     *
     * @param order 订单
     */
    private void regressionStock(StoreOrderDO order, Integer type) {

        LambdaQueryWrapper<StoreOrderCartInfoDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(StoreOrderCartInfoDO::getOid, order.getId());

        List<StoreOrderCartInfoDO> cartInfoList = storeOrderCartInfoMapper.selectList(wrapper);
        for (StoreOrderCartInfoDO cartInfo : cartInfoList) {
            appStoreProductService.incProductStock(cartInfo.getNumber(), cartInfo.getProductId(), cartInfo.getSpec(),
                    0L, null);
        }
    }


    /**
     * 处理订单状态
     * @param payStatus
     * @param status
     * @param refundStatus
     * @param del
     * @return
     */
    private String handleOrderStatus(Integer payStatus,Integer status,Integer refundStatus,Integer del) {
        String statusName = "";
        if (del == 1){
            statusName = "已删除";
        }else if (payStatus == 0 && status == 0) {
            statusName = "未支付";
        } else if (payStatus == 1 && status == 0 && refundStatus == 0) {
            statusName = "未发货";
        }  else if (payStatus == 1 && status == 1 && refundStatus == 0) {
            statusName = "待收货";
        }  else if (payStatus == 1 && status == 2 && refundStatus == 0) {
            statusName = "待评价";
        } else if (payStatus == 1 && status == 3 && refundStatus == 0) {
            statusName = "已完成";
        } else if (payStatus == 1 && refundStatus == 2) {
            statusName = "已退款";
        }
        else if (payStatus == 1 && refundStatus == 1) {
            statusName = "退款中";
        }
        return statusName;
    }




}
