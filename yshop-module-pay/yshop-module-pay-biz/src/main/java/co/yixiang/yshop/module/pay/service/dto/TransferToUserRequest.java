package co.yixiang.yshop.module.pay.service.dto;

import co.yixiang.yshop.module.pay.service.tramsfer.TransferToUserService;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class TransferToUserRequest {
    @SerializedName("appid")
    private String appid;

    @SerializedName("out_bill_no")
    private String outBillNo;

    @SerializedName("transfer_scene_id")
    private String transferSceneId;

    @SerializedName("openid")
    private String openid;

    @SerializedName("user_name")
    private String userName;

    @SerializedName("transfer_amount")
    private Long transferAmount;

    @SerializedName("transfer_remark")
    private String transferRemark;

    @SerializedName("notify_url")
    private String notifyUrl;

    @SerializedName("user_recv_perception")
    private String userRecvPerception;

    @SerializedName("transfer_scene_report_infos")
    private List<TransferSceneReportInfo> transferSceneReportInfos = new ArrayList<TransferSceneReportInfo>();
}
