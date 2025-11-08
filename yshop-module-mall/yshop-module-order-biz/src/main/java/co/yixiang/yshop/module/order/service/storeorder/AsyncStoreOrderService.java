package co.yixiang.yshop.module.order.service.storeorder;

import co.yixiang.yshop.module.member.controller.app.user.vo.AppUserOrderCountVo;
import co.yixiang.yshop.module.order.controller.admin.storeorder.vo.ShoperOrderTimeDataVo;
import co.yixiang.yshop.module.order.controller.app.order.vo.AppStoreOrderQueryVo;
import co.yixiang.yshop.module.order.controller.app.order.vo.message.ShopOrderMsgVo;
import co.yixiang.yshop.module.order.controller.app.order.vo.message.UserCartMsgVo;
import co.yixiang.yshop.module.order.dal.dataobject.storeorder.StoreOrderDO;
import co.yixiang.yshop.module.order.service.storeorder.dto.OrderTimeDataDto;

/**
 * 异步订单 Service 接口
 *
 * @author yshop
 */
public interface AsyncStoreOrderService {

    /**
     * 计算某个用户的订单统计数据
     * @param uid uid>0 取用户 否则取所有
     * @return UserOrderCountVo
     */
    void orderData(Long uid);



    /**
     * 异步后台数据统计
     */
    void getOrderTimeData();


    void print(StoreOrderDO storeOrderDO);

    /**
     * websocket同步消息
     * @param userCartMsgVo
     */
    void pubCartInfo(UserCartMsgVo userCartMsgVo);

    /**
     * websocket发布新订单
     * @param shopOrderMsgVo
     */
    void pubOrderInfo(ShopOrderMsgVo shopOrderMsgVo);


}
