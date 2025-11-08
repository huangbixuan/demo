package co.yixiang.yshop.module.desk.service.qrcode;

import cn.binarywang.wx.miniapp.api.WxMaQrcodeService;
import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaCodeLineColor;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.crypto.digest.MD5;
import cn.hutool.extra.qrcode.QrCodeUtil;
import cn.hutool.json.JSONUtil;
import co.yixiang.yshop.module.desk.dal.dataobject.qrcode.QrcodeDO;
import co.yixiang.yshop.module.desk.dal.mysql.qrcode.QrcodeMapper;
import co.yixiang.yshop.module.desk.enums.QrcodeTypeEnum;
import co.yixiang.yshop.module.mp.service.account.MpAccountService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.apache.commons.codec.cli.Digest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.util.UriUtils;

import jakarta.annotation.Resource;

import java.io.File;
import java.util.*;

import static co.yixiang.yshop.framework.common.exception.util.ServiceExceptionUtil.exception;
import static co.yixiang.yshop.module.desk.enums.ErrorCodeConstants.CREATE_QRCODE_FAIL;
import static co.yixiang.yshop.module.desk.enums.ErrorCodeConstants.SHOP_DESK_NOT_EXISTS;

/**
 * 二维码 Service 实现类
 *
 * @author yshop
 */
@Service
@Validated
@Slf4j
public class QrcodeServiceImpl implements QrcodeService {

    @Resource
    private QrcodeMapper qrcodeMapper;
   // @Resource
   // private WxMaService wxMaService;

    @Value("${yshop.file-path}")
    private String filePath;

    @Value("${wx.miniapp.appid}")
    private String appId;

    @Value("${yshop.h5}")
    private String H5Url;

    @Resource
    private MpAccountService mpAccountService;

    /**
     * 创建小程序二维码
     * @param page 跳转的页面
     * @param scene 参数
     * @param isUpdate 是否覆盖
     * @param env  小程序发布环境 MiniQrcodeEnvEnum
     * @return
     */
    @Override
    public QrcodeDO createMiniQrcode(String page, String scene, Boolean isUpdate, String env) {

        String hashStr = DigestUtil.md5Hex(appId+page+scene+env);
        QrcodeDO qrcodeDO = qrcodeMapper.selectOne(new LambdaQueryWrapper<QrcodeDO>().eq(QrcodeDO::getHash,hashStr));
        if(qrcodeDO == null || isUpdate) {
            try {
                String path = filePath + "qrcode" + File.separator;
                FileUtil.mkdir(new File(path));
                WxMaService wxMaService = mpAccountService.getMaService();
                File file = wxMaService.getQrcodeService().createWxaCodeUnlimit(scene,page,path,false,env,600,
                        true,null, false);
                String fileName = IdUtil.randomUUID() + ".jpg";;
                File target = new File(path + fileName);
                //临时文件移动到磁盘
                FileUtil.copyFile(file,target);
                QrcodeDO qrcodeDO1 = new QrcodeDO();
                qrcodeDO1.setType(QrcodeTypeEnum.MINI_UNLIMIT.getValue());
                qrcodeDO1.setHash(hashStr);
                SortedMap<Object, Object> sortedMap = new TreeMap<Object, Object>() {
                    private static final long serialVersionUID = 1L;
                    {
                        put("page", page);
                        put("scene", scene);
                        put("env", env);
                    }};
                qrcodeDO1.setData(JSONUtil.toJsonStr(sortedMap));
                qrcodeDO1.setSrc("file" + File.separator  +"qrcode" + File.separator + fileName);
                qrcodeDO1.setFullPath(path + fileName);
                qrcodeMapper.insert(qrcodeDO1);

                return qrcodeDO1;
            } catch (Exception e) {
                log.error(e.getMessage());
                //throw exception(CREATE_QRCODE_FAIL);
            }
        }

        return qrcodeDO;
    }

    /**
     * 创建普通二维码
     * @param page 跳转的页面
     * @param scene 参数
     * @param isUpdate 是否覆盖
     * @return
     */
    @Override
    public QrcodeDO createQrcode(String page, String scene, Boolean isUpdate) {
        String hashStr = DigestUtil.md5Hex(H5Url + page + scene);
        QrcodeDO qrcodeDO = qrcodeMapper.selectOne(new LambdaQueryWrapper<QrcodeDO>().eq(QrcodeDO::getHash,hashStr));
        String url = H5Url +"/#/" + page + "?scene=" + UriUtils.encode(scene,"UTF-8");

        System.out.println("url:"+url);


        if(qrcodeDO == null || isUpdate) {
            String path = filePath + "qrcode" + File.separator;
            String fileName = IdUtil.randomUUID() + ".jpg";;
            File target = new File(path + fileName);
            QrCodeUtil.generate(url, 680, 680, target);
            QrcodeDO qrcodeDO2 = new QrcodeDO();
            qrcodeDO2.setType(QrcodeTypeEnum.NORMAL.getValue());
            qrcodeDO2.setHash(hashStr);
            SortedMap<Object, Object> sortedMap = new TreeMap<Object, Object>() {
                private static final long serialVersionUID = 1L;
                {
                    put("page", url);
                    put("scene", scene);
                }};
            qrcodeDO2.setData(JSONUtil.toJsonStr(sortedMap));
            qrcodeDO2.setSrc("file" + File.separator  +"qrcode" + File.separator + fileName);
            qrcodeDO2.setFullPath(path + fileName);
            qrcodeMapper.insert(qrcodeDO2);

            return qrcodeDO2;
        }

        return qrcodeDO;
    }



}
