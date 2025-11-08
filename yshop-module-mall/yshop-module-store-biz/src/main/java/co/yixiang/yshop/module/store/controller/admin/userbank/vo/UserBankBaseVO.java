package co.yixiang.yshop.module.store.controller.admin.userbank.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import jakarta.validation.constraints.*;

/**
* 提现账户 Base VO，提供给添加、修改、详细的子 VO 使用
* 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
*/
@Data
public class UserBankBaseVO {

    @Schema(description = "门店ID", required = true, example = "23731")
    @NotNull(message = "门店ID不能为空")
    private Long shopId;

    @Schema(description = "门店名称", required = true, example = "王五")
    private String shopName;

    @Schema(description = "用户ID", required = true, example = "4427")
    private Long uid;

    @Schema(description = "提现类型 1=银行卡 2=微信 3=支付宝", example = "2")
    private Integer type;

    @Schema(description = "银行名称", example = "李四")
    //@NotBlank(message = "银行名称不能为空")
    private String bankName;

    @Schema(description = "姓名", example = "张三")
    @NotBlank(message = "姓名不能为空")
    private String name;

    @Schema(description = "银行卡号")
    //@NotBlank(message = "银行卡号不能为空")
    private String bankCode;

    @Schema(description = "手机号")
    //@NotBlank(message = "银行卡号不能为空")
    private String bankMobile;

    @Schema(description = "支付宝账号")
    private String zfbCode;

    @Schema(description = "微信账号")
    private String wxCode;

    @Schema(description = "支付宝收款码")
    private String zfbImg;

    @Schema(description = "微信收款码")
    private String wxImg;

    @Schema(description = "状态:0=审核中,1=审核通过,2=审核拒绝", example = "2")
    private Integer status;

    @Schema(description = "审核拒绝原因")
    private String refuse;

    @Schema(description = "图标")
    private String bankIcon;

    @Schema(description = "银行卡类型", example = "2")
    private String bankType;

}
