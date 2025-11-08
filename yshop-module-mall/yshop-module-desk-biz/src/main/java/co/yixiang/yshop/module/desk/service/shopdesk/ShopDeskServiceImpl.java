package co.yixiang.yshop.module.desk.service.shopdesk;

import cn.binarywang.wx.miniapp.api.WxMaQrcodeService;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import co.yixiang.yshop.framework.common.constant.ShopConstants;
import co.yixiang.yshop.framework.datapermission.core.util.DataPermissionUtils;
import co.yixiang.yshop.module.desk.dal.dataobject.qrcode.QrcodeDO;
import co.yixiang.yshop.module.desk.dal.dataobject.shopdeskcategory.ShopDeskCategoryDO;
import co.yixiang.yshop.module.desk.dal.mysql.shopdeskcategory.ShopDeskCategoryMapper;
import co.yixiang.yshop.module.desk.enums.MiniQrcodeEnvEnum;
import co.yixiang.yshop.module.desk.service.qrcode.QrcodeService;
import co.yixiang.yshop.module.store.dal.dataobject.storeshop.StoreShopDO;
import co.yixiang.yshop.module.store.dal.mysql.storeshop.StoreShopMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import co.yixiang.yshop.module.desk.controller.admin.shopdesk.vo.*;
import co.yixiang.yshop.module.desk.dal.dataobject.shopdesk.ShopDeskDO;
import co.yixiang.yshop.framework.common.pojo.PageResult;

import co.yixiang.yshop.module.desk.convert.shopdesk.ShopDeskConvert;
import co.yixiang.yshop.module.desk.dal.mysql.shopdesk.ShopDeskMapper;

import static co.yixiang.yshop.framework.common.exception.util.ServiceExceptionUtil.exception;
import static co.yixiang.yshop.module.desk.enums.ErrorCodeConstants.*;

/**
 * 门店 - 桌号 Service 实现类
 *
 * @author yshop
 */
@Service
@Validated
public class ShopDeskServiceImpl implements ShopDeskService {

    @Resource
    private ShopDeskMapper shopDeskMapper;
    @Resource
    private StoreShopMapper shopMapper;
    @Resource
    private QrcodeService qrcodeService;
    @Resource
    private ShopDeskCategoryMapper shopDeskCategoryMapper;


    @Override
    public Long createShopDesk(ShopDeskCreateReqVO createReqVO) {
        // 插入
        ShopDeskDO shopDesk = ShopDeskConvert.INSTANCE.convert(createReqVO);
        StoreShopDO storeShopDO = this.getShop(createReqVO.getShopId());
        shopDesk.setShopName(storeShopDO.getName());
        shopDesk.setCateName(getCate(createReqVO.getCateId()));
        shopDeskMapper.insert(shopDesk);
        // 返回
        return shopDesk.getId();
    }

    /**
     * 批量添加
     * @param createReqVO
     */
    @Override
    public void batchAdd(ShopDeskCreateBatchVO createReqVO) {
        StoreShopDO storeShopDO = this.getShop(createReqVO.getShopId());
        if(!NumberUtil.isNumber(createReqVO.getStartNumber()) || !NumberUtil.isNumber(createReqVO.getEndNumber())){
            throw exception(NOT_VALID_NUMBER);
        }
        int start = Integer.parseInt(createReqVO.getStartNumber());
        int end = Integer.parseInt(createReqVO.getEndNumber());
        if(NumberUtil.compare(start, end) >= 0){
            throw exception(DESK_VALID_NUMBER);
        }
        for(int i = start; i <= end ; i++){
            // 判断如果不存在，在进行插入
            String number = createReqVO.getNumberPre() + i;
            ShopDeskDO existShopDesk = shopDeskMapper.selectOne(new LambdaQueryWrapper<ShopDeskDO>()
                    .eq(ShopDeskDO::getShopId,storeShopDO.getId()).eq(ShopDeskDO::getNumber,number));
            //存在过滤
            if(existShopDesk != null) {
                continue;
            }

            ShopDeskDO shopDeskDO = ShopDeskDO.builder()
                    .number(number)
                    .shopId(storeShopDO.getId())
                    .shopName(storeShopDO.getName())
                    .note(createReqVO.getNote())
                    .status(createReqVO.getStatus())
                    .build();

            shopDeskMapper.insert(shopDeskDO);
        }
    }

    @Override
    public void updateShopDesk(ShopDeskUpdateReqVO updateReqVO) {
        // 校验存在
        validateShopDeskExists(updateReqVO.getId());
        // 更新
        ShopDeskDO updateObj = ShopDeskConvert.INSTANCE.convert(updateReqVO);
        StoreShopDO storeShopDO = this.getShop(updateObj.getShopId());
        updateObj.setShopName(storeShopDO.getName());
        updateObj.setCateName(getCate(updateReqVO.getCateId()));
        shopDeskMapper.updateById(updateObj);
    }

    @Override
    public void deleteShopDesk(Long id) {
        // 校验存在
        validateShopDeskExists(id);
        // 删除
        shopDeskMapper.deleteById(id);
    }

    private void validateShopDeskExists(Long id) {
        if (shopDeskMapper.selectById(id) == null) {
            throw exception(SHOP_DESK_NOT_EXISTS);
        }
    }

    @Override
    public ShopDeskDO getShopDesk(Long id) {
        return shopDeskMapper.selectById(id);
    }

    @Override
    public List<ShopDeskDO> getShopDeskList(Collection<Long> ids) {
        return shopDeskMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<ShopDeskDO> getShopDeskPage(ShopDeskPageReqVO pageReqVO) {
        return shopDeskMapper.selectPage(pageReqVO);
    }

    @Override
    public List<ShopDeskDO> getShopDeskList(ShopDeskExportReqVO exportReqVO) {
        return shopDeskMapper.selectList(exportReqVO);
    }

    /**
     * 批量导入桌号
     *
     * @param importDesks     导入桌号列表
     * @param isUpdateSupport 是否支持更新
     * @return 导入结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class) // 添加事务，异常则回滚所有导入
    public DeskImportRespVO importShopDeskList(List<ShopDeskTemplateExcelVO> importDesks, boolean isUpdateSupport) {
        if (CollUtil.isEmpty(importDesks)) {
            throw exception(DESK_IMPORT_LIST_IS_EMPTY);
        }
        DeskImportRespVO respVO = DeskImportRespVO.builder().createDeskNames(new ArrayList<>())
                .updateDeskNames(new ArrayList<>()).failureDeskNames(new LinkedHashMap<>()).build();
        AtomicInteger i = new AtomicInteger();
        importDesks.forEach(importDesk -> {
            i.getAndIncrement();
            if(StrUtil.isBlank(importDesk.getShopName()) || StrUtil.isBlank(importDesk.getNumber())){
                respVO.getFailureDeskNames().put("第" + i.get() + "行", "当前行数据门店或者桌号有空值");
                return;
            }
            //查找门店
            StoreShopDO storeShopDO = shopMapper.selectOne(new LambdaQueryWrapper<StoreShopDO>()
                    .eq(StoreShopDO::getName,importDesk.getShopName()));
            if(storeShopDO == null){
                respVO.getFailureDeskNames().put("第" + i.get() + "行", "当前门店" + importDesk.getShopName() + "不存在");
                return;
            }
            // 判断如果不存在，在进行插入
            ShopDeskDO existShopDesk = shopDeskMapper.selectOne(new LambdaQueryWrapper<ShopDeskDO>()
                    .eq(ShopDeskDO::getShopName,importDesk.getNumber()).eq(ShopDeskDO::getNumber,importDesk.getNumber()));
            if (existShopDesk == null) {
                ShopDeskDO data = ShopDeskDO.builder().shopId(storeShopDO.getId()).shopName(storeShopDO.getName())
                        .number(importDesk.getNumber())
                        .note(importDesk.getNote())
                        .build();
                shopDeskMapper.insert(data);
                respVO.getCreateDeskNames().add(importDesk.getShopName() + importDesk.getNumber());
                return;
            }

        });
        return respVO;
    }

    /**
     * 组合二维码
     * @param id
     * @return
     */
    @Override
    public Map<String, String> mapQrcode(Long id) {
        LambdaQueryWrapper<ShopDeskDO> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.orderByDesc(ShopDeskDO::getId);
        if(ObjectUtil.isNotNull(id) && id > 0){
            lambdaQueryWrapper.eq(ShopDeskDO::getId,id);
        }

        List<ShopDeskDO> list = shopDeskMapper.selectList(lambdaQueryWrapper);

        Map<String, String> bindingMap = new HashMap<>();
        for (ShopDeskDO shopDeskDO : list) {
            String keyMini = "qrcode/mini/" + shopDeskDO.getShopName() + shopDeskDO.getNumber() + ".jpg";
            String keyH5 = "qrcode/h5/" + shopDeskDO.getShopName() + shopDeskDO.getNumber() + ".jpg";
            String scene = "id=" + shopDeskDO.getId() + "&number=" + shopDeskDO.getNumber() + "&shopId=" +  shopDeskDO.getShopId();
            QrcodeDO qrcodeDO = qrcodeService.createMiniQrcode(ShopConstants.PAGE_GOOD_HOME,scene,false,
                    MiniQrcodeEnvEnum.RELEASE.getValue());
            QrcodeDO qrcodeDO1 = qrcodeService.createQrcode(ShopConstants.PAGE_GOOD_HOME,scene,false);
            bindingMap.put(keyMini,qrcodeDO.getFullPath());
            bindingMap.put(keyH5,qrcodeDO1.getFullPath());
        }

        return bindingMap;
    }



    /**
     * 获取门店
     * @param id
     * @return
     */
    private StoreShopDO getShop(Long id){
        //查找门店
        StoreShopDO storeShopDO = shopMapper.selectById(id);
        return  storeShopDO;
    }

    private String getCate(Long id){
        if(id == null){
            return "";
        }
        ShopDeskCategoryDO shopDeskCategoryDO =  shopDeskCategoryMapper.selectById(id);
        if(shopDeskCategoryDO == null){
            return "";
        }

        return shopDeskCategoryDO.getName();

    }

}
