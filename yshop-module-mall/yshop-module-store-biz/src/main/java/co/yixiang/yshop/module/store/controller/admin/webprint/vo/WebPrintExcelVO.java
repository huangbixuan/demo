package co.yixiang.yshop.module.store.controller.admin.webprint.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;

import com.alibaba.excel.annotation.ExcelProperty;

/**
 * 易联云打印机 Excel VO
 *
 * @author yshop
 */
@Data
public class WebPrintExcelVO {

    @ExcelProperty("id")
    private Long id;

    @ExcelProperty("打印机名称")
    private String title;

    @ExcelProperty("终端号")
    private String mechineCode;

    @ExcelProperty("终端密钥")
    private String msign;

    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}
