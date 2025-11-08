package co.yixiang.yshop.module.store.service.storewithdrawal;

import cn.hutool.core.util.IdUtil;
import co.yixiang.yshop.framework.common.enums.CommonStatusEnum;
import co.yixiang.yshop.framework.common.enums.OrderTypeEnum;
import co.yixiang.yshop.framework.common.enums.ShopCommonEnum;
import co.yixiang.yshop.framework.common.exception.ErrorCode;
import co.yixiang.yshop.framework.common.util.object.BeanUtils;
import co.yixiang.yshop.module.pay.enums.TransferBillStatus;
import co.yixiang.yshop.module.pay.service.dto.TransferBillEntity;
import co.yixiang.yshop.module.pay.service.dto.TransferToUserResponse;
import co.yixiang.yshop.module.pay.service.tramsfer.TransferToUserService;
import co.yixiang.yshop.module.store.controller.admin.userbank.vo.UserBankBaseVO;
import co.yixiang.yshop.module.store.controller.admin.userbank.vo.UserBankRespVO;
import co.yixiang.yshop.module.store.convert.userbank.UserBankConvert;
import co.yixiang.yshop.module.store.dal.dataobject.storerevenue.StoreRevenueDO;
import co.yixiang.yshop.module.store.dal.dataobject.storeshop.StoreShopDO;
import co.yixiang.yshop.module.store.dal.dataobject.userbank.UserBankDO;
import co.yixiang.yshop.module.store.dal.mysql.storerevenue.StoreRevenueMapper;
import co.yixiang.yshop.module.store.dal.mysql.storeshop.StoreShopMapper;
import co.yixiang.yshop.module.store.dal.mysql.userbank.UserBankMapper;
import co.yixiang.yshop.module.store.enums.BankTypeEnum;
import co.yixiang.yshop.module.store.enums.WithdrawalStatusEnum;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.util.*;
import co.yixiang.yshop.module.store.controller.admin.storewithdrawal.vo.*;
import co.yixiang.yshop.module.store.dal.dataobject.storewithdrawal.StoreWithdrawalDO;
import co.yixiang.yshop.framework.common.pojo.PageResult;

import co.yixiang.yshop.module.store.convert.storewithdrawal.StoreWithdrawalConvert;
import co.yixiang.yshop.module.store.dal.mysql.storewithdrawal.StoreWithdrawalMapper;

import static co.yixiang.yshop.framework.common.exception.util.ServiceExceptionUtil.exception;
import static co.yixiang.yshop.module.store.enums.ErrorCodeConstants.*;

/**
 * 提现管理 Service 实现类
 *
 * @author yshop
 */
@Service
@Validated
public class StoreWithdrawalServiceImpl implements StoreWithdrawalService {

    @Resource
    private StoreWithdrawalMapper withdrawalMapper;
    @Resource
    private StoreShopMapper shopMapper;
    @Resource
    private UserBankMapper userBankMapper;
    @Resource
    private StoreRevenueMapper storeRevenueMapper;

    @Resource
    private TransferToUserService transferToUserService;


    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Long createWithdrawal(StoreWithdrawalCreateReqVO createReqVO) {
        // 插入
        StoreWithdrawalDO withdrawal = StoreWithdrawalConvert.INSTANCE.convert(createReqVO);
        StoreShopDO storeShopDO = this.getShop(createReqVO.getShopId());
        this.check(withdrawal,storeShopDO);
        withdrawal.setShopName(storeShopDO.getName());
        String outBillNo = IdUtil.getSnowflake(0, 0).nextIdStr();
        withdrawal.setOutBillNo(outBillNo);
        withdrawalMapper.insert(withdrawal);

        //创建支出流水
        StoreRevenueDO storeRevenueDO = StoreRevenueDO.builder()
                .orderType(OrderTypeEnum.WITH_ORDER.getValue())
                .orderId(withdrawal.getId().toString())
                .amount(withdrawal.getAmount())
                .type(ShopCommonEnum.ADD_2.getValue())
                .shopId(storeShopDO.getId())
                .shopName(storeShopDO.getName())
                .build();

        storeRevenueMapper.insert(storeRevenueDO);
        // 返回
        return withdrawal.getId();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void updateWithdrawal(StoreWithdrawalUpdateReqVO updateReqVO) {
        // 校验存在
        StoreWithdrawalDO storeWithdrawalDO = validateWithdrawalExists(updateReqVO.getId());
        // 更新
        StoreWithdrawalDO updateObj = StoreWithdrawalConvert.INSTANCE.convert(updateReqVO);
        StoreShopDO storeShopDO = this.getShop(updateObj.getShopId());
        if(updateReqVO.getIsCheck()) {
            if(WithdrawalStatusEnum.STATUS_2.getValue().equals(storeWithdrawalDO.getStatus()) ||
                    WithdrawalStatusEnum.STATUS_3.getValue().equals(storeWithdrawalDO.getStatus())){
                throw exception(new ErrorCode(202406120,"审核已经通过或者拒绝不可以再次审核"));
            }
            StoreRevenueDO storeRevenueDO = storeRevenueMapper.selectOne(new LambdaQueryWrapper<StoreRevenueDO>()
                    .eq(StoreRevenueDO::getOrderId,updateObj.getId().toString())
                    .eq(StoreRevenueDO::getOrderType, OrderTypeEnum.WITH_ORDER.getValue()));
            if(storeRevenueDO == null){
                throw exception(new ErrorCode(202509260,"当前提现流水记录不存在"));
            }
            if(WithdrawalStatusEnum.STATUS_2.getValue().equals(updateReqVO.getStatus())){
                //店铺金额加回去
                BigDecimal newBalance = storeShopDO.getBalance().add(updateObj.getAmount());
                storeShopDO.setBalance(newBalance);
                shopMapper.updateById(storeShopDO);
                //删除这个流水记录
                storeRevenueMapper.deleteById(storeRevenueDO);
            }
            if(WithdrawalStatusEnum.STATUS_3.getValue().equals(updateReqVO.getStatus())){
                //增加微信转账功能
                if(BankTypeEnum.TYPE_2.getValue().equals(updateObj.getType())){
                    UserBankDO userBankDO = userBankMapper.selectOne(new LambdaQueryWrapper<UserBankDO>()
                            .eq(UserBankDO::getId,updateReqVO.getBankId()));
                    TransferToUserResponse transferToUserResponse =  transferToUserService
                            .startTransfer(storeWithdrawalDO.getOutBillNo(), storeWithdrawalDO.getAmount(),userBankDO.getWxCode());
                    updateObj.setStatus(WithdrawalStatusEnum.STATUS_3.getValue());
                    updateObj.setPackageInfo(transferToUserResponse.getPackageInfo());
                    updateObj.setState(transferToUserResponse.getState().toString());
                    System.out.println("response:"+transferToUserResponse);
                    //微信转账需要用户确认才能收款成功
                }else{
                    //更新流水状态完成
                    storeRevenueDO.setIsFinish(ShopCommonEnum.IS_FINISH_1.getValue());
                    storeRevenueMapper.updateById(storeRevenueDO);
                }

            }
        }else {
            this.check(updateObj,storeShopDO);
        }

        updateObj.setShopName(storeShopDO.getName());
        withdrawalMapper.updateById(updateObj);
    }

    @Override
    public void deleteWithdrawal(Long id) {
        // 校验存在
        validateWithdrawalExists(id);
        // 删除
        withdrawalMapper.deleteById(id);
    }

    private StoreWithdrawalDO validateWithdrawalExists(Long id) {
        StoreWithdrawalDO storeWithdrawalDO = withdrawalMapper.selectById(id);
        if (storeWithdrawalDO == null) {
            throw exception(WITHDRAWAL_NOT_EXISTS);
        }
        return storeWithdrawalDO;
    }

    @Override
    public StoreWithdrawalRespVO getWithdrawal(Long id) {
        StoreWithdrawalRespVO storeWithdrawalRespVO = StoreWithdrawalConvert.INSTANCE.convert(withdrawalMapper.selectById(id));
        UserBankDO userBankDO = userBankMapper.selectById(storeWithdrawalRespVO.getBankId());
        UserBankRespVO userBankRespVO = UserBankConvert.INSTANCE.convert(userBankDO);
        storeWithdrawalRespVO.setUserBank(userBankRespVO);
        return storeWithdrawalRespVO;
    }

    @Override
    public List<StoreWithdrawalDO> getWithdrawalList(Collection<Long> ids) {
        return withdrawalMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<StoreWithdrawalRespVO> getWithdrawalPage(StoreWithdrawalPageReqVO pageReqVO) {
        PageResult<StoreWithdrawalDO> pageResult =  withdrawalMapper.selectPage(pageReqVO);
        PageResult<StoreWithdrawalRespVO> storeWithdrawalRespVOPageResult =  BeanUtils.toBean(pageResult,StoreWithdrawalRespVO.class);
        for (StoreWithdrawalRespVO storeWithdrawalRespVO : storeWithdrawalRespVOPageResult.getList()) {
            UserBankDO userBankDO = userBankMapper.selectById(storeWithdrawalRespVO.getBankId());
            UserBankRespVO userBankRespVO = UserBankConvert.INSTANCE.convert(userBankDO);
            storeWithdrawalRespVO.setUserBank(userBankRespVO);
        }
        return storeWithdrawalRespVOPageResult;
    }

    @Override
    public List<StoreWithdrawalDO> getWithdrawalList(StoreWithdrawalExportReqVO exportReqVO) {
        return withdrawalMapper.selectList(exportReqVO);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void findBill(Long id){
        StoreWithdrawalDO storeWithdrawalDO = validateWithdrawalExists(id);
        if(TransferBillStatus.SUCCESS.toString().equals(storeWithdrawalDO.getState())){
            return;
        }
        TransferBillEntity transferBillEntity = transferToUserService.transferBill(storeWithdrawalDO.getOutBillNo());
        System.out.println("transferBillEntity:"+transferBillEntity);
        storeWithdrawalDO.setState(transferBillEntity.getState().toString());

        StoreRevenueDO storeRevenueDO = storeRevenueMapper.selectOne(new LambdaQueryWrapper<StoreRevenueDO>()
                .eq(StoreRevenueDO::getOrderId,storeWithdrawalDO.getId().toString())
                .eq(StoreRevenueDO::getIsFinish,ShopCommonEnum.IS_FINISH_0.getValue())
                .eq(StoreRevenueDO::getOrderType, OrderTypeEnum.WITH_ORDER.getValue()));
        if(TransferBillStatus.SUCCESS.toString().equals(transferBillEntity.getState().toString())){
            if (storeRevenueDO != null){
                //更新为结算
                storeRevenueDO.setIsFinish(ShopCommonEnum.IS_FINISH_1.getValue());
                storeRevenueMapper.updateById(storeRevenueDO);
            }
        } else if (TransferBillStatus.FAIL.toString().equals(transferBillEntity.getState().toString())) {
            storeWithdrawalDO.setRefuse(transferBillEntity.getFailReason());
            //店铺金额加回去
            StoreShopDO storeShopDO = this.getShop(storeWithdrawalDO.getShopId());
            BigDecimal newBalance = storeShopDO.getBalance().add(storeWithdrawalDO.getAmount());
            storeShopDO.setBalance(newBalance);
            shopMapper.updateById(storeShopDO);
            //删除这个流水记录
            storeRevenueMapper.deleteById(storeRevenueDO);
        }
        withdrawalMapper.updateById(storeWithdrawalDO);
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


    private void check(StoreWithdrawalDO withdrawal,StoreShopDO storeShopDO){
        //return;
        Long count =  withdrawalMapper.selectCount(new LambdaQueryWrapper<StoreWithdrawalDO>()
                .eq(StoreWithdrawalDO::getShopId,storeShopDO.getId()).le(StoreWithdrawalDO::getStatus,1));
        if(count > 0){
            throw exception(new ErrorCode(1008016007, "清等待上次提现结果审核"));
        }
        if(BigDecimal.ZERO.compareTo(withdrawal.getAmount()) >= 0){
            throw exception(new ErrorCode(1008016005, "提现金额必须大于0"));
        }
        if(withdrawal.getAmount().compareTo(storeShopDO.getBalance()) > 0) {
            throw exception(new ErrorCode(1008016006, "提现金额超额"));
        }

        //店铺金额减少
        BigDecimal newBalance = storeShopDO.getBalance().subtract(withdrawal.getAmount());
        storeShopDO.setBalance(newBalance);
        shopMapper.updateById(storeShopDO);




    }

}
