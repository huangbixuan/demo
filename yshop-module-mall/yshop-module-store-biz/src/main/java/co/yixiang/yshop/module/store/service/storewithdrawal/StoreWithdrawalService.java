package co.yixiang.yshop.module.store.service.storewithdrawal;

import java.util.*;
import jakarta.validation.*;
import co.yixiang.yshop.module.store.controller.admin.storewithdrawal.vo.*;
import co.yixiang.yshop.module.store.dal.dataobject.storewithdrawal.StoreWithdrawalDO;
import co.yixiang.yshop.framework.common.pojo.PageResult;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 提现管理 Service 接口
 *
 * @author yshop
 */
public interface StoreWithdrawalService {

    /**
     * 创建提现管理
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createWithdrawal(@Valid StoreWithdrawalCreateReqVO createReqVO);

    /**
     * 更新提现管理
     *
     * @param updateReqVO 更新信息
     */
    void updateWithdrawal(@Valid StoreWithdrawalUpdateReqVO updateReqVO);

    /**
     * 删除提现管理
     *
     * @param id 编号
     */
    void deleteWithdrawal(Long id);

    /**
     * 获得提现管理
     *
     * @param id 编号
     * @return 提现管理
     */
    StoreWithdrawalRespVO getWithdrawal(Long id);

    /**
     * 获得提现管理列表
     *
     * @param ids 编号
     * @return 提现管理列表
     */
    List<StoreWithdrawalDO> getWithdrawalList(Collection<Long> ids);

    /**
     * 获得提现管理分页
     *
     * @param pageReqVO 分页查询
     * @return 提现管理分页
     */
    PageResult<StoreWithdrawalRespVO> getWithdrawalPage(StoreWithdrawalPageReqVO pageReqVO);

    /**
     * 获得提现管理列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 提现管理列表
     */
    List<StoreWithdrawalDO> getWithdrawalList(StoreWithdrawalExportReqVO exportReqVO);

    /**
     * 微信账单查询
     *
     * @param id 编号
     */
    void findBill(Long id);

}
