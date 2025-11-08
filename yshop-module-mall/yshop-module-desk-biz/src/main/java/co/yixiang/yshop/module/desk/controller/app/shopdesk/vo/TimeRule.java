package co.yixiang.yshop.module.desk.controller.app.shopdesk.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TimeRule {

    @Schema(description = "标签名")
    private String tag;

    @Schema(description = "规则时间字符串")
    private String time;

    @Schema(description = "规则时间")
    private LocalDateTime timeDate;

    @Schema(description = "状态 0-正常 1-已经被预约")
    private Integer status;
}
