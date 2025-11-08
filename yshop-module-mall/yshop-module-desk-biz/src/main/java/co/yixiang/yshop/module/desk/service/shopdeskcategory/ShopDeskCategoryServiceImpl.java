package co.yixiang.yshop.module.desk.service.shopdeskcategory;

import co.yixiang.yshop.module.store.dal.dataobject.storeshop.StoreShopDO;
import co.yixiang.yshop.module.store.dal.mysql.storeshop.StoreShopMapper;
import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import co.yixiang.yshop.module.desk.controller.admin.shopdeskcategory.vo.*;
import co.yixiang.yshop.module.desk.dal.dataobject.shopdeskcategory.ShopDeskCategoryDO;
import co.yixiang.yshop.framework.common.pojo.PageResult;
import co.yixiang.yshop.framework.common.pojo.PageParam;
import co.yixiang.yshop.framework.common.util.object.BeanUtils;

import co.yixiang.yshop.module.desk.dal.mysql.shopdeskcategory.ShopDeskCategoryMapper;

import static co.yixiang.yshop.framework.common.exception.util.ServiceExceptionUtil.exception;
import static co.yixiang.yshop.module.desk.enums.ErrorCodeConstants.*;

/**
 * 门店桌号分类 Service 实现类
 *
 * @author yshop
 */
@Service
@Validated
public class ShopDeskCategoryServiceImpl implements ShopDeskCategoryService {

    @Resource
    private ShopDeskCategoryMapper shopDeskCategoryMapper;
    @Resource
    private StoreShopMapper shopMapper;

    @Override
    public Long createShopDeskCategory(ShopDeskCategorySaveReqVO createReqVO) {
        // 插入
        ShopDeskCategoryDO shopDeskCategory = BeanUtils.toBean(createReqVO, ShopDeskCategoryDO.class);
        StoreShopDO storeShopDO = this.getShop(createReqVO.getShopId());
        shopDeskCategory.setShopName(storeShopDO.getName());
        shopDeskCategoryMapper.insert(shopDeskCategory);
        // 返回
        return shopDeskCategory.getId();
    }

    @Override
    public void updateShopDeskCategory(ShopDeskCategorySaveReqVO updateReqVO) {
        // 校验存在
        validateShopDeskCategoryExists(updateReqVO.getId());
        // 更新
        ShopDeskCategoryDO updateObj = BeanUtils.toBean(updateReqVO, ShopDeskCategoryDO.class);
        StoreShopDO storeShopDO = this.getShop(updateReqVO.getShopId());
        updateObj.setShopName(storeShopDO.getName());
        shopDeskCategoryMapper.updateById(updateObj);
    }

    @Override
    public void deleteShopDeskCategory(Long id) {
        // 校验存在
        validateShopDeskCategoryExists(id);
        // 删除
        shopDeskCategoryMapper.deleteById(id);
    }

    private void validateShopDeskCategoryExists(Long id) {
        if (shopDeskCategoryMapper.selectById(id) == null) {
            throw exception(SHOP_DESK_CATEGORY_NOT_EXISTS);
        }
    }

    @Override
    public ShopDeskCategoryDO getShopDeskCategory(Long id) {
        return shopDeskCategoryMapper.selectById(id);
    }

    @Override
    public PageResult<ShopDeskCategoryDO> getShopDeskCategoryPage(ShopDeskCategoryPageReqVO pageReqVO) {
        return shopDeskCategoryMapper.selectPage(pageReqVO);
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