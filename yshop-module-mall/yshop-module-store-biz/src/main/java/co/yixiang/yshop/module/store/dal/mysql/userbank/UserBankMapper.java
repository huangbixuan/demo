package co.yixiang.yshop.module.store.dal.mysql.userbank;

import java.util.*;

import co.yixiang.yshop.framework.common.pojo.PageResult;
import co.yixiang.yshop.framework.mybatis.core.query.LambdaQueryWrapperX;
import co.yixiang.yshop.framework.mybatis.core.mapper.BaseMapperX;
import co.yixiang.yshop.framework.security.core.util.SecurityFrameworkUtils;
import co.yixiang.yshop.module.store.dal.dataobject.userbank.UserBankDO;
import org.apache.ibatis.annotations.Mapper;
import co.yixiang.yshop.module.store.controller.admin.userbank.vo.*;

/**
 * 提现账户 Mapper
 *
 * @author yshop
 */
@Mapper
public interface UserBankMapper extends BaseMapperX<UserBankDO> {

    default PageResult<UserBankDO> selectPage(UserBankPageReqVO reqVO) {
        LambdaQueryWrapperX<UserBankDO> wrapper = new LambdaQueryWrapperX();
        Long shopId = SecurityFrameworkUtils.getLoginUser().getShopId();
        if(shopId > 0) {
            wrapper.eq(UserBankDO::getShopId,shopId);
        }
        wrapper.likeIfPresent(UserBankDO::getShopName, reqVO.getShopName())
                .likeIfPresent(UserBankDO::getBankName, reqVO.getBankName())
                .likeIfPresent(UserBankDO::getName, reqVO.getName())
                .eqIfPresent(UserBankDO::getBankCode, reqVO.getBankCode())
                .eqIfPresent(UserBankDO::getBankMobile, reqVO.getBankMobile())
                .orderByDesc(UserBankDO::getId);
        return selectPage(reqVO,wrapper);
    }

    default List<UserBankDO> selectList(UserBankExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<UserBankDO>()
                .likeIfPresent(UserBankDO::getShopName, reqVO.getShopName())
                .eqIfPresent(UserBankDO::getUid, reqVO.getUid())
                .eqIfPresent(UserBankDO::getType, reqVO.getType())
                .likeIfPresent(UserBankDO::getBankName, reqVO.getBankName())
                .likeIfPresent(UserBankDO::getName, reqVO.getName())
                .eqIfPresent(UserBankDO::getBankCode, reqVO.getBankCode())
                .eqIfPresent(UserBankDO::getBankMobile, reqVO.getBankMobile())
                .eqIfPresent(UserBankDO::getZfbCode, reqVO.getZfbCode())
                .eqIfPresent(UserBankDO::getWxCode, reqVO.getWxCode())
                .eqIfPresent(UserBankDO::getZfbImg, reqVO.getZfbImg())
                .eqIfPresent(UserBankDO::getWxImg, reqVO.getWxImg())
                .eqIfPresent(UserBankDO::getStatus, reqVO.getStatus())
                .eqIfPresent(UserBankDO::getRefuse, reqVO.getRefuse())
                .eqIfPresent(UserBankDO::getBankIcon, reqVO.getBankIcon())
                .eqIfPresent(UserBankDO::getBankType, reqVO.getBankType())
                .betweenIfPresent(UserBankDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(UserBankDO::getId));
    }

}
