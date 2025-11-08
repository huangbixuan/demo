package co.yixiang.yshop.module.store.controller.admin.userbank.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import co.yixiang.yshop.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static co.yixiang.yshop.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 提现账户分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class UserBankPageReqVO extends PageParam {

    @Schema(description = "用户ID", example = "31068")
    private Long userId;

    @Schema(description = "门店ID", example = "王五")
    private String shopName;

    @Schema(description = "店铺名称", example = "4427")
    private String uid;

    @Schema(description = "提现类型 1=银行卡 2=微信 3=支付宝", example = "2")
    private Integer type;

    @Schema(description = "银行名称", example = "李四")
    private String bankName;

    @Schema(description = "姓名", example = "张三")
    private String name;

    @Schema(description = "银行卡号")
    private String bankCode;

    @Schema(description = "手机号")
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

    @Schema(description = "添加时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}
