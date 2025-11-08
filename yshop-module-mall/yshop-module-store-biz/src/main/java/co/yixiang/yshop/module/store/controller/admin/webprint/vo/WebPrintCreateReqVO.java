package co.yixiang.yshop.module.store.controller.admin.webprint.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(description = "管理后台 - 易联云打印机创建 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class WebPrintCreateReqVO extends WebPrintBaseVO {

}
