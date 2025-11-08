package co.yixiang.yshop.module.store.controller.admin.webprint;

import co.yixiang.yshop.framework.redis.util.redis.RedisUtil;
import co.yixiang.yshop.framework.tenant.core.context.TenantContextHolder;
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

import co.yixiang.yshop.module.store.controller.admin.webprint.vo.*;
import co.yixiang.yshop.module.store.dal.dataobject.webprint.WebPrintDO;
import co.yixiang.yshop.module.store.convert.webprint.WebPrintConvert;
import co.yixiang.yshop.module.store.service.webprint.WebPrintService;

@Tag(name = "管理后台 - 易联云打印机")
@RestController
@RequestMapping("/store/web-print")
@Validated
public class WebPrintController {

    @Resource
    private WebPrintService webPrintService;

    @PostMapping("/create")
    @Operation(summary = "创建易联云打印机")
    @PreAuthorize("@ss.hasPermission('store:web-print:create')")
    public CommonResult<Long> createWebPrint(@Valid @RequestBody WebPrintCreateReqVO createReqVO) {
        return success(webPrintService.createWebPrint(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新易联云打印机")
    @PreAuthorize("@ss.hasPermission('store:web-print:update')")
    public CommonResult<Boolean> updateWebPrint(@Valid @RequestBody WebPrintUpdateReqVO updateReqVO) {
        webPrintService.updateWebPrint(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除易联云打印机")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('store:web-print:delete')")
    public CommonResult<Boolean> deleteWebPrint(@RequestParam("id") Long id) {
        webPrintService.deleteWebPrint(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得易联云打印机")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('store:web-print:query')")
    public CommonResult<WebPrintRespVO> getWebPrint(@RequestParam("id") Long id) {
        WebPrintDO webPrint = webPrintService.getWebPrint(id);
        return success(WebPrintConvert.INSTANCE.convert(webPrint));
    }

    @GetMapping("/list")
    @Operation(summary = "获得易联云打印机列表")
    //@Parameter(name = "ids", description = "编号列表", required = true, example = "1024,2048")
   // @PreAuthorize("@ss.hasPermission('store:web-print:query')")
    public CommonResult<List<WebPrintRespVO>> getWebPrintList() {
        List<WebPrintDO> list = webPrintService.getWebPrintList();
        return success(WebPrintConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    @Operation(summary = "获得易联云打印机分页")
    @PreAuthorize("@ss.hasPermission('store:web-print:query')")
    public CommonResult<PageResult<WebPrintRespVO>> getWebPrintPage(@Valid WebPrintPageReqVO pageVO) {
        PageResult<WebPrintDO> pageResult = webPrintService.getWebPrintPage(pageVO);
        return success(WebPrintConvert.INSTANCE.convertPage(pageResult));
    }



    @GetMapping("/getRedisSet")
    @Operation(summary = "获得易联云打印机配置")
    @Parameter(name = "brand", description = "打印机品牌", required = true, example = "yly")
    public CommonResult<WebPrintSetVO> getRedisSet(@RequestParam("brand") String brand) {
        return success(webPrintService.getPrintConfig(brand));
    }

    @PostMapping("/saveRedisSet")
    @Operation(summary = "易联云打印机配置")
    public CommonResult<Boolean> saveRedisSet(@Valid @RequestBody WebPrintSetVO webPrintSetVO) {
        webPrintService.savePrintConfig(webPrintSetVO);
        return success(true);
    }

}
