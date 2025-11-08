package co.yixiang.yshop.module.store.service.shopduerule;

import java.util.*;
import jakarta.validation.*;
import co.yixiang.yshop.module.store.controller.admin.shopduerule.vo.*;
import co.yixiang.yshop.module.store.dal.dataobject.shopduerule.ShopDueRuleDO;
import co.yixiang.yshop.framework.common.pojo.PageResult;
import co.yixiang.yshop.framework.common.pojo.PageParam;

/**
 * 预约规则 Service 接口
 *
 * @author yshop
 */
public interface ShopDueRuleService {

    /**
     * 创建预约规则
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createShopDueRule(@Valid ShopDueRuleSaveReqVO createReqVO);

    /**
     * 更新预约规则
     *
     * @param updateReqVO 更新信息
     */
    void updateShopDueRule(@Valid ShopDueRuleSaveReqVO updateReqVO);

    /**
     * 删除预约规则
     *
     * @param id 编号
     */
    void deleteShopDueRule(Long id);

    /**
     * 获得预约规则
     *
     * @param id 编号
     * @return 预约规则
     */
    ShopDueRuleDO getShopDueRule(Long id);

    /**
     * 获得预约规则分页
     *
     * @param pageReqVO 分页查询
     * @return 预约规则分页
     */
    PageResult<ShopDueRuleRespVO> getShopDueRulePage(ShopDueRulePageReqVO pageReqVO);

}