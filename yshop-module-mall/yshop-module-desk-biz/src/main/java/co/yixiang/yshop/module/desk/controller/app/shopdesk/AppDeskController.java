/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package co.yixiang.yshop.module.desk.controller.app.shopdesk;

import co.yixiang.yshop.framework.common.pojo.CommonResult;
import co.yixiang.yshop.framework.security.core.annotations.PreAuthenticated;
import co.yixiang.yshop.module.desk.controller.app.shopdesk.vo.AppDeskDueVO;
import co.yixiang.yshop.module.desk.controller.app.shopdesk.vo.AppShopDeskVO;
import co.yixiang.yshop.module.desk.controller.app.shopdesk.vo.AppShopDueParam;
import co.yixiang.yshop.module.desk.service.shopdesk.AppShopDeskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static co.yixiang.yshop.framework.common.pojo.CommonResult.success;
import static co.yixiang.yshop.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

/**
 * <p>
 * 门店桌面预约
 * </p>
 *
 * @author hupeng
 * @since 2024-06-19
 */
@Slf4j
@RestController
@Tag(name = "用户 APP - 门店桌面预约")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/due")
public class AppDeskController {

    private final AppShopDeskService appShopDeskService;

    /**
     * 获取桌面列表
     */
    @GetMapping("/getList")
    @Operation(summary = "桌面列表")
    public CommonResult<List<AppDeskDueVO>> getList( @RequestParam("shopId") Long shopId,
                                                     @RequestParam(name="deskStatus",required = false) String deskStatus) {
        return success(appShopDeskService.getList(shopId,deskStatus));
    }

    /**
     * 获取桌面详情
     */
    @GetMapping("/getDetail")
    @Operation(summary = "获取桌面详情")
    public CommonResult<AppShopDeskVO> getDetail(@RequestParam("deskId") Long deskId) {
        return success(appShopDeskService.getDetail(deskId));
    }

    /**
     * 获取桌面信息
     */
    @GetMapping("/get-desk-info")
    @Operation(summary = "获取桌面信息")
    public CommonResult<AppShopDeskVO> getDesk(@RequestParam("deskId") Long deskId) {
        return success(appShopDeskService.getDesk(deskId));
    }

    /**
     * 提交预约订单
     */
    @PreAuthenticated
    @PostMapping("/createOrder")
    @Operation(summary = "提交预约订单")
    public CommonResult<String> create(@RequestBody @Valid AppShopDueParam param) {
        Long uid = getLoginUserId();
        return success(appShopDeskService.createDueOrder(uid,param));
    }





}

