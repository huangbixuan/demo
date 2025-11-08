package co.yixiang.yshop.module.desk.controller.admin.shopdesk.vo;

import co.yixiang.yshop.framework.excel.core.annotations.DictFormat;
import co.yixiang.yshop.framework.excel.core.convert.DictConvert;
import co.yixiang.yshop.module.system.enums.DictTypeConstants;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 桌号 Excel 导入 VO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = false) // 设置 chain = false，避免用户导入有问题
public class DeskImportExcelVO {

    @ExcelProperty("门店名称")
    private String shopName;

    @ExcelProperty("桌号")
    private String number;

    @ExcelProperty("备注")
    private String note;



}
