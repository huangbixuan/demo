package co.yixiang.yshop.module.merchant.service.home;

import cn.hutool.core.date.DateUtil;
import co.yixiang.yshop.framework.common.enums.OrderInfoEnum;
import co.yixiang.yshop.framework.common.enums.ShopCommonEnum;
import co.yixiang.yshop.framework.common.exception.ErrorCode;
import co.yixiang.yshop.framework.common.util.object.BeanUtils;
import co.yixiang.yshop.framework.redis.util.redis.RedisUtil;
import co.yixiang.yshop.module.member.dal.dataobject.storeuser.StoreUserDO;
import co.yixiang.yshop.module.member.dal.mysql.storeuser.StoreUserMapper;
import co.yixiang.yshop.module.merchant.controller.app.home.vo.AppHomeVO;
import co.yixiang.yshop.module.merchant.controller.app.home.vo.AppOrderCountVO;
import co.yixiang.yshop.module.merchant.controller.app.home.vo.AppTodayDataVO;
import co.yixiang.yshop.module.merchant.controller.app.home.vo.ShopReqVO;
import co.yixiang.yshop.module.order.dal.dataobject.storeorder.StoreOrderDO;
import co.yixiang.yshop.module.order.dal.mysql.storeorder.StoreOrderMapper;
import co.yixiang.yshop.module.order.enums.OrderDueStatusEnum;
import co.yixiang.yshop.module.order.enums.OrderLogEnum;
import co.yixiang.yshop.module.order.enums.OrderStatusEnum;
import co.yixiang.yshop.module.store.controller.app.storeshop.vo.AppStoreShopVO;
import co.yixiang.yshop.module.store.dal.dataobject.storeshop.StoreShopDO;
import co.yixiang.yshop.module.store.dal.mysql.storeshop.StoreShopMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.util.Date;

import static co.yixiang.yshop.framework.common.constant.ShopConstants.DAY_COUNT_KEY;
import static co.yixiang.yshop.framework.common.exception.util.ServiceExceptionUtil.exception;


/**
 * 会员卡 Service 实现类
 *
 * @author yshop
 */
@Service
@Validated
public class AppHomeServiceImpl implements AppHomeService {

    @Resource
    private StoreShopMapper storeShopMapper;
    @Resource
    private StoreOrderMapper storeOrderMapper;
    @Resource
    private StoreUserMapper storeUserMapper;

    @Value("${yshop.demo}")
    private Boolean isDemo;



    @Override
    public AppHomeVO getData(Long uid) {
        Long shopId = check(uid);
        if(isDemo) {
            shopId = 2L;
        }
        StoreShopDO storeShopDO = storeShopMapper.selectById(shopId);
        AppStoreShopVO appStoreShopVO = BeanUtils.toBean(storeShopDO,AppStoreShopVO.class);

        Date today = DateUtil.beginOfDay(new Date());
        //今日成交额
        LambdaQueryWrapper<StoreOrderDO> wrapperOne = new LambdaQueryWrapper<>();
        wrapperOne.ne(StoreOrderDO::getOrderType,OrderLogEnum.ORDER_TAKE_DUE.getValue())
                .ge(StoreOrderDO::getPayTime, today)
                .eq(StoreOrderDO::getShopId,shopId)
                .eq(StoreOrderDO::getPaid, OrderInfoEnum.PAY_STATUS_1.getValue())
                .eq(StoreOrderDO::getRefundStatus, OrderInfoEnum.REFUND_STATUS_0.getValue());
        BigDecimal todayPrice = BigDecimal.valueOf(storeOrderMapper.todayPrice(wrapperOne));
        Long todayCount = storeOrderMapper.selectCount(wrapperOne);
        Object object = RedisUtil.hget(DAY_COUNT_KEY,shopId.toString());
        Integer todayVisit = 0;
        if(object != null){
            todayVisit = Integer.valueOf(RedisUtil.hget(DAY_COUNT_KEY,shopId.toString()).toString());
        }


        //今日退货
        LambdaQueryWrapper<StoreOrderDO> wrapperTwo = new LambdaQueryWrapper<>();
        wrapperTwo
                .ne(StoreOrderDO::getOrderType,OrderLogEnum.ORDER_TAKE_DUE.getValue())
                .ge(StoreOrderDO::getPayTime, today)
                .eq(StoreOrderDO::getShopId,shopId)
                .eq(StoreOrderDO::getPaid, OrderInfoEnum.PAY_STATUS_1.getValue())
                .gt(StoreOrderDO::getRefundStatus, OrderInfoEnum.REFUND_STATUS_0.getValue());
        Long todayRefundCount = storeOrderMapper.selectCount(wrapperTwo);
        AppTodayDataVO appTodayDataVO = AppTodayDataVO.builder()
                .num01(todayPrice)
                .num02(todayCount)
                .num03(todayVisit)
                .num04(todayRefundCount).build();

        //订单统计
        LambdaQueryWrapper<StoreOrderDO> wrapperThree = new LambdaQueryWrapper<>();
        wrapperThree.eq(StoreOrderDO::getShopId,shopId)
                .ne(StoreOrderDO::getOrderType,OrderLogEnum.ORDER_TAKE_DUE.getValue())
                .eq(StoreOrderDO::getRefundStatus, OrderInfoEnum.REFUND_STATUS_0.getValue())
                .eq(StoreOrderDO::getStatus, OrderInfoEnum.STATUS_0.getValue())
                .and(i->i.eq(StoreOrderDO::getPaid, OrderInfoEnum.PAY_STATUS_1.getValue())
                        .or(j->j.eq(StoreOrderDO::getOrderType, OrderLogEnum.ORDER_TAKE_DESK.getValue())
                                .eq(StoreOrderDO::getPaid, OrderInfoEnum.PAY_STATUS_0.getValue())));
        //未出单
        Long num1 = storeOrderMapper.selectCount(wrapperThree);

        //已出单
        Long num2 = storeOrderMapper.selectCount(new LambdaQueryWrapper<StoreOrderDO>()
                .ne(StoreOrderDO::getOrderType,OrderLogEnum.ORDER_TAKE_DUE.getValue())
                .eq(StoreOrderDO::getShopId,shopId)
                .eq(StoreOrderDO::getPaid, OrderInfoEnum.PAY_STATUS_1.getValue())
                .eq(StoreOrderDO::getRefundStatus, OrderInfoEnum.REFUND_STATUS_0.getValue())
                .eq(StoreOrderDO::getStatus, OrderInfoEnum.STATUS_1.getValue()));
        //已完成
        Long num3 = storeOrderMapper.selectCount(new LambdaQueryWrapper<StoreOrderDO>()
                .ne(StoreOrderDO::getOrderType,OrderLogEnum.ORDER_TAKE_DUE.getValue())
                .eq(StoreOrderDO::getShopId,shopId)
                .eq(StoreOrderDO::getPaid, OrderInfoEnum.PAY_STATUS_1.getValue())
                .eq(StoreOrderDO::getRefundStatus, OrderInfoEnum.REFUND_STATUS_0.getValue())
                .eq(StoreOrderDO::getStatus, OrderInfoEnum.STATUS_3.getValue()));

        //待退款
        Long num4 = storeOrderMapper.selectCount(new LambdaQueryWrapper<StoreOrderDO>()
                .ne(StoreOrderDO::getOrderType,OrderLogEnum.ORDER_TAKE_DUE.getValue())
                .eq(StoreOrderDO::getShopId,shopId)
                .eq(StoreOrderDO::getRefundStatus,OrderInfoEnum.REFUND_STATUS_1.getValue()));

        //待支付
        Long num5 = storeOrderMapper.selectCount(new LambdaQueryWrapper<StoreOrderDO>()
                .ne(StoreOrderDO::getOrderType,OrderLogEnum.ORDER_TAKE_DUE.getValue())
                .eq(StoreOrderDO::getShopId,shopId)
                .eq(StoreOrderDO::getPaid, OrderInfoEnum.PAY_STATUS_0.getValue())
                        .eq(StoreOrderDO::getRefundStatus, OrderInfoEnum.REFUND_STATUS_0.getValue())
                        .eq(StoreOrderDO::getStatus, OrderInfoEnum.STATUS_0.getValue()));

        //已经退款
        Long num6 = storeOrderMapper.selectCount(new LambdaQueryWrapper<StoreOrderDO>()
                .ne(StoreOrderDO::getOrderType,OrderLogEnum.ORDER_TAKE_DUE.getValue())
                .eq(StoreOrderDO::getShopId,shopId)
                .eq(StoreOrderDO::getRefundStatus,OrderInfoEnum.REFUND_STATUS_2.getValue()));

        Long num7 = storeOrderMapper.selectCount(new LambdaQueryWrapper<StoreOrderDO>()
                .eq(StoreOrderDO::getOrderType,OrderLogEnum.ORDER_TAKE_DUE.getValue())
                .eq(StoreOrderDO::getShopId,shopId)
                .eq(StoreOrderDO::getDueStatus, OrderDueStatusEnum.DUE_STATUS_1.getValue()));

        AppOrderCountVO appOrderCountVO = AppOrderCountVO.builder()
                .num01(num1).num02(num2).num03(num3)
                .num04(num4).num05(num5).num06(num6).num07(num7).build();


        return AppHomeVO.builder()
                .appStoreShopVO(appStoreShopVO)
                .appTodayDataVO(appTodayDataVO)
                .appOrderCountVO(appOrderCountVO)
                .build();
    }

    @Override
    public void changeStatus(Long shopId, Integer status) {
        storeShopMapper.update(new StoreShopDO().setStatus(status),
                new LambdaQueryWrapper<StoreShopDO>().eq(StoreShopDO::getId,shopId));
    }

    @Override
    public Long check(Long uid) {
        if(isDemo) {
            return 9999L;
        }
        StoreUserDO storeUserDO = storeUserMapper.selectOne(new LambdaQueryWrapper<StoreUserDO>()
                .eq(StoreUserDO::getUid,uid));
        if(storeUserDO == null){
            throw exception(new ErrorCode(202406070,"你不是商家哦"));
        }
        if(storeUserDO.getStatus().equals(ShopCommonEnum.IS_STATUS_0.getValue())){
            throw exception(new ErrorCode(202406071,"你已经被禁止进入，请联系管理员"));
        }

        return storeUserDO.getShopId();
    }

    @Override
    public AppStoreShopVO updateShop(ShopReqVO shopReqVO) {
        StoreShopDO storeShopDO = storeShopMapper.selectById(shopReqVO.getId());
        storeShopDO.setName(shopReqVO.getName());
        storeShopDO.setMobile(shopReqVO.getMobile());
        storeShopDO.setAddress(shopReqVO.getAddress());
        storeShopDO.setAddressMap(shopReqVO.getAddressMap());
        storeShopDO.setLat(shopReqVO.getLat());
        storeShopDO.setLng(shopReqVO.getLng());
        storeShopDO.setStartTime(shopReqVO.getStartTime());
        storeShopDO.setEndTime(shopReqVO.getEndTime());
        storeShopMapper.updateById(storeShopDO);
        return BeanUtils.toBean(storeShopDO,AppStoreShopVO.class);
    }
}
