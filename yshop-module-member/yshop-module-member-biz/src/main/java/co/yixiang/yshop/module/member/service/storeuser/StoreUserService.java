package co.yixiang.yshop.module.member.service.storeuser;

import co.yixiang.yshop.module.member.controller.admin.storeuser.vo.StoreUserPageReqVO;
import co.yixiang.yshop.module.member.controller.admin.storeuser.vo.StoreUserRespVO;
import co.yixiang.yshop.module.member.controller.admin.storeuser.vo.StoreUserSaveReqVO;
import jakarta.validation.*;
import co.yixiang.yshop.module.member.dal.dataobject.storeuser.StoreUserDO;
import co.yixiang.yshop.framework.common.pojo.PageResult;

/**
 * 门店移动端商家用户关联 Service 接口
 *
 * @author yshop
 */
public interface StoreUserService {

    /**
     * 创建门店移动端商家用户关联
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createStoreUser(@Valid StoreUserSaveReqVO createReqVO);

    /**
     * 更新门店移动端商家用户关联
     *
     * @param updateReqVO 更新信息
     */
    void updateStoreUser(@Valid StoreUserSaveReqVO updateReqVO);

    /**
     * 删除门店移动端商家用户关联
     *
     * @param id 编号
     */
    void deleteStoreUser(Long id);

    /**
     * 获得门店移动端商家用户关联
     *
     * @param id 编号
     * @return 门店移动端商家用户关联
     */
    StoreUserRespVO getStoreUser(Long id);

    /**
     * 获得门店移动端商家用户关联分页
     *
     * @param pageReqVO 分页查询
     * @return 门店移动端商家用户关联分页
     */
    PageResult<StoreUserRespVO> getStoreUserPage(StoreUserPageReqVO pageReqVO);

}