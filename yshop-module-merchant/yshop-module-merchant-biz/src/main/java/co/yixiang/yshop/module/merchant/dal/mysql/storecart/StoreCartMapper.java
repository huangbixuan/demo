package co.yixiang.yshop.module.merchant.dal.mysql.storecart;

import co.yixiang.yshop.framework.common.enums.ShopCommonEnum;
import co.yixiang.yshop.framework.mybatis.core.mapper.BaseMapperX;
import co.yixiang.yshop.module.member.controller.admin.storeuser.vo.StoreUserRespVO;
import co.yixiang.yshop.module.member.dal.dataobject.storeuser.StoreUserDO;
import co.yixiang.yshop.module.member.dal.dataobject.user.MemberUserDO;
import co.yixiang.yshop.module.merchant.controller.admin.cashier.vo.HangOrderRespVO;
import co.yixiang.yshop.module.merchant.controller.admin.cashier.vo.StoreCartRespVO;
import co.yixiang.yshop.module.merchant.dal.dataobject.storecart.StoreCartDO;
import co.yixiang.yshop.module.product.dal.dataobject.storeproduct.StoreProductDO;
import co.yixiang.yshop.module.product.dal.dataobject.storeproductattrvalue.StoreProductAttrValueDO;
import co.yixiang.yshop.module.store.dal.dataobject.storeshop.StoreShopDO;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 购物车 Mapper
 *
 * @author yshop
 */
@Mapper
public interface StoreCartMapper extends BaseMapperX<StoreCartDO> {
    default List<StoreCartRespVO> selectList(Long shopId) {
        return selectJoinList(StoreCartRespVO.class,new MPJLambdaWrapper<StoreCartDO>()
                .selectAll(StoreCartDO.class)
                .select(StoreProductDO::getStoreName)
                .select(StoreProductAttrValueDO::getSku,StoreProductAttrValueDO::getImage,StoreProductAttrValueDO::getPrice)
                .leftJoin(StoreProductDO.class,StoreProductDO::getId,StoreCartDO::getProductId)
                .leftJoin(StoreProductAttrValueDO.class,StoreProductAttrValueDO::getUnique,StoreCartDO::getProductAttrUnique)
                .eq(StoreCartDO::getShopId,shopId).eq(StoreCartDO::getIsHang, ShopCommonEnum.IS_STATUS_0.getValue()));
    }

    @Select("select hang_no as hangNo from yshop_store_cart " +
            "where is_hang=1 and deleted=0 and shop_id = #{shopId} group by hangNo order by hangNo")
    List<HangOrderRespVO> getHangList(@Param("shopId") Long shopId);

}