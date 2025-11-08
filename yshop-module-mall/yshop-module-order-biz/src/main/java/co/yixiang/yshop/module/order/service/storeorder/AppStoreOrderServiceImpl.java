package co.yixiang.yshop.module.order.service.storeorder;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import co.yixiang.yshop.framework.common.constant.ShopConstants;
import co.yixiang.yshop.framework.common.enums.OrderInfoEnum;
import co.yixiang.yshop.framework.common.enums.PayIdEnum;
import co.yixiang.yshop.framework.common.enums.ShopCommonEnum;
import co.yixiang.yshop.framework.common.exception.ErrorCode;
import co.yixiang.yshop.framework.common.util.object.BeanUtils;
import co.yixiang.yshop.framework.tenant.core.aop.TenantIgnore;
import co.yixiang.yshop.framework.tenant.core.context.TenantContextHolder;
import co.yixiang.yshop.module.card.dal.dataobject.vipcard.VipCardDO;
import co.yixiang.yshop.module.card.service.vipcard.AppVipCardService;
import co.yixiang.yshop.module.coupon.dal.dataobject.couponuser.CouponUserDO;
import co.yixiang.yshop.module.coupon.service.couponuser.AppCouponUserService;
import co.yixiang.yshop.module.desk.controller.app.shopdesk.vo.AppShopDeskVO;
import co.yixiang.yshop.module.desk.dal.dataobject.shopdesk.ShopDeskDO;
import co.yixiang.yshop.module.desk.dal.mysql.shopdesk.ShopDeskMapper;
import co.yixiang.yshop.module.express.controller.app.express.vo.SameCityBodyVO;
import co.yixiang.yshop.module.express.controller.app.express.vo.SameCityCallBackBodyVO;
import co.yixiang.yshop.module.express.controller.app.express.vo.SameCityCallBackParmVO;
import co.yixiang.yshop.module.express.controller.app.express.vo.SameCityExpressVO;
import co.yixiang.yshop.module.express.dal.dataobject.express.ExpressDO;
import co.yixiang.yshop.module.express.dal.mysql.express.ExpressMapper;
import co.yixiang.yshop.module.express.dal.redis.express.ExpressRedisDAO;
import co.yixiang.yshop.module.express.enums.ExpressCancelEnum;
import co.yixiang.yshop.module.express.enums.ExpressGoodsTypeEnum;
import co.yixiang.yshop.module.express.enums.ExpressTypeEnum;
import co.yixiang.yshop.module.express.kdniao.model.dto.KdhundredApiBaseDTO;
import co.yixiang.yshop.module.member.controller.app.user.vo.AppUserQueryVo;
import co.yixiang.yshop.module.member.dal.dataobject.user.MemberUserDO;
import co.yixiang.yshop.module.member.dal.dataobject.useraddress.UserAddressDO;
import co.yixiang.yshop.module.member.dal.dataobject.userbill.UserBillDO;
import co.yixiang.yshop.module.member.enums.BillDetailEnum;
import co.yixiang.yshop.module.member.service.user.MemberUserService;
import co.yixiang.yshop.module.member.service.useraddress.AppUserAddressService;
import co.yixiang.yshop.module.member.service.userbill.UserBillService;
import co.yixiang.yshop.module.message.enums.WechatTempateEnum;
import co.yixiang.yshop.module.message.mq.message.WeixinNoticeMessage;
import co.yixiang.yshop.module.message.mq.producer.WeixinNoticeProducer;
import co.yixiang.yshop.module.message.redismq.msg.OrderMsg;
import co.yixiang.yshop.module.order.controller.app.order.param.AppOrderParam;
import co.yixiang.yshop.module.order.controller.app.order.param.AppPayParam;
import co.yixiang.yshop.module.order.controller.app.order.vo.AppDeskOrderGoodsVo;
import co.yixiang.yshop.module.order.controller.app.order.vo.AppDeskOrderVo;
import co.yixiang.yshop.module.order.controller.app.order.vo.AppStoreOrderQueryVo;
import co.yixiang.yshop.module.order.controller.app.order.vo.message.CartMsgVo;
import co.yixiang.yshop.module.order.controller.app.order.vo.message.ShopOrderMsgVo;
import co.yixiang.yshop.module.order.convert.storeorder.StoreOrderConvert;
import co.yixiang.yshop.module.order.dal.dataobject.ordernumber.OrderNumberDO;
import co.yixiang.yshop.module.order.dal.dataobject.storecartshare.StoreCartShareDO;
import co.yixiang.yshop.module.order.dal.dataobject.storeorder.StoreOrderDO;
import co.yixiang.yshop.module.order.dal.dataobject.storeordercartinfo.StoreOrderCartInfoDO;
import co.yixiang.yshop.module.order.dal.mysql.ordernumber.OrderNumberMapper;
import co.yixiang.yshop.module.order.dal.mysql.storecartshare.StoreCartShareMapper;
import co.yixiang.yshop.module.order.dal.mysql.storeorder.StoreOrderMapper;
import co.yixiang.yshop.module.order.enums.*;
import co.yixiang.yshop.module.order.service.storeorder.dto.StatusDto;
import co.yixiang.yshop.module.order.service.storeordercartinfo.StoreOrderCartInfoService;
import co.yixiang.yshop.module.order.service.storeorderstatus.StoreOrderStatusService;
import co.yixiang.yshop.module.pay.dal.dataobject.merchantdetails.MerchantDetailsDO;
import co.yixiang.yshop.module.pay.service.merchantdetails.MerchantDetailsService;
import co.yixiang.yshop.module.product.dal.dataobject.storeproduct.StoreProductDO;
import co.yixiang.yshop.module.product.dal.dataobject.storeproductattrvalue.StoreProductAttrValueDO;
import co.yixiang.yshop.module.product.service.storeproduct.AppStoreProductService;
import co.yixiang.yshop.module.product.service.storeproductattrvalue.StoreProductAttrValueService;
import co.yixiang.yshop.module.score.dal.dataobject.scoreorder.ScoreOrderDO;
import co.yixiang.yshop.module.score.dal.mysql.scoreorder.ScoreOrderMapper;
import co.yixiang.yshop.module.shop.dal.dataobject.recharge.RechargeDO;
import co.yixiang.yshop.module.shop.service.recharge.AppRechargeService;
import co.yixiang.yshop.module.store.convert.storeshop.StoreShopConvert;
import co.yixiang.yshop.module.store.dal.dataobject.storerevenue.StoreRevenueDO;
import co.yixiang.yshop.module.store.dal.dataobject.storeshop.StoreShopDO;
import co.yixiang.yshop.module.store.dal.mysql.storerevenue.StoreRevenueMapper;
import co.yixiang.yshop.module.store.service.storeshop.AppStoreShopService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.egzosn.pay.spring.boot.core.PayServiceManager;
import com.egzosn.pay.spring.boot.core.bean.MerchantPayOrder;
import com.google.gson.Gson;
import com.kuaidi100.sdk.api.BsameCityExpress;
import com.kuaidi100.sdk.core.IBaseClient;
import com.kuaidi100.sdk.pojo.HttpResult;
import com.kuaidi100.sdk.request.PrintReq;
import com.kuaidi100.sdk.request.bsamecity.BsamecityCancelReq;
import com.kuaidi100.sdk.request.bsamecity.BsamecityOrderReq;
import com.kuaidi100.sdk.request.bsamecity.Goods;
import com.kuaidi100.sdk.utils.SignUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBlockingDeque;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static co.yixiang.yshop.framework.common.exception.util.ServiceExceptionUtil.exception;
import static co.yixiang.yshop.module.member.enums.ErrorCodeConstants.COUPON_NOT_CONDITION;
import static co.yixiang.yshop.module.member.enums.ErrorCodeConstants.USER_ADDRESS_NOT_EXISTS;
import static co.yixiang.yshop.module.order.enums.ErrorCodeConstants.*;

/**
 * 订单 Service 实现类
 *
 * @author yshop
 */
@Slf4j
@Service
@Validated
public class AppStoreOrderServiceImpl extends ServiceImpl<StoreOrderMapper,StoreOrderDO> implements AppStoreOrderService {

    @Resource
    private StoreOrderMapper storeOrderMapper;

    @Resource
    private AppUserAddressService appUserAddressService;
    @Resource
    private MemberUserService userService;
    @Resource
    private AppStoreProductService appStoreProductService;
    @Resource
    private StoreOrderCartInfoService storeOrderCartInfoService;
    @Resource
    private StoreOrderStatusService storeOrderStatusService;
    @Resource
    private UserBillService billService;
    @Resource
    private PayServiceManager manager;
    @Resource
    private WeixinNoticeProducer weixinNoticeProducer;
    @Resource
    private RedissonClient redissonClient;
    @Resource
    private StoreProductAttrValueService storeProductAttrValueService;
    @Resource
    private AppStoreShopService appStoreShopService;
    @Resource
    private OrderNumberMapper orderNumberMapper;
    @Resource
    private AppCouponUserService appCouponUserService;
    @Resource
    private AsyncStoreOrderService asyncStoreOrderService;
    @Resource
    private MerchantDetailsService merchantDetailsService;
    @Resource
    private AppRechargeService appRechargeService;
    @Resource
    private ShopDeskMapper shopDeskMapper;
    @Resource
    private AppVipCardService appVipCardService;
    @Resource
    private StoreRevenueMapper storeRevenueMapper;
    @Resource
    private ScoreOrderMapper scoreOrderMapper;
    @Resource
    private StoreCartShareMapper storeCartShareMapper;
    @Resource
    private ExpressRedisDAO expressRedisDAO;
    @Resource
    private ExpressMapper expressMapper;



    private static final String LOCK_KEY = "cart:check:stock:lock";
    private static final String STOCK_LOCK_KEY = "cart:do:stock:lock";




    /**
     * 订单信息
     *
     * @param unique 唯一值或者单号
     * @param uid    用户id
     * @return YxStoreOrderQueryVo
     */
    @Override
    public AppStoreOrderQueryVo getOrderInfo(String unique, Long uid) {
        LambdaQueryWrapper<StoreOrderDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.and(
                i -> i.eq(StoreOrderDO::getOrderId, unique).or().eq(StoreOrderDO::getUnique, unique).or()
                        .eq(StoreOrderDO::getExtendOrderId, unique));
//        if (uid != null) {
//            wrapper.eq(StoreOrderDO::getUid, uid);
//        }
        StoreOrderDO storeOrderDO = storeOrderMapper.selectOne(wrapper);
        AppStoreOrderQueryVo appStoreOrderQueryVo = BeanUtils.toBean(storeOrderDO,AppStoreOrderQueryVo.class);
        if(storeOrderDO != null && StrUtil.isNotEmpty(storeOrderDO.getSameCityTaskId())){
            ExpressDO expressDO = expressMapper.selectOne(new LambdaQueryWrapper<ExpressDO>()
                    .eq(ExpressDO::getType, ExpressTypeEnum.TYPE_1.getValue())
                    .eq(ExpressDO::getIsMain,ShopCommonEnum.DEFAULT_1.getValue()));
            appStoreOrderQueryVo.setExpressName(expressDO.getName());
        }

        return appStoreOrderQueryVo;
    }


    /**
     * 创建订单
     *
     * @param uid 用户uid
     * @param param    param
     * @return YxStoreOrder
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Map<String, Object> createOrder(Long uid,  AppOrderParam param) {
        //转换参数
        List<String> productIds = param.getProductId();
        List<String> numbers = param.getNumber();
        List<String> specs = param.getSpec();
        List<Long> userIdList = new ArrayList<>();
        if((param.getIsAdmin() == null || !param.getIsAdmin()) && OrderLogEnum.ORDER_TAKE_DESK.getValue().equals(param.getOrderType())){
            if(ObjectUtil.isNull(param.getDeskId())){
                throw exception(STORE_ORDER_DESK_NOT);
            }
            //点餐走共享菜单
            List<StoreCartShareDO> storeCartShareDOS = storeCartShareMapper.selectList(new LambdaQueryWrapper<StoreCartShareDO>()
                    .eq(StoreCartShareDO::getShopId,param.getShopId()).eq(StoreCartShareDO::getDeskId,param.getDeskId()));
            if(storeCartShareDOS == null || storeCartShareDOS.isEmpty()){
                ShopDeskDO shopDeskDO = shopDeskMapper.selectById(param.getDeskId());
                Map<String,Object> map = new HashMap<>();
                map.put("orderId",shopDeskDO.getLastOrderNo());
                map.put("msg","您的菜单已被朋友提交哦！");
                return map;
               // throw exception(new ErrorCode(202504290,"您的菜单已被朋友提交哦！"));
            }
            //JSONArray content
            List<JSONArray> contents = storeCartShareDOS.stream().map(StoreCartShareDO::getContent).collect(Collectors.toList());
            productIds = new ArrayList<>();
            numbers = new ArrayList<>();
            specs = new ArrayList<>();
            userIdList = storeCartShareDOS.stream().map(StoreCartShareDO::getUid).collect(Collectors.toList());
            for (JSONArray item : contents){
                List<CartMsgVo> cartMsgVoList = JSON.parseArray(JSON.toJSONString(item),CartMsgVo.class);
                for (CartMsgVo i : cartMsgVoList){
                    productIds.add(i.getId().toString());
                }
                for (CartMsgVo i : cartMsgVoList){
                    numbers.add(i.getNumber().toString());
                }
                for (CartMsgVo i : cartMsgVoList){
                    if(StrUtil.isBlank(i.getValueStr())){
                        specs.add("默认");
                    }else{
                        specs.add(i.getValueStr());
                    }

                }

            }


        }


        Integer totalNum = 0;
        List<String> cartIds = new ArrayList<>();

        StoreShopDO storeShopDO = appStoreShopService.getById(param.getShopId());

        BigDecimal sumPrice = BigDecimal.ZERO;
        BigDecimal couponPrice = BigDecimal.ZERO;
        //外卖运费
        BigDecimal postagePrice = BigDecimal.ZERO;
       // BigDecimal postagePrice = OrderLogEnum.ORDER_TAKE_OUT.getValue().equals(param.getOrderType()) ? storeShopDO.getDeliveryPrice() : BigDecimal.ZERO;
        BigDecimal deductionPrice = param.getDeductionPrice() != null ?  param.getDeductionPrice()  : BigDecimal.ZERO;

        //对库存检查加锁
        RLock lock = redissonClient.getLock(LOCK_KEY);
        if (lock.tryLock()){
            try {
                for (int i = 0;i < productIds.size();i++){
                    String newSku = StrUtil.replace(specs.get(i),"|",",");
                    appStoreProductService.checkProductStock(uid, Long.valueOf(productIds.get(i)),
                            Integer.valueOf(numbers.get(i)), newSku);
                    totalNum += Integer.valueOf(numbers.get(i));

                    StoreProductAttrValueDO storeProductAttrValue = storeProductAttrValueService
                            .getOne(Wrappers.<StoreProductAttrValueDO>lambdaQuery()
                                    .eq(StoreProductAttrValueDO::getSku, newSku)
                                    .eq(StoreProductAttrValueDO::getProductId, productIds.get(i)));

                    sumPrice = NumberUtil.add(sumPrice, NumberUtil.mul(numbers.get(i),
                            storeProductAttrValue.getPrice().toString()));
                }

            }catch (Exception ex) {
                log.error("[checkProductStock][执行异常]", ex);
                throw exception(new ErrorCode(999999,ex.getMessage()));
            } finally {
                lock.unlock();
            }
        }

        //计算优惠券价格
        if(StrUtil.isNotBlank(param.getCouponId())){
            CouponUserDO couponUserDO = appCouponUserService.getById(param.getCouponId());
            if(couponUserDO != null){
                if(couponUserDO.getLeast().compareTo(sumPrice) > 0) {
                    throw exception(COUPON_NOT_CONDITION);
                }
                couponPrice = couponUserDO.getValue();

                //使用了优惠券扣优惠券
                couponUserDO.setStatus(ShopCommonEnum.IS_STATUS_1.getValue());
                appCouponUserService.updateById(couponUserDO);

            }
        }

        //计算会员折扣价
        if((StrUtil.isEmpty(param.getUidType()) || param.getUidType().equals("user")) && NumberUtil.compare(uid,0) > 0){
            MemberUserDO memberUserDO = userService.getById(uid);
            if(memberUserDO.getCardId() != null && memberUserDO.getCardId() > 0){
                BigDecimal vipPrice = sumPrice.subtract(sumPrice.multiply(NumberUtil.div(memberUserDO.getDiscount()+"","10")));
                deductionPrice = deductionPrice.add(vipPrice);
            }
        }

//        BigDecimal payPrice = BigDecimal.ZERO;
//        //计算最终支付价格
//        if(OrderLogEnum.ORDER_TAKE_OUT.getValue().equals(param.getOrderType())){
//            payPrice = NumberUtil.sub(NumberUtil.add(sumPrice,postagePrice),couponPrice,deductionPrice);
//        }else{
//            payPrice = NumberUtil.sub(sumPrice,couponPrice,deductionPrice);
//        }
        BigDecimal payPrice = NumberUtil.sub(NumberUtil.add(sumPrice,postagePrice),couponPrice,deductionPrice);
        //如果金额小于0直接设置0
        if(payPrice.compareTo(BigDecimal.ZERO) < 0){
            payPrice = BigDecimal.ZERO;
        }



        //计算奖励积分
        BigDecimal gainIntegral = this.getGainIntegral(productIds);

        StoreOrderDO storeOrder = new StoreOrderDO();
        String orderSn = "";
        //判断是否是加餐
        if(OrderLogEnum.ORDER_TAKE_DESK.getValue().equals(param.getOrderType())
                && StrUtil.isNotEmpty(param.getOrderId())){
            storeOrder = this.getOne(new LambdaQueryWrapper<StoreOrderDO>()
                    .eq(StoreOrderDO::getOrderId,param.getOrderId())
                    .eq(StoreOrderDO::getPaid,OrderInfoEnum.PAY_STATUS_0.getValue()));
            if(storeOrder == null) {
                throw exception(STORE_ORDER_NOT_EXISTS);
            }
            orderSn = param.getOrderId();

            if(OrderLogEnum.ORDER_TAKE_DUE.getValue().equals(storeOrder.getOrderType())){
                //增加状态
                storeOrderStatusService.create(uid,storeOrder.getId(), OrderLogEnum.SWITCH_ORDER.getValue(),
                        OrderLogEnum.SWITCH_ORDER.getDesc());
                storeOrder.setOrderType(param.getOrderType());
            }else{
                storeOrderStatusService.create(uid,storeOrder.getId(), OrderLogEnum.ADD_ORDER.getValue(),
                        OrderLogEnum.ADD_ORDER.getDesc());
            }


            storeOrder.setTotalNum(totalNum + storeOrder.getTotalNum());
            storeOrder.setTotalPrice(sumPrice.add(storeOrder.getTotalPrice()));
            storeOrder.setCouponPrice(couponPrice.add(storeOrder.getCouponPrice()));
            storeOrder.setPayPrice(payPrice.add(storeOrder.getPayPrice()));
            storeOrder.setGainIntegral(gainIntegral.add(storeOrder.getGainIntegral()));
            storeOrder.setCreateTime(LocalDateTime.now());
            storeOrder.setStatus(OrderInfoEnum.STATUS_0.getValue());
            if(StrUtil.isEmpty(param.getUidType()) || param.getUidType().equals("user")){
                if(StrUtil.isNotEmpty(storeOrder.getUserIds())){
                    List<String> userids = StrUtil.split(storeOrder.getUserIds(),",");
                    List<Long> userIdsLong = userids.stream().map(Long::valueOf).collect(Collectors.toList());
                    userIdsLong.addAll(userIdList);
                    Set<Long> set = new HashSet<>(userIdsLong);//去重复
                    storeOrder.setUserIds(CollUtil.join(set,","));
                }else{
                    storeOrder.setUserIds(CollUtil.join(userIdList,","));
                }

               // Set<Long> set = new HashSet<>(userIdsLong);//去重复
               // storeOrder.setUserIds(CollUtil.join(set,","));
//                if(!userids.contains(uid+"")){
//                    storeOrder.setUserIds(storeOrder.getUserIds() + "," + uid);
//                }
            }
            if(param.getDeskPeople() != null && param.getDeskPeople() > 0){
                storeOrder.setDeskPeople(param.getDeskPeople());
            }
            this.updateById(storeOrder);
        }else{
            //生成分布式唯一值
            orderSn = IdUtil.getSnowflake(0, 0).nextIdStr();

            //添加取餐表
            OrderNumberDO orderNumberDO = OrderNumberDO.builder().orderId(orderSn).build();
            orderNumberMapper.insert(orderNumberDO);

            //组合数据
            LocalDateTime localDateTime = LocalDateTime.now();
            storeOrder.setGetTime(localDateTime.plusMinutes(param.getGettime()));
            storeOrder.setNumberId(orderNumberDO.getId());
            storeOrder.setShopId(storeShopDO.getId());
            storeOrder.setShopName(storeShopDO.getName());
            if(StrUtil.isNotEmpty(param.getUidType()) && param.getUidType().equals("admin")){
                //如果市店员协助点餐此处不存用户ID,如果选择了会员取当前会员ID
                if(uid > 0){
                    storeOrder.setUid(uid);
                    storeOrder.setUserIds(uid+"");
                }else{
                    storeOrder.setUid(0L);
                    storeOrder.setUserIds("");
                }
            }else{
                storeOrder.setUid(uid);
                if(!userIdList.isEmpty()){
                    storeOrder.setUserIds(CollUtil.join(userIdList,","));
                }else {
                    storeOrder.setUserIds(uid+"");
                }

            }


            storeOrder.setOrderId(orderSn);
            //处理如果是外卖 地址
            if(OrderLogEnum.ORDER_TAKE_OUT.getValue().equals(param.getOrderType())){
                if (StrUtil.isEmpty(param.getAddressId())) {
                    throw exception(SELECT_ADDRESS);
                }
                UserAddressDO userAddress = appUserAddressService.getById(param.getAddressId());
                if (ObjectUtil.isNull(userAddress)) {
                    throw exception(USER_ADDRESS_NOT_EXISTS);
                }
                storeOrder.setRealName(userAddress.getRealName());
                storeOrder.setUserPhone(userAddress.getPhone());
                storeOrder.setUserAddress(userAddress.getAddress() + " " + userAddress.getDetail());
                //是否第三方外卖
                if(ShopCommonEnum.DELIVERY_2.getValue().equals(storeShopDO.getDeliveryType())){
                    KdhundredApiBaseDTO kdhundredApiBaseDTO = expressRedisDAO.get();
                    //正式环境，测试不走正式下单
                    if(ShopCommonEnum.DEFAULT_1.getValue().equals(kdhundredApiBaseDTO.getEnv())){
                        param.setPayPrice(payPrice);
                        SameCityExpressVO sameCityExpressVO = sameCityPriceOrOrder(param,"order");
                        storeOrder.setSameCityOrderId(sameCityExpressVO.getOrderId());
                        storeOrder.setSameCityTaskId(sameCityExpressVO.getTaskId());
                        storeOrder.setDeliveryId(sameCityExpressVO.getKuaidinum());
                        storeOrder.setSameCityDeliveryDistance(sameCityExpressVO.getDeliveryDistance());
                        storeOrder.setFreightPrice(new BigDecimal(sameCityExpressVO.getDiscountFee()));
                        storeOrder.setSameCityDeliveryTime(sameCityExpressVO.getEstimateDeliveryTime());
                    }

                }else{
                    storeOrder.setFreightPrice(storeShopDO.getDeliveryPrice());
                }
                //加上运费
                payPrice = payPrice.add(storeOrder.getFreightPrice());
            }
            storeOrder.setCartId(StrUtil.join(",", cartIds));
            storeOrder.setTotalNum(totalNum);
            storeOrder.setTotalPrice(sumPrice);
            //storeOrder.setTotalPostage(postagePrice);

            storeOrder.setCouponId(StrUtil.isBlank(param.getCouponId()) ? 0 : Integer.valueOf(param.getCouponId()));
            storeOrder.setCouponPrice(couponPrice);
            storeOrder.setPayPrice(payPrice);
            //storeOrder.setPayPostage(storeShopDO.getDeliveryPrice());
            storeOrder.setDeductionPrice(deductionPrice);
            storeOrder.setPaid(OrderInfoEnum.PAY_STATUS_0.getValue());
            storeOrder.setPayType(param.getPayType());
            storeOrder.setUseIntegral(BigDecimal.ZERO);
            storeOrder.setBackIntegral(BigDecimal.ZERO);
            storeOrder.setGainIntegral(gainIntegral);
            storeOrder.setMark(param.getRemark());
            storeOrder.setCost(BigDecimal.ZERO);
            //storeOrder.setUnique(key);
            storeOrder.setShippingType(OrderInfoEnum.SHIPPIING_TYPE_1.getValue());
            storeOrder.setOrderType(param.getOrderType());

            //扫码点餐
            storeOrder.setDeskId(param.getDeskId());
            storeOrder.setDeskNumber(param.getDeskNumber());
            storeOrder.setDeskPeople(param.getDeskPeople());


            boolean res = this.save(storeOrder);
            if (!res) {
                throw exception(ORDER_GEN_FAIL);
            }
        }


        // 减库存加销量
        this.deStockIncSale(productIds,numbers,specs);


        //保存购物车商品信息，异步执行
        storeOrderCartInfoService.saveCartInfo(uid,param.getUidType(),storeOrder.getId(), storeOrder.getOrderId(),
                productIds,numbers,specs);

        //异步更新桌面信息
        if(OrderLogEnum.ORDER_TAKE_DESK.getValue().equals(param.getOrderType())){
            storeOrderCartInfoService.updateDeskInfo(storeOrder);
            //websocket通信
            asyncStoreOrderService.pubOrderInfo(new ShopOrderMsgVo().setShopId(Long.valueOf(param.getShopId())));
        }

        if(StrUtil.isBlank(param.getOrderId())){
            //增加状态
            storeOrderStatusService.create(uid,storeOrder.getId(), OrderLogEnum.CREATE_ORDER.getValue(),
                    OrderLogEnum.CREATE_ORDER.getDesc());
        }





        //堂食点餐不需要
        if(!OrderLogEnum.ORDER_TAKE_DESK.getValue().equals(param.getOrderType())) {
            //加入延时队列，30分钟自动取消
            try {
                RBlockingDeque<Object> blockingDeque = redissonClient.getBlockingDeque(ShopConstants.REDIS_ORDER_OUTTIME_UNPAY_QUEUE);
                RDelayedQueue<Object> delayedQueue = redissonClient.getDelayedQueue(blockingDeque);
                delayedQueue.offer(OrderMsg.builder().orderId(orderSn).build(), ShopConstants.ORDER_OUTTIME_UNPAY, TimeUnit.MINUTES);
                String s = TimeUnit.SECONDS.toSeconds(ShopConstants.ORDER_OUTTIME_UNPAY) + "分钟";
                log.info("添加延时队列成功 ，延迟时间：" + s + "订单编号：" + orderSn);
            } catch (Exception e) {
                log.error("添加延时队列失败：{}",e.getMessage());
            }
        }else{
            //扫码堂食2个小时自动确认收货
            try {
                RBlockingDeque<Object> blockingDeque = redissonClient.getBlockingDeque(ShopConstants.REDIS_ORDER_DESK_CONFIRM);
                RDelayedQueue<Object> delayedQueue = redissonClient.getDelayedQueue(blockingDeque);
                delayedQueue.offer(OrderMsg.builder().orderId(orderSn).build(), ShopConstants.ORDER_TWO_HOUR, TimeUnit.HOURS);
            } catch (Exception e) {
                log.error("添加延时队列失败：{}",e.getMessage());
            }

            //发送消息队列进行推送消息通知管理员,堂食不需要
            //公众号订单通知管理员
            weixinNoticeProducer.sendNoticeMessage( new WeixinNoticeMessage()
                    .setOrderId(orderSn)
                    .setTempkey(WechatTempateEnum.NEW_ORDER_NOTICE.getValue())
                    .setType(WechatTempateEnum.TEMPLATES.getValue()));

        }

        Map<String,Object> map = new HashMap<>();
        map.put("orderId",orderSn);
        return map;
    }

    /**
     * 第三方支付
     * @param uid  用户id
     * @param param 订单参数
     * @return
     */
    @Override
    public Map<String, Object> pay(Long uid, AppPayParam param) {
        AppStoreOrderQueryVo orderInfo = getOrderInfo(param.getUni(), uid);
        UserBillDO userBillDO =  billService.getOne(new LambdaQueryWrapper<UserBillDO>().eq(UserBillDO::getUid,uid)
                .eq(UserBillDO::getExtendField,param.getUni()));
        ScoreOrderDO scoreOrderDO = scoreOrderMapper.selectOne(new LambdaQueryWrapper<ScoreOrderDO>()
                .eq(ScoreOrderDO::getOrderId,param.getUni()));

        if (ObjectUtil.isNull(orderInfo) && ObjectUtil.isNull(userBillDO) && ObjectUtil.isNull(scoreOrderDO) ) {
            throw exception(STORE_ORDER_NOT_EXISTS);
        }
        if(ObjectUtil.isNotNull(orderInfo) && orderInfo.getPaid().equals(OrderInfoEnum.PAY_STATUS_1.getValue())) {
            throw exception(ORDER_PAY_FINISH);
        }
        if(ObjectUtil.isNotNull(userBillDO) && userBillDO.getStatus().equals(OrderInfoEnum.PAY_STATUS_1.getValue())) {
            throw exception(ORDER_PAY_FINISH);
        }
        if(ObjectUtil.isNotNull(scoreOrderDO) && scoreOrderDO.getHavePaid().equals(OrderInfoEnum.PAY_STATUS_1.getValue())) {
            throw exception(ORDER_PAY_FINISH);
        }

        MemberUserDO memberUserDO = userService.getUser(uid);
        Map<String, Object> map = new LinkedHashMap<>();
        BigDecimal price = BigDecimal.ZERO;
        String msg = "";
        String detailsId = "";
        if(orderInfo != null) {
            price = orderInfo.getPayPrice();
            msg = "商品购买";
        }else if(userBillDO != null){
            if(BillDetailEnum.TYPE_11.getValue().equals(userBillDO.getType())){
                //会员卡购买
                VipCardDO vipCardDO =  appVipCardService.getById(userBillDO.getLinkId());
                price = vipCardDO.getPrice();
                msg = "会员卡购买";
            }else if(BillDetailEnum.TYPE_1.getValue().equals(userBillDO.getType())){
                //充值
                RechargeDO rechargeDO =  appRechargeService.getById(userBillDO.getLinkId());
                price = rechargeDO.getSellPrice();
                msg = "用户充值";
            }
        }else if(scoreOrderDO != null){
            price = scoreOrderDO.getPrice();
            msg = "积分商品兑换购买";
        }
        Long tenantId = TenantContextHolder.getTenantId();
        switch (PayTypeEnum.toType(param.getPaytype())){
            case WEIXIN:
                if(AppFromEnum.H5.getValue().equals(param.getFrom())){
                    detailsId = PayIdEnum.WX_WECHAT.getValue() + tenantId;
                    //todo 如果启用微信H5支付充值 需要另外增加一个配置用于同步跳转页面 比如下面的增加了一个id=5的配置,微信支付公众号与H%配置是一样 基本
//                    if(orderInfo != null) {
//                        detailsId = "4";
//                    }else{
//                        detailsId = "5";
//                    }
                    MerchantPayOrder payOrder = new MerchantPayOrder(detailsId, "MWEB", msg,
                            msg, price, param.getUni());

                    Map<String, Object> payOrderInfo = manager.getOrderInfo(payOrder);
                    MerchantDetailsDO merchantDetailsDO = merchantDetailsService.getMerchantDetails(detailsId);
                    String url = merchantDetailsDO.getReturnUrl();

                    String newUrl = "";
                    try {
                        newUrl =  String.format("%s%s", payOrderInfo.get("mweb_url"), "&redirect_url=" + URLEncoder.encode(url,"UTF-8"));
                    } catch (UnsupportedEncodingException e) {
                        log.error(e.getMessage());
                    }
                    map.put("data",newUrl);
                    map.put("trade_type","MWEB");
                } else if(AppFromEnum.WECHAT.getValue().equals(param.getFrom())){//微信公众号
                    MerchantPayOrder payOrder = new MerchantPayOrder(PayIdEnum.WX_WECHAT.getValue() + tenantId, "JSAPI", msg,
                            msg, price, param.getUni());
                    payOrder.setOpenid(memberUserDO.getOpenid());
                    map.put("data",manager.getOrderInfo(payOrder));
                    map.put("trade_type","W-JSAPI");
                } else if(AppFromEnum.SCAN_PC.getValue().equals(param.getFrom())){//付款码支付
                    MerchantPayOrder payOrder = new MerchantPayOrder(PayIdEnum.WX_MINIAPP.getValue() + tenantId, "MICROPAY", msg,
                            msg, price, param.getUni());
                    payOrder.setAuthCode(param.getAuthCode());
                    Map<String, Object> mapRes =  manager.getOrderInfo(payOrder);
                    if(mapRes.get("return_code").equals("SUCCESS") && mapRes.get("result_code").equals("SUCCESS")){
                        paySuccess(mapRes.get("out_trade_no").toString(),param.getPaytype());
                    }else{
                        throw exception(new ErrorCode(20247170,mapRes.get("return_msg").toString()));
                    }
                    map.put("data","");
                    map.put("trade_type","MICROPAY");
                }else {//微信小程序
                    MerchantPayOrder payOrder = new MerchantPayOrder(PayIdEnum.WX_MINIAPP.getValue() + tenantId, "JSAPI", msg,
                            msg, price, param.getUni());
                    payOrder.setOpenid(memberUserDO.getRoutineOpenid());
                    map.put("data",manager.getOrderInfo(payOrder));
                    map.put("trade_type","JSAPI");

                }
                break;
            case YUE:
                this.yuePay(param.getUni(), uid);
                map.put("status","ok");
                break;
            case CASH:
                paySuccess(param.getUni(),PayTypeEnum.CASH.getValue());
                map.put("status","ok");
                break;
            case ALI:
                detailsId = PayIdEnum.ALI_H5.getValue() + tenantId;
                if(AppFromEnum.SCAN_PC.getValue().equals(param.getFrom())){//付款码支付
                    MerchantPayOrder payOrder = new MerchantPayOrder(detailsId, "BAR_CODE", msg,
                            msg, price, param.getUni());
                    payOrder.setAuthCode(param.getAuthCode());
                    Map<String, Object> mapRes =  manager.getOrderInfo(payOrder);
                    if(mapRes.get("code").equals("10000") && mapRes.get("msg").equals("Success")){
                        paySuccess(mapRes.get("out_trade_no").toString(),param.getPaytype());
                    }else{
                        throw exception(new ErrorCode(20247171,mapRes.get("msg").toString()));
                    }
                    map.put("data","");
                    map.put("trade_type","MICROPAY");
                }else{
                    //h5支付

                    //todo 如果启用支付宝H5支付充值 需要另外增加一个配置用于同步跳转页面 比如下面的增加了一个id=6的配置
//                if(orderInfo != null) {
//                    detailsId = "1";
//                }else{
//                    detailsId = "6";
//                }
                    MerchantPayOrder payOrder = new MerchantPayOrder(detailsId, "WAP", msg,
                            msg, price, param.getUni());
                    map.put("data",manager.toPay(payOrder));
                }


            default:
        }
        return map;
    }

    /**
     * 余额支付
     *
     * @param orderId 订单号
     * @param uid     用户id
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void yuePay(String orderId, Long uid) {
        AppStoreOrderQueryVo orderInfo = getOrderInfo(orderId, uid);
        if (ObjectUtil.isNull(orderInfo)) {
            throw exception(STORE_ORDER_NOT_EXISTS);
        }

        if (OrderInfoEnum.PAY_STATUS_1.getValue().equals(orderInfo.getPaid())) {
            throw exception(ORDER_PAY_FINISH);
        }

        AppUserQueryVo userInfo = userService.getAppUser(uid);

        if (userInfo.getNowMoney().compareTo(orderInfo.getPayPrice()) < 0) {
            throw exception(PAY_YUE_NOT);
        }

        userService.decPrice(uid, orderInfo.getPayPrice());

        //支付成功后处理
        this.paySuccess(orderInfo.getOrderId(), PayTypeEnum.YUE.getValue());
    }


    /**
     * 支付成功后操作
     *
     * @param orderId 订单号
     * @param payType 支付方式
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @TenantIgnore
    public void paySuccess(String orderId, String payType) {
        //处理充值与会员卡订单
        UserBillDO userBillDO =  billService.getOne(new LambdaQueryWrapper<UserBillDO>()
                .eq(UserBillDO::getExtendField,orderId));
        ScoreOrderDO scoreOrderDO = scoreOrderMapper.selectOne(new LambdaQueryWrapper<ScoreOrderDO>()
                .eq(ScoreOrderDO::getOrderId,orderId));
        if(userBillDO != null) {
            userBillDO.setStatus(ShopCommonEnum.IS_STATUS_1.getValue());
            billService.updateById(userBillDO);
            if(BillDetailEnum.TYPE_11.getValue().equals(userBillDO.getType())){
                //会员卡购买
                VipCardDO vipCardDO =  appVipCardService.getById(userBillDO.getLinkId());
                MemberUserDO memberUserDO = userService.getById(userBillDO.getUid());
                memberUserDO.setCardId(vipCardDO.getId());
                memberUserDO.setCardName(vipCardDO.getName());
                memberUserDO.setDiscount(vipCardDO.getDiscount());
                memberUserDO.setNowMoney(memberUserDO.getNowMoney().add(vipCardDO.getMony()));
                memberUserDO.setIntegral(memberUserDO.getIntegral().add(new BigDecimal(vipCardDO.getIntegral())));
                userService.updateById(memberUserDO);
                if(vipCardDO.getMony().intValue() > 0){
                    String mark = "购买会员卡，系统增加了"+vipCardDO.getMony()+"余额";
                    //BigDecimal newMoney = memberUserDO.getNowMoney().add(vipCardDO.getMony());
                    billService.income(memberUserDO.getId(),"系统增加余额", BillDetailEnum.CATEGORY_1.getValue(),
                            BillDetailEnum.TYPE_6.getValue(),vipCardDO.getMony().doubleValue()
                            ,memberUserDO.getNowMoney().doubleValue(), mark,"",vipCardDO.getTenantId());
                }
                if(vipCardDO.getIntegral() > 0){
                    String mark = "购买会员卡，系统增加了"+vipCardDO.getIntegral()+"积分";
                    //BigDecimal newIntegral = memberUserDO.getIntegral().add(new BigDecimal(vipCardDO.getIntegral()));
                    billService.income(memberUserDO.getId(),"系统增加积分", BillDetailEnum.CATEGORY_2.getValue(),
                            BillDetailEnum.TYPE_6.getValue(),Double.valueOf(vipCardDO.getIntegral()),
                            memberUserDO.getIntegral().doubleValue(), mark,"",vipCardDO.getTenantId());
                }
            }else if(BillDetailEnum.TYPE_1.getValue().equals(userBillDO.getType())){
                //充值
                userService.incMoney(userBillDO.getUid(), userBillDO.getNumber());
            }

            return;
        }else if(scoreOrderDO != null){
            //处理积分订单
            MemberUserDO userInfo = userService.getUser(scoreOrderDO.getUid());
            //减去积分
            userService.decScore(scoreOrderDO.getUid(),scoreOrderDO.getScore());

            scoreOrderDO.setHavePaid(OrderInfoEnum.PAY_STATUS_1.getValue());
            scoreOrderMapper.updateById(scoreOrderDO);

            //增加流水
            billService.expend(scoreOrderDO.getUid(), "积分兑换",
                    BillDetailEnum.CATEGORY_2.getValue(),
                    BillDetailEnum.TYPE_3.getValue(),
                    scoreOrderDO.getScore().doubleValue(), userInfo.getIntegral().doubleValue(),
                    scoreOrderDO.getScore() + "积分兑换商品",scoreOrderDO.getTenantId());

            return;
        }

        log.info("orderId:[{}]",orderId);
        AppStoreOrderQueryVo orderInfo = getOrderInfo(orderId, null);
        log.info("orderInfo:[{}]",orderInfo);
        if(orderInfo == null){
            return;
        }

        System.out.println("orderInfo:"+orderInfo.getTenantId());


        //更新订单状态
        LambdaQueryWrapper<StoreOrderDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StoreOrderDO::getOrderId, orderId);
        StoreOrderDO storeOrder = new StoreOrderDO();
        storeOrder.setPaid(OrderInfoEnum.PAY_STATUS_1.getValue());
        storeOrder.setPayType(payType);
        storeOrder.setPayTime(LocalDateTime.now());

        //如果是堂食 最后付款后直接完成
        if(OrderLogEnum.ORDER_TAKE_DESK.getValue().equals(orderInfo.getOrderType())){
            storeOrder.setStatus(OrderInfoEnum.STATUS_3.getValue());//如果是预约中的订单
          if(OrderDueStatusEnum.DUE_STATUS_1.getValue().equals(orderInfo.getDueStatus())){
              storeOrder.setDueStatus(OrderDueStatusEnum.DUE_STATUS_3.getValue());
           }
            ShopDeskDO shopDeskDO = new ShopDeskDO();
            //查询当前的桌台是否还有预约订单
            Long count = storeOrderMapper.selectCount(new LambdaQueryWrapper<StoreOrderDO>()
                    .eq(StoreOrderDO::getDeskId,orderInfo.getDeskId())
                    .eq(StoreOrderDO::getDueStatus,OrderDueStatusEnum.DUE_STATUS_1.getValue()));
            if(count > 0){
                shopDeskDO.setBookStatus(ShopCommonEnum.IS_STATUS_1.getValue());
            }
            shopDeskDO.setLastOrderStatus(OrderInfoEnum.DESK_ORDER_STATUS_CONFIRM.getValue());
            shopDeskMapper.update(shopDeskDO,new LambdaQueryWrapper<ShopDeskDO>()
                    .eq(ShopDeskDO::getId,orderInfo.getDeskId()));
        }else{
            //websocket通信
            asyncStoreOrderService.pubOrderInfo(new ShopOrderMsgVo().setShopId(Long.valueOf(orderInfo.getShopId())));
        }
        this.update(storeOrder, wrapper);

        //增加用户购买次数
        userService.incPayCount(orderInfo.getUid());
        //增加状态
        storeOrderStatusService.create(orderInfo.getUid(),orderInfo.getId(), OrderLogEnum.PAY_ORDER_SUCCESS.getValue(),
                OrderLogEnum.PAY_ORDER_SUCCESS.getDesc());


        MemberUserDO userInfo = userService.getUser(orderInfo.getUid());
        //增加流水
        String payTypeMsg = PayTypeEnum.WEIXIN.getDesc();
        if (PayTypeEnum.YUE.getValue().equals(payType)) {
            payTypeMsg = PayTypeEnum.YUE.getDesc();
        }else if (PayTypeEnum.ALI.getValue().equals(payType)) {
            payTypeMsg = PayTypeEnum.ALI.getDesc();
        }else if(PayTypeEnum.CASH.getValue().equals(payType)){
            payTypeMsg = PayTypeEnum.CASH.getDesc();
        }
        billService.expend(userInfo == null ? 0 : userInfo.getId(), "购买商品",
                BillDetailEnum.CATEGORY_1.getValue(),
                BillDetailEnum.TYPE_3.getValue(),
                orderInfo.getPayPrice().doubleValue(), userInfo == null ? 0d : userInfo.getNowMoney().doubleValue(),
                payTypeMsg + orderInfo.getPayPrice() + "元购买商品",orderInfo.getTenantId());

        StoreRevenueDO storeRevenueDO = StoreRevenueDO.builder()
                .orderId(orderInfo.getOrderId())
                .shopId(orderInfo.getShopId())
                .shopName(orderInfo.getShopName())
                .uid(orderInfo.getUid())
                .amount(orderInfo.getPayPrice())
                .build();

        storeRevenueDO.setTenantId(orderInfo.getTenantId());
        storeRevenueMapper.insert(storeRevenueDO);



        //发送消息队列进行推送消息通知管理员,堂食不需要
        if(!OrderLogEnum.ORDER_TAKE_DESK.getValue().equals(orderInfo.getOrderType()) && userInfo != null &&
                userInfo.getLoginType().equals(AppFromEnum.ROUNTINE.getValue())){

            //公众号订单通知管理员
            weixinNoticeProducer.sendNoticeMessage( new WeixinNoticeMessage()
                    .setOrderId(orderInfo.getOrderId())
                    .setTempkey(WechatTempateEnum.NEW_ORDER_NOTICE.getValue())
                    .setType(WechatTempateEnum.TEMPLATES.getValue()));


        }


    }

    /**
     * 减库存增加销量
     *
     * @param productIds 商品id
     * @param numbers 商品数量
     * @param specs 商品规格
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void deStockIncSale(List<String> productIds,List<String> numbers,List<String> specs) {


        log.info("========减库存增加销量start=========");
        //对库存加锁
        RLock lock = redissonClient.getLock(STOCK_LOCK_KEY);
        if (lock.tryLock()) {
            try {
                for (int i = 0;i < productIds.size();i++){
                    String newSku = StrUtil.replace(specs.get(i),"|",",");
                    appStoreProductService.decProductStock(Integer.valueOf(numbers.get(i)),
                            Long.valueOf(productIds.get(i)),
                            newSku, 0L, "");
                }
            }catch (Exception ex) {
                log.error("[deStockIncSale][执行异常]", ex);
                throw exception(new ErrorCode(999999,ex.getMessage()));
            } finally {
                lock.unlock();
            }
        }
    }


    /**
     * 订单列表
     *
     * @param uid   用户id
     * @param orderType 订单类型
     * @param type  OrderStatusEnum
     * @param page  page
     * @param limit limit
     * @return list
     */
    @Override
    public List<AppStoreOrderQueryVo> orderList(Long uid,String orderType, int type, int page, int limit) {
        LambdaQueryWrapper<StoreOrderDO> wrapper = new LambdaQueryWrapper<>();
        if (uid != null) {
            wrapper.apply(
                    "FIND_IN_SET ('" + uid + "',user_ids)");
           // wrapper.eq(StoreOrderDO::getUid, uid);
        }
        wrapper.eq(StoreOrderDO::getIsSystemDel,ShopCommonEnum.DELETE_0.getValue())
                .orderByDesc(StoreOrderDO::getId);
        if(StrUtil.isNotEmpty(orderType)){
            wrapper.eq(StoreOrderDO::getOrderType,orderType);
        }
       // wrapper.eq(StoreOrderDO::getPaid, OrderInfoEnum.PAY_STATUS_1.getValue())
                //.eq(StoreOrderDO::getRefundStatus, OrderInfoEnum.REFUND_STATUS_0.getValue())
                //.eq(StoreOrderDO::getStatus, OrderInfoEnum.STATUS_0.getValue())
               // .orderByDesc(StoreOrderDO::getId);
        if(OrderLogEnum.ORDER_TAKE_DUE.getValue().equals(orderType)){
            //wrapper.eq(StoreOrderDO::getDueStatus,type);
        }else{
//            switch (OrderStatusEnum.toType(type)) {
//                case STATUS__1:
//                    break;
//                //未支付
//                case STATUS_0:
//                    wrapper.eq(StoreOrderDO::getPaid, OrderInfoEnum.PAY_STATUS_0.getValue())
//                            .eq(StoreOrderDO::getRefundStatus, OrderInfoEnum.REFUND_STATUS_0.getValue())
//                            .eq(StoreOrderDO::getStatus, OrderInfoEnum.STATUS_0.getValue());
//                    break;
//                //已经支付
//                case STATUS_1:
//                    wrapper.eq(StoreOrderDO::getPaid, OrderInfoEnum.PAY_STATUS_1.getValue())
//                            .eq(StoreOrderDO::getRefundStatus, OrderInfoEnum.REFUND_STATUS_0.getValue())
//                            .eq(StoreOrderDO::getStatus, OrderInfoEnum.STATUS_0.getValue());
//                    break;
//                //待收货
//                case STATUS_2:
//                    wrapper.eq(StoreOrderDO::getPaid, OrderInfoEnum.PAY_STATUS_1.getValue())
//                            .eq(StoreOrderDO::getRefundStatus, OrderInfoEnum.REFUND_STATUS_0.getValue())
//                            .eq(StoreOrderDO::getStatus, OrderInfoEnum.STATUS_1.getValue());
//                    break;
////            //待评价
////            case STATUS_3:
////                wrapper.eq(StoreOrderDO::getPaid, OrderInfoEnum.PAY_STATUS_1.getValue())
////                        .eq(StoreOrderDO::getRefundStatus, OrderInfoEnum.REFUND_STATUS_0.getValue())
////                        .eq(StoreOrderDO::getStatus, OrderInfoEnum.STATUS_2.getValue());
////                break;
//                //已完成
//                case STATUS_4:
//                    wrapper.eq(StoreOrderDO::getPaid, OrderInfoEnum.PAY_STATUS_1.getValue())
//                            .eq(StoreOrderDO::getRefundStatus, OrderInfoEnum.REFUND_STATUS_0.getValue())
//                            .ge(StoreOrderDO::getStatus, OrderInfoEnum.STATUS_2.getValue());
//                    break;
//                //退款
//                case STATUS_MINUS_3:
//                    String[] strs = {"1", "2"};
//                    wrapper.in(StoreOrderDO::getRefundStatus, Arrays.asList(strs));
//                    break;
//                default:
//            }
        }


        Page<StoreOrderDO> pageModel = new Page<>(page, limit);
        IPage<StoreOrderDO> pageList = storeOrderMapper.selectPage(pageModel, wrapper);
        List<AppStoreOrderQueryVo> list = StoreOrderConvert.INSTANCE.convertList01(pageList.getRecords());

        return list.stream().map(this::handleOrder).collect(Collectors.toList());

    }

    public List<StoreOrderCartInfoDO> streamData(Integer i,List<StoreOrderCartInfoDO> cartInfos){
        List<StoreOrderCartInfoDO> orderCartInfoDOS = cartInfos.stream()
                .filter(o -> i.compareTo(o.getAddProductMark()) == 0)
                .collect(Collectors.toList());

        return orderCartInfoDOS;
    }

    /**
     * 处理订单返回的状态
     *
     * @param order order
     * @return YxStoreOrderQueryVo
     */
    @Override
    public AppStoreOrderQueryVo handleOrder(AppStoreOrderQueryVo order) {
        LambdaQueryWrapper<StoreOrderCartInfoDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StoreOrderCartInfoDO::getOid, order.getId())
                .orderByDesc(StoreOrderCartInfoDO::getId);
        List<StoreOrderCartInfoDO> cartInfos = storeOrderCartInfoService.list(wrapper);

        order.setCartInfo(cartInfos);

        StoreShopDO storeShopDO = appStoreShopService.getById(order.getShopId());
        order.setShop(StoreShopConvert.INSTANCE.convert02(storeShopDO));

        if(ObjectUtil.isNotNull(order.getDeskId())){
            ShopDeskDO shopDeskDO = shopDeskMapper.selectById(order.getDeskId());
            order.setAppShopDeskVO(BeanUtils.toBean(shopDeskDO, AppShopDeskVO.class));
        }

        long count = storeOrderMapper.selectCount(new LambdaQueryWrapper<StoreOrderDO>()
                .eq(StoreOrderDO::getShopId,order.getShopId())
                .lt(StoreOrderDO::getCreateTime,order.getCreateTime())
                .eq(StoreOrderDO::getPaid, OrderInfoEnum.PAY_STATUS_1.getValue())
                .eq(StoreOrderDO::getRefundStatus, OrderInfoEnum.REFUND_STATUS_0.getValue())
                .eq(StoreOrderDO::getStatus, OrderInfoEnum.STATUS_0.getValue()));
        order.setPreNum(count);


        StatusDto statusDTO = new StatusDto();
        if (OrderStatusEnum.STATUS_0.getValue().equals(order.getPaid())) {
            //计算未支付到自动取消订 时间
            int offset = Integer.valueOf(String.valueOf(ShopConstants.ORDER_OUTTIME_UNPAY));
            //Date time = DateUtil.offsetMinute(order.getCreateTime()., offset);
            statusDTO.setYClass("nobuy");
            //statusDTO.setMsg(StrUtil.format("请在{}前完成支付", DateUtil.formatDateTime(time)));
            statusDTO.setType("0");
            statusDTO.setTitle("未支付");
        } else if (OrderInfoEnum.REFUND_STATUS_1.getValue().equals(order.getRefundStatus())) {
            statusDTO.setYClass("state-sqtk");
            statusDTO.setMsg("商家审核中,请耐心等待");
            statusDTO.setType("-1");
            statusDTO.setTitle("申请退款中");
        } else if (OrderInfoEnum.REFUND_STATUS_2.getValue().equals(order.getRefundStatus())) {
            statusDTO.setYClass("state-sqtk");
            statusDTO.setMsg("已为您退款,感谢您的支持");
            statusDTO.setType("-2");
            statusDTO.setTitle("已退款");
        } else if (OrderInfoEnum.STATUS_0.getValue().equals(order.getStatus())) {
            // 拼团 todo
            if (order.getPinkId() > 0) {
            } else {
                statusDTO.setYClass("state-nfh");
                statusDTO.setMsg("商家未发货,请耐心等待");
                statusDTO.setType("1");
                statusDTO.setTitle("制作中");
            }

        } else if (OrderInfoEnum.STATUS_1.getValue().equals(order.getStatus())) {
            if(OrderLogEnum.ORDER_TAKE_OUT.getValue().equals(order.getOrderType())){
                statusDTO.setTitle("配送中");
            }else{
                statusDTO.setTitle("待取餐");
            }
            statusDTO.setYClass("state-ysh");
            statusDTO.setMsg("服务商已发货");
            statusDTO.setType("2");

        } else if (OrderInfoEnum.STATUS_2.getValue().equals(order.getStatus())) {
            if(OrderLogEnum.ORDER_TAKE_OUT.getValue().equals(order.getOrderType())){
                statusDTO.setTitle("已收货");
            }else{
                statusDTO.setTitle("已取餐");
            }
            statusDTO.setYClass("state-ypj");
            statusDTO.setMsg("已收货,快去评价一下吧");
            statusDTO.setType("3");
        } else if (OrderInfoEnum.STATUS_3.getValue().equals(order.getStatus())) {
            statusDTO.setYClass("state-ytk");
            statusDTO.setMsg("交易完成,感谢您的支持");
            statusDTO.setType("4");
            statusDTO.setTitle("交易完成");
        }

        if (PayTypeEnum.WEIXIN.getValue().equals(order.getPayType())) {
            statusDTO.setPayType("微信支付");
        } else if (PayTypeEnum.YUE.getValue().equals(order.getPayType())) {
            statusDTO.setPayType("余额支付");
        } else if (PayTypeEnum.ALI.getValue().equals(order.getPayType())) {
            statusDTO.setPayType("支付宝支付");
        }else {
            statusDTO.setPayType("积分支付");
        }

        order.setStatusDto(statusDTO);

        //如果扫描点餐 处理下新订单展示信息
        if(!OrderLogEnum.ORDER_TAKE_DUE.getValue().equals(order.getOrderType()) && cartInfos != null && !cartInfos.isEmpty()){
            List<AppDeskOrderVo> appDeskOrderVos = new ArrayList<>();
            //获取当前最后加餐次数
            Integer lastAddProductMark = cartInfos.get(0).getAddProductMark();
            while (lastAddProductMark  >= 0){
                List<StoreOrderCartInfoDO> orderCartInfoDOS = streamData(lastAddProductMark,cartInfos);
                AppDeskOrderVo appDeskOrderVo = BeanUtils.toBean(orderCartInfoDOS.get(0),AppDeskOrderVo.class);
                List<AppDeskOrderGoodsVo> appDeskOrderGoodsVos = BeanUtils.toBean(orderCartInfoDOS,AppDeskOrderGoodsVo.class);
                appDeskOrderVo.setAppDeskOrderGoodsVos(appDeskOrderGoodsVos);
                appDeskOrderVos.add(appDeskOrderVo);
                lastAddProductMark --;
            }

            order.setAppDeskOrderVo(appDeskOrderVos);

        }


        return order;
    }


    /**
     * 订单确认收货
     *
     * @param orderId 单号
     * @param uid     uid
     */
    @Override
    public void takeOrder(String orderId, Long uid) {
        AppStoreOrderQueryVo order = this.getOrderInfo(orderId, uid);
        if (ObjectUtil.isNull(order)) {
            throw exception(STORE_ORDER_NOT_EXISTS);
        }

//        if (OrderInfoEnum.PAY_STATUS_0.getValue().equals(order.getPaid())) {
//            throw exception(ORDER_STATUS_ERROR);
//        }

        if (OrderInfoEnum.STATUS_3.getValue().equals(order.getStatus())){
            throw exception(ORDER_STATUS_FINISH);
        }
        order = handleOrder(order);
//        if (order.getOrderType().equals(OrderLogEnum.ORDER_TAKE_OUT.getValue())
//                && !OrderStatusEnum.STATUS_2.getValue().toString().equals(order.get_status().get_type())) {
//            throw exception(ORDER_STATUS_ERROR);
//        }



        StoreOrderDO storeOrder = new StoreOrderDO();
        storeOrder.setStatus(OrderInfoEnum.STATUS_3.getValue());
        storeOrder.setId(order.getId());
        if(OrderLogEnum.ORDER_TAKE_DESK.getValue().equals(order.getOrderType())){
            storeOrder.setPaid(OrderInfoEnum.PAY_STATUS_1.getValue());
            storeOrder.setPayTime(LocalDateTime.now());
            ShopDeskDO shopDeskDO = new ShopDeskDO();
            shopDeskDO.setLastOrderStatus(OrderInfoEnum.DESK_ORDER_STATUS_CONFIRM.getValue());
            shopDeskMapper.update(shopDeskDO,new LambdaQueryWrapper<ShopDeskDO>()
                    .eq(ShopDeskDO::getId,order.getDeskId()));

            //删除当前桌面单共享菜单
            storeCartShareMapper.delete(new LambdaQueryWrapper<StoreCartShareDO>()
                    .eq(StoreCartShareDO::getShopId,order.getShopId())
                    .eq(StoreCartShareDO::getDeskId,order.getDeskId()));
        }
        this.updateById(storeOrder);

        //增加状态
        storeOrderStatusService.create(order.getUid(),order.getId(), OrderLogEnum.TAKE_ORDER_DELIVERY.getValue(), OrderLogEnum.TAKE_ORDER_DELIVERY.getDesc());

        //奖励积分
        this.gainUserIntegral(order);


        //分销计算 todo

    }

    /**
     * 申请退款
     *
     * @param explain 退款备注
     * @param Img     图片
     * @param text    理由
     * @param orderId 订单号
     * @param uid     uid
     */
    @Override
    public void orderApplyRefund(String explain, String Img, String text, String orderId, Long uid) {
        AppStoreOrderQueryVo order = this.getOrderInfo(orderId, uid);
        if (ObjectUtil.isNull(order)) {
            throw exception(STORE_ORDER_NOT_EXISTS);
        }

        if (OrderInfoEnum.REFUND_STATUS_2.getValue().equals(order.getRefundStatus())) {
            throw exception(ORDER_REFUNDED);
        }
        if (OrderInfoEnum.REFUND_STATUS_1.getValue().equals(order.getRefundStatus())) {
            throw exception(ORDER_REFUNDING);
        }


        StoreOrderDO storeOrder = new StoreOrderDO();
        storeOrder.setRefundStatus(OrderInfoEnum.REFUND_STATUS_1.getValue());
        storeOrder.setRefundReasonTime(LocalDateTime.now());
        storeOrder.setRefundReasonWapExplain(explain);
        storeOrder.setRefundReasonWapImg(Img);
        storeOrder.setRefundReasonWap(text);
        storeOrder.setId(order.getId());
        this.updateById(storeOrder);

        //增加状态
        storeOrderStatusService.create(order.getUid(),order.getId(),
                OrderLogEnum.REFUND_ORDER_APPLY.getValue(),
                "用户申请退款，原因：" + text);

        //如果是外卖同城配送，取消配送
        sameCityCancelOrder(order.getId());
    }



    /**
     * 删除订单
     *
     * @param orderId 单号
     * @param uid     uid
     */
    @Override
    public void removeOrder(String orderId, Long uid) {
        AppStoreOrderQueryVo order = this.getOrderInfo(orderId,  uid);
        if (order == null) {
            throw exception(STORE_ORDER_NOT_EXISTS);
        }
        order = handleOrder(order);
        if (!OrderInfoEnum.STATUS_3.getValue().equals(order.getStatus())) {
            throw exception(ORDER_NOT_DELETE);
        }

        this.removeById(order.getId());

        //增加状态
        storeOrderStatusService.create(uid,order.getId(),
                OrderLogEnum.REMOVE_ORDER.getValue(),
                OrderLogEnum.REMOVE_ORDER.getDesc());
    }

    /**
     * 未付款取消订单
     *
     * @param orderId 订单号
     * @param uid     用户id
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void cancelOrder(String orderId, Long uid) {
        log.info("订单取消：({})",orderId);
        AppStoreOrderQueryVo order = this.getOrderInfo(orderId, uid);
        if (ObjectUtil.isNull(order)) {
            throw exception(STORE_ORDER_NOT_EXISTS);
        }
        if (order.getPaid() != 0) {
            throw exception(ORDER_NOT_CANCEL);
        }


        this.regressionStock(order);

        this.regressionCoupon(order, 0);

        storeOrderMapper.deleteById(order.getId());
    }


    @Override
    public SameCityExpressVO sameCityPriceOrOrder(AppOrderParam appOrderParam, String method) {
        //查询启用的哪个同城派送
        ExpressDO expressDO = expressMapper.selectOne(new LambdaQueryWrapper<ExpressDO>()
                .eq(ExpressDO::getType, ExpressTypeEnum.TYPE_1.getValue())
                .eq(ExpressDO::getIsMain,ShopCommonEnum.DEFAULT_1.getValue()));
        PrintReq printReq = new PrintReq();
        BsamecityOrderReq bsamecityOrderReq = new BsamecityOrderReq();
        bsamecityOrderReq.setKuaidicom(expressDO.getCode());
        bsamecityOrderReq.setLbsType(2);//1：百度坐标，2：高德坐标
        //收件人地址
        UserAddressDO userAddressDO = appUserAddressService.getById(appOrderParam.getAddressId());
        bsamecityOrderReq.setRecManName(userAddressDO.getRealName());
        bsamecityOrderReq.setRecManMobile(userAddressDO.getPhone());
        bsamecityOrderReq.setRecManProvince(userAddressDO.getProvince());
        bsamecityOrderReq.setRecManCity(userAddressDO.getCity());
        bsamecityOrderReq.setRecManDistrict(userAddressDO.getDistrict());
        bsamecityOrderReq.setRecManAddr(userAddressDO.getAddress());
        bsamecityOrderReq.setRecManLat(StrUtil.sub(userAddressDO.getLatitude(),0,10));
        bsamecityOrderReq.setRecManLng(StrUtil.sub(userAddressDO.getLongitude(),0,10));
        //获取门店地址
        StoreShopDO storeShopDO = appStoreShopService.getById(appOrderParam.getShopId());
        bsamecityOrderReq.setSendManName(storeShopDO.getName());
        bsamecityOrderReq.setSendManMobile(storeShopDO.getMobile());
        bsamecityOrderReq.setSendManProvince(storeShopDO.getProvince());
        bsamecityOrderReq.setSendManCity(storeShopDO.getCity());
        bsamecityOrderReq.setSendManDistrict(storeShopDO.getDistrict());
        bsamecityOrderReq.setSendManAddr(storeShopDO.getAddressMap());
        bsamecityOrderReq.setSendManLat(StrUtil.sub(storeShopDO.getLat(),0,10));
        bsamecityOrderReq.setSendManLng(StrUtil.sub(storeShopDO.getLng(),0,10));
        bsamecityOrderReq.setWeight(storeShopDO.getWeight() != null ? storeShopDO.getWeight() : "1");
        //bsamecityOrderReq.setRemark("测试下单");
        //bsamecityOrderReq.setVolume("");
        //bsamecityOrderReq.setOrderType(0);
        //bsamecityOrderReq.setExpectPickupTime("");
        //bsamecityOrderReq.setExpectFinishTime("");

        bsamecityOrderReq.setPrice(appOrderParam.getPayPrice().toString());
        //组合商品
        List<String> productIds = appOrderParam.getProductId();
        List<String> numbers = appOrderParam.getNumber();
        List<String> specs = appOrderParam.getSpec();
        ArrayList<Goods> goodsList = new ArrayList<>();
        for (int i = 0;i < productIds.size();i++){
            StoreProductDO storeProductDO = appStoreProductService.getById(productIds.get(i));
            Goods goods = new Goods(storeProductDO.getStoreName()+specs.get(i),
                    ExpressGoodsTypeEnum.TYPE_1.getDesc(), Integer.valueOf(numbers.get(i)));
            goodsList.add(goods);
        }

        bsamecityOrderReq.setGoods(goodsList);
        KdhundredApiBaseDTO kdhundredApiBaseDTO = expressRedisDAO.get();
        //设置回调地址
        //http://yshop.free.svipss.top/app-api/order/samecity/callBack
        if(method.equals("order")){
            bsamecityOrderReq.setCallbackUrl(kdhundredApiBaseDTO.getCallBackUrl());
        }
        //测试阶段启用这个，用预约时间长点后期取消，当然你可以前端那边设置这个预约功能，自己二开拓展
        if(ShopCommonEnum.DEFAULT_0.getValue().equals(kdhundredApiBaseDTO.getEnv())){
            bsamecityOrderReq.setOrderType(2);
            DateTime newDate3 = DateUtil.offsetHour(new Date(), 6);
            bsamecityOrderReq.setExpectPickupTime(DateUtil.formatDateTime(newDate3));
        }

        String t = System.currentTimeMillis() + "";
        String param = new Gson().toJson(bsamecityOrderReq);

        printReq.setKey(kdhundredApiBaseDTO.getKey());
        printReq.setMethod(method); //price-预下单 //order-正式下单 正式下单会扣钱的所以账号里面要有钱
        printReq.setT(t);
        printReq.setSign(SignUtils.printSign(param, t, kdhundredApiBaseDTO.getKey(), kdhundredApiBaseDTO.getSecret()));
        printReq.setParam(param);

        IBaseClient bsamecityPrice = new BsameCityExpress();
        try {
            HttpResult httpResult = bsamecityPrice.execute(printReq);
            SameCityBodyVO sameCityBodyVO = JSONUtil.toBean(httpResult.getBody(), SameCityBodyVO.class);
            if(!sameCityBodyVO.getSuccess()){
                throw exception(new ErrorCode(202508280,sameCityBodyVO.getMessage()));
            }
            SameCityExpressVO sameCityExpressVO = sameCityBodyVO.getData();
            sameCityExpressVO.setExpressName(expressDO.getName());
            log.info("sameCityExpressVO:{}",sameCityExpressVO);
            return sameCityBodyVO.getData();
        } catch (Exception e) {
            throw exception(new ErrorCode(202508260,e.getMessage()));
        }


    }

    @Override
    public void bsameCallBack(SameCityCallBackBodyVO sameCityCallBackBodyVO) {
        StoreOrderDO storeOrderDO = storeOrderMapper.selectOne(new LambdaQueryWrapper<StoreOrderDO>()
                .eq(StoreOrderDO::getSameCityTaskId,sameCityCallBackBodyVO.getTaskId()));
        if(storeOrderDO == null) return;
        //用户已经取消了订单不再同步
        if(storeOrderDO.getSameCityDeliveryStatus() != null && storeOrderDO.getSameCityDeliveryStatus().equals(710)) return;
        SameCityCallBackParmVO sameCityCallBackParmVO = JSONUtil.toBean(sameCityCallBackBodyVO.getParam(), SameCityCallBackParmVO.class);
        storeOrderDO.setSameCityDeliveryStatus(sameCityCallBackParmVO.getStatus());
        storeOrderDO.setSameCityDeliveryStatusDes(sameCityCallBackParmVO.getStatusDesc());
        if(StrUtil.isNotBlank(sameCityCallBackParmVO.getCourierName())){
            storeOrderDO.setSameCityDeliveryCourierName(sameCityCallBackParmVO.getCourierName());
        }
        if(StrUtil.isNotBlank(sameCityCallBackParmVO.getCourierMobile())){
            storeOrderDO.setSameCityDeliveryCourierMobile(sameCityCallBackParmVO.getCourierMobile());
        }
        if(StrUtil.isNotBlank(sameCityCallBackParmVO.getExpectFinishTime())){
            storeOrderDO.setSameCityDeliveryExpectFinishTime(sameCityCallBackParmVO.getExpectFinishTime());
        }

        storeOrderMapper.updateById(storeOrderDO);


    }

    @Override
    public void sameCityCancelOrder(Long id) {
        StoreOrderDO storeOrderDO = storeOrderMapper.selectById(id);
        if(StrUtil.isEmpty(storeOrderDO.getSameCityOrderId())) return;
        PrintReq printReq = new PrintReq();
        BsamecityCancelReq bsamecityCancelReq = new BsamecityCancelReq();
        bsamecityCancelReq.setOrderId(storeOrderDO.getSameCityOrderId());
        bsamecityCancelReq.setCancelMsgType(ExpressCancelEnum.TYPE_1.getValue());
       // bsamecityCancelReq.setCancelMsg("测试寄件");
        bsamecityCancelReq.setTaskId(storeOrderDO.getSameCityTaskId());

        String t = System.currentTimeMillis() + "";
        String param = new Gson().toJson(bsamecityCancelReq);
        KdhundredApiBaseDTO kdhundredApiBaseDTO = expressRedisDAO.get();
        printReq.setKey(kdhundredApiBaseDTO.getKey());
        printReq.setMethod("cancel");
        printReq.setT(t);
        printReq.setSign(SignUtils.printSign(param, t, kdhundredApiBaseDTO.getKey(), kdhundredApiBaseDTO.getSecret()));
        printReq.setParam(param);

        IBaseClient bsamecityPrice = new BsameCityExpress();

        try {
            HttpResult httpResult = bsamecityPrice.execute(printReq);
            SameCityBodyVO sameCityBodyVO = JSONUtil.toBean(httpResult.getBody(), SameCityBodyVO.class);
            if(!sameCityBodyVO.getSuccess()){
                throw exception(new ErrorCode(202508290,sameCityBodyVO.getMessage()));
            }
            storeOrderDO.setSameCityDeliveryStatus(710);
            storeOrderDO.setSameCityDeliveryStatusDes("用户取消订单");
            storeOrderMapper.updateById(storeOrderDO);
            log.info("sameCityBodyVO:{}",sameCityBodyVO);

        } catch (Exception e) {
            throw exception(new ErrorCode(202508291,e.getMessage()));
        }
    }

    /**
     * 退回积分
     *
     * @param order 订单
     */
    private void regressionIntegral(AppStoreOrderQueryVo order, Integer type) {
        if (OrderInfoEnum.PAY_STATUS_1.getValue().equals(order.getPaid())
                || OrderStatusEnum.STATUS_MINUS_2.getValue().equals(order.getStatus())) {
            return;
        }

        if (order.getPayIntegral().compareTo(BigDecimal.ZERO) > 0) {
            order.setUseIntegral(order.getPayIntegral());
        }
        if (order.getUseIntegral().compareTo(BigDecimal.ZERO) <= 0) {
            return;
        }

        if (!OrderStatusEnum.STATUS_MINUS_2.getValue().equals(order.getStatus())
                && !OrderInfoEnum.REFUND_STATUS_2.getValue().equals(order.getRefundStatus())
                && order.getBackIntegral().compareTo(BigDecimal.ZERO) > 0) {
            return;
        }

        MemberUserDO yxUser = userService.getById(order.getUid());

        //增加积分
        BigDecimal newIntegral = NumberUtil.add(order.getUseIntegral(), yxUser.getIntegral());
        yxUser.setIntegral(newIntegral);
        userService.updateById(yxUser);

        //增加流水
        billService.income(yxUser.getId(), "积分回退", BillDetailEnum.CATEGORY_2.getValue(),
                BillDetailEnum.TYPE_8.getValue(),
                order.getUseIntegral().doubleValue(),
                newIntegral.doubleValue(),
                "购买商品失败,回退积分" + order.getUseIntegral(), order.getId().toString(),null);

        //更新回退积分
        StoreOrderDO storeOrder = new StoreOrderDO();
        storeOrder.setBackIntegral(order.getUseIntegral());
        storeOrder.setId(order.getId());
        this.updateById(storeOrder);
    }

    /**
     * 退回优惠券
     *
     * @param order 订单 todo
     */
    private void regressionCoupon(AppStoreOrderQueryVo order, Integer type) {
        if (OrderInfoEnum.PAY_STATUS_1.getValue().equals(order.getPaid())
                || OrderStatusEnum.STATUS_MINUS_2.getValue().equals(order.getStatus())) {
            return;
        }

        if (order.getCouponId() != null && order.getCouponId() > 0) {

            CouponUserDO couponUser = appCouponUserService
                    .getOne(Wrappers.<CouponUserDO>lambdaQuery()
                            .eq(CouponUserDO::getId, order.getCouponId())
                            .eq(CouponUserDO::getStatus, ShopCommonEnum.IS_STATUS_1.getValue())
                            .eq(CouponUserDO::getUserId, order.getUid()));

            if (ObjectUtil.isNotNull(couponUser)) {
                couponUser.setStatus(ShopCommonEnum.IS_STATUS_0.getValue());
                appCouponUserService.updateById(couponUser);
            }
        }
    }

    /**
     * 退回库存
     *
     * @param order 订单
     */
    private void regressionStock(AppStoreOrderQueryVo order) {
        if (OrderInfoEnum.PAY_STATUS_1.getValue().equals(order.getPaid())
                || OrderStatusEnum.STATUS_MINUS_2.getValue().equals(order.getStatus())) {
            return;
        }

        LambdaQueryWrapper<StoreOrderCartInfoDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StoreOrderCartInfoDO::getOid, order.getId());

        List<StoreOrderCartInfoDO> cartInfoList = storeOrderCartInfoService.list(wrapper);
        for (StoreOrderCartInfoDO cartInfo : cartInfoList) {
            String newSku = StrUtil.replace(cartInfo.getSpec(),"|",",");
            appStoreProductService.incProductStock(cartInfo.getNumber(), cartInfo.getProductId()
                    ,newSku, 0L, null);
        }
    }
    

    /**
     * 奖励积分
     *
     * @param order 订单
     */
    private void gainUserIntegral(AppStoreOrderQueryVo order) {
        if (order.getGainIntegral().compareTo(BigDecimal.ZERO) > 0) {
            MemberUserDO user = userService.getUser(order.getUid());
            //处理收银台后台点单订单不需要积分
            if(user == null) return;

            BigDecimal newIntegral = NumberUtil.add(user.getIntegral(), order.getGainIntegral());
            user.setIntegral(newIntegral);
            user.setId(order.getUid());
            userService.updateById(user);

            //增加流水
            billService.income(user.getId(), "购买商品赠送积分", BillDetailEnum.CATEGORY_2.getValue(),
                    BillDetailEnum.TYPE_9.getValue(),
                    order.getGainIntegral().doubleValue(),
                    newIntegral.doubleValue(),
                    "购买商品赠送" + order.getGainIntegral() + "积分", order.getId().toString(),null);
        }
    }



    /**
     * 计算奖励的积分
     *
     * @param productIds
     * @return double
     */
    private BigDecimal getGainIntegral(List<String> productIds) {
        BigDecimal gainIntegral = BigDecimal.ZERO;
        for (int i = 0;i < productIds.size();i++){
            StoreProductDO storeProductDO = appStoreProductService.getById(productIds.get(i));
            if(storeProductDO.getGiveIntegral().intValue() == 0){
                continue;
            }
            gainIntegral = NumberUtil.add(gainIntegral, storeProductDO.getGiveIntegral());
        }
        return gainIntegral;
    }













}
