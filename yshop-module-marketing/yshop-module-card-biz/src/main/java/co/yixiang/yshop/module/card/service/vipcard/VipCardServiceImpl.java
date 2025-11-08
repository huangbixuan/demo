package co.yixiang.yshop.module.card.service.vipcard;

import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import co.yixiang.yshop.module.card.controller.admin.vipcard.vo.*;
import co.yixiang.yshop.module.card.dal.dataobject.vipcard.VipCardDO;
import co.yixiang.yshop.framework.common.pojo.PageResult;

import co.yixiang.yshop.module.card.convert.vipcard.VipCardConvert;
import co.yixiang.yshop.module.card.dal.mysql.vipcard.VipCardMapper;

import static co.yixiang.yshop.framework.common.exception.util.ServiceExceptionUtil.exception;
import static co.yixiang.yshop.module.merchant.enums.ErrorCodeConstants.*;

/**
 * 会员卡 Service 实现类
 *
 * @author yshop
 */
@Service
@Validated
public class VipCardServiceImpl implements VipCardService {

    @Resource
    private VipCardMapper vipCardMapper;

    @Override
    public Long createVipCard(VipCardCreateReqVO createReqVO) {
        // 插入
        VipCardDO vipCard = VipCardConvert.INSTANCE.convert(createReqVO);
        vipCardMapper.insert(vipCard);
        // 返回
        return vipCard.getId();
    }

    @Override
    public void updateVipCard(VipCardUpdateReqVO updateReqVO) {
        // 校验存在
        validateVipCardExists(updateReqVO.getId());
        // 更新
        VipCardDO updateObj = VipCardConvert.INSTANCE.convert(updateReqVO);
        vipCardMapper.updateById(updateObj);
    }

    @Override
    public void deleteVipCard(Long id) {
        // 校验存在
        validateVipCardExists(id);
        // 删除
        vipCardMapper.deleteById(id);
    }

    private void validateVipCardExists(Long id) {
        if (vipCardMapper.selectById(id) == null) {
            throw exception(VIP_CARD_NOT_EXISTS);
        }
    }

    @Override
    public VipCardDO getVipCard(Long id) {
        return vipCardMapper.selectById(id);
    }

    @Override
    public List<VipCardDO> getVipCardList(Collection<Long> ids) {
        return vipCardMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<VipCardDO> getVipCardPage(VipCardPageReqVO pageReqVO) {
        return vipCardMapper.selectPage(pageReqVO);
    }

    @Override
    public List<VipCardDO> getVipCardList(VipCardExportReqVO exportReqVO) {
        return vipCardMapper.selectList(exportReqVO);
    }

}
