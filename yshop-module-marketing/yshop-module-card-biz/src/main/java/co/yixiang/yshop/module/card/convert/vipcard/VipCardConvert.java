package co.yixiang.yshop.module.card.convert.vipcard;

import java.util.*;

import co.yixiang.yshop.framework.common.pojo.PageResult;

import co.yixiang.yshop.module.card.controller.app.vipcard.vo.AppVipCardVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import co.yixiang.yshop.module.card.controller.admin.vipcard.vo.*;
import co.yixiang.yshop.module.card.dal.dataobject.vipcard.VipCardDO;

/**
 * 会员卡 Convert
 *
 * @author yshop
 */
@Mapper
public interface VipCardConvert {

    VipCardConvert INSTANCE = Mappers.getMapper(VipCardConvert.class);

    VipCardDO convert(VipCardCreateReqVO bean);

    VipCardDO convert(VipCardUpdateReqVO bean);

    VipCardRespVO convert(VipCardDO bean);

    AppVipCardVO convert01(VipCardDO bean);

    List<VipCardRespVO> convertList(List<VipCardDO> list);

    List<AppVipCardVO> convertList01(List<VipCardDO> list);

    PageResult<VipCardRespVO> convertPage(PageResult<VipCardDO> page);

    List<VipCardExcelVO> convertList02(List<VipCardDO> list);

}
