package co.yixiang.yshop.module.score.service.scoreproduct;

import co.yixiang.yshop.framework.common.util.object.BeanUtils;
import co.yixiang.yshop.module.score.dal.dataobject.scoreproductcategory.ScoreProductCategoryDO;
import co.yixiang.yshop.module.score.dal.mysql.scoreproductcategory.ScoreProductCategoryNewMapper;
import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import co.yixiang.yshop.module.score.controller.admin.scoreproduct.vo.*;
import co.yixiang.yshop.module.score.dal.dataobject.scoreproduct.ScoreProductDO;
import co.yixiang.yshop.framework.common.pojo.PageResult;

import co.yixiang.yshop.module.score.convert.scoreproduct.ScoreProductConvert;
import co.yixiang.yshop.module.score.dal.mysql.scoreproduct.ScoreProductMapper;

import static co.yixiang.yshop.framework.common.exception.util.ServiceExceptionUtil.exception;
import static co.yixiang.yshop.module.score.enums.ErrorCodeConstants.*;

/**
 * 积分产品 Service 实现类
 *
 * @author yshop
 */
@Service
@Validated
public class ScoreProductServiceImpl implements ScoreProductService {

    @Resource
    private ScoreProductMapper productMapper;
    @Resource
    private ScoreProductCategoryNewMapper scoreProductCategoryNewMapper;

    @Override
    public Long createProduct(ScoreProductCreateReqVO createReqVO) {
        // 插入
        ScoreProductDO product = BeanUtils.toBean(createReqVO,ScoreProductDO.class);
        productMapper.insert(product);
        // 返回
        return product.getId();
    }

    @Override
    public void updateProduct(ScoreProductUpdateReqVO updateReqVO) {
        // 校验存在
        validateProductExists(updateReqVO.getId());
        // 更新
        ScoreProductDO updateObj = BeanUtils.toBean(updateReqVO,ScoreProductDO.class);
        productMapper.updateById(updateObj);
    }

    @Override
    public void deleteProduct(Long id) {
        // 校验存在
        validateProductExists(id);
        // 删除
        productMapper.deleteById(id);
    }

    private void validateProductExists(Long id) {
        if (productMapper.selectById(id) == null) {
            throw exception(PRODUCT_NOT_EXISTS);
        }
    }

    @Override
    public ScoreProductDO getProduct(Long id) {
        return productMapper.selectById(id);
    }

    @Override
    public List<ScoreProductDO> getProductList(Collection<Long> ids) {
        return productMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<ScoreProductRespVO> getProductPage(ScoreProductPageReqVO pageReqVO) {
        PageResult<ScoreProductDO> pageResult = productMapper.selectPage(pageReqVO);
        PageResult<ScoreProductRespVO> pageResult1 = BeanUtils.toBean(pageResult,ScoreProductRespVO.class);
        pageResult1.getList().forEach(v -> {
           ScoreProductCategoryDO productCategoryDO = scoreProductCategoryNewMapper.selectById(v.getCateId());
            v.setCateName(productCategoryDO.getName());
        });
        return pageResult1;
    }

    @Override
    public List<ScoreProductDO> getProductList(ScoreProductExportReqVO exportReqVO) {
        return productMapper.selectList(exportReqVO);
    }

}
