package co.yixiang.yshop.module.store.convert.webprint;

import java.util.*;

import co.yixiang.yshop.framework.common.pojo.PageResult;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import co.yixiang.yshop.module.store.controller.admin.webprint.vo.*;
import co.yixiang.yshop.module.store.dal.dataobject.webprint.WebPrintDO;

/**
 * 易联云打印机 Convert
 *
 * @author yshop
 */
@Mapper
public interface WebPrintConvert {

    WebPrintConvert INSTANCE = Mappers.getMapper(WebPrintConvert.class);

    WebPrintDO convert(WebPrintCreateReqVO bean);

    WebPrintDO convert(WebPrintUpdateReqVO bean);

    WebPrintRespVO convert(WebPrintDO bean);

    List<WebPrintRespVO> convertList(List<WebPrintDO> list);

    PageResult<WebPrintRespVO> convertPage(PageResult<WebPrintDO> page);

    List<WebPrintExcelVO> convertList02(List<WebPrintDO> list);

}
