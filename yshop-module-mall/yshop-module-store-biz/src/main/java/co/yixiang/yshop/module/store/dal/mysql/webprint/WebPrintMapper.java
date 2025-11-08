package co.yixiang.yshop.module.store.dal.mysql.webprint;

import java.util.*;

import co.yixiang.yshop.framework.common.pojo.PageResult;
import co.yixiang.yshop.framework.mybatis.core.query.LambdaQueryWrapperX;
import co.yixiang.yshop.framework.mybatis.core.mapper.BaseMapperX;
import co.yixiang.yshop.module.store.dal.dataobject.webprint.WebPrintDO;
import org.apache.ibatis.annotations.Mapper;
import co.yixiang.yshop.module.store.controller.admin.webprint.vo.*;

/**
 * 易联云打印机 Mapper
 *
 * @author yshop
 */
@Mapper
public interface WebPrintMapper extends BaseMapperX<WebPrintDO> {

    default PageResult<WebPrintDO> selectPage(WebPrintPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<WebPrintDO>()
                .eqIfPresent(WebPrintDO::getTitle, reqVO.getTitle())
                .eqIfPresent(WebPrintDO::getBrand, reqVO.getBrand())
                .orderByDesc(WebPrintDO::getId));
    }

    default List<WebPrintDO> selectList(WebPrintExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<WebPrintDO>()
                .eqIfPresent(WebPrintDO::getTitle, reqVO.getTitle())
                .eqIfPresent(WebPrintDO::getMechineCode, reqVO.getMechineCode())
                .eqIfPresent(WebPrintDO::getMsign, reqVO.getMsign())
                .betweenIfPresent(WebPrintDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(WebPrintDO::getId));
    }

}
