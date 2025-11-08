package co.yixiang.yshop.module.shop.controller.admin.recharge;

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

import co.yixiang.yshop.module.shop.controller.admin.recharge.vo.*;
import co.yixiang.yshop.module.shop.dal.dataobject.recharge.RechargeDO;
import co.yixiang.yshop.module.shop.convert.recharge.RechargeConvert;
import co.yixiang.yshop.module.shop.service.recharge.RechargeService;

@Tag(name = "管理后台 - 充值金额管理")
@RestController
@RequestMapping("/shop/recharge")
@Validated
public class RechargeController {

    @Resource
    private RechargeService rechargeService;

    @PostMapping("/create")
    @Operation(summary = "创建充值金额管理")
    @PreAuthorize("@ss.hasPermission('shop:recharge:create')")
    public CommonResult<Long> createRecharge(@Valid @RequestBody RechargeCreateReqVO createReqVO) {
        return success(rechargeService.createRecharge(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新充值金额管理")
    @PreAuthorize("@ss.hasPermission('shop:recharge:update')")
    public CommonResult<Boolean> updateRecharge(@Valid @RequestBody RechargeUpdateReqVO updateReqVO) {
        rechargeService.updateRecharge(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除充值金额管理")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('shop:recharge:delete')")
    public CommonResult<Boolean> deleteRecharge(@RequestParam("id") Long id) {
        rechargeService.deleteRecharge(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得充值金额管理")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('shop:recharge:query')")
    public CommonResult<RechargeRespVO> getRecharge(@RequestParam("id") Long id) {
        RechargeDO recharge = rechargeService.getRecharge(id);
        return success(RechargeConvert.INSTANCE.convert(recharge));
    }

    @GetMapping("/list")
    @Operation(summary = "获得充值金额管理列表")
    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024,2048")
    @PreAuthorize("@ss.hasPermission('shop:recharge:query')")
    public CommonResult<List<RechargeRespVO>> getRechargeList(@RequestParam("ids") Collection<Long> ids) {
        List<RechargeDO> list = rechargeService.getRechargeList(ids);
        return success(RechargeConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    @Operation(summary = "获得充值金额管理分页")
    @PreAuthorize("@ss.hasPermission('shop:recharge:query')")
    public CommonResult<PageResult<RechargeRespVO>> getRechargePage(@Valid RechargePageReqVO pageVO) {
        PageResult<RechargeDO> pageResult = rechargeService.getRechargePage(pageVO);
        return success(RechargeConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出充值金额管理 Excel")
    @PreAuthorize("@ss.hasPermission('shop:recharge:export')")
    public void exportRechargeExcel(@Valid RechargeExportReqVO exportReqVO,
              HttpServletResponse response) throws IOException {
        List<RechargeDO> list = rechargeService.getRechargeList(exportReqVO);
        // 导出 Excel
        List<RechargeExcelVO> datas = RechargeConvert.INSTANCE.convertList02(list);
        ExcelUtils.write(response, "充值金额管理.xls", "数据", RechargeExcelVO.class, datas);
    }

}
