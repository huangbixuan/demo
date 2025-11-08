package co.yixiang.yshop.module.store.service.shopduelabel;

import co.yixiang.yshop.framework.common.exception.ErrorCode;
import co.yixiang.yshop.module.store.dal.dataobject.shopduerule.ShopDueRuleDO;
import co.yixiang.yshop.module.store.dal.dataobject.storeshop.StoreShopDO;
import co.yixiang.yshop.module.store.dal.mysql.shopduerule.ShopDueRuleMapper;
import co.yixiang.yshop.module.store.dal.mysql.storeshop.StoreShopMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import co.yixiang.yshop.module.store.controller.admin.shopduelabel.vo.*;
import co.yixiang.yshop.module.store.dal.dataobject.shopduelabel.ShopDueLabelDO;
import co.yixiang.yshop.framework.common.pojo.PageResult;
import co.yixiang.yshop.framework.common.pojo.PageParam;
import co.yixiang.yshop.framework.common.util.object.BeanUtils;

import co.yixiang.yshop.module.store.dal.mysql.shopduelabel.ShopDueLabelMapper;

import static co.yixiang.yshop.framework.common.exception.util.ServiceExceptionUtil.exception;
import static co.yixiang.yshop.module.store.enums.ErrorCodeConstants.*;

/**
 * 预约标签 Service 实现类
 *
 * @author yshop
 */
@Service
@Validated
public class ShopDueLabelServiceImpl implements ShopDueLabelService {

    @Resource
    private ShopDueLabelMapper shopDueLabelMapper;
    @Resource
    private StoreShopMapper shopMapper;
    @Resource
    private ShopDueRuleMapper shopDueRuleMapper;

    @Override
    public Long createShopDueLabel(ShopDueLabelSaveReqVO createReqVO) {
        // 插入
        ShopDueLabelDO shopDueLabel = BeanUtils.toBean(createReqVO, ShopDueLabelDO.class);
        StoreShopDO storeShopDO = this.getShop(createReqVO.getShopId());
        shopDueLabel.setShopName(storeShopDO.getName());
        shopDueLabelMapper.insert(shopDueLabel);
        // 返回
        return shopDueLabel.getId();
    }

    @Override
    public void updateShopDueLabel(ShopDueLabelSaveReqVO updateReqVO) {
        // 校验存在
        validateShopDueLabelExists(updateReqVO.getId());
        // 更新
        ShopDueLabelDO updateObj = BeanUtils.toBean(updateReqVO, ShopDueLabelDO.class);
        StoreShopDO storeShopDO = this.getShop(updateReqVO.getShopId());
        updateObj.setShopName(storeShopDO.getName());
        shopDueLabelMapper.updateById(updateObj);
    }

    @Override
    public void deleteShopDueLabel(Long id) {
        // 校验存在
        validateShopDueLabelExists(id);
        //判断当前标签下是否有规则
        Long count = shopDueRuleMapper.selectCount(new LambdaQueryWrapper<ShopDueRuleDO>()
                .eq(ShopDueRuleDO::getLabelId,id));
        if(count > 0){
            throw exception(new ErrorCode(202507301,"当前标签下有预约规则不可删除哦"));
        }

        // 删除
        shopDueLabelMapper.deleteById(id);
    }

    private void validateShopDueLabelExists(Long id) {
        if (shopDueLabelMapper.selectById(id) == null) {
            throw exception(SHOP_DUE_LABEL_NOT_EXISTS);
        }
    }

    @Override
    public ShopDueLabelDO getShopDueLabel(Long id) {
        return shopDueLabelMapper.selectById(id);
    }

    @Override
    public PageResult<ShopDueLabelDO> getShopDueLabelPage(ShopDueLabelPageReqVO pageReqVO) {
        return shopDueLabelMapper.selectPage(pageReqVO);
    }


    /**
     * 获取门店
     * @param id
     * @return
     */
    private StoreShopDO getShop(Long id){
        //查找门店
        StoreShopDO storeShopDO = shopMapper.selectById(id);
        return  storeShopDO;
    }

}