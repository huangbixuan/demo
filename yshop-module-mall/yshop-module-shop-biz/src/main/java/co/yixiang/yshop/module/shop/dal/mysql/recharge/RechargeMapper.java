package co.yixiang.yshop.module.shop.dal.mysql.recharge;

import java.util.*;

import co.yixiang.yshop.framework.common.pojo.PageResult;
import co.yixiang.yshop.framework.mybatis.core.query.LambdaQueryWrapperX;
import co.yixiang.yshop.framework.mybatis.core.mapper.BaseMapperX;
import co.yixiang.yshop.module.shop.dal.dataobject.recharge.RechargeDO;
import org.apache.ibatis.annotations.Mapper;
import co.yixiang.yshop.module.shop.controller.admin.recharge.vo.*;

/**
 * 充值金额管理 Mapper
 *
 * @author yshop
 */
@Mapper
public interface RechargeMapper extends BaseMapperX<RechargeDO> {

    default PageResult<RechargeDO> selectPage(RechargePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<RechargeDO>()
                .likeIfPresent(RechargeDO::getName, reqVO.getName())
                .eqIfPresent(RechargeDO::getSales, reqVO.getSales())
                .eqIfPresent(RechargeDO::getValue, reqVO.getValue())
                .eqIfPresent(RechargeDO::getWeigh, reqVO.getWeigh())
                .eqIfPresent(RechargeDO::getStatus, reqVO.getStatus())
                .eqIfPresent(RechargeDO::getSellPrice, reqVO.getSellPrice())
                .betweenIfPresent(RechargeDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(RechargeDO::getId));
    }

    default List<RechargeDO> selectList(RechargeExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<RechargeDO>()
                .likeIfPresent(RechargeDO::getName, reqVO.getName())
                .eqIfPresent(RechargeDO::getSales, reqVO.getSales())
                .eqIfPresent(RechargeDO::getValue, reqVO.getValue())
                .eqIfPresent(RechargeDO::getWeigh, reqVO.getWeigh())
                .eqIfPresent(RechargeDO::getStatus, reqVO.getStatus())
                .eqIfPresent(RechargeDO::getSellPrice, reqVO.getSellPrice())
                .betweenIfPresent(RechargeDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(RechargeDO::getWeigh));
    }


}
