package co.yixiang.yshop.module.desk.dal.mysql.shopdeskcategory;

import java.util.*;

import co.yixiang.yshop.framework.common.pojo.PageResult;
import co.yixiang.yshop.framework.mybatis.core.query.LambdaQueryWrapperX;
import co.yixiang.yshop.framework.mybatis.core.mapper.BaseMapperX;
import co.yixiang.yshop.framework.security.core.util.SecurityFrameworkUtils;
import co.yixiang.yshop.module.desk.dal.dataobject.shopdesk.ShopDeskDO;
import co.yixiang.yshop.module.desk.dal.dataobject.shopdeskcategory.ShopDeskCategoryDO;
import org.apache.ibatis.annotations.Mapper;
import co.yixiang.yshop.module.desk.controller.admin.shopdeskcategory.vo.*;

/**
 * 门店桌号分类 Mapper
 *
 * @author yshop
 */
@Mapper
public interface ShopDeskCategoryMapper extends BaseMapperX<ShopDeskCategoryDO> {

    default PageResult<ShopDeskCategoryDO> selectPage(ShopDeskCategoryPageReqVO reqVO) {
        LambdaQueryWrapperX<ShopDeskCategoryDO> wrapper = new LambdaQueryWrapperX();
        Long shopId = SecurityFrameworkUtils.getLoginUser().getShopId();
        if(shopId > 0) {
            wrapper.eq(ShopDeskCategoryDO::getShopId,shopId);
        }
        wrapper.eqIfPresent(ShopDeskCategoryDO::getShopId, reqVO.getShopId())
                .likeIfPresent(ShopDeskCategoryDO::getShopName, reqVO.getShopName())
                .likeIfPresent(ShopDeskCategoryDO::getName, reqVO.getName())
                .eqIfPresent(ShopDeskCategoryDO::getPicUrl, reqVO.getPicUrl())
                .eqIfPresent(ShopDeskCategoryDO::getPeople, reqVO.getPeople())
                .orderByDesc(ShopDeskCategoryDO::getId);
        return selectPage(reqVO, wrapper);
    }

}