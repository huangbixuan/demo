package co.yixiang.yshop.module.desk.service.shopdesk;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import jakarta.annotation.Resource;

import co.yixiang.yshop.framework.test.core.ut.BaseDbUnitTest;

import co.yixiang.yshop.module.desk.controller.admin.shopdesk.vo.*;
import co.yixiang.yshop.module.desk.dal.dataobject.shopdesk.ShopDeskDO;
import co.yixiang.yshop.module.desk.dal.mysql.shopdesk.ShopDeskMapper;
import co.yixiang.yshop.framework.common.pojo.PageResult;

import jakarta.annotation.Resource;
import org.springframework.context.annotation.Import;
import java.util.*;
import java.time.LocalDateTime;

import static cn.hutool.core.util.RandomUtil.*;
import static co.yixiang.yshop.module.desk.enums.ErrorCodeConstants.*;
import static co.yixiang.yshop.framework.test.core.util.AssertUtils.*;
import static co.yixiang.yshop.framework.test.core.util.RandomUtils.*;
import static co.yixiang.yshop.framework.common.util.date.LocalDateTimeUtils.*;
import static co.yixiang.yshop.framework.common.util.object.ObjectUtils.*;
import static co.yixiang.yshop.framework.common.util.date.DateUtils.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
* {@link ShopDeskServiceImpl} 的单元测试类
*
* @author yshop
*/
@Import(ShopDeskServiceImpl.class)
public class ShopDeskServiceImplTest extends BaseDbUnitTest {

    @Resource
    private ShopDeskServiceImpl shopDeskService;

    @Resource
    private ShopDeskMapper shopDeskMapper;

    @Test
    public void testCreateShopDesk_success() {
        // 准备参数
        ShopDeskCreateReqVO reqVO = randomPojo(ShopDeskCreateReqVO.class);

        // 调用
        Long shopDeskId = shopDeskService.createShopDesk(reqVO);
        // 断言
        assertNotNull(shopDeskId);
        // 校验记录的属性是否正确
        ShopDeskDO shopDesk = shopDeskMapper.selectById(shopDeskId);
        assertPojoEquals(reqVO, shopDesk);
    }

    @Test
    public void testUpdateShopDesk_success() {
        // mock 数据
        ShopDeskDO dbShopDesk = randomPojo(ShopDeskDO.class);
        shopDeskMapper.insert(dbShopDesk);// @Sql: 先插入出一条存在的数据
        // 准备参数
        ShopDeskUpdateReqVO reqVO = randomPojo(ShopDeskUpdateReqVO.class, o -> {
            o.setId(dbShopDesk.getId()); // 设置更新的 ID
        });

        // 调用
        shopDeskService.updateShopDesk(reqVO);
        // 校验是否更新正确
        ShopDeskDO shopDesk = shopDeskMapper.selectById(reqVO.getId()); // 获取最新的
        assertPojoEquals(reqVO, shopDesk);
    }

    @Test
    public void testUpdateShopDesk_notExists() {
        // 准备参数
        ShopDeskUpdateReqVO reqVO = randomPojo(ShopDeskUpdateReqVO.class);

        // 调用, 并断言异常
        assertServiceException(() -> shopDeskService.updateShopDesk(reqVO), SHOP_DESK_NOT_EXISTS);
    }

    @Test
    public void testDeleteShopDesk_success() {
        // mock 数据
        ShopDeskDO dbShopDesk = randomPojo(ShopDeskDO.class);
        shopDeskMapper.insert(dbShopDesk);// @Sql: 先插入出一条存在的数据
        // 准备参数
        Long id = dbShopDesk.getId();

        // 调用
        shopDeskService.deleteShopDesk(id);
       // 校验数据不存在了
       assertNull(shopDeskMapper.selectById(id));
    }

    @Test
    public void testDeleteShopDesk_notExists() {
        // 准备参数
        Long id = randomLongId();

        // 调用, 并断言异常
        assertServiceException(() -> shopDeskService.deleteShopDesk(id), SHOP_DESK_NOT_EXISTS);
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetShopDeskPage() {
       // mock 数据
       ShopDeskDO dbShopDesk = randomPojo(ShopDeskDO.class, o -> { // 等会查询到
           o.setShopId(null);
           o.setShopName(null);
           o.setNumber(null);
           o.setMiniQrcode(null);
           o.setH5Qrcode(null);
           o.setAliQrcode(null);
           o.setNote(null);
           o.setOrderCount(null);
           o.setCostAmount(null);
           o.setLastOrderNo(null);
           o.setLastOrderTime(null);
           o.setLastOrderStatus(null);
           o.setStatus(null);
           o.setCreateTime(null);
       });
       shopDeskMapper.insert(dbShopDesk);
       // 测试 shopId 不匹配
       shopDeskMapper.insert(cloneIgnoreId(dbShopDesk, o -> o.setShopId(null)));
       // 测试 shopName 不匹配
       shopDeskMapper.insert(cloneIgnoreId(dbShopDesk, o -> o.setShopName(null)));
       // 测试 number 不匹配
       shopDeskMapper.insert(cloneIgnoreId(dbShopDesk, o -> o.setNumber(null)));
       // 测试 miniQrcode 不匹配
       shopDeskMapper.insert(cloneIgnoreId(dbShopDesk, o -> o.setMiniQrcode(null)));
       // 测试 h5Qrcode 不匹配
       shopDeskMapper.insert(cloneIgnoreId(dbShopDesk, o -> o.setH5Qrcode(null)));
       // 测试 aliQrcode 不匹配
       shopDeskMapper.insert(cloneIgnoreId(dbShopDesk, o -> o.setAliQrcode(null)));
       // 测试 note 不匹配
       shopDeskMapper.insert(cloneIgnoreId(dbShopDesk, o -> o.setNote(null)));
       // 测试 orderCount 不匹配
       shopDeskMapper.insert(cloneIgnoreId(dbShopDesk, o -> o.setOrderCount(null)));
       // 测试 costAmount 不匹配
       shopDeskMapper.insert(cloneIgnoreId(dbShopDesk, o -> o.setCostAmount(null)));
       // 测试 lastOrderNo 不匹配
       shopDeskMapper.insert(cloneIgnoreId(dbShopDesk, o -> o.setLastOrderNo(null)));
       // 测试 lastOrderTime 不匹配
       shopDeskMapper.insert(cloneIgnoreId(dbShopDesk, o -> o.setLastOrderTime(null)));
       // 测试 lastOrderStatus 不匹配
       shopDeskMapper.insert(cloneIgnoreId(dbShopDesk, o -> o.setLastOrderStatus(null)));
       // 测试 status 不匹配
       shopDeskMapper.insert(cloneIgnoreId(dbShopDesk, o -> o.setStatus(null)));
       // 测试 createTime 不匹配
       shopDeskMapper.insert(cloneIgnoreId(dbShopDesk, o -> o.setCreateTime(null)));
       // 准备参数
       ShopDeskPageReqVO reqVO = new ShopDeskPageReqVO();
       reqVO.setShopId(null);
       reqVO.setShopName(null);
       reqVO.setNumber(null);
       reqVO.setMiniQrcode(null);
       reqVO.setH5Qrcode(null);
       reqVO.setAliQrcode(null);
       reqVO.setNote(null);
       reqVO.setOrderCount(null);
       reqVO.setCostAmount(null);
       reqVO.setLastOrderNo(null);
       reqVO.setLastOrderTime(buildBetweenTime(2023, 2, 1, 2023, 2, 28));
       reqVO.setLastOrderStatus(null);
       reqVO.setStatus(null);
       reqVO.setCreateTime(buildBetweenTime(2023, 2, 1, 2023, 2, 28));

       // 调用
       PageResult<ShopDeskDO> pageResult = shopDeskService.getShopDeskPage(reqVO);
       // 断言
       assertEquals(1, pageResult.getTotal());
       assertEquals(1, pageResult.getList().size());
       assertPojoEquals(dbShopDesk, pageResult.getList().get(0));
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetShopDeskList() {
       // mock 数据
       ShopDeskDO dbShopDesk = randomPojo(ShopDeskDO.class, o -> { // 等会查询到
           o.setShopId(null);
           o.setShopName(null);
           o.setNumber(null);
           o.setMiniQrcode(null);
           o.setH5Qrcode(null);
           o.setAliQrcode(null);
           o.setNote(null);
           o.setOrderCount(null);
           o.setCostAmount(null);
           o.setLastOrderNo(null);
           o.setLastOrderTime(null);
           o.setLastOrderStatus(null);
           o.setStatus(null);
           o.setCreateTime(null);
       });
       shopDeskMapper.insert(dbShopDesk);
       // 测试 shopId 不匹配
       shopDeskMapper.insert(cloneIgnoreId(dbShopDesk, o -> o.setShopId(null)));
       // 测试 shopName 不匹配
       shopDeskMapper.insert(cloneIgnoreId(dbShopDesk, o -> o.setShopName(null)));
       // 测试 number 不匹配
       shopDeskMapper.insert(cloneIgnoreId(dbShopDesk, o -> o.setNumber(null)));
       // 测试 miniQrcode 不匹配
       shopDeskMapper.insert(cloneIgnoreId(dbShopDesk, o -> o.setMiniQrcode(null)));
       // 测试 h5Qrcode 不匹配
       shopDeskMapper.insert(cloneIgnoreId(dbShopDesk, o -> o.setH5Qrcode(null)));
       // 测试 aliQrcode 不匹配
       shopDeskMapper.insert(cloneIgnoreId(dbShopDesk, o -> o.setAliQrcode(null)));
       // 测试 note 不匹配
       shopDeskMapper.insert(cloneIgnoreId(dbShopDesk, o -> o.setNote(null)));
       // 测试 orderCount 不匹配
       shopDeskMapper.insert(cloneIgnoreId(dbShopDesk, o -> o.setOrderCount(null)));
       // 测试 costAmount 不匹配
       shopDeskMapper.insert(cloneIgnoreId(dbShopDesk, o -> o.setCostAmount(null)));
       // 测试 lastOrderNo 不匹配
       shopDeskMapper.insert(cloneIgnoreId(dbShopDesk, o -> o.setLastOrderNo(null)));
       // 测试 lastOrderTime 不匹配
       shopDeskMapper.insert(cloneIgnoreId(dbShopDesk, o -> o.setLastOrderTime(null)));
       // 测试 lastOrderStatus 不匹配
       shopDeskMapper.insert(cloneIgnoreId(dbShopDesk, o -> o.setLastOrderStatus(null)));
       // 测试 status 不匹配
       shopDeskMapper.insert(cloneIgnoreId(dbShopDesk, o -> o.setStatus(null)));
       // 测试 createTime 不匹配
       shopDeskMapper.insert(cloneIgnoreId(dbShopDesk, o -> o.setCreateTime(null)));
       // 准备参数
       ShopDeskExportReqVO reqVO = new ShopDeskExportReqVO();
       reqVO.setShopId(null);
       reqVO.setShopName(null);
       reqVO.setNumber(null);
       reqVO.setMiniQrcode(null);
       reqVO.setH5Qrcode(null);
       reqVO.setAliQrcode(null);
       reqVO.setNote(null);
       reqVO.setOrderCount(null);
       reqVO.setCostAmount(null);
       reqVO.setLastOrderNo(null);
       reqVO.setLastOrderTime(buildBetweenTime(2023, 2, 1, 2023, 2, 28));
       reqVO.setLastOrderStatus(null);
       reqVO.setStatus(null);
       reqVO.setCreateTime(buildBetweenTime(2023, 2, 1, 2023, 2, 28));

       // 调用
       List<ShopDeskDO> list = shopDeskService.getShopDeskList(reqVO);
       // 断言
       assertEquals(1, list.size());
       assertPojoEquals(dbShopDesk, list.get(0));
    }

}
