package co.yixiang.yshop.module.shop.service.recharge;

import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import co.yixiang.yshop.module.shop.controller.admin.recharge.vo.*;
import co.yixiang.yshop.module.shop.dal.dataobject.recharge.RechargeDO;
import co.yixiang.yshop.framework.common.pojo.PageResult;

import co.yixiang.yshop.module.shop.convert.recharge.RechargeConvert;
import co.yixiang.yshop.module.shop.dal.mysql.recharge.RechargeMapper;

import static co.yixiang.yshop.framework.common.exception.util.ServiceExceptionUtil.exception;
import static co.yixiang.yshop.module.shop.enums.ErrorCodeConstants.*;

/**
 * 充值金额管理 Service 实现类
 *
 * @author yshop
 */
@Service
@Validated
public class RechargeServiceImpl implements RechargeService {

    @Resource
    private RechargeMapper rechargeMapper;

    @Override
    public Long createRecharge(RechargeCreateReqVO createReqVO) {
        // 插入
        RechargeDO recharge = RechargeConvert.INSTANCE.convert(createReqVO);
        rechargeMapper.insert(recharge);
        // 返回
        return recharge.getId();
    }

    @Override
    public void updateRecharge(RechargeUpdateReqVO updateReqVO) {
        // 校验存在
        validateRechargeExists(updateReqVO.getId());
        // 更新
        RechargeDO updateObj = RechargeConvert.INSTANCE.convert(updateReqVO);
        rechargeMapper.updateById(updateObj);
    }

    @Override
    public void deleteRecharge(Long id) {
        // 校验存在
        validateRechargeExists(id);
        // 删除
        rechargeMapper.deleteById(id);
    }

    private void validateRechargeExists(Long id) {
        if (rechargeMapper.selectById(id) == null) {
            throw exception(RECHARGE_NOT_EXISTS);
        }
    }

    @Override
    public RechargeDO getRecharge(Long id) {
        return rechargeMapper.selectById(id);
    }

    @Override
    public List<RechargeDO> getRechargeList(Collection<Long> ids) {
        return rechargeMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<RechargeDO> getRechargePage(RechargePageReqVO pageReqVO) {
        return rechargeMapper.selectPage(pageReqVO);
    }

    @Override
    public List<RechargeDO> getRechargeList(RechargeExportReqVO exportReqVO) {
        return rechargeMapper.selectList(exportReqVO);
    }

}
