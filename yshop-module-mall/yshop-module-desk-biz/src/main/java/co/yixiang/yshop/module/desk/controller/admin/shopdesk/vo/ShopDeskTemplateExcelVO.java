package co.yixiang.yshop.module.desk.controller.admin.shopdesk.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 门店 - 桌号模板 Excel VO
 *
 * @author yshop
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = false) // 设置 chain = false，避免用户导入有问题
public class ShopDeskTemplateExcelVO {

    @ExcelProperty("门店名称")
    private String shopName;

    @ExcelProperty("桌号")
    private String number;


    @ExcelProperty("备注")
    private String note;



}
