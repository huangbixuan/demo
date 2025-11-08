package co.yixiang.yshop.module.score.service.scoreproductcategory;

import co.yixiang.yshop.framework.common.enums.CommonStatusEnum;
import co.yixiang.yshop.framework.mybatis.core.query.LambdaQueryWrapperX;
import co.yixiang.yshop.module.score.controller.app.product.vo.AppScoreProductCategoryRespVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import co.yixiang.yshop.module.score.controller.admin.scoreproductcategory.vo.*;
import co.yixiang.yshop.module.score.dal.dataobject.scoreproductcategory.ScoreProductCategoryDO;
import co.yixiang.yshop.framework.common.pojo.PageResult;
import co.yixiang.yshop.framework.common.util.object.BeanUtils;

import co.yixiang.yshop.module.score.dal.mysql.scoreproductcategory.ScoreProductCategoryNewMapper;

import java.util.List;

import static co.yixiang.yshop.framework.common.exception.util.ServiceExceptionUtil.exception;
import static co.yixiang.yshop.module.score.enums.ErrorCodeConstants.*;

/**
 * 积分商品分类 Service 实现类
 *
 * @author yshop
 */
@Service
@Validated
public class ScoreProductCategoryServiceImpl implements ScoreProductCategoryService {

    @Resource
    private ScoreProductCategoryNewMapper scoreProductCategoryNewMapper;


    @Override
    public List<AppScoreProductCategoryRespVO> getCateList() {
        List<ScoreProductCategoryDO> scoreProductCategoryDOS = scoreProductCategoryNewMapper
                .selectList(new LambdaQueryWrapper<ScoreProductCategoryDO>()
                .eq(ScoreProductCategoryDO::getStatus, CommonStatusEnum.ENABLE.getStatus())
                .orderByDesc(ScoreProductCategoryDO::getSort));
        return BeanUtils.toBean(scoreProductCategoryDOS,AppScoreProductCategoryRespVO.class);
    }

    @Override
    public Long createProductCategory(ScoreProductCategorySaveReqVO createReqVO) {
        // 插入
        ScoreProductCategoryDO productCategory = BeanUtils.toBean(createReqVO, ScoreProductCategoryDO.class);
        scoreProductCategoryNewMapper.insert(productCategory);
        // 返回
        return productCategory.getId();
    }

    @Override
    public void updateProductCategory(ScoreProductCategorySaveReqVO updateReqVO) {
        // 校验存在
        validateProductCategoryExists(updateReqVO.getId());
        // 更新
        ScoreProductCategoryDO updateObj = BeanUtils.toBean(updateReqVO, ScoreProductCategoryDO.class);
        scoreProductCategoryNewMapper.updateById(updateObj);
    }

    @Override
    public void deleteProductCategory(Long id) {
        // 校验存在
        validateProductCategoryExists(id);
        // 删除
        scoreProductCategoryNewMapper.deleteById(id);
    }

    private void validateProductCategoryExists(Long id) {
        if (scoreProductCategoryNewMapper.selectById(id) == null) {
            throw exception(PRODUCT_CATEGORY_NOT_EXISTS);
        }
    }

    @Override
    public ScoreProductCategoryDO getProductCategory(Long id) {
        return scoreProductCategoryNewMapper.selectById(id);
    }

    @Override
    public PageResult<ScoreProductCategoryDO> getProductCategoryPage(ScoreProductCategoryPageReqVO pageReqVO) {
        return scoreProductCategoryNewMapper.selectPage(pageReqVO);
    }

}