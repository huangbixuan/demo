package co.yixiang.yshop.module.order.handle;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import co.yixiang.yshop.framework.common.constant.ShopConstants;
import co.yixiang.yshop.framework.common.enums.OrderInfoEnum;
import co.yixiang.yshop.framework.common.enums.ShopCommonEnum;
import co.yixiang.yshop.framework.tenant.core.util.TenantUtils;
import co.yixiang.yshop.module.desk.dal.dataobject.shopdesk.ShopDeskDO;
import co.yixiang.yshop.module.desk.dal.mysql.shopdesk.ShopDeskMapper;
import co.yixiang.yshop.module.message.redismq.DelayedQueueListener;
import co.yixiang.yshop.module.message.redismq.msg.OrderMsg;
import co.yixiang.yshop.module.order.dal.dataobject.storeorder.StoreOrderDO;
import co.yixiang.yshop.module.order.enums.OrderDueStatusEnum;
import co.yixiang.yshop.module.order.enums.OrderLogEnum;
import co.yixiang.yshop.module.order.enums.OrderStatusEnum;
import co.yixiang.yshop.module.order.service.storeorder.AppStoreOrderService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 预约自动收货订单监听
 */
@Component
@Slf4j
public class OrderDueAutoConfirmListener implements DelayedQueueListener<OrderMsg> {
    @Resource
    private AppStoreOrderService appStoreOrderService;
    @Resource
    private ShopDeskMapper shopDeskMapper;
    @Override
    public String delayedQueueKey() {
        return ShopConstants.REDIS_ORDER_DUE_CONFIRM;
    }

    @Override
    public void consume(OrderMsg message) throws Exception {
        if(ObjectUtil.isNotNull(message) && StrUtil.isNotEmpty(message.getOrderId())) {
            TenantUtils.executeIgnore(() -> {
                StoreOrderDO storeOrderDO = appStoreOrderService.getOne(new LambdaQueryWrapper<StoreOrderDO>()
                        .eq(StoreOrderDO::getOrderId,message.getOrderId()));
                if(storeOrderDO == null){
                    return;
                }
                if(OrderDueStatusEnum.DUE_STATUS_1.getValue().equals(storeOrderDO.getDueStatus())){
                    storeOrderDO.setDueStatus(OrderDueStatusEnum.DUE_STATUS_3.getValue());
                    appStoreOrderService.updateById(storeOrderDO);
                    //设置桌面信息状态未预约
                    Long count = appStoreOrderService.count(new LambdaQueryWrapper<StoreOrderDO>()
                            .eq(StoreOrderDO::getDeskId,storeOrderDO.getDeskId())
                            .eq(StoreOrderDO::getDueStatus,OrderDueStatusEnum.DUE_STATUS_1.getValue()));
                    if(count == 0){
                        shopDeskMapper.update(ShopDeskDO.builder().bookStatus(ShopCommonEnum.IS_STATUS_0.getValue())
                                        .bookTime(null)
                                        .lastOrderNo(null)
                                        .build()
                                ,new LambdaQueryWrapper<ShopDeskDO>().eq(ShopDeskDO::getId,storeOrderDO.getDeskId()));
                    }

                }

                log.info("预约订单编号:{}自动确认成功",message.getOrderId());
            });
        }
    }
}
