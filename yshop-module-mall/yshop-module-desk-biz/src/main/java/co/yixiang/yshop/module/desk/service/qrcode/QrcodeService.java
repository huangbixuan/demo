package co.yixiang.yshop.module.desk.service.qrcode;

import co.yixiang.yshop.module.desk.dal.dataobject.qrcode.QrcodeDO;

import java.util.Map;

/**
 * 二维码 Service 接口
 *
 * @author yshop
 */
public interface QrcodeService {

    /**
     * 创建小程序二维码
     * @param page 跳转的页面
     * @param scene 参数
     * @param isUpdate 是否覆盖
     * @param env  小程序发布环境 MiniQrcodeEnvEnum
     * @return
     */
    QrcodeDO createMiniQrcode(String page,String scene,Boolean isUpdate,String env);

    /**
     * 创建普通二维码
     * @param page 跳转的页面
     * @param scene 参数
     * @param isUpdate 是否覆盖
     * @return
     */
    QrcodeDO createQrcode(String page,String scene,Boolean isUpdate);


}
