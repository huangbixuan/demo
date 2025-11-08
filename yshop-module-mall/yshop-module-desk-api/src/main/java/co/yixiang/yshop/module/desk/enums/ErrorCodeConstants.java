package co.yixiang.yshop.module.desk.enums;
import co.yixiang.yshop.framework.common.exception.ErrorCode;

public interface ErrorCodeConstants {
    ErrorCode SHOP_DESK_NOT_EXISTS = new ErrorCode(1008021001, "门店 - 桌号不存在");
    ErrorCode DESK_IMPORT_LIST_IS_EMPTY = new ErrorCode(1008021002, "导入桌号数据不能为空！");
    ErrorCode NOT_VALID_NUMBER = new ErrorCode(1008021003, "不是有效数字！");
    ErrorCode DESK_VALID_NUMBER = new ErrorCode(1008021003, "桌号范围无效！");
    ErrorCode CREATE_QRCODE_FAIL = new ErrorCode(1008021004, "创建二维码失败！");
    ErrorCode SHOP_DESK_CATEGORY_NOT_EXISTS = new ErrorCode(1008021005, "门店桌号分类不存在");

}

