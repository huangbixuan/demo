package co.yixiang.yshop.module.store.service.storerevenue;

import co.yixiang.yshop.framework.tenant.core.aop.TenantIgnore;
import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import co.yixiang.yshop.module.store.controller.admin.storerevenue.vo.*;
import co.yixiang.yshop.module.store.dal.dataobject.storerevenue.StoreRevenueDO;
import co.yixiang.yshop.framework.common.pojo.PageResult;

import co.yixiang.yshop.module.store.convert.storerevenue.StoreRevenueConvert;
import co.yixiang.yshop.module.store.dal.mysql.storerevenue.StoreRevenueMapper;

import static co.yixiang.yshop.framework.common.exception.util.ServiceExceptionUtil.exception;
import static co.yixiang.yshop.module.store.enums.ErrorCodeConstants.*;

/**
 * 店铺收支明细 Service 实现类
 *
 * @author yshop
 */
@Service
@Validated
public class StoreRevenueServiceImpl implements StoreRevenueService {

    @Resource
    private StoreRevenueMapper storeRevenueMapper;

    @Override
    public Long createStoreRevenue(StoreRevenueCreateReqVO createReqVO) {
        // 插入
        StoreRevenueDO storeRevenue = StoreRevenueConvert.INSTANCE.convert(createReqVO);
        storeRevenueMapper.insert(storeRevenue);
        // 返回
        return storeRevenue.getId();
    }

    @Override
    public void updateStoreRevenue(StoreRevenueUpdateReqVO updateReqVO) {
        // 校验存在
        validateStoreRevenueExists(updateReqVO.getId());
        // 更新
        StoreRevenueDO updateObj = StoreRevenueConvert.INSTANCE.convert(updateReqVO);
        storeRevenueMapper.updateById(updateObj);
    }

    @Override
    public void deleteStoreRevenue(Long id) {
        // 校验存在
        validateStoreRevenueExists(id);
        // 删除
        storeRevenueMapper.deleteById(id);
    }

    private void validateStoreRevenueExists(Long id) {
        if (storeRevenueMapper.selectById(id) == null) {
            throw exception(STORE_REVENUE_NOT_EXISTS);
        }
    }

    @Override
    public StoreRevenueDO getStoreRevenue(Long id) {
        return storeRevenueMapper.selectById(id);
    }

    @Override
    public List<StoreRevenueDO> getStoreRevenueList(Collection<Long> ids) {
        return storeRevenueMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<StoreRevenueDO> getStoreRevenuePage(StoreRevenuePageReqVO pageReqVO) {
        return storeRevenueMapper.selectPage(pageReqVO);
    }

    @Override
    public List<StoreRevenueDO> getStoreRevenueList(StoreRevenueExportReqVO exportReqVO) {
        return storeRevenueMapper.selectList(exportReqVO);
    }

}
