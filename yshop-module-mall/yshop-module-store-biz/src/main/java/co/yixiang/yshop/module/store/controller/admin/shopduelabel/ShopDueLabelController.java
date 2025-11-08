package co.yixiang.yshop.module.store.controller.admin.shopduelabel;

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

import co.yixiang.yshop.module.store.controller.admin.shopduelabel.vo.*;
import co.yixiang.yshop.module.store.dal.dataobject.shopduelabel.ShopDueLabelDO;
import co.yixiang.yshop.module.store.service.shopduelabel.ShopDueLabelService;

@Tag(name = "管理后台 - 预约标签")
@RestController
@RequestMapping("/store/shop-due-label")
@Validated
public class ShopDueLabelController {

    @Resource
    private ShopDueLabelService shopDueLabelService;

    @PostMapping("/create")
    @Operation(summary = "创建预约标签")
    @PreAuthorize("@ss.hasPermission('store:shop-due-label:create')")
    public CommonResult<Long> createShopDueLabel(@Valid @RequestBody ShopDueLabelSaveReqVO createReqVO) {
        return success(shopDueLabelService.createShopDueLabel(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新预约标签")
    @PreAuthorize("@ss.hasPermission('store:shop-due-label:update')")
    public CommonResult<Boolean> updateShopDueLabel(@Valid @RequestBody ShopDueLabelSaveReqVO updateReqVO) {
        shopDueLabelService.updateShopDueLabel(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除预约标签")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('store:shop-due-label:delete')")
    public CommonResult<Boolean> deleteShopDueLabel(@RequestParam("id") Long id) {
        shopDueLabelService.deleteShopDueLabel(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得预约标签")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('store:shop-due-label:query')")
    public CommonResult<ShopDueLabelRespVO> getShopDueLabel(@RequestParam("id") Long id) {
        ShopDueLabelDO shopDueLabel = shopDueLabelService.getShopDueLabel(id);
        return success(BeanUtils.toBean(shopDueLabel, ShopDueLabelRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得预约标签分页")
    @PreAuthorize("@ss.hasPermission('store:shop-due-label:query')")
    public CommonResult<PageResult<ShopDueLabelRespVO>> getShopDueLabelPage(@Valid ShopDueLabelPageReqVO pageReqVO) {
        PageResult<ShopDueLabelDO> pageResult = shopDueLabelService.getShopDueLabelPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, ShopDueLabelRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出预约标签 Excel")
    @PreAuthorize("@ss.hasPermission('store:shop-due-label:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportShopDueLabelExcel(@Valid ShopDueLabelPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ShopDueLabelDO> list = shopDueLabelService.getShopDueLabelPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "预约标签.xls", "数据", ShopDueLabelRespVO.class,
                        BeanUtils.toBean(list, ShopDueLabelRespVO.class));
    }

}