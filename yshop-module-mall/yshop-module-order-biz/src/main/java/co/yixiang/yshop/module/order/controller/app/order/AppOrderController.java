/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package co.yixiang.yshop.module.order.controller.app.order;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import co.yixiang.yshop.framework.common.enums.OrderInfoEnum;
import co.yixiang.yshop.framework.common.exception.ErrorCode;
import co.yixiang.yshop.framework.common.pojo.CommonResult;
import co.yixiang.yshop.framework.security.core.annotations.PreAuthenticated;
import co.yixiang.yshop.module.desk.dal.dataobject.shopdesk.ShopDeskDO;
import co.yixiang.yshop.module.desk.dal.mysql.shopdesk.ShopDeskMapper;
import co.yixiang.yshop.module.desk.service.shopdesk.ShopDeskService;
import co.yixiang.yshop.module.express.controller.app.express.vo.SameCityCallBackBodyVO;
import co.yixiang.yshop.module.express.controller.app.express.vo.SameCityExpressVO;
import co.yixiang.yshop.module.member.controller.app.user.vo.AppUserOrderCountVo;
import co.yixiang.yshop.module.order.controller.app.order.param.*;
import co.yixiang.yshop.module.order.controller.app.order.vo.AppStoreOrderQueryVo;
import co.yixiang.yshop.module.order.controller.app.order.vo.message.CartMsgVo;
import co.yixiang.yshop.module.order.controller.app.order.vo.message.UserCartMsgVo;
import co.yixiang.yshop.module.order.dal.dataobject.storecartshare.StoreCartShareDO;
import co.yixiang.yshop.module.order.dal.dataobject.storeorder.StoreOrderDO;
import co.yixiang.yshop.module.order.dal.redis.order.AsyncOrderRedisDAO;
import co.yixiang.yshop.module.order.service.storeorder.AppStoreOrderService;
import co.yixiang.yshop.module.order.service.storeorder.StoreOrderService;
import co.yixiang.yshop.module.pay.http.HttpRequestNoticeNewParams;
import co.yixiang.yshop.module.store.controller.app.storeshop.vo.AppStoreShopVO;
import co.yixiang.yshop.module.store.convert.storeshop.StoreShopConvert;
import co.yixiang.yshop.module.store.dal.dataobject.storeshop.StoreShopDO;
import co.yixiang.yshop.module.store.service.storeshop.AppStoreShopService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.egzosn.pay.spring.boot.core.PayServiceManager;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static co.yixiang.yshop.framework.common.exception.util.ServiceExceptionUtil.exception;
import static co.yixiang.yshop.framework.common.pojo.CommonResult.success;
import static co.yixiang.yshop.framework.security.core.util.SecurityFrameworkUtils.getLoginUser;
import static co.yixiang.yshop.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;
import static co.yixiang.yshop.module.order.enums.ErrorCodeConstants.PARAM_ERROR;
import static co.yixiang.yshop.module.order.enums.ErrorCodeConstants.STORE_ORDER_NOT_EXISTS;

/**
 * <p>
 * 订单控制器
 * </p>
 *
 * @author hupeng
 * @since 2023-6-23
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Tag(name = "用户 APP - 订单模块")
@RequestMapping("/order")
public class AppOrderController {

    private final AppStoreOrderService appStoreOrderService;
    private final AsyncOrderRedisDAO asyncOrderRedisDAO;
    private final PayServiceManager manager;
    private final AppStoreShopService appStoreShopService;
    private final ShopDeskService shopDeskService;
    private final ShopDeskMapper shopDeskMapper;
    private final StoreOrderService storeOrderService;






    /**
     * 订单创建
     */
    @PreAuthenticated
    @PostMapping("/create")
    @Operation(summary = "订单创建")
    public CommonResult<Map<String, Object>> create(@RequestBody @Valid AppOrderParam param) {
        Long uid = getLoginUserId();
        return success(appStoreOrderService.createOrder(uid, param));
    }


    /**
     * 订单支付
     */
    @PreAuthenticated
    @PostMapping(value = "/pay")
    @Operation(summary = "订单支付")
    public CommonResult<Map<String, Object>> pay(@RequestBody @Valid AppPayParam param) {
        Long uid = getLoginUserId();
        return success(appStoreOrderService.pay(uid,param));
    }

    /**
     * 支付回调地址
     *
     * @param request   请求
     * @param detailsId 列表id
     * @return 支付是否成功
     */
    @RequestMapping(value = "/notify/payBack{detailsId}.json")
    public String payBack(HttpServletRequest request, @PathVariable String detailsId)  {
        return manager.payBack(detailsId, new HttpRequestNoticeNewParams(request));
    }


    /**
     * 订单列表
     */
    @PreAuthenticated
    @GetMapping("/list")
    @Operation(summary = "订单列表")
    @Parameters({
            @Parameter(name = "orderType",required = true),
            @Parameter(name = "type", description = "商品状态,-1全部 默认为0未支付 1待发货 2待收货 3待评价 4已完成 5退款中 6已退款 7退款",
                    required = true, example = "1"),
            @Parameter(name = "page", description = "页码,默认为1",
                    required = true, example = "1"),
            @Parameter(name = "limit", description = "页大小,默认为10",
                    required = true, example = "10      ")
    })
    public CommonResult<List<AppStoreOrderQueryVo>> orderList(
                                       @RequestParam(value = "orderType") String orderType,
                                       @RequestParam(value = "type", defaultValue = "0") int type,
                                       @RequestParam(value = "page", defaultValue = "1") int page,
                                       @RequestParam(value = "limit", defaultValue = "10") int limit) {
        Long uid = getLoginUserId();
        type = 99;
        return success(appStoreOrderService.orderList(uid, orderType,type, page, limit));
    }


    /**
     * 订单详情
     */
    @PreAuthenticated
    @GetMapping("/detail/{key}")
    @Operation(summary = "订单详情")
    @Parameter(name = "key", description = "唯一的uni值或者订单号", required = true, example = "10      ")
    public CommonResult<AppStoreOrderQueryVo> detail(@PathVariable String key) {
        Long uid = getLoginUserId();
        if (StrUtil.isEmpty(key)) {
            throw exception(PARAM_ERROR);
        }
        AppStoreOrderQueryVo storeOrder = appStoreOrderService.getOrderInfo(key, uid);
        if (ObjectUtil.isNull(storeOrder)) {
            throw exception(STORE_ORDER_NOT_EXISTS);
        }
        return success(appStoreOrderService.handleOrder(storeOrder));
    }


    /**
     * 订单收货
     */
    @PreAuthenticated
    @PostMapping("/take")
    @Operation(summary = "订单收货")
    public CommonResult<Boolean> orderTake(@RequestBody @Validated AppDoOrderParam param) {
        Long uid = getLoginUserId();
        appStoreOrderService.takeOrder(param.getUni(), uid);
        return success(true);
    }

    /**
     * 订单退款审核
     */
    @PostMapping("/refund")
    @Operation(summary = "订单退款审核")
    public CommonResult<Boolean> refundVerify(@RequestBody AppRefundParam param) {
        Long uid = getLoginUserId();
        appStoreOrderService.orderApplyRefund(param.getRefundReasonWapExplain(),
                param.getRefundReasonWapImg(),
                param.getText(),
                param.getUni(), uid);
        return success(true);
    }




    /**
     * 订单删除
     */
    @PreAuthenticated
    @PostMapping("/del")
    @Operation(summary = "订单删除")
    public CommonResult<Boolean> orderDel(@Validated @RequestBody AppDoOrderParam param) {
        Long uid = getLoginUserId();
        appStoreOrderService.removeOrder(param.getUni(), uid);
        return success(true);
    }




    /**
     * 订单取消   未支付的订单回退积分,回退优惠券,回退库存
     */
    @PreAuthenticated
    @PostMapping("/cancel")
    @Operation(summary = "订单取消")
    public CommonResult<Boolean> cancelOrder(@Validated @RequestBody AppHandleOrderParam param) {
        Long uid = getLoginUserId();
        appStoreOrderService.cancelOrder(param.getId(), uid);
        return success(true);
    }

    /**
     * 个人中心订单统计
     */
    @PreAuthenticated
    @PostMapping("/user_count")
    @Operation(summary = "个人中心订单统计")
    public CommonResult<AppUserOrderCountVo> countOrder() {
        Long uid = getLoginUserId();
        AppUserOrderCountVo appUserOrderCountVo = asyncOrderRedisDAO.get(uid);
        return success(appUserOrderCountVo);

    }


    @PreAuthenticated
    @GetMapping("/getShop")
    @Operation(summary = "获取店铺与桌面判断")
    public CommonResult<AppStoreShopVO> getShop(@RequestParam("shopId") Integer shopId, @RequestParam("deskId") Long deskId) {
        StoreShopDO storeShopDO = appStoreShopService.getById(shopId);
        //Long uid = getLoginUserId();
        LocalDateTime nowTime = LocalDateTime.now();
        ShopDeskDO shopDeskDO = shopDeskMapper.selectOne(new LambdaQueryWrapper<ShopDeskDO>()
                .eq(ShopDeskDO::getShopId,shopId)
                .eq(ShopDeskDO::getId,deskId)
                .ge(ShopDeskDO::getLastOrderTime,nowTime.minusHours(2))
                .eq(ShopDeskDO::getLastOrderStatus, OrderInfoEnum.DESK_ORDER_STATUS_ING.getValue()));
        AppStoreShopVO appStoreShopVO =  StoreShopConvert.INSTANCE.convert02(storeShopDO);
        appStoreShopVO.setIsEmpty(true);
        if(shopDeskDO != null) {
            appStoreShopVO.setIsEmpty(false);
            StoreOrderDO storeOrderDO = appStoreOrderService.getOne(new LambdaQueryWrapper<StoreOrderDO>()
                    .eq(StoreOrderDO::getOrderId,shopDeskDO.getLastOrderNo())
                    //.eq(StoreOrderDO::getUid,uid)
                    .eq(StoreOrderDO::getPaid, OrderInfoEnum.PAY_STATUS_0.getValue()));
            if(storeOrderDO != null){
                appStoreShopVO.setDeskOrderId(shopDeskDO.getLastOrderNo());
                appStoreShopVO.setDeskPeople(storeOrderDO.getDeskPeople());
            }else {
                appStoreShopVO.setDeskOrderId("");
                appStoreShopVO.setDeskPeople(Integer.valueOf(shopDeskDO.getNote()));
            }
        }
        return success(appStoreShopVO);
    }



    @PreAuthenticated
    @GetMapping("/openDesk")
    @Operation(summary = "开台")
    public CommonResult<Boolean> openDesk(@RequestParam("shopId") Integer shopId, @RequestParam("deskId") Long deskId,
                                          @RequestParam("people") String people) {
        Long uid = getLoginUserId();
        StoreOrderDO dueOrder = storeOrderService.getDueOrder(deskId,uid);
        if(dueOrder != null){
            throw exception(new ErrorCode(2025090241,"您有当前桌台预约订单,请去预约单里直接点餐即可!"));
        }
        Long count = storeOrderService.getDueOrderCount(deskId);
        if(count > 0){
            throw exception(new ErrorCode(2025090240,"今天此桌台已经被预约啦!"));
        }
        LocalDateTime nowTime = LocalDateTime.now();
        ShopDeskDO shopDeskDO = shopDeskMapper.selectOne(new LambdaQueryWrapper<ShopDeskDO>()
                .eq(ShopDeskDO::getShopId,shopId)
                .eq(ShopDeskDO::getId,deskId));
        if(shopDeskDO == null){
            throw exception(new ErrorCode(2025070500,"当前门店不存在此桌号"));
        }
        if(shopDeskDO.getLastOrderStatus() == null || !OrderInfoEnum.DESK_ORDER_STATUS_ING.getValue().equals(shopDeskDO.getLastOrderStatus())){
            shopDeskDO.setLastOrderTime(nowTime);
            shopDeskDO.setLastOrderStatus(OrderInfoEnum.DESK_ORDER_STATUS_ING.getValue());
            shopDeskDO.setNote(people);
            shopDeskDO.setLastOrderNo("");
            shopDeskMapper.updateById(shopDeskDO);
        }

        return success(true);
    }



    /**
     * 取消预约
     */
    @GetMapping("/cancelDue")
    @Operation(summary = "取消预约")
    public CommonResult<Boolean> cancelDue( @RequestParam("id") Long id) {
        storeOrderService.cancelDueOrder(id);
        return success(true);
    }

    @GetMapping("/offPay")
    @Operation(summary = "订单线下支付")
    public CommonResult<Boolean> payStoreOrder(@RequestParam("id") Long id) {
        storeOrderService.payStoreOrder(id);
        return success(true);
    }


    @PostMapping("/sync-cart")
    @Operation(summary = "同步购物车信息")
    public CommonResult<Boolean> syncCart(@Validated @RequestBody UserCartMsgVo userCartMsgVo) {
        storeOrderService.syncCartInfo(userCartMsgVo);
        return success(true);
    }

    @GetMapping("/get-share-cart")
    @Operation(summary = "获取共享菜单")
    public CommonResult<List<StoreCartShareDO>> getShareCart(@RequestParam("shopId") Long shopId, @RequestParam("deskId") Long deskId) {
        return success(storeOrderService.getShareCart(shopId,deskId));
    }


    @PostMapping("/samecity/price")
    @Operation(summary = "同城配送预下单")
    public CommonResult<SameCityExpressVO> getBsameCityPrice(@RequestBody @Valid AppOrderParam param) {
        SameCityExpressVO sameCityExpressVO = appStoreOrderService.sameCityPriceOrOrder(param,"price");
        return success(sameCityExpressVO);
    }

    @RequestMapping(value="/samecity/callBack",consumes = "application/x-www-form-urlencoded")
    @Operation(summary = "同城配送预下单")
    public CommonResult<Map<String, Object>> bsameCallBack(@ModelAttribute SameCityCallBackBodyVO sameCityCallBackBodyVO){
        System.out.println("sameCityCallBackBodyVO:"+sameCityCallBackBodyVO);
        appStoreOrderService.bsameCallBack(sameCityCallBackBodyVO);
        Map<String, Object> map = MapUtil.newHashMap();
        map.put("result",true);
        map.put("returnCode","200");
        map.put("message","提交成功");

        return success(map);
    }




}

