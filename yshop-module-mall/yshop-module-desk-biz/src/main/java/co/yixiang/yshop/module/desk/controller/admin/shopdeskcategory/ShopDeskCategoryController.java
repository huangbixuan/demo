package co.yixiang.yshop.module.desk.controller.admin.shopdeskcategory;

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

import co.yixiang.yshop.module.desk.controller.admin.shopdeskcategory.vo.*;
import co.yixiang.yshop.module.desk.dal.dataobject.shopdeskcategory.ShopDeskCategoryDO;
import co.yixiang.yshop.module.desk.service.shopdeskcategory.ShopDeskCategoryService;

@Tag(name = "管理后台 - 门店桌号分类")
@RestController
@RequestMapping("/desk/shop-desk-category")
@Validated
public class ShopDeskCategoryController {

    @Resource
    private ShopDeskCategoryService shopDeskCategoryService;

    @PostMapping("/create")
    @Operation(summary = "创建门店桌号分类")
    @PreAuthorize("@ss.hasPermission('desk:shop-desk-category:create')")
    public CommonResult<Long> createShopDeskCategory(@Valid @RequestBody ShopDeskCategorySaveReqVO createReqVO) {
        return success(shopDeskCategoryService.createShopDeskCategory(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新门店桌号分类")
    @PreAuthorize("@ss.hasPermission('desk:shop-desk-category:update')")
    public CommonResult<Boolean> updateShopDeskCategory(@Valid @RequestBody ShopDeskCategorySaveReqVO updateReqVO) {
        shopDeskCategoryService.updateShopDeskCategory(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除门店桌号分类")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('desk:shop-desk-category:delete')")
    public CommonResult<Boolean> deleteShopDeskCategory(@RequestParam("id") Long id) {
        shopDeskCategoryService.deleteShopDeskCategory(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得门店桌号分类")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('desk:shop-desk-category:query')")
    public CommonResult<ShopDeskCategoryRespVO> getShopDeskCategory(@RequestParam("id") Long id) {
        ShopDeskCategoryDO shopDeskCategory = shopDeskCategoryService.getShopDeskCategory(id);
        return success(BeanUtils.toBean(shopDeskCategory, ShopDeskCategoryRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得门店桌号分类分页")
    @PreAuthorize("@ss.hasPermission('desk:shop-desk-category:query')")
    public CommonResult<PageResult<ShopDeskCategoryRespVO>> getShopDeskCategoryPage(@Valid ShopDeskCategoryPageReqVO pageReqVO) {
        PageResult<ShopDeskCategoryDO> pageResult = shopDeskCategoryService.getShopDeskCategoryPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, ShopDeskCategoryRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出门店桌号分类 Excel")
    @PreAuthorize("@ss.hasPermission('desk:shop-desk-category:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportShopDeskCategoryExcel(@Valid ShopDeskCategoryPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ShopDeskCategoryDO> list = shopDeskCategoryService.getShopDeskCategoryPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "门店桌号分类.xls", "数据", ShopDeskCategoryRespVO.class,
                        BeanUtils.toBean(list, ShopDeskCategoryRespVO.class));
    }

}