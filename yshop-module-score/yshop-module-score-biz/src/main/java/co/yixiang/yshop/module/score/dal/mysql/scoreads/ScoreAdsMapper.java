package co.yixiang.yshop.module.score.dal.mysql.scoreads;

import java.util.*;

import co.yixiang.yshop.framework.common.pojo.PageResult;
import co.yixiang.yshop.framework.mybatis.core.query.LambdaQueryWrapperX;
import co.yixiang.yshop.framework.mybatis.core.mapper.BaseMapperX;
import co.yixiang.yshop.module.score.dal.dataobject.scoreads.ScoreAdsDO;
import org.apache.ibatis.annotations.Mapper;
import co.yixiang.yshop.module.score.controller.admin.scoreads.vo.*;

/**
 * 积分商城广告图管理 Mapper
 *
 * @author yshop
 */
@Mapper
public interface ScoreAdsMapper extends BaseMapperX<ScoreAdsDO> {

    default PageResult<ScoreAdsDO> selectPage(ScoreAdsPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ScoreAdsDO>()
                .eqIfPresent(ScoreAdsDO::getImage, reqVO.getImage())
                .eqIfPresent(ScoreAdsDO::getIsSwitch, reqVO.getIsSwitch())
                .eqIfPresent(ScoreAdsDO::getWeigh, reqVO.getWeigh())
                .likeIfPresent(ScoreAdsDO::getShopName, reqVO.getShopName())
                .betweenIfPresent(ScoreAdsDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(ScoreAdsDO::getId));
    }

}