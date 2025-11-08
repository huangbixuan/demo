/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package co.yixiang.yshop.module.merchant.controller.app.home;

import co.yixiang.yshop.framework.common.pojo.CommonResult;
import co.yixiang.yshop.module.merchant.controller.app.home.vo.AppHomeVO;
import co.yixiang.yshop.module.merchant.controller.app.home.vo.ShopReqVO;
import co.yixiang.yshop.module.merchant.service.home.AppHomeService;
import co.yixiang.yshop.module.store.controller.admin.storewithdrawal.vo.StoreWithdrawalCreateReqVO;
import co.yixiang.yshop.module.store.controller.app.storeshop.vo.AppStoreShopVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static co.yixiang.yshop.framework.common.pojo.CommonResult.success;
import static co.yixiang.yshop.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

/**
 * <p>
 * 商户中心首页
 * </p>
 *
 * @author hupeng
 * @since 2024-6-5
 */
@Slf4j
@RestController
@Tag(name = "用户 APP - 商户中心首页")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/merchant/home")
public class AppHomeController {

    private final AppHomeService appHomeService;


    @GetMapping("/getData")
    @Operation(summary = "获取首页数据")
    public CommonResult<AppHomeVO> getData() {
        Long uid = getLoginUserId();
        return success(appHomeService.getData(uid));
    }

    @GetMapping("/changeStatus")
    @Operation(summary = "更新店铺状态")
    public CommonResult<Boolean> changeStatus( @RequestParam("status") Integer status,
                                          @RequestParam("shopId") Long shopId) {
        appHomeService.changeStatus(shopId,status);
        return success(true);
    }

    @GetMapping("/check")
    @Operation(summary = "获取首页数据")
    public CommonResult<Long> check() {
        Long uid = getLoginUserId();
        return success(appHomeService.check(uid));
    }

    @PostMapping("/updateShop")
    @Operation(summary = "更新店铺资料")
    public CommonResult<AppStoreShopVO> updateShop(@Valid @RequestBody ShopReqVO shopReqVO) {
        return success(appHomeService.updateShop(shopReqVO));
    }








}

