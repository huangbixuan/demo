package co.yixiang.yshop.module.store.convert.storewithdrawal;

import java.util.*;

import co.yixiang.yshop.framework.common.pojo.PageResult;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import co.yixiang.yshop.module.store.controller.admin.storewithdrawal.vo.*;
import co.yixiang.yshop.module.store.dal.dataobject.storewithdrawal.StoreWithdrawalDO;

/**
 * 提现管理 Convert
 *
 * @author yshop
 */
@Mapper
public interface StoreWithdrawalConvert {

    StoreWithdrawalConvert INSTANCE = Mappers.getMapper(StoreWithdrawalConvert.class);

    StoreWithdrawalDO convert(StoreWithdrawalCreateReqVO bean);

    StoreWithdrawalDO convert(StoreWithdrawalUpdateReqVO bean);

    StoreWithdrawalRespVO convert(StoreWithdrawalDO bean);

    List<StoreWithdrawalRespVO> convertList(List<StoreWithdrawalDO> list);

    PageResult<StoreWithdrawalRespVO> convertPage(PageResult<StoreWithdrawalDO> page);

    List<StoreWithdrawalExcelVO> convertList02(List<StoreWithdrawalDO> list);

}
