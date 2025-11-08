package co.yixiang.yshop.module.store.controller.admin.storerevenue;

import co.yixiang.yshop.framework.common.util.object.BeanUtils;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;

import jakarta.validation.constraints.*;
import jakarta.validation.*;
import java.util.*;
import java.io.IOException;

import co.yixiang.yshop.framework.common.pojo.PageResult;
import co.yixiang.yshop.framework.common.pojo.CommonResult;
import static co.yixiang.yshop.framework.common.pojo.CommonResult.success;

import co.yixiang.yshop.framework.excel.core.util.ExcelUtils;


import co.yixiang.yshop.module.store.controller.admin.storerevenue.vo.*;
import co.yixiang.yshop.module.store.dal.dataobject.storerevenue.StoreRevenueDO;
import co.yixiang.yshop.module.store.convert.storerevenue.StoreRevenueConvert;
import co.yixiang.yshop.module.store.service.storerevenue.StoreRevenueService;

@Tag(name = "管理后台 - 店铺收支明细")
@RestController
@RequestMapping("/mall/store-revenue")
@Validated
public class StoreRevenueController {

    @Resource
    private StoreRevenueService storeRevenueService;

    @PostMapping("/create")
    @Operation(summary = "创建店铺收支明细")
    @PreAuthorize("@ss.hasPermission('mall:store-revenue:create')")
    public CommonResult<Long> createStoreRevenue(@Valid @RequestBody StoreRevenueCreateReqVO createReqVO) {
        return success(storeRevenueService.createStoreRevenue(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新店铺收支明细")
    @PreAuthorize("@ss.hasPermission('mall:store-revenue:update')")
    public CommonResult<Boolean> updateStoreRevenue(@Valid @RequestBody StoreRevenueUpdateReqVO updateReqVO) {
        storeRevenueService.updateStoreRevenue(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除店铺收支明细")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('mall:store-revenue:delete')")
    public CommonResult<Boolean> deleteStoreRevenue(@RequestParam("id") Long id) {
        storeRevenueService.deleteStoreRevenue(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得店铺收支明细")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('mall:store-revenue:query')")
    public CommonResult<StoreRevenueRespVO> getStoreRevenue(@RequestParam("id") Long id) {
        StoreRevenueDO storeRevenue = storeRevenueService.getStoreRevenue(id);
        return success(BeanUtils.toBean(storeRevenue, StoreRevenueRespVO.class));
    }

    @GetMapping("/list")
    @Operation(summary = "获得店铺收支明细列表")
    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024,2048")
    @PreAuthorize("@ss.hasPermission('mall:store-revenue:query')")
    public CommonResult<List<StoreRevenueRespVO>> getStoreRevenueList(@RequestParam("ids") Collection<Long> ids) {
        List<StoreRevenueDO> list = storeRevenueService.getStoreRevenueList(ids);
        return success(StoreRevenueConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    @Operation(summary = "获得店铺收支明细分页")
    @PreAuthorize("@ss.hasPermission('mall:store-revenue:query')")
    public CommonResult<PageResult<StoreRevenueRespVO>> getStoreRevenuePage(@Valid StoreRevenuePageReqVO pageVO) {
        PageResult<StoreRevenueDO> pageResult = storeRevenueService.getStoreRevenuePage(pageVO);
        return success(BeanUtils.toBean(pageResult, StoreRevenueRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出店铺收支明细 Excel")
    @PreAuthorize("@ss.hasPermission('mall:store-revenue:export')")
    public void exportStoreRevenueExcel(@Valid StoreRevenueExportReqVO exportReqVO,
              HttpServletResponse response) throws IOException {
        List<StoreRevenueDO> list = storeRevenueService.getStoreRevenueList(exportReqVO);
        // 导出 Excel
        List<StoreRevenueExcelVO> datas = StoreRevenueConvert.INSTANCE.convertList02(list);
        ExcelUtils.write(response, "店铺收支明细.xls", "数据", StoreRevenueExcelVO.class, datas);
    }

}
