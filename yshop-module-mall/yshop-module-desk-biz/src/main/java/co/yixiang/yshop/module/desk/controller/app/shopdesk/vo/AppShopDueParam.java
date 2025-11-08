package co.yixiang.yshop.module.desk.controller.app.shopdesk.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;


@Data
public class AppShopDueParam {

    @Schema(description = "桌面ID")
    @NotNull(message = "参数错误")
    private Long deskId;

    @Schema(description = "预约时间")
    @NotNull(message = "预约时间不能为空")
    private LocalDateTime dueTime;

    @Schema(description = "到店时间")
    @NotEmpty(message = "到店时间不能为空")
    private String reachTime;

    @Schema(description = "姓名")
    @NotEmpty(message = "姓名不能为空")
    private String realName;

    @Schema(description = "手机")
    @NotEmpty(message = "手机不能为空")
    private String userPhone;

    @Schema(description = "就餐人数")
    @NotNull(message = "就餐人数不能为空")
    private Integer deskPeople;


}
