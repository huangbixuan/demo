package co.yixiang.yshop.module.store.controller.admin.webprint.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import jakarta.validation.constraints.*;

/**
* 易联云打印机 Base VO，提供给添加、修改、详细的子 VO 使用
* 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
*/
@Data
public class WebPrintBaseVO {

    @Schema(description = "打印机名称")
    private String title;

    @Schema(description = "终端号")
    private String mechineCode;

    @Schema(description = "终端密钥")
    private String msign;

    @Schema(description = "品牌")
    private String brand;

}
