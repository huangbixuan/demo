package co.yixiang.yshop.module.shop.convert.recharge;

import java.util.*;

import co.yixiang.yshop.framework.common.pojo.PageResult;

import co.yixiang.yshop.module.shop.controller.app.recharge.vo.AppRechargeListVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import co.yixiang.yshop.module.shop.controller.admin.recharge.vo.*;
import co.yixiang.yshop.module.shop.dal.dataobject.recharge.RechargeDO;

/**
 * 充值金额管理 Convert
 *
 * @author yshop
 */
@Mapper
public interface RechargeConvert {

    RechargeConvert INSTANCE = Mappers.getMapper(RechargeConvert.class);

    RechargeDO convert(RechargeCreateReqVO bean);

    RechargeDO convert(RechargeUpdateReqVO bean);

    RechargeRespVO convert(RechargeDO bean);

    List<RechargeRespVO> convertList(List<RechargeDO> list);

    PageResult<RechargeRespVO> convertPage(PageResult<RechargeDO> page);

    List<RechargeExcelVO> convertList02(List<RechargeDO> list);

    List<AppRechargeListVO> convertList03(List<RechargeDO> list);

}
