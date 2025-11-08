package co.yixiang.yshop.module.order.service.storeorder;

import co.yixiang.yshop.framework.common.pojo.PageResult;
import co.yixiang.yshop.module.order.controller.admin.storeorder.vo.*;
import co.yixiang.yshop.module.order.controller.app.order.vo.message.UserCartMsgVo;
import co.yixiang.yshop.module.order.dal.dataobject.storecartshare.StoreCartShareDO;
import co.yixiang.yshop.module.order.dal.dataobject.storeorder.StoreOrderDO;
import co.yixiang.yshop.module.order.service.storeorder.dto.OrderTimeDataDto;
import jakarta.validation.Valid;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

/**
 * 订单 Service 接口
 *
 * @author yshop
 */
public interface StoreOrderService {

    /**
     * 创建订单
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createStoreOrder(@Valid StoreOrderCreateReqVO createReqVO);

    /**
     * 更新订单
     *
     * @param updateReqVO 更新信息
     */
    void updateStoreOrder(@Valid StoreOrderUpdateReqVO updateReqVO);

    /**
     * 删除订单
     *
     * @param id 编号
     */
    void deleteStoreOrder(Long id);

    /**
     * 订单线下支付
     *
     * @param id 编号
     */
    void payStoreOrder(Long id);

    /**
     * 确认收货
     *
     * @param id 编号
     */
    void takeStoreOrder(Long id);

    /**
     * 获得订单
     *
     * @param id 编号
     * @return 订单
     */
    StoreOrderRespVO getStoreOrder(Long id);

    StoreOrderRespVO getStoreOrder(String orderId);

    /**
     * 获得订单列表
     *
     * @param deskId 编号
     * @return 订单列表
     */
    List<StoreOrderDO> getStoreOrderList(Long deskId);

    /**
     * 获得订单分页
     *
     * @param pageReqVO 分页查询
     * @return 订单分页
     */
    PageResult<StoreOrderRespVO> getStoreOrderPage(StoreOrderPageReqVO pageReqVO);

    /**
     * 获得订单列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 订单列表
     */
    List<StoreOrderDO> getStoreOrderList(StoreOrderExportReqVO exportReqVO);

    /**
     * 确认订单退款
     *
     * @param id 单号
     * @param price   金额
     * @param type    ShopCommonEnum
     * @param salesId 售后id
     */
    void orderRefund(Long id, BigDecimal price, Integer type, Long salesId);

    /**
     * 订单30s内通知
     * @return
     */
    Long orderNotice();

    /**
     * 取消预约
     *
     * @param id 编号
     */
    void cancelDueOrder(Long id);

    OrderTimeDataDto orderCount();

    /**
     * 同步购物车信息
     * @param userCartMsgVo
     */
    void syncCartInfo(UserCartMsgVo userCartMsgVo);

    /**
     * 获取桌面共享菜单
     * @param shopId
     * @param deskId
     * @return
     */
    List<StoreCartShareDO>  getShareCart(Long shopId, Long deskId);

    /**
     * 打印订单
     * @param id
     */
    void printOrder(Long id);

    /**
     * 退台
     * @param id
     */
    void claerDesk(Long id);


    /**
     * 退台
     * @param sourceId 原桌台
     * @param targetId 目标桌台
     */
    void changeDesk(Long sourceId,Long targetId);

   /**
     * 获取桌面预约订单数量
     * @param deskId 桌面ID
     * @return
     */
    Long getDueOrderCount(Long deskId);



    /**
     * 获取当前用户预约订单
     * @param deskId 桌面ID
     * @param uid 用户ID
     * @return
     */
    StoreOrderDO getDueOrder(Long deskId,Long uid);

    /**
     * 根据订单ID查询预约订单是否是当日订单
     * @return
     */
    boolean todayDueOrder(String orderId);


}
