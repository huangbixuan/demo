package co.yixiang.yshop.module.score.service.scoreproductcategory;

import java.util.*;

import co.yixiang.yshop.module.score.controller.app.product.vo.AppScoreProductCategoryRespVO;
import jakarta.validation.*;
import co.yixiang.yshop.module.score.controller.admin.scoreproductcategory.vo.*;
import co.yixiang.yshop.module.score.dal.dataobject.scoreproductcategory.ScoreProductCategoryDO;
import co.yixiang.yshop.framework.common.pojo.PageResult;
import co.yixiang.yshop.framework.common.pojo.PageParam;

/**
 * 积分商品分类 Service 接口
 *
 * @author yshop
 */
public interface ScoreProductCategoryService {


    List<AppScoreProductCategoryRespVO> getCateList();

    /**
     * 创建积分商品分类
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createProductCategory(@Valid ScoreProductCategorySaveReqVO createReqVO);

    /**
     * 更新积分商品分类
     *
     * @param updateReqVO 更新信息
     */
    void updateProductCategory(@Valid ScoreProductCategorySaveReqVO updateReqVO);

    /**
     * 删除积分商品分类
     *
     * @param id 编号
     */
    void deleteProductCategory(Long id);

    /**
     * 获得积分商品分类
     *
     * @param id 编号
     * @return 积分商品分类
     */
    ScoreProductCategoryDO getProductCategory(Long id);

    /**
     * 获得积分商品分类分页
     *
     * @param pageReqVO 分页查询
     * @return 积分商品分类分页
     */
    PageResult<ScoreProductCategoryDO> getProductCategoryPage(ScoreProductCategoryPageReqVO pageReqVO);

}