package co.yixiang.yshop.module.store.controller.admin.storewithdrawal.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import jakarta.validation.constraints.*;

/**
* 提现管理 Base VO，提供给添加、修改、详细的子 VO 使用
* 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
*/
@Data
public class StoreWithdrawalBaseVO {

    @Schema(description = "用户ID", example = "9568")
    private Long uid;

    @Schema(description = "门店ID", required = true, example = "23731")
    @NotNull(message = "门店ID不能为空")
    private Long shopId;

    @Schema(description = "门店名称", required = true, example = "张三")
    private String shopName;

    @Schema(description = "提现金额")
    @NotNull(message = "提现金额不能为空")
    private BigDecimal amount;

    @Schema(description = "提现方式", example = "2")
    private Integer type;

    @Schema(description = "银行卡ID", example = "2")
    private Long bankId;

    @Schema(description = "状态:0=未审核,1=待到账,2=审核拒绝,3=已到账", example = "2")
    private Integer status;

    @Schema(description = "审核拒绝原因")
    private String refuse;

    @Schema(description = "年月")
    private String month;

    @Schema(description = "剩余可提现金额")
    private BigDecimal residueAmount;

    //微信转账返回的信息
    private String packageInfo;

    //微信转账返回的状态
    private String state;


}
