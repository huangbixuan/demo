package co.yixiang.yshop.module.desk.service.shopdeskcategory;

import java.util.*;
import jakarta.validation.*;
import co.yixiang.yshop.module.desk.controller.admin.shopdeskcategory.vo.*;
import co.yixiang.yshop.module.desk.dal.dataobject.shopdeskcategory.ShopDeskCategoryDO;
import co.yixiang.yshop.framework.common.pojo.PageResult;
import co.yixiang.yshop.framework.common.pojo.PageParam;

/**
 * 门店桌号分类 Service 接口
 *
 * @author yshop
 */
public interface ShopDeskCategoryService {

    /**
     * 创建门店桌号分类
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createShopDeskCategory(@Valid ShopDeskCategorySaveReqVO createReqVO);

    /**
     * 更新门店桌号分类
     *
     * @param updateReqVO 更新信息
     */
    void updateShopDeskCategory(@Valid ShopDeskCategorySaveReqVO updateReqVO);

    /**
     * 删除门店桌号分类
     *
     * @param id 编号
     */
    void deleteShopDeskCategory(Long id);

    /**
     * 获得门店桌号分类
     *
     * @param id 编号
     * @return 门店桌号分类
     */
    ShopDeskCategoryDO getShopDeskCategory(Long id);

    /**
     * 获得门店桌号分类分页
     *
     * @param pageReqVO 分页查询
     * @return 门店桌号分类分页
     */
    PageResult<ShopDeskCategoryDO> getShopDeskCategoryPage(ShopDeskCategoryPageReqVO pageReqVO);

}