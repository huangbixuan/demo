package co.yixiang.yshop.module.merchant.service.storecart;

import co.yixiang.yshop.module.merchant.controller.admin.cashier.vo.HangOrderRespVO;
import co.yixiang.yshop.module.merchant.controller.admin.cashier.vo.StoreCartRespVO;
import co.yixiang.yshop.module.merchant.controller.admin.cashier.vo.StoreCartSaveReqVO;
import co.yixiang.yshop.module.order.controller.app.order.param.AppOrderParam;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 购物车 Service 接口
 *
 * @author yshop
 */
public interface StoreCartService {

    /**
     * 添加购物车
     * @param reqVO
     */
    void addCart(StoreCartSaveReqVO reqVO);

    /**
     * 修改购物车数量
     * @param reqVO
     */
    void updateCartNum(StoreCartSaveReqVO reqVO);

    /**
     * 删除
     * @param ids
     */
    void removeCart(Collection<Long> ids);

    /**
     * 挂单
     * @param ids 购物车ID
     */
    void hangUpOrder(Collection<Long> ids);

    /**
     * 取单
     * @param shopId
     * @param hangNo
     */
    void hangOff(Long shopId,String hangNo);

    /**
     * 取单
     * @param shopId
     * @param hangNo
     */
    void hangDel(Long shopId,String hangNo);

    /**
     * 获取挂单列表
     * @param shopId
     * @return
     */
    List<HangOrderRespVO> getHangList(Long shopId);

    /**
     * 获取购物车列表
     * @param shopId
     * @return
     */
    List<StoreCartRespVO> getCartList(Long shopId);

    /**
     * 创建订单
     * @param param param
     * @return map
     */
    Map<String, Object> submitOrder(AppOrderParam param);


    /**
     * 更新订单
     * @param param param
     * @return map
     */
    Map<String, Object> updateOrder(AppOrderParam param);

    /**
     * 打印小票
     * @param orderId
     */
    void printOrder(String orderId);


}