package co.yixiang.yshop.module.desk.controller.app.shopdesk.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class DayTime {

    @Schema(description = "标签名")
    private String dayStr;

    @Schema(description = "标签时间")
    private LocalDateTime dayDate;

    @Schema(description = "标签规则")
    private List<TimeRule> timeRule;
}
