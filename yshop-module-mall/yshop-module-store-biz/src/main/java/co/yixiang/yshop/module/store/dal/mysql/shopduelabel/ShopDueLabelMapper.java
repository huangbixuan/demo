package co.yixiang.yshop.module.store.dal.mysql.shopduelabel;

import java.util.*;

import co.yixiang.yshop.framework.common.pojo.PageResult;
import co.yixiang.yshop.framework.mybatis.core.query.LambdaQueryWrapperX;
import co.yixiang.yshop.framework.mybatis.core.mapper.BaseMapperX;
import co.yixiang.yshop.framework.security.core.util.SecurityFrameworkUtils;
import co.yixiang.yshop.module.store.dal.dataobject.shopduelabel.ShopDueLabelDO;
import org.apache.ibatis.annotations.Mapper;
import co.yixiang.yshop.module.store.controller.admin.shopduelabel.vo.*;

/**
 * 预约标签 Mapper
 *
 * @author yshop
 */
@Mapper
public interface ShopDueLabelMapper extends BaseMapperX<ShopDueLabelDO> {

    default PageResult<ShopDueLabelDO> selectPage(ShopDueLabelPageReqVO reqVO) {
        LambdaQueryWrapperX<ShopDueLabelDO> wrapper = new LambdaQueryWrapperX();
        Long shopId = SecurityFrameworkUtils.getLoginUser().getShopId();
        if(shopId > 0) {
            wrapper.eq(ShopDueLabelDO::getShopId,shopId);
        }
        wrapper.eqIfPresent(ShopDueLabelDO::getTitle, reqVO.getTitle())
                .eqIfPresent(ShopDueLabelDO::getShopId, reqVO.getShopId())
                .likeIfPresent(ShopDueLabelDO::getShopName, reqVO.getShopName())
                .betweenIfPresent(ShopDueLabelDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(ShopDueLabelDO::getId);
        return selectPage(reqVO,wrapper);
    }

}