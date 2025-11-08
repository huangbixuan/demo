package co.yixiang.yshop.module.store.service.storerevenue;

import java.util.*;
import jakarta.validation.*;
import co.yixiang.yshop.module.store.controller.admin.storerevenue.vo.*;
import co.yixiang.yshop.module.store.dal.dataobject.storerevenue.StoreRevenueDO;
import co.yixiang.yshop.framework.common.pojo.PageResult;

/**
 * 店铺收支明细 Service 接口
 *
 * @author yshop
 */
public interface StoreRevenueService {

    /**
     * 创建店铺收支明细
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createStoreRevenue(@Valid StoreRevenueCreateReqVO createReqVO);

    /**
     * 更新店铺收支明细
     *
     * @param updateReqVO 更新信息
     */
    void updateStoreRevenue(@Valid StoreRevenueUpdateReqVO updateReqVO);

    /**
     * 删除店铺收支明细
     *
     * @param id 编号
     */
    void deleteStoreRevenue(Long id);

    /**
     * 获得店铺收支明细
     *
     * @param id 编号
     * @return 店铺收支明细
     */
    StoreRevenueDO getStoreRevenue(Long id);

    /**
     * 获得店铺收支明细列表
     *
     * @param ids 编号
     * @return 店铺收支明细列表
     */
    List<StoreRevenueDO> getStoreRevenueList(Collection<Long> ids);

    /**
     * 获得店铺收支明细分页
     *
     * @param pageReqVO 分页查询
     * @return 店铺收支明细分页
     */
    PageResult<StoreRevenueDO> getStoreRevenuePage(StoreRevenuePageReqVO pageReqVO);

    /**
     * 获得店铺收支明细列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 店铺收支明细列表
     */
    List<StoreRevenueDO> getStoreRevenueList(StoreRevenueExportReqVO exportReqVO);

}
