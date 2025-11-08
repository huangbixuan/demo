package co.yixiang.yshop.module.merchant.service.order;


import co.yixiang.yshop.module.merchant.controller.app.home.vo.AppHomeVO;
import co.yixiang.yshop.module.order.controller.app.order.vo.AppStoreOrderQueryVo;

import java.util.List;

/**
 * 会员卡 Service 接口
 *
 * @author yshop
 */
public interface AppOrderService {

    /**
     * 订单列表
     * @param shopId 店铺id
     * @param type OrderStatusEnum
     * @param orderType orderType
     * @param page page
     * @param limit limit
     * @return list
     */
    List<AppStoreOrderQueryVo> orderList(Long shopId, String orderType,int type, int page, int limit,String key);

    void orderSend(Long id);

    void takeStoreOrder(Long id);

    void orderRefund(Long id,String price);

    AppStoreOrderQueryVo orderDetail(Long id);

}
