package co.yixiang.yshop.module.store.dal.mysql.shopduerule;

import java.util.*;

import co.yixiang.yshop.framework.common.pojo.PageResult;
import co.yixiang.yshop.framework.mybatis.core.query.LambdaQueryWrapperX;
import co.yixiang.yshop.framework.mybatis.core.mapper.BaseMapperX;
import co.yixiang.yshop.framework.security.core.util.SecurityFrameworkUtils;
import co.yixiang.yshop.module.store.dal.dataobject.shopduerule.ShopDueRuleDO;
import org.apache.ibatis.annotations.Mapper;
import co.yixiang.yshop.module.store.controller.admin.shopduerule.vo.*;

/**
 * 预约规则 Mapper
 *
 * @author yshop
 */
@Mapper
public interface ShopDueRuleMapper extends BaseMapperX<ShopDueRuleDO> {

    default PageResult<ShopDueRuleDO> selectPage(ShopDueRulePageReqVO reqVO) {
        LambdaQueryWrapperX<ShopDueRuleDO> wrapper = new LambdaQueryWrapperX();
        Long shopId = SecurityFrameworkUtils.getLoginUser().getShopId();
        if(shopId > 0) {
            wrapper.eq(ShopDueRuleDO::getShopId,shopId);
        }
        wrapper.eqIfPresent(ShopDueRuleDO::getShopId, reqVO.getShopId())
                .likeIfPresent(ShopDueRuleDO::getShopName, reqVO.getShopName())
                .eqIfPresent(ShopDueRuleDO::getLabelId, reqVO.getLabelId())
                .eqIfPresent(ShopDueRuleDO::getInterval, reqVO.getInterval())
                .betweenIfPresent(ShopDueRuleDO::getStartTime, reqVO.getStartTime())
                .betweenIfPresent(ShopDueRuleDO::getEndTime, reqVO.getEndTime())
                .betweenIfPresent(ShopDueRuleDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(ShopDueRuleDO::getId);
        return selectPage(reqVO, wrapper);
    }

}