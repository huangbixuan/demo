package co.yixiang.yshop.module.store.service.userbank;

import co.yixiang.yshop.framework.common.exception.ErrorCode;
import co.yixiang.yshop.framework.security.core.util.SecurityFrameworkUtils;
import co.yixiang.yshop.module.member.api.user.MemberUserApi;
import co.yixiang.yshop.module.member.api.user.dto.MemberUserRespDTO;
import co.yixiang.yshop.module.member.enums.LoginTypeEnum;
import co.yixiang.yshop.module.store.dal.dataobject.storeshop.StoreShopDO;
import co.yixiang.yshop.module.store.dal.dataobject.storewithdrawal.StoreWithdrawalDO;
import co.yixiang.yshop.module.store.dal.mysql.storeshop.StoreShopMapper;
import co.yixiang.yshop.module.store.dal.mysql.storewithdrawal.StoreWithdrawalMapper;
import co.yixiang.yshop.module.store.enums.BankTypeEnum;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import co.yixiang.yshop.module.store.controller.admin.userbank.vo.*;
import co.yixiang.yshop.module.store.dal.dataobject.userbank.UserBankDO;
import co.yixiang.yshop.framework.common.pojo.PageResult;

import co.yixiang.yshop.module.store.convert.userbank.UserBankConvert;
import co.yixiang.yshop.module.store.dal.mysql.userbank.UserBankMapper;

import static co.yixiang.yshop.framework.common.exception.util.ServiceExceptionUtil.exception;
import static co.yixiang.yshop.module.store.enums.ErrorCodeConstants.*;

/**
 * 提现账户 Service 实现类
 *
 * @author yshop
 */
@Service
@Validated
public class UserBankServiceImpl implements UserBankService {

    @Resource
    private UserBankMapper userBankMapper;
    @Resource
    private StoreShopMapper shopMapper;
    @Resource
    private StoreWithdrawalMapper storeWithdrawalMapper;
    @Resource
    private MemberUserApi memberUserApi;

    @Override
    public Long createUserBank(UserBankCreateReqVO createReqVO) {
        // 插入
        Long uid = SecurityFrameworkUtils.getLoginUserId();
        UserBankDO userBank = UserBankConvert.INSTANCE.convert(createReqVO);
        StoreShopDO storeShopDO = this.getShop(createReqVO.getShopId());
        userBank.setShopName(storeShopDO.getName());
        userBank.setUid(uid);
        MemberUserRespDTO userRespDTO = memberUserApi.getUser(uid);
        if(BankTypeEnum.TYPE_2.getValue().equals(createReqVO.getType())){
            String openid = userRespDTO.getOpenid();
            if(LoginTypeEnum.ROUNTINE.getValue().equals(userRespDTO.getLoginType())){
                openid = userRespDTO.getRoutineOpenid();
            }
            userBank.setWxCode(openid);
        }
        userBankMapper.insert(userBank);
        // 返回
        return userBank.getId();
    }

    @Override
    public void updateUserBank(UserBankUpdateReqVO updateReqVO) {
        // 校验存在
        validateUserBankExists(updateReqVO.getId());
        // 更新
        UserBankDO updateObj = UserBankConvert.INSTANCE.convert(updateReqVO);
        StoreShopDO storeShopDO = this.getShop(updateReqVO.getShopId());
        updateObj.setShopName(storeShopDO.getName());
        userBankMapper.updateById(updateObj);
    }

    @Override
    public void deleteUserBank(Long id) {
        // 校验存在
        validateUserBankExists(id);
        StoreWithdrawalDO storeWithdrawalDO = storeWithdrawalMapper
                .selectOne(new LambdaQueryWrapper<StoreWithdrawalDO>().eq(StoreWithdrawalDO::getBankId,id));
        if(storeWithdrawalDO != null){
            throw exception(new ErrorCode(202406110,"此银行卡已经参与提现不可以删除或者解绑"));
        }
        // 删除
        userBankMapper.deleteById(id);
    }

    private void validateUserBankExists(Long id) {
        if (userBankMapper.selectById(id) == null) {
            throw exception(USER_BANK_NOT_EXISTS);
        }
    }

    @Override
    public UserBankDO getUserBank(Long id) {
        return userBankMapper.selectById(id);
    }

    @Override
    public List<UserBankDO> getUserBankList(Long shopId) {
        return userBankMapper.selectList(new LambdaQueryWrapper<UserBankDO>().eq(UserBankDO::getShopId,shopId));
    }

    @Override
    public PageResult<UserBankDO> getUserBankPage(UserBankPageReqVO pageReqVO) {
        return userBankMapper.selectPage(pageReqVO);
    }

    @Override
    public List<UserBankDO> getUserBankList(UserBankExportReqVO exportReqVO) {
        return userBankMapper.selectList(exportReqVO);
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

}
