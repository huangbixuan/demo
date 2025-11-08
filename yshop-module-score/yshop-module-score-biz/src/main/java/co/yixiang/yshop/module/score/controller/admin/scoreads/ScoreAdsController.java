package co.yixiang.yshop.module.score.controller.admin.scoreads;

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

import co.yixiang.yshop.module.score.controller.admin.scoreads.vo.*;
import co.yixiang.yshop.module.score.dal.dataobject.scoreads.ScoreAdsDO;
import co.yixiang.yshop.module.score.service.scoreads.ScoreAdsService;

@Tag(name = "管理后台 - 积分商城广告图管理")
@RestController
@RequestMapping("/score/ads")
@Validated
public class ScoreAdsController {

    @Resource
    private ScoreAdsService adsService;

    @PostMapping("/create")
    @Operation(summary = "创建积分商城广告图管理")
    @PreAuthorize("@ss.hasPermission('score:ads:create')")
    public CommonResult<Long> createAds(@Valid @RequestBody ScoreAdsSaveReqVO createReqVO) {
        return success(adsService.createAds(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新积分商城广告图管理")
    @PreAuthorize("@ss.hasPermission('score:ads:update')")
    public CommonResult<Boolean> updateAds(@Valid @RequestBody ScoreAdsSaveReqVO updateReqVO) {
        adsService.updateAds(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除积分商城广告图管理")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('score:ads:delete')")
    public CommonResult<Boolean> deleteAds(@RequestParam("id") Long id) {
        adsService.deleteAds(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得积分商城广告图管理")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('score:ads:query')")
    public CommonResult<ScoreAdsRespVO> getAds(@RequestParam("id") Long id) {
        ScoreAdsDO ads = adsService.getAds(id);
        return success(BeanUtils.toBean(ads, ScoreAdsRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得积分商城广告图管理分页")
    @PreAuthorize("@ss.hasPermission('score:ads:query')")
    public CommonResult<PageResult<ScoreAdsRespVO>> getAdsPage(@Valid ScoreAdsPageReqVO pageReqVO) {
        PageResult<ScoreAdsDO> pageResult = adsService.getAdsPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, ScoreAdsRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出积分商城广告图管理 Excel")
    @PreAuthorize("@ss.hasPermission('score:ads:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportAdsExcel(@Valid ScoreAdsPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ScoreAdsDO> list = adsService.getAdsPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "积分商城广告图管理.xls", "数据", ScoreAdsRespVO.class,
                        BeanUtils.toBean(list, ScoreAdsRespVO.class));
    }

}