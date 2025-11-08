package co.yixiang.yshop.module.score.dal.mysql.scoreproductcategory;

import co.yixiang.yshop.framework.common.pojo.PageResult;
import co.yixiang.yshop.framework.mybatis.core.query.LambdaQueryWrapperX;
import co.yixiang.yshop.framework.mybatis.core.mapper.BaseMapperX;
import co.yixiang.yshop.module.score.dal.dataobject.scoreproductcategory.ScoreProductCategoryDO;
import org.apache.ibatis.annotations.Mapper;
import co.yixiang.yshop.module.score.controller.admin.scoreproductcategory.vo.*;

/**
 * 积分商品分类 Mapper
 *
 * @author yshop
 */
@Mapper
public interface ScoreProductCategoryNewMapper extends BaseMapperX<ScoreProductCategoryDO> {

    default PageResult<ScoreProductCategoryDO> selectPage(ScoreProductCategoryPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ScoreProductCategoryDO>()
                .eqIfPresent(ScoreProductCategoryDO::getParentId, reqVO.getParentId())
                .likeIfPresent(ScoreProductCategoryDO::getShopName, reqVO.getShopName())
                .likeIfPresent(ScoreProductCategoryDO::getName, reqVO.getName())
                .eqIfPresent(ScoreProductCategoryDO::getPicUrl, reqVO.getPicUrl())
                .eqIfPresent(ScoreProductCategoryDO::getSort, reqVO.getSort())
                .eqIfPresent(ScoreProductCategoryDO::getDescription, reqVO.getDescription())
                .eqIfPresent(ScoreProductCategoryDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(ScoreProductCategoryDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(ScoreProductCategoryDO::getId));
    }

}