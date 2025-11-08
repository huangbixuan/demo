package co.yixiang.yshop.module.order.service.storeorder;

import co.yixiang.yshop.module.express.controller.app.express.vo.SameCityCallBackBodyVO;
import co.yixiang.yshop.module.express.controller.app.express.vo.SameCityExpressVO;
import co.yixiang.yshop.module.order.controller.app.order.param.AppComputeOrderParam;
import co.yixiang.yshop.module.order.controller.app.order.param.AppOrderParam;
import co.yixiang.yshop.module.order.controller.app.order.param.AppPayParam;
import co.yixiang.yshop.module.order.controller.app.order.vo.AppConfirmOrderVo;
import co.yixiang.yshop.module.order.controller.app.order.vo.AppStoreOrderQueryVo;
import co.yixiang.yshop.module.order.dal.dataobject.storeorder.StoreOrderDO;
import co.yixiang.yshop.module.order.dal.dataobject.storeordercartinfo.StoreOrderCartInfoDO;
import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpServletRequest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 订单 Service 接口
 *
 * @author yshop
 */
public interface AppStoreOrderService extends IService<StoreOrderDO> {



    /**
     * 订单信息
     * @param unique 唯一值或者单号
     * @param uid 用户id
     * @return YxStoreOrderQueryVo
     */
    AppStoreOrderQueryVo getOrderInfo(String unique, Long uid);


    /**
     * 创建订单
     * @param uid 用户uid
     * @param param param
     * @return map
     */
    Map<String, Object> createOrder(Long uid, AppOrderParam param);


    /**
     * 第三方支付
     * @param param 订单
     * @param uid 用户id
     */
    Map<String, Object> pay(Long uid, AppPayParam param);


    /**
     * 余额支付
     * @param orderId 订单号
     * @param uid 用户id
     */
    void yuePay(String orderId,Long uid);

    /**
     * 支付成功后操作
     * @param orderId 订单号
     * @param payType 支付方式
     */
    void paySuccess(String orderId,String payType);


    /**
     * 订单列表
     * @param uid 用户id
     * @param orderType 订单类型
     * @param type OrderStatusEnum
     * @param page page
     * @param limit limit
     * @return list
     */
    List<AppStoreOrderQueryVo> orderList(Long uid,String orderType, int type, int page, int limit);

    List<StoreOrderCartInfoDO> streamData(Integer i, List<StoreOrderCartInfoDO> cartInfos);

    /**
     * 处理订单返回的状态
     * @param order order
     * @return YxStoreOrderQueryVo
     */
    AppStoreOrderQueryVo handleOrder(AppStoreOrderQueryVo order);

    /**
     * 订单确认收货
     * @param orderId 单号
     * @param uid uid
     */
    void takeOrder(String orderId,Long uid);

    /**
     * 申请退款
     * @param explain 退款备注
     * @param Img 图片
     * @param text 理由
     * @param orderId 订单号
     * @param uid uid
     */
    void orderApplyRefund(String explain,String Img,String text,String orderId, Long uid);

    /**
     * 删除订单
     * @param orderId 单号
     * @param uid uid
     */
    void removeOrder(String orderId,Long uid);

    /**
     * 未付款取消订单
     * @param orderId 订单号
     * @param uid 用户id
     */
    void cancelOrder(String orderId,Long uid);


    /**
     * 同城配送下单
     * @param appOrderParam 参数
     * @param method price-预下单 order-真实下单
     * @return
     */
    SameCityExpressVO sameCityPriceOrOrder(AppOrderParam appOrderParam, String method);

    /**
     * 同城回调
     * @param sameCityCallBackBodyVO sameCityCallBackBodyVO
     */
    void bsameCallBack(SameCityCallBackBodyVO sameCityCallBackBodyVO);

    /**
     * 取消同城订单
     * @param id 订单ID
     */
    void sameCityCancelOrder(Long id);



}
