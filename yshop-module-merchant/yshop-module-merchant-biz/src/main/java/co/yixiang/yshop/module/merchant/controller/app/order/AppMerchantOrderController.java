/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package co.yixiang.yshop.module.merchant.controller.app.order;

import co.yixiang.yshop.framework.common.pojo.CommonResult;
import co.yixiang.yshop.framework.security.core.annotations.PreAuthenticated;
import co.yixiang.yshop.module.merchant.service.order.AppOrderService;
import co.yixiang.yshop.module.order.controller.app.order.vo.AppStoreOrderQueryVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static co.yixiang.yshop.framework.common.pojo.CommonResult.success;

/**
 * <p>
 * 商户中心首页
 * </p>
 *
 * @author hupeng
 * @since 2024-6-7
 */
@Slf4j
@RestController
@Tag(name = "用户 APP - 商户中心订单")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/merchant/order")
public class AppMerchantOrderController {

    private final AppOrderService appOrderService;


    /**
     * 订单列表
     */
    @PreAuthenticated
    @GetMapping("/list")
    @Operation(summary = "订单列表")
    @Parameters({
            @Parameter(name = "key", description = "搜索",
                     example = "1"),
            @Parameter(name = "shopId", description = "店铺ID",
                    required = true, example = "1"),
            @Parameter(name = "orderType",required = true),
            @Parameter(name = "type", description = "商品状态",
                    required = true, example = "1"),
            @Parameter(name = "page", description = "页码,默认为1",
                    required = true, example = "1"),
            @Parameter(name = "limit", description = "页大小,默认为10",
                    required = true, example = "10 ")
    })
    public CommonResult<List<AppStoreOrderQueryVo>> orderList(@RequestParam(value = "key") String key,
                                                              @RequestParam(value = "orderType", defaultValue = "takein") String orderType,
                                                              @RequestParam(value = "shopId", defaultValue = "0") Long shopId,
                                                              @RequestParam(value = "type", defaultValue = "0") int type,
                                                              @RequestParam(value = "page", defaultValue = "1") int page,
                                                              @RequestParam(value = "limit", defaultValue = "10") int limit) {
        return success(appOrderService.orderList(shopId, orderType,type, page, limit,key));
    }

    @GetMapping("/send")
    @Operation(summary = "出单")
    public CommonResult<Boolean> orderSend(@RequestParam("id") Long id) {
        appOrderService.orderSend(id);
        return success(true);
    }

    @GetMapping("/take")
    @Operation(summary = "确认收货")
    @Parameter(name = "id", description = "编号", required = true)
    public CommonResult<Boolean> takeStoreOrder(@RequestParam("id") Long id) {
        appOrderService.takeStoreOrder(id);
        return success(true);
    }

    @GetMapping("/refund")
    @Operation(summary = "退货")
    public CommonResult<Boolean> orderRefund(@RequestParam("id") Long id,@RequestParam("price") String price) {
        appOrderService.orderRefund(id,price);
        return success(true);
    }

    @GetMapping("/detail")
    @Operation(summary = "订单详情")
    public CommonResult<AppStoreOrderQueryVo> orderDetail(@RequestParam("id") Long id) {
        return success(appOrderService.orderDetail(id));
    }




}

