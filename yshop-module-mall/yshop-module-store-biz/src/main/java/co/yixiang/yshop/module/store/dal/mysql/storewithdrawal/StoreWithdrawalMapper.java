package co.yixiang.yshop.module.store.dal.mysql.storewithdrawal;

import java.util.*;

import co.yixiang.yshop.framework.common.pojo.PageResult;
import co.yixiang.yshop.framework.mybatis.core.query.LambdaQueryWrapperX;
import co.yixiang.yshop.framework.mybatis.core.mapper.BaseMapperX;
import co.yixiang.yshop.framework.security.core.util.SecurityFrameworkUtils;
import co.yixiang.yshop.module.store.dal.dataobject.storewithdrawal.StoreWithdrawalDO;
import co.yixiang.yshop.module.store.dal.dataobject.userbank.UserBankDO;
import org.apache.ibatis.annotations.Mapper;
import co.yixiang.yshop.module.store.controller.admin.storewithdrawal.vo.*;

/**
 * 提现管理 Mapper
 *
 * @author yshop
 */
@Mapper
public interface StoreWithdrawalMapper extends BaseMapperX<StoreWithdrawalDO> {

    default PageResult<StoreWithdrawalDO> selectPage(StoreWithdrawalPageReqVO reqVO) {
        LambdaQueryWrapperX<StoreWithdrawalDO> wrapper = new LambdaQueryWrapperX();
        Long shopId = SecurityFrameworkUtils.getLoginUser().getShopId();
        if(shopId > 0) {
            wrapper.eq(StoreWithdrawalDO::getShopId,shopId);
        }
        wrapper.likeIfPresent(StoreWithdrawalDO::getShopName, reqVO.getShopName())
                .eqIfPresent(StoreWithdrawalDO::getShopId,reqVO.getShopId())
                .eqIfPresent(StoreWithdrawalDO::getAmount, reqVO.getAmount())
                .eqIfPresent(StoreWithdrawalDO::getStatus, reqVO.getStatus())
                .orderByDesc(StoreWithdrawalDO::getId);
        return selectPage(reqVO, wrapper);
    }

    default List<StoreWithdrawalDO> selectList(StoreWithdrawalExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<StoreWithdrawalDO>()
                .eqIfPresent(StoreWithdrawalDO::getUid, reqVO.getUid())
                .eqIfPresent(StoreWithdrawalDO::getShopId, reqVO.getShopId())
                .likeIfPresent(StoreWithdrawalDO::getShopName, reqVO.getShopName())
                .eqIfPresent(StoreWithdrawalDO::getAmount, reqVO.getAmount())
                .eqIfPresent(StoreWithdrawalDO::getType, reqVO.getType())
                .eqIfPresent(StoreWithdrawalDO::getStatus, reqVO.getStatus())
                .eqIfPresent(StoreWithdrawalDO::getRefuse, reqVO.getRefuse())
                .eqIfPresent(StoreWithdrawalDO::getMonth, reqVO.getMonth())
                .eqIfPresent(StoreWithdrawalDO::getResidueAmount, reqVO.getResidueAmount())
                .betweenIfPresent(StoreWithdrawalDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(StoreWithdrawalDO::getId));
    }

}
