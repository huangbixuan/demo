package co.yixiang.yshop.module.order.api;

import co.yixiang.yshop.module.order.api.dto.AppShopDueDTO;
import co.yixiang.yshop.module.order.api.dto.AppStoreOrderDTO;

import java.time.LocalDateTime;
import java.util.Map;

public interface OrderApi {

    /**
     * 获取预约订单
     * @param deskId 桌面ID
     * @param time  预约时间
     * @return AppStoreOrderDTO
     */
    AppStoreOrderDTO getDueOrderInfo(Long deskId, LocalDateTime time);

    String addDueOrder(AppShopDueDTO appShopDueDTO);

    AppStoreOrderDTO getOrderInfo(String orderId);

    /**
     * 微信转账
     * @param outBillNo 订单编号
     * @param openid 微信openid
     * @param amount 金额
     * @param remark 备注
     * @param userName 用户名
     * @return
     */
    Map<String, Object>  weixinTransfer(String outBillNo,String openid,String amount,String remark, String userName);
}
