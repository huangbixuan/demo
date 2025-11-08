package co.yixiang.yshop.module.store.convert.userbank;

import java.util.*;

import co.yixiang.yshop.framework.common.pojo.PageResult;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import co.yixiang.yshop.module.store.controller.admin.userbank.vo.*;
import co.yixiang.yshop.module.store.dal.dataobject.userbank.UserBankDO;

/**
 * 提现账户 Convert
 *
 * @author yshop
 */
@Mapper
public interface UserBankConvert {

    UserBankConvert INSTANCE = Mappers.getMapper(UserBankConvert.class);

    UserBankDO convert(UserBankCreateReqVO bean);

    UserBankDO convert(UserBankUpdateReqVO bean);

    UserBankRespVO convert(UserBankDO bean);

    List<UserBankRespVO> convertList(List<UserBankDO> list);

    PageResult<UserBankRespVO> convertPage(PageResult<UserBankDO> page);

    List<UserBankExcelVO> convertList02(List<UserBankDO> list);

}
