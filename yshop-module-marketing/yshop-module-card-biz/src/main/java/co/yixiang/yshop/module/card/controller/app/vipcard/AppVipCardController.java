/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package co.yixiang.yshop.module.card.controller.app.vipcard;

import co.yixiang.yshop.framework.common.enums.ShopCommonEnum;
import co.yixiang.yshop.framework.common.pojo.CommonResult;
import co.yixiang.yshop.module.card.controller.app.vipcard.vo.AppVipCardVO;
import co.yixiang.yshop.module.card.convert.vipcard.VipCardConvert;
import co.yixiang.yshop.module.card.dal.dataobject.vipcard.VipCardDO;
import co.yixiang.yshop.module.card.service.vipcard.AppVipCardService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.Resource;
import java.util.List;

import static co.yixiang.yshop.framework.common.pojo.CommonResult.success;

/**
 * <p>
 * 会员卡控制器
 * </p>
 *
 * @author hupeng
 * @since 2024-1-7
 */
@Slf4j
@RestController
@Tag(name = "用户 APP - 会员卡")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/vip")
public class AppVipCardController {

    @Resource
    private AppVipCardService appVipCardService;


    @GetMapping("/getList")
    @Operation(summary = "获取会员卡")
    public CommonResult<List<AppVipCardVO>> getList() {
        List<VipCardDO> list = appVipCardService.list(new LambdaQueryWrapper<VipCardDO>()
                .eq(VipCardDO::getStatus, ShopCommonEnum.DEFAULT_0.getValue())
                .orderByDesc(VipCardDO::getSort));
        return success(VipCardConvert.INSTANCE.convertList01(list));
    }

    @GetMapping("/getDetail")
    @Operation(summary = "获取会员卡详情")
    public CommonResult<AppVipCardVO> getDetail(@RequestParam(value = "id")  Long id) {
        VipCardDO vipCardDO = appVipCardService.getById(id);
        return success(VipCardConvert.INSTANCE.convert01(vipCardDO));
    }


}

