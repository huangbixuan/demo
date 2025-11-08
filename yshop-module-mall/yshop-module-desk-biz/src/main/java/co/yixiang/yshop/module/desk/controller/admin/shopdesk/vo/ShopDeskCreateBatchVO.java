package co.yixiang.yshop.module.desk.controller.admin.shopdesk.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import jakarta.validation.constraints.NotNull;

@Schema(description = "管理后台 - 门店 - 桌号批量创建 Request VO")
@Data
public class ShopDeskCreateBatchVO {

    @Schema(description = "门店ID", required = true, example = "6456")
    @NotNull(message = "门店ID不能为空")
    private Long shopId;

    @Schema(description = "编号前缀", required = true, example = "Y")
    @NotNull(message = "编号前缀不能为空")
    private String numberPre;

    @Schema(description = "编号开始", required = true, example = "1")
    @NotNull(message = "编号开始不能为空")
    private String startNumber;

    @Schema(description = "编号结束", required = true, example = "99")
    @NotNull(message = "编号结束不能为空")
    private String endNumber;

    @Schema(description = "备注")
    private String note;

    @Schema(description = "状态：1=启用，0=禁用", required = true, example = "1")
    @NotNull(message = "状态不能为空")
    private Integer status;

}
