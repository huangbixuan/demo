package co.yixiang.yshop.module.desk.convert.shopdesk;

import java.util.*;

import co.yixiang.yshop.framework.common.pojo.PageResult;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import co.yixiang.yshop.module.desk.controller.admin.shopdesk.vo.*;
import co.yixiang.yshop.module.desk.dal.dataobject.shopdesk.ShopDeskDO;

/**
 * 门店 - 桌号 Convert
 *
 * @author yshop
 */
@Mapper
public interface ShopDeskConvert {

    ShopDeskConvert INSTANCE = Mappers.getMapper(ShopDeskConvert.class);

    ShopDeskDO convert(ShopDeskCreateReqVO bean);

    ShopDeskDO convert(ShopDeskUpdateReqVO bean);

    ShopDeskRespVO convert(ShopDeskDO bean);

    List<ShopDeskRespVO> convertList(List<ShopDeskDO> list);

    PageResult<ShopDeskRespVO> convertPage(PageResult<ShopDeskDO> page);

    List<ShopDeskExcelVO> convertList02(List<ShopDeskDO> list);

}
