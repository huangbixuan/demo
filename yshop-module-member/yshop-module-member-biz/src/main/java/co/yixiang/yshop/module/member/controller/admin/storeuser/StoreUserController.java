package co.yixiang.yshop.module.member.controller.admin.storeuser;

import co.yixiang.yshop.module.member.controller.admin.storeuser.vo.StoreUserPageReqVO;
import co.yixiang.yshop.module.member.controller.admin.storeuser.vo.StoreUserRespVO;
import co.yixiang.yshop.module.member.controller.admin.storeuser.vo.StoreUserSaveReqVO;
import org.springframework.web.bind.annotation.*;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;

import jakarta.validation.*;

import co.yixiang.yshop.framework.common.pojo.PageResult;
import co.yixiang.yshop.framework.common.pojo.CommonResult;
import co.yixiang.yshop.framework.common.util.object.BeanUtils;
import static co.yixiang.yshop.framework.common.pojo.CommonResult.success;

import co.yixiang.yshop.module.member.dal.dataobject.storeuser.StoreUserDO;
import co.yixiang.yshop.module.member.service.storeuser.StoreUserService;

@Tag(name = "管理后台 - 门店移动端商家用户关联")
@RestController
@RequestMapping("/store/store-user")
@Validated
public class StoreUserController {

    @Resource
    private StoreUserService storeUserService;

    @PostMapping("/create")
    @Operation(summary = "创建门店移动端商家用户关联")
    @PreAuthorize("@ss.hasPermission('yshop:store-user:create')")
    public CommonResult<Long> createStoreUser(@Valid @RequestBody StoreUserSaveReqVO createReqVO) {
        return success(storeUserService.createStoreUser(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新门店移动端商家用户关联")
    @PreAuthorize("@ss.hasPermission('yshop:store-user:update')")
    public CommonResult<Boolean> updateStoreUser(@Valid @RequestBody StoreUserSaveReqVO updateReqVO) {
        storeUserService.updateStoreUser(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除门店移动端商家用户关联")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('yshop:store-user:delete')")
    public CommonResult<Boolean> deleteStoreUser(@RequestParam("id") Long id) {
        storeUserService.deleteStoreUser(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得门店移动端商家用户关联")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('yshop:store-user:query')")
    public CommonResult<StoreUserRespVO> getStoreUser(@RequestParam("id") Long id) {
        return success(storeUserService.getStoreUser(id));
    }

    @GetMapping("/page")
    @Operation(summary = "获得门店移动端商家用户关联分页")
    @PreAuthorize("@ss.hasPermission('yshop:store-user:query')")
    public CommonResult<PageResult<StoreUserRespVO>> getStoreUserPage(@Valid StoreUserPageReqVO pageReqVO) {
        PageResult<StoreUserRespVO> pageResult = storeUserService.getStoreUserPage(pageReqVO);
        return success(pageResult);
    }



}