package co.yixiang.yshop.module.store.controller.admin.shopduerule;

import org.springframework.web.bind.annotation.*;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;

import jakarta.validation.constraints.*;
import jakarta.validation.*;
import jakarta.servlet.http.*;
import java.util.*;
import java.io.IOException;

import co.yixiang.yshop.framework.common.pojo.PageParam;
import co.yixiang.yshop.framework.common.pojo.PageResult;
import co.yixiang.yshop.framework.common.pojo.CommonResult;
import co.yixiang.yshop.framework.common.util.object.BeanUtils;
import static co.yixiang.yshop.framework.common.pojo.CommonResult.success;

import co.yixiang.yshop.framework.excel.core.util.ExcelUtils;

import co.yixiang.yshop.framework.apilog.core.annotation.ApiAccessLog;
import static co.yixiang.yshop.framework.apilog.core.enums.OperateTypeEnum.*;

import co.yixiang.yshop.module.store.controller.admin.shopduerule.vo.*;
import co.yixiang.yshop.module.store.dal.dataobject.shopduerule.ShopDueRuleDO;
import co.yixiang.yshop.module.store.service.shopduerule.ShopDueRuleService;

@Tag(name = "管理后台 - 预约规则")
@RestController
@RequestMapping("/store/shop-due-rule")
@Validated
public class ShopDueRuleController {

    @Resource
    private ShopDueRuleService shopDueRuleService;

    @PostMapping("/create")
    @Operation(summary = "创建预约规则")
    @PreAuthorize("@ss.hasPermission('store:shop-due-rule:create')")
    public CommonResult<Long> createShopDueRule(@Valid @RequestBody ShopDueRuleSaveReqVO createReqVO) {
        return success(shopDueRuleService.createShopDueRule(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新预约规则")
    @PreAuthorize("@ss.hasPermission('store:shop-due-rule:update')")
    public CommonResult<Boolean> updateShopDueRule(@Valid @RequestBody ShopDueRuleSaveReqVO updateReqVO) {
        shopDueRuleService.updateShopDueRule(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除预约规则")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('store:shop-due-rule:delete')")
    public CommonResult<Boolean> deleteShopDueRule(@RequestParam("id") Long id) {
        shopDueRuleService.deleteShopDueRule(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得预约规则")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('store:shop-due-rule:query')")
    public CommonResult<ShopDueRuleRespVO> getShopDueRule(@RequestParam("id") Long id) {
        ShopDueRuleDO shopDueRule = shopDueRuleService.getShopDueRule(id);
        return success(BeanUtils.toBean(shopDueRule, ShopDueRuleRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得预约规则分页")
    @PreAuthorize("@ss.hasPermission('store:shop-due-rule:query')")
    public CommonResult<PageResult<ShopDueRuleRespVO>> getShopDueRulePage(@Valid ShopDueRulePageReqVO pageReqVO) {
        return success(shopDueRuleService.getShopDueRulePage(pageReqVO));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出预约规则 Excel")
    @PreAuthorize("@ss.hasPermission('store:shop-due-rule:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportShopDueRuleExcel(@Valid ShopDueRulePageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ShopDueRuleRespVO> list = shopDueRuleService.getShopDueRulePage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "预约规则.xls", "数据", ShopDueRuleRespVO.class, list);
    }

}