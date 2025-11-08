package co.yixiang.yshop.module.pay.controller.app.merchant;

import co.yixiang.yshop.framework.common.enums.PayIdEnum;
import co.yixiang.yshop.framework.common.pojo.CommonResult;
import co.yixiang.yshop.framework.common.util.object.BeanUtils;
import co.yixiang.yshop.framework.tenant.core.context.TenantContextHolder;
import co.yixiang.yshop.module.pay.controller.admin.merchantdetails.vo.MerchantDetailsRespVO;
import co.yixiang.yshop.module.pay.dal.dataobject.merchantdetails.MerchantDetailsDO;
import co.yixiang.yshop.module.pay.service.merchantdetails.MerchantDetailsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static co.yixiang.yshop.framework.common.pojo.CommonResult.success;

@Tag(name = "用户APP - 支付服务商配置")
@RestController
@RequestMapping("/pay/merchant")
@Validated
public class AppMerchantController {

    @Resource
    private MerchantDetailsService merchantDetailsService;


    @GetMapping("/getInfo")
    @Operation(summary = "获得支付服务商配置")
    public CommonResult<MerchantDetailsRespVO> getMerchantDetails() {
        Long tenantId = TenantContextHolder.getTenantId();
        MerchantDetailsDO merchantDetails = merchantDetailsService.getMerchantDetails(
                PayIdEnum.WX_MINIAPP.getValue() + tenantId);
        return success(BeanUtils.toBean(merchantDetails, MerchantDetailsRespVO.class));
    }




}
