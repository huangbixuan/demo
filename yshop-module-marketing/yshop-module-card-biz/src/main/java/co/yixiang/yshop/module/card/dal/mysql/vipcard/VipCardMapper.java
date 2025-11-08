package co.yixiang.yshop.module.card.dal.mysql.vipcard;

import java.util.*;

import co.yixiang.yshop.framework.common.pojo.PageResult;
import co.yixiang.yshop.framework.mybatis.core.query.LambdaQueryWrapperX;
import co.yixiang.yshop.framework.mybatis.core.mapper.BaseMapperX;
import co.yixiang.yshop.module.card.dal.dataobject.vipcard.VipCardDO;
import org.apache.ibatis.annotations.Mapper;
import co.yixiang.yshop.module.card.controller.admin.vipcard.vo.*;

/**
 * 会员卡 Mapper
 *
 * @author yshop
 */
@Mapper
public interface VipCardMapper extends BaseMapperX<VipCardDO> {

    default PageResult<VipCardDO> selectPage(VipCardPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<VipCardDO>()
                .likeIfPresent(VipCardDO::getName, reqVO.getName())
                .eqIfPresent(VipCardDO::getStyleImg, reqVO.getStyleImg())
                .eqIfPresent(VipCardDO::getSort, reqVO.getSort())
                .eqIfPresent(VipCardDO::getIsDiscount, reqVO.getIsDiscount())
                .eqIfPresent(VipCardDO::getDiscount, reqVO.getDiscount())
                .eqIfPresent(VipCardDO::getIntegral, reqVO.getIntegral())
                .eqIfPresent(VipCardDO::getCoupon, reqVO.getCoupon())
                .eqIfPresent(VipCardDO::getMony, reqVO.getMony())
                .eqIfPresent(VipCardDO::getPeriod, reqVO.getPeriod())
                .eqIfPresent(VipCardDO::getPrice, reqVO.getPrice())
                .eqIfPresent(VipCardDO::getStatus, reqVO.getStatus())
                .eqIfPresent(VipCardDO::getRule, reqVO.getRule())
                .betweenIfPresent(VipCardDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(VipCardDO::getId));
    }

    default List<VipCardDO> selectList(VipCardExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<VipCardDO>()
                .likeIfPresent(VipCardDO::getName, reqVO.getName())
                .eqIfPresent(VipCardDO::getStyleImg, reqVO.getStyleImg())
                .eqIfPresent(VipCardDO::getSort, reqVO.getSort())
                .eqIfPresent(VipCardDO::getIsDiscount, reqVO.getIsDiscount())
                .eqIfPresent(VipCardDO::getDiscount, reqVO.getDiscount())
                .eqIfPresent(VipCardDO::getIntegral, reqVO.getIntegral())
                .eqIfPresent(VipCardDO::getCoupon, reqVO.getCoupon())
                .eqIfPresent(VipCardDO::getMony, reqVO.getMony())
                .eqIfPresent(VipCardDO::getPeriod, reqVO.getPeriod())
                .eqIfPresent(VipCardDO::getPrice, reqVO.getPrice())
                .eqIfPresent(VipCardDO::getStatus, reqVO.getStatus())
                .eqIfPresent(VipCardDO::getRule, reqVO.getRule())
                .betweenIfPresent(VipCardDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(VipCardDO::getId));
    }

}
