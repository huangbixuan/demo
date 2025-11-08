package co.yixiang.yshop.module.member.controller.admin.storeuser.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import co.yixiang.yshop.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static co.yixiang.yshop.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 门店移动端商家用户关联分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class StoreUserPageReqVO extends PageParam {

    @Schema(description = "门店ID", example = "7809")
    private Long shopId;

    @Schema(description = "用户昵称", example = "用户昵称")
    private String nickname;

    @Schema(description = "用户id", example = "19316")
    private Long uid;

    @Schema(description = "0-禁止 1-开启", example = "2")
    private Integer status;

    @Schema(description = "添加时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}