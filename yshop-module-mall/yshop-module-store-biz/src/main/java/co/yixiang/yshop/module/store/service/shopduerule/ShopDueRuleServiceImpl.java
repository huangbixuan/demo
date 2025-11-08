package co.yixiang.yshop.module.store.service.shopduerule;

import cn.hutool.core.date.DateUtil;
import co.yixiang.yshop.framework.common.exception.ErrorCode;
import co.yixiang.yshop.framework.mybatis.core.query.LambdaQueryWrapperX;
import co.yixiang.yshop.module.store.dal.dataobject.shopduelabel.ShopDueLabelDO;
import co.yixiang.yshop.module.store.dal.dataobject.storeshop.StoreShopDO;
import co.yixiang.yshop.module.store.dal.mysql.shopduelabel.ShopDueLabelMapper;
import co.yixiang.yshop.module.store.dal.mysql.storeshop.StoreShopMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import co.yixiang.yshop.module.store.controller.admin.shopduerule.vo.*;
import co.yixiang.yshop.module.store.dal.dataobject.shopduerule.ShopDueRuleDO;
import co.yixiang.yshop.framework.common.pojo.PageResult;
import co.yixiang.yshop.framework.common.pojo.PageParam;
import co.yixiang.yshop.framework.common.util.object.BeanUtils;

import co.yixiang.yshop.module.store.dal.mysql.shopduerule.ShopDueRuleMapper;

import static co.yixiang.yshop.framework.common.exception.util.ServiceExceptionUtil.exception;
import static co.yixiang.yshop.module.store.enums.ErrorCodeConstants.*;

/**
 * 预约规则 Service 实现类
 *
 * @author yshop
 */
@Service
@Validated
public class ShopDueRuleServiceImpl implements ShopDueRuleService {

    @Resource
    private ShopDueRuleMapper shopDueRuleMapper;
    @Resource
    private StoreShopMapper shopMapper;
    @Resource
    private ShopDueLabelMapper shopDueLabelMapper;

    @Override
    public Long createShopDueRule(ShopDueRuleSaveReqVO createReqVO) {
       this.check(createReqVO);
        // 插入
        ShopDueRuleDO shopDueRule = BeanUtils.toBean(createReqVO, ShopDueRuleDO.class);
        StoreShopDO storeShopDO = this.getShop(createReqVO.getShopId());
        shopDueRule.setShopName(storeShopDO.getName());
        shopDueRuleMapper.insert(shopDueRule);
        // 返回
        return shopDueRule.getId();
    }

    @Override
    public void updateShopDueRule(ShopDueRuleSaveReqVO updateReqVO) {

        this.check(updateReqVO);

        // 校验存在
        validateShopDueRuleExists(updateReqVO.getId());
        // 更新
        ShopDueRuleDO updateObj = BeanUtils.toBean(updateReqVO, ShopDueRuleDO.class);
        StoreShopDO storeShopDO = this.getShop(updateReqVO.getShopId());
        updateObj.setShopName(storeShopDO.getName());
        shopDueRuleMapper.updateById(updateObj);
    }

    @Override
    public void deleteShopDueRule(Long id) {
        // 校验存在
        validateShopDueRuleExists(id);
        // 删除
        shopDueRuleMapper.deleteById(id);
    }

    private void validateShopDueRuleExists(Long id) {
        if (shopDueRuleMapper.selectById(id) == null) {
            throw exception(SHOP_DUE_RULE_NOT_EXISTS);
        }
    }

    @Override
    public ShopDueRuleDO getShopDueRule(Long id) {
        return shopDueRuleMapper.selectById(id);
    }

    @Override
    public PageResult<ShopDueRuleRespVO> getShopDueRulePage(ShopDueRulePageReqVO pageReqVO) {
        PageResult<ShopDueRuleDO> pageResult = shopDueRuleMapper.selectPage(pageReqVO);
        PageResult<ShopDueRuleRespVO> pageResult1 = BeanUtils.toBean(pageResult, ShopDueRuleRespVO.class);
        for (ShopDueRuleRespVO shopDueRuleRespVO : pageResult1.getList()){
            ShopDueLabelDO shopDueLabelDO = shopDueLabelMapper.selectById(shopDueRuleRespVO.getLabelId());
            shopDueRuleRespVO.setLabelName(shopDueLabelDO.getTitle());
        }
        return pageResult1;
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

    private void check(ShopDueRuleSaveReqVO saveReqVO){
        Date startTime = DateUtil.parse(saveReqVO.getStartTime());
        Date endTime = DateUtil.parse(saveReqVO.getEndTime());
        if(DateUtil.compare(startTime,endTime) > 0){
            throw exception(new ErrorCode(20246172,"开始时间不可以大于结束时间!"));
        }

        ShopDueRuleDO shopDueRuleDO = shopDueRuleMapper.selectOne(new LambdaQueryWrapperX<ShopDueRuleDO>()
                .neIfPresent(ShopDueRuleDO::getId,saveReqVO.getId())
                .eq(ShopDueRuleDO::getLabelId,saveReqVO.getLabelId()));
        if(shopDueRuleDO != null){
            throw exception(new ErrorCode(20246171,"此标签单规则已经添加过哦!"));
        }
    }

}