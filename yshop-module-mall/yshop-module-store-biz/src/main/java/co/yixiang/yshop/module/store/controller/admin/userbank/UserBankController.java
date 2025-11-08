package co.yixiang.yshop.module.store.controller.admin.userbank;

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

import co.yixiang.yshop.module.store.controller.admin.userbank.vo.*;
import co.yixiang.yshop.module.store.dal.dataobject.userbank.UserBankDO;
import co.yixiang.yshop.module.store.convert.userbank.UserBankConvert;
import co.yixiang.yshop.module.store.service.userbank.UserBankService;

@Tag(name = "管理后台 - 提现账户")
@RestController
@RequestMapping("/store/user-bank")
@Validated
public class UserBankController {

    @Resource
    private UserBankService userBankService;

    @PostMapping("/create")
    @Operation(summary = "创建提现账户")
    @PreAuthorize("@ss.hasPermission('store:user-bank:create')")
    public CommonResult<Long> createUserBank(@Valid @RequestBody UserBankCreateReqVO createReqVO) {
        return success(userBankService.createUserBank(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新提现账户")
    @PreAuthorize("@ss.hasPermission('store:user-bank:update')")
    public CommonResult<Boolean> updateUserBank(@Valid @RequestBody UserBankUpdateReqVO updateReqVO) {
        userBankService.updateUserBank(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除提现账户")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('store:user-bank:delete')")
    public CommonResult<Boolean> deleteUserBank(@RequestParam("id") Long id) {
        userBankService.deleteUserBank(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得提现账户")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('store:user-bank:query')")
    public CommonResult<UserBankRespVO> getUserBank(@RequestParam("id") Long id) {
        UserBankDO userBank = userBankService.getUserBank(id);
        return success(UserBankConvert.INSTANCE.convert(userBank));
    }

    @GetMapping("/list")
    @Operation(summary = "获得提现账户列表")
    @Parameter(name = "shopId", description = "店铺ID", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('store:user-bank:query')")
    public CommonResult<List<UserBankRespVO>> getUserBankList(@RequestParam("shopId") Long shopId) {
        List<UserBankDO> list = userBankService.getUserBankList(shopId);
        return success(UserBankConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    @Operation(summary = "获得提现账户分页")
    @PreAuthorize("@ss.hasPermission('store:user-bank:query')")
    public CommonResult<PageResult<UserBankRespVO>> getUserBankPage(@Valid UserBankPageReqVO pageVO) {
        PageResult<UserBankDO> pageResult = userBankService.getUserBankPage(pageVO);
        return success(UserBankConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出提现账户 Excel")
    @PreAuthorize("@ss.hasPermission('store:user-bank:export')")
    public void exportUserBankExcel(@Valid UserBankExportReqVO exportReqVO,
              HttpServletResponse response) throws IOException {
        List<UserBankDO> list = userBankService.getUserBankList(exportReqVO);
        // 导出 Excel
        List<UserBankExcelVO> datas = UserBankConvert.INSTANCE.convertList02(list);
        ExcelUtils.write(response, "提现账户.xls", "数据", UserBankExcelVO.class, datas);
    }

}
