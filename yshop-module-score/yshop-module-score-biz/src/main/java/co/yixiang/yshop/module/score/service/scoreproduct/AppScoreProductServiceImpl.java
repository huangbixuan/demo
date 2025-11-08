package co.yixiang.yshop.module.score.service.scoreproduct;

import co.yixiang.yshop.framework.common.util.object.BeanUtils;
import co.yixiang.yshop.module.score.controller.app.product.vo.AppScoreProductVO;
import co.yixiang.yshop.module.score.convert.scoreproduct.ScoreProductConvert;
import co.yixiang.yshop.module.score.dal.dataobject.scoreproduct.ScoreProductDO;
import co.yixiang.yshop.module.score.dal.mysql.scoreproduct.ScoreProductMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import jakarta.annotation.Resource;
import java.util.List;

/**
 * 积分产品 Service 实现类
 *
 * @author yshop
 */
@Service
@Validated
public class AppScoreProductServiceImpl  extends ServiceImpl<ScoreProductMapper,ScoreProductDO> implements AppScoreProductService {

    @Resource
    private ScoreProductMapper productMapper;


    /**
     * 订单列表
     * @param page page
     * @param limit limit
     * @return list
     */
    @Override
    public List<AppScoreProductVO> getList(int page, int limit,Long cateId) {
        LambdaQueryWrapper<ScoreProductDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(ScoreProductDO::getWeigh);

        if(cateId != null && cateId > 0){
            wrapper.eq(ScoreProductDO::getCateId,cateId);
        }

        Page<ScoreProductDO> pageModel = new Page<>(page, limit);
        IPage<ScoreProductDO> pageList = productMapper.selectPage(pageModel, wrapper);
        List<AppScoreProductVO> list = BeanUtils.toBean(pageList.getRecords(),AppScoreProductVO.class);

        return list;
    }


    /**
     * 详情
     * @param id 产品ID
     * @return list
     */
    @Override
    public AppScoreProductVO getDetail(Long id) {
        ScoreProductDO scoreProductDO = this.getById(id);
        return BeanUtils.toBean(scoreProductDO,AppScoreProductVO.class);
    }
}
