package co.yixiang.yshop.module.member.controller.admin.storeuser.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 门店移动端商家用户关联 Response VO")
@Data
@ExcelIgnoreUnannotated
public class StoreUserRespVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "10579")
    @ExcelProperty("ID")
    private Long id;

    @Schema(description = "门店ID", example = "7809")
    @ExcelProperty("门店ID")
    private Long shopId;

    @Schema(description = "门店名称", example = "门店名称")
    @ExcelProperty("门店名称")
    private String shopName;

    @Schema(description = "用户id", example = "19316")
    @ExcelProperty("用户id")
    private Long uid;

    @Schema(description = "用户昵称", example = "用户昵称")
    @ExcelProperty("用户昵称")
    private String nickname;

    @Schema(description = "0-禁止 1-开启", example = "2")
    @ExcelProperty("0-禁止 1-开启")
    private Integer status;

    @Schema(description = "添加时间")
    @ExcelProperty("添加时间")
    private LocalDateTime createTime;

}