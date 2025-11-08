package co.yixiang.yshop.module.member.service.user;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import co.yixiang.yshop.framework.common.enums.CommonStatusEnum;
import co.yixiang.yshop.framework.common.enums.ShopCommonEnum;
import co.yixiang.yshop.module.card.dal.dataobject.vipcard.VipCardDO;
import co.yixiang.yshop.module.card.service.vipcard.AppVipCardService;
import co.yixiang.yshop.module.coupon.dal.dataobject.couponuser.CouponUserDO;
import co.yixiang.yshop.module.coupon.service.couponuser.AppCouponUserService;
import co.yixiang.yshop.module.infra.api.file.FileApi;
import co.yixiang.yshop.module.member.controller.app.user.vo.AppUserQueryVo;
import co.yixiang.yshop.module.member.controller.app.user.vo.AppUserUpdateMobileReqVO;
import co.yixiang.yshop.module.member.convert.user.UserConvert;
import co.yixiang.yshop.module.member.dal.dataobject.user.MemberUserDO;
import co.yixiang.yshop.module.member.dal.dataobject.userbill.UserBillDO;
import co.yixiang.yshop.module.member.dal.mysql.user.MemberUserMapper;
import co.yixiang.yshop.module.member.dal.mysql.userbill.UserBillMapper;
import co.yixiang.yshop.module.member.enums.BillDetailEnum;
import co.yixiang.yshop.module.member.service.userbill.UserBillService;
import co.yixiang.yshop.module.shop.dal.dataobject.recharge.RechargeDO;
import co.yixiang.yshop.module.shop.dal.mysql.recharge.RechargeMapper;
import co.yixiang.yshop.module.system.api.sms.SmsCodeApi;
import co.yixiang.yshop.module.system.api.sms.dto.code.SmsCodeUseReqDTO;
import co.yixiang.yshop.module.system.enums.sms.SmsSceneEnum;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.annotations.VisibleForTesting;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.time.LocalDateTime;

import static co.yixiang.yshop.framework.common.exception.util.ServiceExceptionUtil.exception;
import static co.yixiang.yshop.framework.common.util.servlet.ServletUtils.getClientIP;
import static co.yixiang.yshop.module.merchant.enums.ErrorCodeConstants.VIP_CARD_NOT_EXISTS;
import static co.yixiang.yshop.module.member.enums.ErrorCodeConstants.USER_NOT_EXISTS;
import static co.yixiang.yshop.module.shop.enums.ErrorCodeConstants.RECHARGE_NOT_EXISTS;

/**
 * 会员 User Service 实现类
 *
 * @author yshop
 */
@Service
@Valid
@Slf4j
public class MemberUserServiceImpl extends ServiceImpl<MemberUserMapper,MemberUserDO> implements MemberUserService {

    @Resource
    private MemberUserMapper memberUserMapper;

    @Resource
    private FileApi fileApi;
    @Resource
    private SmsCodeApi smsCodeApi;

    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    private AppCouponUserService appCouponUserService;
    @Resource
    private UserBillMapper userBillMapper;
    @Resource
    private UserBillService billService;
    @Resource
    private RechargeMapper rechargeMapper;
    @Resource
    private AppVipCardService appVipCardService;



    @Override
    public MemberUserDO getUserByMobile(String mobile) {
        return memberUserMapper.selectByMobile(mobile);
    }

    @Override
    public List<MemberUserDO> getUserListByNickname(String nickname) {
        return memberUserMapper.selectListByNicknameLike(nickname);
    }

    @Override
    public MemberUserDO createUserIfAbsent(String mobile, String registerIp,String from) {
        // 用户已经存在
        if(StrUtil.isNotEmpty(mobile)){
            MemberUserDO user = memberUserMapper.selectByMobile(mobile);
            if (user != null) {
                return user;
            }
        }

        // 用户不存在，则进行创建
        return this.createUser(mobile, registerIp,from);
    }

    private MemberUserDO createUser(String mobile, String registerIp,String from) {
        // 生成密码
        String password = IdUtil.fastSimpleUUID();
        // 插入用户
        MemberUserDO user = new MemberUserDO();
        user.setNickname(mobile);
        user.setUsername(mobile);
        user.setMobile(mobile);
        user.setStatus(CommonStatusEnum.ENABLE.getStatus()); // 默认开启
        user.setPassword(encodePassword(password)); // 加密密码
        user.setRegisterIp(registerIp);
        user.setLoginType(from);
        memberUserMapper.insert(user);
        return user;
    }

    @Override
    public void updateUserLogin(Long id, String loginIp) {
        memberUserMapper.updateById(new MemberUserDO().setId(id)
                .setLoginIp(loginIp).setLoginDate(LocalDateTime.now()));
    }

    @Override
    public MemberUserDO getUser(Long id) {
        return memberUserMapper.selectById(id);
    }

    @Override
    public AppUserQueryVo getAppUser(Long id){
        AppUserQueryVo appUserQueryVo = UserConvert.INSTANCE.convert3(memberUserMapper.selectById(id));
        Long count = appCouponUserService.count(new LambdaQueryWrapper<CouponUserDO>()
                .eq(CouponUserDO::getUserId,id)
                .eq(CouponUserDO::getStatus, ShopCommonEnum.IS_STATUS_1));
        appUserQueryVo.setCouponCount(count);
        QueryWrapper<UserBillDO> wrapper = new QueryWrapper<>();
        wrapper.select("SUM(number) as sumAll")
                .eq("category", BillDetailEnum.CATEGORY_1.getValue())
                .eq("type",BillDetailEnum.TYPE_3.getValue()).eq("uid",id);

        UserBillDO userBillDO =  userBillMapper.selectOne(wrapper);
        if(userBillDO == null){
            appUserQueryVo.setSumMoney(BigDecimal.ZERO);
        }else{
            appUserQueryVo.setSumMoney(userBillDO.getSumAll());
        }
        //检测会员卡有效期
        this.checkVipCardDate(id,appUserQueryVo.getCardId());

        return appUserQueryVo;
    }
    /**
     * 减去用户余额
     * @param uid uid
     * @param payPrice 金额
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void decPrice(Long uid, BigDecimal payPrice) {
        memberUserMapper.decPrice(payPrice,uid);
    }

    /**
     * 减去用户积分
     * @param uid uid
     * @param score 积分
     */
    @Override
    public void decScore(Long uid, Integer score) {
        memberUserMapper.decScore(score,uid);
    }

    /**
     * 增加购买次数
     * @param uid uid
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void incPayCount(Long uid) {
        memberUserMapper.incPayCount(uid);
    }


    @Override
    public List<MemberUserDO> getUserList(Collection<Long> ids) {
        return memberUserMapper.selectBatchIds(ids);
    }

    @Override
    public void updateUserNickname(Long userId, String nickname,String birthday, Integer gender,
                                   String avatar,
                                   String mobile) {
        MemberUserDO user = this.checkUserExists(userId);
        // 仅当新昵称不等于旧昵称时进行修改
//        if (nickname.equals(user.getNickname())){
//            return;
//        }
        MemberUserDO userDO = new MemberUserDO();
        userDO.setId(user.getId());
        userDO.setNickname(nickname);
        userDO.setBirthday(birthday);
        userDO.setGender(gender);
        userDO.setAvatar(avatar);
        userDO.setMobile(mobile);
        memberUserMapper.updateById(userDO);
    }

    @Override
    public String updateUserAvatar(Long userId, InputStream avatarFile) throws Exception {
        this.checkUserExists(userId);
        // 创建文件
        String avatar = fileApi.createFile(IoUtil.readBytes(avatarFile));
        // 更新头像路径
        memberUserMapper.updateById(MemberUserDO.builder().id(userId).avatar(avatar).build());
        return avatar;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUserMobile(Long userId, AppUserUpdateMobileReqVO reqVO) {
        // 检测用户是否存在
        checkUserExists(userId);
        // TODO yshop：oldMobile 应该不用传递

        // 校验旧手机和旧验证码
        smsCodeApi.useSmsCode(new SmsCodeUseReqDTO().setMobile(reqVO.getOldMobile()).setCode(reqVO.getOldCode())
                .setScene(SmsSceneEnum.MEMBER_UPDATE_MOBILE.getScene()).setUsedIp(getClientIP()));
        // 使用新验证码
        smsCodeApi.useSmsCode(new SmsCodeUseReqDTO().setMobile(reqVO.getMobile()).setCode(reqVO.getCode())
                .setScene(SmsSceneEnum.MEMBER_UPDATE_MOBILE.getScene()).setUsedIp(getClientIP()));

        // 更新用户手机
        memberUserMapper.updateById(MemberUserDO.builder().id(userId).mobile(reqVO.getMobile()).build());
    }

    @Override
    public boolean isPasswordMatch(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    /**
     * 更新用户余额
     * @param uid y用户id
     * @param price 金额
     */
    @Override
    public void incMoney(Long uid, BigDecimal price) {
        if(price!=null&&price.doubleValue()>0){
            memberUserMapper.incMoney(uid,price);
        }
    }

    /**
     * 充值
     * @param uid 用户id
     * @param rechargeId 充值ID
     */
    @Override
    public String balanceRecharge(Long uid, String rechargeId) {
        //生成分布式唯一值
        String orderSn = IdUtil.getSnowflake(0, 0).nextIdStr();
        RechargeDO rechargeDO = rechargeMapper.selectById(rechargeId);
        if(rechargeDO == null){
            throw exception(RECHARGE_NOT_EXISTS);
        }
        MemberUserDO memberUserDO = this.getById(uid);
        BigDecimal newMoney = memberUserDO.getNowMoney().add(rechargeDO.getValue());
        //增加流水
        billService.income(uid, "用户充值", BillDetailEnum.CATEGORY_1.getValue(),
                BillDetailEnum.TYPE_1.getValue(),
                rechargeDO.getValue().doubleValue(),
                newMoney.doubleValue(),
                "用户充值,金额" + rechargeDO.getValue(), rechargeId,orderSn,null);

        return orderSn;
    }

    /**
     * 购买会员卡
     * @param uid 用户id
     * @param cardId ID
     */
    @Override
    public String buyCard(Long uid, String cardId) {
        //生成分布式唯一值
        String orderSn = IdUtil.getSnowflake(0, 0).nextIdStr();

        VipCardDO vipCardDO = appVipCardService.getById(cardId);
        if(vipCardDO == null){
            throw exception(VIP_CARD_NOT_EXISTS);
        }
        MemberUserDO memberUserDO = this.getById(uid);
        //增加流水
        billService.income(uid, "用户购买会员卡", BillDetailEnum.CATEGORY_1.getValue(),
                BillDetailEnum.TYPE_11.getValue(),
                vipCardDO.getPrice().doubleValue(),
                memberUserDO.getNowMoney().doubleValue(),
                "用户购买会员卡,金额" + vipCardDO.getPrice(), cardId,orderSn,null);

        return orderSn;
    }


    /**
     * 检测会员卡是否过期
     * @param uid
     */
    private void checkVipCardDate(Long uid,Long cardId) {
        VipCardDO vipCardDO = appVipCardService.getById(cardId);
        if(vipCardDO == null || vipCardDO.getPeriod() == 0) {
            return;
        }
        LocalDateTime nowTime = LocalDateTime.now();
        UserBillDO userBillDO = userBillMapper
                .selectOne(new LambdaQueryWrapper<UserBillDO>()
                .eq(UserBillDO::getUid,uid)
                .eq(UserBillDO::getLinkId,cardId)
                .eq(UserBillDO::getStatus,ShopCommonEnum.IS_STATUS_1.getValue())
                .orderByDesc(UserBillDO::getId)
                .last("LIMIT 1"));
        LocalDateTime useTime = userBillDO.getCreateTime().plusMonths(vipCardDO.getPeriod());
        if(useTime.compareTo(nowTime) <= 0){
            //已经过期 取消会员卡资格
            MemberUserDO memberUserDO = MemberUserDO.builder().cardId(0L).cardName("").discount(0).build();
            this.update(memberUserDO,new LambdaQueryWrapper<MemberUserDO>().eq(MemberUserDO::getId,uid));
        }

    }


    /**
     * 对密码进行加密
     *
     * @param password 密码
     * @return 加密后的密码
     */
    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    @VisibleForTesting
    public MemberUserDO checkUserExists(Long id) {
        if (id == null) {
            return null;
        }
        MemberUserDO user = memberUserMapper.selectById(id);
        if (user == null) {
            throw exception(USER_NOT_EXISTS);
        }
        return user;
    }

}
