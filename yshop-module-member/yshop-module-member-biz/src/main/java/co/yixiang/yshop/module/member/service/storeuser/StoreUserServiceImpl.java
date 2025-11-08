package co.yixiang.yshop.module.member.service.storeuser;

import co.yixiang.yshop.framework.common.exception.ErrorCode;
import co.yixiang.yshop.module.member.controller.admin.storeuser.vo.StoreUserPageReqVO;
import co.yixiang.yshop.module.member.controller.admin.storeuser.vo.StoreUserRespVO;
import co.yixiang.yshop.module.member.controller.admin.storeuser.vo.StoreUserSaveReqVO;
import co.yixiang.yshop.module.member.dal.dataobject.user.MemberUserDO;
import co.yixiang.yshop.module.member.dal.mysql.user.MemberUserMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import co.yixiang.yshop.module.member.dal.dataobject.storeuser.StoreUserDO;
import co.yixiang.yshop.framework.common.pojo.PageResult;
import co.yixiang.yshop.framework.common.util.object.BeanUtils;

import co.yixiang.yshop.module.member.dal.mysql.storeuser.StoreUserMapper;

import static co.yixiang.yshop.framework.common.exception.util.ServiceExceptionUtil.exception;
import static co.yixiang.yshop.module.store.enums.ErrorCodeConstants.STORE_USER_NOT_EXISTS;

/**
 * 门店移动端商家用户关联 Service 实现类
 *
 * @author yshop
 */
@Service
@Validated
public class StoreUserServiceImpl implements StoreUserService {

    @Resource
    private StoreUserMapper storeUserMapper;
    @Resource
    private MemberUserMapper memberUserMapper;

    @Override
    public Long createStoreUser(StoreUserSaveReqVO createReqVO) {
        StoreUserDO storeUserDO = storeUserMapper.selectOne(new LambdaQueryWrapper<StoreUserDO>()
                .eq(StoreUserDO::getUid,createReqVO.getUid()));
        if(storeUserDO != null){
            throw exception(new ErrorCode(202406083,"此用户已经绑定过店铺"));
        }
        // 插入
        StoreUserDO storeUser = BeanUtils.toBean(createReqVO, StoreUserDO.class);
        storeUserMapper.insert(storeUser);
        // 返回
        return storeUser.getId();
    }

    @Override
    public void updateStoreUser(StoreUserSaveReqVO updateReqVO) {
        StoreUserDO storeUserDO = storeUserMapper.selectOne(new LambdaQueryWrapper<StoreUserDO>()
                        .ne(StoreUserDO::getId,updateReqVO.getId())
                        .eq(StoreUserDO::getUid,updateReqVO.getUid()));
        if(storeUserDO != null){
            throw exception(new ErrorCode(202406083,"此用户已经绑定过店铺"));
        }
        // 校验存在
        validateStoreUserExists(updateReqVO.getId());
        // 更新
        StoreUserDO updateObj = BeanUtils.toBean(updateReqVO, StoreUserDO.class);
        storeUserMapper.updateById(updateObj);
    }

    @Override
    public void deleteStoreUser(Long id) {
        // 校验存在
        validateStoreUserExists(id);
        // 删除
        storeUserMapper.deleteById(id);
    }

    private void validateStoreUserExists(Long id) {
        if (storeUserMapper.selectById(id) == null) {
            throw exception(STORE_USER_NOT_EXISTS);
        }
    }

    @Override
    public StoreUserRespVO getStoreUser(Long id) {
        StoreUserDO storeUserDO = storeUserMapper.selectById(id);
        StoreUserRespVO storeUserRespVO = BeanUtils.toBean(storeUserDO, StoreUserRespVO.class);
        MemberUserDO memberUserDO = memberUserMapper.selectById(storeUserDO.getUid());
        storeUserRespVO.setNickname(memberUserDO.getNickname());
        return storeUserRespVO;
    }

    @Override
    public PageResult<StoreUserRespVO> getStoreUserPage(StoreUserPageReqVO pageReqVO) {
        return storeUserMapper.selectPage(pageReqVO);
    }

}