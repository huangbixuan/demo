package co.yixiang.yshop.module.shop.controller.admin.recharge.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDateTime;

import com.alibaba.excel.annotation.ExcelProperty;

/**
 * 充值金额管理 Excel VO
 *
 * @author yshop
 */
@Data
public class RechargeExcelVO {

    @ExcelProperty("id")
    private Long id;

    @ExcelProperty("标题")
    private String name;

    @ExcelProperty("销量")
    private Integer sales;

    @ExcelProperty("价值")
    private BigDecimal value;

    @ExcelProperty("权重")
    private Integer weigh;

    @ExcelProperty("状态:1=显示,0=隐藏")
    private Integer status;

    @ExcelProperty("销售价")
    private BigDecimal sellPrice;

    @ExcelProperty("添加时间")
    private LocalDateTime createTime;

}
