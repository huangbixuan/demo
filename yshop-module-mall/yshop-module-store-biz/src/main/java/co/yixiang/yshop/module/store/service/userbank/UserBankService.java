package co.yixiang.yshop.module.store.service.userbank;

import java.util.*;
import jakarta.validation.*;
import co.yixiang.yshop.module.store.controller.admin.userbank.vo.*;
import co.yixiang.yshop.module.store.dal.dataobject.userbank.UserBankDO;
import co.yixiang.yshop.framework.common.pojo.PageResult;

/**
 * 提现账户 Service 接口
 *
 * @author yshop
 */
public interface UserBankService {

    /**
     * 创建提现账户
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createUserBank(@Valid UserBankCreateReqVO createReqVO);

    /**
     * 更新提现账户
     *
     * @param updateReqVO 更新信息
     */
    void updateUserBank(@Valid UserBankUpdateReqVO updateReqVO);

    /**
     * 删除提现账户
     *
     * @param id 编号
     */
    void deleteUserBank(Long id);

    /**
     * 获得提现账户
     *
     * @param id 编号
     * @return 提现账户
     */
    UserBankDO getUserBank(Long id);

    /**
     * 获得提现账户列表
     *
     * @param shopId 店铺ID
     * @return 提现账户列表
     */
    List<UserBankDO> getUserBankList(Long shopId);

    /**
     * 获得提现账户分页
     *
     * @param pageReqVO 分页查询
     * @return 提现账户分页
     */
    PageResult<UserBankDO> getUserBankPage(UserBankPageReqVO pageReqVO);

    /**
     * 获得提现账户列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 提现账户列表
     */
    List<UserBankDO> getUserBankList(UserBankExportReqVO exportReqVO);

}
