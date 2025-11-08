package co.yixiang.yshop.module.store.dal.mysql.storerevenue;

import java.util.*;

import co.yixiang.yshop.framework.common.pojo.PageResult;
import co.yixiang.yshop.framework.mybatis.core.query.LambdaQueryWrapperX;
import co.yixiang.yshop.framework.mybatis.core.mapper.BaseMapperX;
import co.yixiang.yshop.framework.security.core.util.SecurityFrameworkUtils;
import co.yixiang.yshop.module.store.dal.dataobject.storerevenue.StoreRevenueDO;
import co.yixiang.yshop.module.store.dal.dataobject.userbank.UserBankDO;
import org.apache.ibatis.annotations.Mapper;
import co.yixiang.yshop.module.store.controller.admin.storerevenue.vo.*;

/**
 * 店铺收支明细 Mapper
 *
 * @author yshop
 */
@Mapper
public interface StoreRevenueMapper extends BaseMapperX<StoreRevenueDO> {

    default PageResult<StoreRevenueDO> selectPage(StoreRevenuePageReqVO reqVO) {
        LambdaQueryWrapperX<StoreRevenueDO> wrapper = new LambdaQueryWrapperX();
        Long shopId = SecurityFrameworkUtils.getLoginUser().getShopId();
        if(shopId > 0) {
            wrapper.eq(StoreRevenueDO::getShopId,shopId);
        }
         wrapper.eqIfPresent(StoreRevenueDO::getShopId, reqVO.getShopId())
                .likeIfPresent(StoreRevenueDO::getShopName, reqVO.getShopName())
                .eqIfPresent(StoreRevenueDO::getType, reqVO.getType())
                .eqIfPresent(StoreRevenueDO::getAmount, reqVO.getAmount())
                .eqIfPresent(StoreRevenueDO::getUid, reqVO.getUid())
                .betweenIfPresent(StoreRevenueDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(StoreRevenueDO::getId);
        return selectPage(reqVO,wrapper);
    }

    default List<StoreRevenueDO> selectList(StoreRevenueExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<StoreRevenueDO>()
                .eqIfPresent(StoreRevenueDO::getShopId, reqVO.getShopId())
                .likeIfPresent(StoreRevenueDO::getShopName, reqVO.getShopName())
                .eqIfPresent(StoreRevenueDO::getType, reqVO.getType())
                .eqIfPresent(StoreRevenueDO::getAmount, reqVO.getAmount())
                .eqIfPresent(StoreRevenueDO::getUid, reqVO.getUid())
                .betweenIfPresent(StoreRevenueDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(StoreRevenueDO::getId));
    }

}
