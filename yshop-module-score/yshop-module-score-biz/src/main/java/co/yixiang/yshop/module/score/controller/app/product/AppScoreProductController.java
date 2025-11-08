package co.yixiang.yshop.module.score.controller.app.product;

import co.yixiang.yshop.framework.common.pojo.CommonResult;
import co.yixiang.yshop.module.score.controller.app.product.param.AppScoreProductQueryParam;
import co.yixiang.yshop.module.score.controller.app.product.vo.AppScoreAdsRespVO;
import co.yixiang.yshop.module.score.controller.app.product.vo.AppScoreProductCategoryRespVO;
import co.yixiang.yshop.module.score.controller.app.product.vo.AppScoreProductVO;
import co.yixiang.yshop.module.score.service.scoreads.ScoreAdsService;
import co.yixiang.yshop.module.score.service.scoreproduct.AppScoreProductService;
import co.yixiang.yshop.module.score.service.scoreproductcategory.ScoreProductCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static co.yixiang.yshop.framework.common.pojo.CommonResult.success;

@Tag(name = "用户 APP - 积分商品")
@RestController
@RequestMapping("/score-product")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AppScoreProductController {

    private final AppScoreProductService appScoreProductService;
    private final ScoreAdsService scoreAdsService;
    private final ScoreProductCategoryService scoreProductCategoryService;


    /**
     * 获取积分广告列表
     */
    @GetMapping("/getAds")
    @Operation(summary = "获取积分广告列表")
    public CommonResult<List<AppScoreAdsRespVO>> adsList(){
        return success(scoreAdsService.getAds());
    }

    /**
     * 获取积分分类列表
     */
    @GetMapping("/getCate")
    @Operation(summary = "获取积分分类列表")
    public CommonResult<List<AppScoreProductCategoryRespVO>> cateList(){
        return success(scoreProductCategoryService.getCateList());
    }

    /**
     * 获取积分商品列表
     */
    @GetMapping("/list")
    @Operation(summary = "获取积分商品列表")
    public CommonResult<List<AppScoreProductVO>> goodsList(AppScoreProductQueryParam productQueryParam){
        return success(appScoreProductService
                .getList(productQueryParam.getPage(),productQueryParam.getLimit(),productQueryParam.getCateId()));
    }

    /**
     * 获取积分商品详情
     */
    @GetMapping("/detail")
    @Operation(summary = "获取积分商品详情")
    public CommonResult<AppScoreProductVO> goodsDetail(@RequestParam(value = "id", defaultValue = "0") Long id){
        return success(appScoreProductService.getDetail(id));
    }

}
