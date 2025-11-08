package co.yixiang.yshop.module.desk.controller.admin.shopdesk;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.core.util.ZipUtil;
import co.yixiang.yshop.framework.common.constant.ShopConstants;
import co.yixiang.yshop.framework.common.enums.CommonStatusEnum;
import co.yixiang.yshop.framework.common.util.servlet.ServletUtils;
import co.yixiang.yshop.module.desk.dal.dataobject.qrcode.QrcodeDO;
import co.yixiang.yshop.module.desk.service.qrcode.QrcodeService;
import co.yixiang.yshop.module.system.enums.common.SexEnum;
import io.swagger.v3.oas.annotations.Parameters;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.HttpRequestHandler;
import org.springframework.web.bind.annotation.*;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;

import jakarta.validation.constraints.*;
import jakarta.validation.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.*;
import java.io.IOException;

import co.yixiang.yshop.framework.common.pojo.PageResult;
import co.yixiang.yshop.framework.common.pojo.CommonResult;
import static co.yixiang.yshop.framework.common.pojo.CommonResult.success;

import co.yixiang.yshop.framework.excel.core.util.ExcelUtils;

import co.yixiang.yshop.module.desk.controller.admin.shopdesk.vo.*;
import co.yixiang.yshop.module.desk.dal.dataobject.shopdesk.ShopDeskDO;
import co.yixiang.yshop.module.desk.convert.shopdesk.ShopDeskConvert;
import co.yixiang.yshop.module.desk.service.shopdesk.ShopDeskService;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "管理后台 - 门店 - 桌号")
@RestController
@RequestMapping("/desk/shop-desk")
@Validated
public class ShopDeskController {

    @Resource
    private ShopDeskService shopDeskService;
    @Resource
    private QrcodeService qrcodeService;

    @PostMapping("/create")
    @Operation(summary = "创建门店 - 桌号")
    @PreAuthorize("@ss.hasPermission('desk:shop-desk:create')")
    public CommonResult<Long> createShopDesk(@Valid @RequestBody ShopDeskCreateReqVO createReqVO) {
        return success(shopDeskService.createShopDesk(createReqVO));
    }

    @PostMapping("/batchAdd")
    @Operation(summary = "创建门店 - 批量添加桌号")
    @PreAuthorize("@ss.hasPermission('desk:shop-desk:create')")
    public CommonResult<Boolean> batchAddShopDesk(@Valid @RequestBody ShopDeskCreateBatchVO createReqVO) {
        shopDeskService.batchAdd(createReqVO);
        return success(true);
    }

    @PutMapping("/update")
    @Operation(summary = "更新门店 - 桌号")
    @PreAuthorize("@ss.hasPermission('desk:shop-desk:update')")
    public CommonResult<Boolean> updateShopDesk(@Valid @RequestBody ShopDeskUpdateReqVO updateReqVO) {
        shopDeskService.updateShopDesk(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除门店 - 桌号")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('desk:shop-desk:delete')")
    public CommonResult<Boolean> deleteShopDesk(@RequestParam("id") Long id) {
        shopDeskService.deleteShopDesk(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得门店 - 桌号")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('desk:shop-desk:query')")
    public CommonResult<ShopDeskRespVO> getShopDesk(@RequestParam("id") Long id) {
        ShopDeskDO shopDesk = shopDeskService.getShopDesk(id);
        return success(ShopDeskConvert.INSTANCE.convert(shopDesk));
    }

    @GetMapping("/list")
    @Operation(summary = "获得门店 - 桌号列表")
    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024,2048")
    @PreAuthorize("@ss.hasPermission('desk:shop-desk:query')")
    public CommonResult<List<ShopDeskRespVO>> getShopDeskList(@RequestParam("ids") Collection<Long> ids) {
        List<ShopDeskDO> list = shopDeskService.getShopDeskList(ids);
        return success(ShopDeskConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    @Operation(summary = "获得门店 - 桌号分页")
    @PreAuthorize("@ss.hasPermission('desk:shop-desk:query')")
    public CommonResult<PageResult<ShopDeskRespVO>> getShopDeskPage(@Valid ShopDeskPageReqVO pageVO) {
        PageResult<ShopDeskDO> pageResult = shopDeskService.getShopDeskPage(pageVO);
        return success(ShopDeskConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出门店 - 桌号 Excel")
    @PreAuthorize("@ss.hasPermission('desk:shop-desk:export')")
    public void exportShopDeskExcel(@Valid ShopDeskExportReqVO exportReqVO,
              HttpServletResponse response) throws IOException {
        List<ShopDeskDO> list = shopDeskService.getShopDeskList(exportReqVO);
        // 导出 Excel
        List<ShopDeskExcelVO> datas = ShopDeskConvert.INSTANCE.convertList02(list);
        ExcelUtils.write(response, "门店 - 桌号.xls", "数据", ShopDeskExcelVO.class, datas);
    }

    @GetMapping("/export-excel-template")
    @Operation(summary = "导出门店 - 桌号模板 Excel")
    @PreAuthorize("@ss.hasPermission('desk:shop-desk:export')")
    public void exportShopDeskTemplateExcel(HttpServletResponse response) throws IOException {
        // 手动创建导出 demo
        List<ShopDeskTemplateExcelVO> list = Arrays.asList(
                ShopDeskTemplateExcelVO.builder().shopName("yshop店铺，这里是店铺必填")
                        .number("A001，这里是桌号必填").note("这里是备注，可以选填").build()
        );
        ExcelUtils.write(response, "批量导入桌号模板.xls", "数据", ShopDeskTemplateExcelVO.class, list);
    }

    @PostMapping("/import")
    @Operation(summary = "导入桌号")
    @Parameters({
            @Parameter(name = "file", description = "Excel 文件", required = true),
            @Parameter(name = "updateSupport", description = "是否支持更新，默认为 false", example = "true")
    })
    @PreAuthorize("@ss.hasPermission('desk:shop-desk:export')")
    public CommonResult<DeskImportRespVO> importExcel(@RequestParam("file") MultipartFile file,
                                                      @RequestParam(value = "updateSupport", required = false, defaultValue = "false") Boolean updateSupport) throws Exception {
        List<ShopDeskTemplateExcelVO> list = ExcelUtils.read(file, ShopDeskTemplateExcelVO.class);
        return success(shopDeskService.importShopDeskList(list, updateSupport));
    }

    @GetMapping("/single-qrcode")
    @Operation(summary = "生成单个二维码")
    public CommonResult<DeskQrcodeRespVO> getSingleQrcode(HttpServletRequest httpServletRequest, QrcodeVO qrcodeVO) {
        String scene = "id=" + qrcodeVO.getId() + "&number=" + qrcodeVO.getNumber() + "&shopId=" +  qrcodeVO.getShopId();
        QrcodeDO qrcodeDO = qrcodeService.createMiniQrcode(ShopConstants.PAGE_GOOD_HOME,scene,false,qrcodeVO.getEnv());
        QrcodeDO qrcodeDO1 = qrcodeService.createQrcode(ShopConstants.PAGE_GOOD_HOME,scene,false);
        String miniQrcode = "";
        if(qrcodeDO != null){
            miniQrcode = ServletUtils.getRequstHost(httpServletRequest) + "/admin-api/"+qrcodeDO.getSrc();
        }
        String normalQrcode = ServletUtils.getRequstHost(httpServletRequest) + "/admin-api/"+qrcodeDO1.getSrc();

        DeskQrcodeRespVO deskQrcodeRespVO = DeskQrcodeRespVO.builder()
                .miniQrcode(miniQrcode)
                .normalQrcode(normalQrcode).build();
        return success(deskQrcodeRespVO);

    }

    @Operation(summary = "下载二维码")
    @GetMapping("/download")
    @Parameter(name = "ID", description = "桌号ID", required = true, example = "1024")
    public void downloadQrcode(@RequestParam("id") Long id,
                                HttpServletResponse response) throws IOException {

        Map<String, String> qrcodes = shopDeskService.mapQrcode(id);
        // 构建 zip 包
        String[] paths = qrcodes.keySet().toArray(new String[0]);
        ByteArrayInputStream[] ins = qrcodes.values().stream().map(FileUtil::readBytes).map(IoUtil::toStream).toArray(ByteArrayInputStream[]::new);

        // 输出
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipUtil.zip(outputStream, paths, ins);
        ServletUtils.writeAttachment(response, "qrcode.zip", outputStream.toByteArray());
    }


}
