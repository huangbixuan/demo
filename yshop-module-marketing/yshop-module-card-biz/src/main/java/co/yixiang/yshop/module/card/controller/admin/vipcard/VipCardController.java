package co.yixiang.yshop.module.card.controller.admin.vipcard;

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

import co.yixiang.yshop.module.card.controller.admin.vipcard.vo.*;
import co.yixiang.yshop.module.card.dal.dataobject.vipcard.VipCardDO;
import co.yixiang.yshop.module.card.convert.vipcard.VipCardConvert;
import co.yixiang.yshop.module.card.service.vipcard.VipCardService;

@Tag(name = "管理后台 - 会员卡")
@RestController
@RequestMapping("/card/vip-card")
@Validated
public class VipCardController {

    @Resource
    private VipCardService vipCardService;

    @PostMapping("/create")
    @Operation(summary = "创建会员卡")
    @PreAuthorize("@ss.hasPermission('card:vip-card:create')")
    public CommonResult<Long> createVipCard(@Valid @RequestBody VipCardCreateReqVO createReqVO) {
        return success(vipCardService.createVipCard(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新会员卡")
    @PreAuthorize("@ss.hasPermission('card:vip-card:update')")
    public CommonResult<Boolean> updateVipCard(@Valid @RequestBody VipCardUpdateReqVO updateReqVO) {
        vipCardService.updateVipCard(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除会员卡")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('card:vip-card:delete')")
    public CommonResult<Boolean> deleteVipCard(@RequestParam("id") Long id) {
        vipCardService.deleteVipCard(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得会员卡")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('card:vip-card:query')")
    public CommonResult<VipCardRespVO> getVipCard(@RequestParam("id") Long id) {
        VipCardDO vipCard = vipCardService.getVipCard(id);
        return success(VipCardConvert.INSTANCE.convert(vipCard));
    }

    @GetMapping("/list")
    @Operation(summary = "获得会员卡列表")
    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024,2048")
    @PreAuthorize("@ss.hasPermission('card:vip-card:query')")
    public CommonResult<List<VipCardRespVO>> getVipCardList(@RequestParam("ids") Collection<Long> ids) {
        List<VipCardDO> list = vipCardService.getVipCardList(ids);
        return success(VipCardConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    @Operation(summary = "获得会员卡分页")
    @PreAuthorize("@ss.hasPermission('card:vip-card:query')")
    public CommonResult<PageResult<VipCardRespVO>> getVipCardPage(@Valid VipCardPageReqVO pageVO) {
        PageResult<VipCardDO> pageResult = vipCardService.getVipCardPage(pageVO);
        return success(VipCardConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出会员卡 Excel")
    @PreAuthorize("@ss.hasPermission('card:vip-card:export')")
    public void exportVipCardExcel(@Valid VipCardExportReqVO exportReqVO,
              HttpServletResponse response) throws IOException {
        List<VipCardDO> list = vipCardService.getVipCardList(exportReqVO);
        // 导出 Excel
        List<VipCardExcelVO> datas = VipCardConvert.INSTANCE.convertList02(list);
        ExcelUtils.write(response, "会员卡.xls", "数据", VipCardExcelVO.class, datas);
    }

}
