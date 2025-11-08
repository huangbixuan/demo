package co.yixiang.yshop.module.store.service.webprint;

import java.util.*;

import co.yixiang.yshop.module.store.dal.dataobject.webprintconfig.WebPrintConfigDO;
import jakarta.validation.*;
import co.yixiang.yshop.module.store.controller.admin.webprint.vo.*;
import co.yixiang.yshop.module.store.dal.dataobject.webprint.WebPrintDO;
import co.yixiang.yshop.framework.common.pojo.PageResult;

/**
 * 易联云打印机 Service 接口
 *
 * @author yshop
 */
public interface WebPrintService {

    /**
     * 创建易联云打印机
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createWebPrint(@Valid WebPrintCreateReqVO createReqVO);

    /**
     * 更新易联云打印机
     *
     * @param updateReqVO 更新信息
     */
    void updateWebPrint(@Valid WebPrintUpdateReqVO updateReqVO);

    /**
     * 删除易联云打印机
     *
     * @param id 编号
     */
    void deleteWebPrint(Long id);

    /**
     * 获得易联云打印机
     *
     * @param id 编号
     * @return 易联云打印机
     */
    WebPrintDO getWebPrint(Long id);

    /**
     * 获得易联云打印机列表
     *
     * @return 易联云打印机列表
     */
    List<WebPrintDO> getWebPrintList();

    /**
     * 获得易联云打印机分页
     *
     * @param pageReqVO 分页查询
     * @return 易联云打印机分页
     */
    PageResult<WebPrintDO> getWebPrintPage(WebPrintPageReqVO pageReqVO);

    /**
     * 获得易联云打印机列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 易联云打印机列表
     */
    List<WebPrintDO> getWebPrintList(WebPrintExportReqVO exportReqVO);

    /**
     * 小票打印
     * @param mechineCode 设备号
     * @param content 打印内容
     * @param originId  订单号
     * @return
     */
    String printOrder(String mechineCode,String content,String originId);

    /**
     *
     * @param sn 打印机编码
     * @param content 打印内容
     */
    void feiePrintOrder(String sn,String content);

    /**
     * 配置打印机
     * @param webPrintSetVO
     */
    void savePrintConfig(WebPrintSetVO webPrintSetVO);

    WebPrintSetVO getPrintConfig(String brand);

}
