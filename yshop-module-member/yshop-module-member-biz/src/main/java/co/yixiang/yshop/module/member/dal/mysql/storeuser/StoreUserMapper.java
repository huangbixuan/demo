package co.yixiang.yshop.module.member.dal.mysql.storeuser;

import co.yixiang.yshop.framework.common.pojo.PageResult;
import co.yixiang.yshop.framework.mybatis.core.mapper.BaseMapperX;
import co.yixiang.yshop.module.member.controller.admin.storeuser.vo.StoreUserPageReqVO;
import co.yixiang.yshop.module.member.controller.admin.storeuser.vo.StoreUserRespVO;
import co.yixiang.yshop.module.member.dal.dataobject.user.MemberUserDO;
import co.yixiang.yshop.module.store.dal.dataobject.storeshop.StoreShopDO;
import co.yixiang.yshop.module.member.dal.dataobject.storeuser.StoreUserDO;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 门店移动端商家用户关联 Mapper
 *
 * @author yshop
 */
@Mapper
public interface StoreUserMapper extends BaseMapperX<StoreUserDO> {

    default PageResult<StoreUserRespVO> selectPage(StoreUserPageReqVO reqVO) {
        return selectJoinPage(reqVO,StoreUserRespVO.class,new MPJLambdaWrapper<StoreUserDO>()
                .selectAll(StoreUserDO.class)
                .selectAs(StoreShopDO::getName,StoreUserRespVO::getShopName)
                .select(MemberUserDO::getNickname)
                .leftJoin(StoreShopDO.class,StoreShopDO::getId,StoreUserDO::getShopId)
                .leftJoin(MemberUserDO.class,MemberUserDO::getId,StoreUserDO::getUid)
                .eqIfExists(MemberUserDO::getNickname,reqVO.getNickname()));
    }

}