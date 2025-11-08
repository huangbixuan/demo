package co.yixiang.yshop.module.store.controller.admin.storewithdrawal;

import co.yixiang.yshop.framework.common.pojo.CommonResult;
import co.yixiang.yshop.framework.common.pojo.PageResult;
import co.yixiang.yshop.module.store.controller.admin.storewithdrawal.vo.StoreWithdrawalCreateReqVO;
import co.yixiang.yshop.module.store.controller.admin.storewithdrawal.vo.StoreWithdrawalPageReqVO;
import co.yixiang.yshop.module.store.controller.admin.storewithdrawal.vo.StoreWithdrawalRespVO;
import co.yixiang.yshop.module.store.controller.admin.storewithdrawal.vo.StoreWithdrawalUpdateReqVO;
import co.yixiang.yshop.module.store.convert.storewithdrawal.StoreWithdrawalConvert;
import co.yixiang.yshop.module.store.dal.dataobject.storewithdrawal.StoreWithdrawalDO;
import co.yixiang.yshop.module.store.service.storewithdrawal.StoreWithdrawalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

import static co.yixiang.yshop.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 提现管理")
@RestController
@RequestMapping("/store/withdrawal")
@Validated
public class StoreWithdrawalController {

    @Resource
    private StoreWithdrawalService withdrawalService;

    @PostMapping("/create")
    @Operation(summary = "创建提现管理")
    @PreAuthorize("@ss.hasPermission('store:withdrawal:create')")
    public CommonResult<Long> createWithdrawal(@Valid @RequestBody StoreWithdrawalCreateReqVO createReqVO) {
        return success(withdrawalService.createWithdrawal(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新提现管理")
    @PreAuthorize("@ss.hasPermission('store:withdrawal:update')")
    public CommonResult<Boolean> updateWithdrawal(@Valid @RequestBody StoreWithdrawalUpdateReqVO updateReqVO) {
        withdrawalService.updateWithdrawal(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除提现管理")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('store:withdrawal:delete')")
    public CommonResult<Boolean> deleteWithdrawal(@RequestParam("id") Long id) {
        withdrawalService.deleteWithdrawal(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得提现管理")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('store:withdrawal:query')")
    public CommonResult<StoreWithdrawalRespVO> getWithdrawal(@RequestParam("id") Long id) {
        return success(withdrawalService.getWithdrawal(id));
    }

    @GetMapping("/list")
    @Operation(summary = "获得提现管理列表")
    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024,2048")
    @PreAuthorize("@ss.hasPermission('store:withdrawal:query')")
    public CommonResult<List<StoreWithdrawalRespVO>> getWithdrawalList(@RequestParam("ids") Collection<Long> ids) {
        List<StoreWithdrawalDO> list = withdrawalService.getWithdrawalList(ids);
        return success(StoreWithdrawalConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    @Operation(summary = "获得提现管理分页")
    @PreAuthorize("@ss.hasPermission('store:withdrawal:query')")
    public CommonResult<PageResult<StoreWithdrawalRespVO>> getWithdrawalPage(@Valid StoreWithdrawalPageReqVO pageVO) {
        return success(withdrawalService.getWithdrawalPage(pageVO));
    }

    @GetMapping("/checkBill")
    @Operation(summary = "检测微信转账账单")
    public CommonResult<Boolean> checkBill(@RequestParam("id") Long id) {
        withdrawalService.findBill(id);
        return success(true);
    }

}
