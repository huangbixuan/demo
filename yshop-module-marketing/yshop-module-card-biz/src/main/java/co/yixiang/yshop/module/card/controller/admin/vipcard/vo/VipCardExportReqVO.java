package co.yixiang.yshop.module.card.controller.admin.vipcard.vo;

import lombok.*;

import java.math.BigDecimal;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import co.yixiang.yshop.framework.common.pojo.PageParam;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

import static co.yixiang.yshop.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 会员卡 Excel 导出 Request VO，参数和 VipCardPageReqVO 是一致的")
@Data
public class VipCardExportReqVO {

    @Schema(description = "会员卡名称", example = "李四")
    private String name;

    @Schema(description = "会员卡样式")
    private String styleImg;

    @Schema(description = "排序")
    private Integer sort;

    @Schema(description = "是否有折扣0-无1-有", example = "31583")
    private Boolean isDiscount;

    @Schema(description = "折扣比例", example = "16926")
    private Integer discount;

    @Schema(description = "赠送方式 no -无 integral-赠送积分 coupon-优惠券 mony-余额")
    private String giveMethod;

    @Schema(description = "赠送积分数量")
    private Integer integral;

    @Schema(description = "赠送的优惠券")
    private String coupon;

    @Schema(description = "赠送的余额")
    private BigDecimal mony;

    @Schema(description = "有效期 单位月0-表示永久")
    private Integer period;

    @Schema(description = "购买的价格", example = "25392")
    private BigDecimal price;

    @Schema(description = "0-正常 1-关闭", example = "1")
    private Boolean status;

    @Schema(description = "使用的规则")
    private String rule;

    @Schema(description = "添加时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}
