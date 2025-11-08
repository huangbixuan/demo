package co.yixiang.yshop.module.store.convert.storerevenue;

import java.util.*;

import co.yixiang.yshop.framework.common.pojo.PageResult;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import co.yixiang.yshop.module.store.controller.admin.storerevenue.vo.*;
import co.yixiang.yshop.module.store.dal.dataobject.storerevenue.StoreRevenueDO;

/**
 * 店铺收支明细 Convert
 *
 * @author yshop
 */
@Mapper
public interface StoreRevenueConvert {

    StoreRevenueConvert INSTANCE = Mappers.getMapper(StoreRevenueConvert.class);

    StoreRevenueDO convert(StoreRevenueCreateReqVO bean);

    StoreRevenueDO convert(StoreRevenueUpdateReqVO bean);

    StoreRevenueRespVO convert(StoreRevenueDO bean);

    List<StoreRevenueRespVO> convertList(List<StoreRevenueDO> list);

    PageResult<StoreRevenueRespVO> convertPage(PageResult<StoreRevenueDO> page);

    List<StoreRevenueExcelVO> convertList02(List<StoreRevenueDO> list);

}
