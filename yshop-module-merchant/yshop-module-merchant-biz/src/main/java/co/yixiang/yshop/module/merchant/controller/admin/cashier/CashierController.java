package co.yixiang.yshop.module.merchant.controller.admin.cashier;

import co.yixiang.yshop.framework.common.pojo.CommonResult;
import co.yixiang.yshop.framework.security.core.annotations.PreAuthenticated;
import co.yixiang.yshop.module.merchant.controller.admin.cashier.vo.HangOrderRespVO;
import co.yixiang.yshop.module.merchant.controller.admin.cashier.vo.StoreCartRespVO;
import co.yixiang.yshop.module.merchant.controller.admin.cashier.vo.StoreCartSaveReqVO;
import co.yixiang.yshop.module.merchant.service.storecart.StoreCartService;
import co.yixiang.yshop.module.order.controller.app.order.param.AppOrderParam;
import co.yixiang.yshop.module.order.controller.app.order.param.AppPayParam;
import co.yixiang.yshop.module.order.service.storeorder.AppStoreOrderService;
import co.yixiang.yshop.module.product.controller.app.product.vo.AppStoreProductRespVo;
import co.yixiang.yshop.module.product.service.storeproduct.AppStoreProductService;
import co.yixiang.yshop.module.shop.controller.admin.recharge.vo.RechargeRespVO;
import co.yixiang.yshop.module.shop.convert.recharge.RechargeConvert;
import co.yixiang.yshop.module.shop.dal.dataobject.recharge.RechargeDO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import static co.yixiang.yshop.framework.common.pojo.CommonResult.success;
import static co.yixiang.yshop.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

@Tag(name = "管理后台 - 收银台")
@RestController
@RequestMapping("/cashier/")
@Validated
public class CashierController {

    @Resource
    private AppStoreProductService storeProductService;
    @Resource
    private StoreCartService storeCartService;
    @Resource
    private  AppStoreOrderService appStoreOrderService;

    @GetMapping("/printOrder")
    @Operation(summary = "打印小票")
    @Parameter(name = "orderId", description = "订单编号", required = true, example = "1024")
    public CommonResult<Boolean> printOrder(@RequestParam("orderId") String orderId) {
        storeCartService.printOrder(orderId);
        return success(true);
    }

    /**
     * 订单创建
     */
    @PostMapping("/createOrder")
    @Operation(summary = "订单创建")
    public CommonResult<Map<String, Object>> createOrder(@RequestBody @Valid AppOrderParam param) {
        return success(storeCartService.submitOrder(param));
    }

    /**
     * 更新订单
     */
    @PostMapping("/updateOrder")
    @Operation(summary = "更新订单")
    public CommonResult<Map<String, Object>> updateOrder(@RequestBody @Valid AppOrderParam param) {
        return success(storeCartService.updateOrder(param));
    }

    /**
     * 订单支付
     */
    @PreAuthenticated
    @PostMapping(value = "/pay")
    @Operation(summary = "订单支付")
    public CommonResult<Map<String, Object>> pay(@RequestBody @Valid AppPayParam param) {
        return success(appStoreOrderService.pay(param.getUid(),param));
    }


    @GetMapping("/getCarts")
    @Operation(summary = "获得购物车列表")
    @Parameter(name = "shopId", description = "店铺ID", required = true, example = "1024")
    public CommonResult<List<StoreCartRespVO>> getCartList(@RequestParam("shopId") Long shopId) {
        return success(storeCartService.getCartList(shopId));
    }


    @PostMapping("/addCart")
    @Operation(summary = "添加购物车")
    public CommonResult<Boolean> addCart(@Valid @RequestBody StoreCartSaveReqVO reqVO) {
        storeCartService.addCart(reqVO);
        return success(true);
    }

    @PostMapping("/updateCartNum")
    @Operation(summary = "修改购物车数量")
    public CommonResult<Boolean> updateCartNum(@Valid @RequestBody StoreCartSaveReqVO reqVO) {
        storeCartService.updateCartNum(reqVO);
        return success(true);
    }

    @GetMapping("/delCart")
    @Operation(summary = "删除购物车")
    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024")
    public CommonResult<Boolean> delCart(@RequestParam("ids") Collection<Long> ids) {
        storeCartService.removeCart(ids);
        return success(true);
    }

    @GetMapping("/hangUp")
    @Operation(summary = "挂单")
    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024,2048")
    public CommonResult<Boolean> hangUp(@RequestParam("ids") Collection<Long> ids) {
        storeCartService.hangUpOrder(ids);
        return success(true);
    }

    @GetMapping("/hangOff")
    @Operation(summary = "取单")
    @Parameters({
            @Parameter(name = "shopId", description = "门店ID", required = true, example = "1024"),
            @Parameter(name = "hangNo", description = "挂单号", required = true, example = "2048")
    })
    public CommonResult<Boolean> hangOff(@RequestParam("shopId") Long shopId,@RequestParam("hangNo") String hangNo) {
        storeCartService.hangOff(shopId,hangNo);
        return success(true);
    }

    @GetMapping("/hangDel")
    @Operation(summary = "删除取单")
    @Parameters({
            @Parameter(name = "shopId", description = "门店ID", required = true, example = "1024"),
            @Parameter(name = "hangNo", description = "挂单号", required = true, example = "2048")
    })
    public CommonResult<Boolean> hangDel(@RequestParam("shopId") Long shopId,@RequestParam("hangNo") String hangNo) {
        storeCartService.hangDel(shopId,hangNo);
        return success(true);
    }

    @GetMapping("/hangList")
    @Operation(summary = "挂单列表")
    @Parameter(name = "id", description = "门店id", required = true, example = "1024")
    public CommonResult<List<HangOrderRespVO>> hangList(@RequestParam("id") Long id) {
        return success(storeCartService.getHangList(id));
    }


    @GetMapping("/get/product")
    @Operation(summary = "获得商品详情")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<AppStoreProductRespVo> get(@RequestParam("id") Long id) {
        return success(storeProductService.getStoreProductById(id));
    }


}
