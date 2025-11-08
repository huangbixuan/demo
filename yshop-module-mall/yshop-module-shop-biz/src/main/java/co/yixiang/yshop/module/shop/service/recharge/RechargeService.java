package co.yixiang.yshop.module.shop.service.recharge;

import java.util.*;
import jakarta.validation.*;
import co.yixiang.yshop.module.shop.controller.admin.recharge.vo.*;
import co.yixiang.yshop.module.shop.dal.dataobject.recharge.RechargeDO;
import co.yixiang.yshop.framework.common.pojo.PageResult;

/**
 * 充值金额管理 Service 接口
 *
 * @author yshop
 */
public interface RechargeService {

    /**
     * 创建充值金额管理
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createRecharge(@Valid RechargeCreateReqVO createReqVO);

    /**
     * 更新充值金额管理
     *
     * @param updateReqVO 更新信息
     */
    void updateRecharge(@Valid RechargeUpdateReqVO updateReqVO);

    /**
     * 删除充值金额管理
     *
     * @param id 编号
     */
    void deleteRecharge(Long id);

    /**
     * 获得充值金额管理
     *
     * @param id 编号
     * @return 充值金额管理
     */
    RechargeDO getRecharge(Long id);

    /**
     * 获得充值金额管理列表
     *
     * @param ids 编号
     * @return 充值金额管理列表
     */
    List<RechargeDO> getRechargeList(Collection<Long> ids);

    /**
     * 获得充值金额管理分页
     *
     * @param pageReqVO 分页查询
     * @return 充值金额管理分页
     */
    PageResult<RechargeDO> getRechargePage(RechargePageReqVO pageReqVO);

    /**
     * 获得充值金额管理列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 充值金额管理列表
     */
    List<RechargeDO> getRechargeList(RechargeExportReqVO exportReqVO);

}
