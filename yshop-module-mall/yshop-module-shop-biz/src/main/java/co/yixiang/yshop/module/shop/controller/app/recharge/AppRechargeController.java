package co.yixiang.yshop.module.shop.controller.app.recharge;

import co.yixiang.yshop.framework.common.enums.ShopCommonEnum;
import co.yixiang.yshop.framework.common.pojo.CommonResult;
import co.yixiang.yshop.framework.security.core.annotations.PreAuthenticated;
import co.yixiang.yshop.module.shop.controller.admin.recharge.vo.RechargeExportReqVO;
import co.yixiang.yshop.module.shop.controller.app.recharge.vo.AppRechargeListVO;
import co.yixiang.yshop.module.shop.convert.recharge.RechargeConvert;
import co.yixiang.yshop.module.shop.dal.dataobject.recharge.RechargeDO;
import co.yixiang.yshop.module.shop.service.recharge.RechargeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static co.yixiang.yshop.framework.common.pojo.CommonResult.success;

@Tag(name = "充值金额")
@RestController
@RequestMapping("/recharge")
@Validated
public class AppRechargeController {

    @Resource
    private RechargeService rechargeService;



    @GetMapping("/getMoneyList")
    @Operation(summary = "获得充值金额列表")
    @PreAuthenticated
    public CommonResult<List<AppRechargeListVO>> getRechargeList() {
        RechargeExportReqVO exportReqVO = new RechargeExportReqVO();
        exportReqVO.setStatus(ShopCommonEnum.IS_STATUS_1.getValue());
        List<RechargeDO> list = rechargeService.getRechargeList(exportReqVO);
        return success(RechargeConvert.INSTANCE.convertList03(list));
    }


}
