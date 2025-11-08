/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package co.yixiang.yshop.module.merchant.controller.app.withdrawal;

import co.yixiang.yshop.framework.common.pojo.CommonResult;
import co.yixiang.yshop.framework.common.pojo.PageResult;
import co.yixiang.yshop.framework.common.util.object.BeanUtils;
import co.yixiang.yshop.module.store.controller.admin.storerevenue.vo.StoreRevenuePageReqVO;
import co.yixiang.yshop.module.store.controller.admin.storerevenue.vo.StoreRevenueRespVO;
import co.yixiang.yshop.module.store.controller.admin.storewithdrawal.vo.StoreWithdrawalCreateReqVO;
import co.yixiang.yshop.module.store.controller.admin.storewithdrawal.vo.StoreWithdrawalPageReqVO;
import co.yixiang.yshop.module.store.controller.admin.storewithdrawal.vo.StoreWithdrawalRespVO;
import co.yixiang.yshop.module.store.controller.admin.userbank.vo.UserBankCreateReqVO;
import co.yixiang.yshop.module.store.controller.admin.userbank.vo.UserBankRespVO;
import co.yixiang.yshop.module.store.convert.storerevenue.StoreRevenueConvert;
import co.yixiang.yshop.module.store.convert.userbank.UserBankConvert;
import co.yixiang.yshop.module.store.dal.dataobject.storerevenue.StoreRevenueDO;
import co.yixiang.yshop.module.store.dal.dataobject.userbank.UserBankDO;
import co.yixiang.yshop.module.store.service.storerevenue.StoreRevenueService;
import co.yixiang.yshop.module.store.service.storewithdrawal.StoreWithdrawalService;
import co.yixiang.yshop.module.store.service.userbank.UserBankService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static co.yixiang.yshop.framework.common.pojo.CommonResult.success;
import static co.yixiang.yshop.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

/**
 * <p>
 * 提现
 * </p>
 *
 * @author hupeng
 * @since 2024-6-9
 */
@Slf4j
@RestController
@Tag(name = "用户 APP - 商户中心提现")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/merchant/with")
public class AppWithdrawalController {

    private final UserBankService userBankService;
    private final StoreWithdrawalService withdrawalService;
    private final StoreRevenueService storeRevenueService;


    @PostMapping("/addCard")
    @Operation(summary = "添加银行卡")
    public CommonResult<Long> addCard(@Valid @RequestBody UserBankCreateReqVO createReqVO) {
        return success(userBankService.createUserBank(createReqVO));
    }

    @GetMapping("/getList")
    @Operation(summary = "获取银行卡")
    public CommonResult<List<UserBankRespVO>> getList(@RequestParam("shopId") Long shopId) {
        List<UserBankDO> list = userBankService.getUserBankList(shopId);
        return success(UserBankConvert.INSTANCE.convertList(list));
    }

    @PostMapping("/createWithdrawal")
    @Operation(summary = "提现")
    public CommonResult<Long> createWithdrawal(@Valid @RequestBody StoreWithdrawalCreateReqVO createReqVO) {
        Long uid = getLoginUserId();
        createReqVO.setUid(uid);
        return success(withdrawalService.createWithdrawal(createReqVO));
    }

    @GetMapping("/getRevenue")
    @Operation(summary = "获得店铺收支明细")
    public CommonResult<List<StoreRevenueRespVO>> getStoreRevenuePage(@Valid StoreRevenuePageReqVO pageVO) {
        PageResult<StoreRevenueDO> pageResult = storeRevenueService.getStoreRevenuePage(pageVO);
        return success(BeanUtils.toBean(pageResult.getList(), StoreRevenueRespVO.class));
    }


    @GetMapping("/deleteCard")
    @Operation(summary = "删除提现账户")
    public CommonResult<Boolean> deleteUserBank(@RequestParam("id") Long id) {
        userBankService.deleteUserBank(id);
        return success(true);
    }


    @GetMapping("/withdrawals")
    @Operation(summary = "获得提现列表")
    public CommonResult<List<StoreWithdrawalRespVO>> getWithdrawals(@Valid StoreWithdrawalPageReqVO pageVO) {
        return success(withdrawalService.getWithdrawalPage(pageVO).getList());

    }


}

