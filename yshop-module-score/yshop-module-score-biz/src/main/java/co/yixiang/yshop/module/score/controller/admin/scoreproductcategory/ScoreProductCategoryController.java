package co.yixiang.yshop.module.score.controller.admin.scoreproductcategory;

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

import co.yixiang.yshop.module.score.controller.admin.scoreproductcategory.vo.*;
import co.yixiang.yshop.module.score.dal.dataobject.scoreproductcategory.ScoreProductCategoryDO;
import co.yixiang.yshop.module.score.service.scoreproductcategory.ScoreProductCategoryService;

@Tag(name = "管理后台 - 积分商品分类")
@RestController
@RequestMapping("/score/product-category")
@Validated
public class ScoreProductCategoryController {

    @Resource
    private ScoreProductCategoryService productCategoryService;

    @PostMapping("/create")
    @Operation(summary = "创建积分商品分类")
    @PreAuthorize("@ss.hasPermission('score:product-category:create')")
    public CommonResult<Long> createProductCategory(@Valid @RequestBody ScoreProductCategorySaveReqVO createReqVO) {
        return success(productCategoryService.createProductCategory(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新积分商品分类")
    @PreAuthorize("@ss.hasPermission('score:product-category:update')")
    public CommonResult<Boolean> updateProductCategory(@Valid @RequestBody ScoreProductCategorySaveReqVO updateReqVO) {
        productCategoryService.updateProductCategory(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除积分商品分类")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('score:product-category:delete')")
    public CommonResult<Boolean> deleteProductCategory(@RequestParam("id") Long id) {
        productCategoryService.deleteProductCategory(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得积分商品分类")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('score:product-category:query')")
    public CommonResult<ScoreProductCategoryRespVO> getProductCategory(@RequestParam("id") Long id) {
        ScoreProductCategoryDO productCategory = productCategoryService.getProductCategory(id);
        return success(BeanUtils.toBean(productCategory, ScoreProductCategoryRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得积分商品分类分页")
    @PreAuthorize("@ss.hasPermission('score:product-category:query')")
    public CommonResult<PageResult<ScoreProductCategoryRespVO>> getProductCategoryPage(@Valid ScoreProductCategoryPageReqVO pageReqVO) {
        PageResult<ScoreProductCategoryDO> pageResult = productCategoryService.getProductCategoryPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, ScoreProductCategoryRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出积分商品分类 Excel")
    @PreAuthorize("@ss.hasPermission('score:product-category:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportProductCategoryExcel(@Valid ScoreProductCategoryPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ScoreProductCategoryDO> list = productCategoryService.getProductCategoryPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "积分商品分类.xls", "数据", ScoreProductCategoryRespVO.class,
                        BeanUtils.toBean(list, ScoreProductCategoryRespVO.class));
    }

}