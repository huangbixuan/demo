package co.yixiang.yshop.module.pay.service.dto;

import co.yixiang.yshop.module.pay.enums.TransferBillStatus;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class TransferBillEntity {
    @SerializedName("mch_id")
    private String mchId;

    @SerializedName("out_bill_no")
    private String outBillNo;

    @SerializedName("transfer_bill_no")
    private String transferBillNo;

    @SerializedName("appid")
    private String appid;

    @SerializedName("state")
    private TransferBillStatus state;

    @SerializedName("transfer_amount")
    private Long transferAmount;

    @SerializedName("transfer_remark")
    private String transferRemark;

    @SerializedName("fail_reason")
    private String failReason;

    @SerializedName("openid")
    private String openid;

    @SerializedName("user_name")
    private String userName;

    @SerializedName("create_time")
    private String createTime;

    @SerializedName("update_time")
    private String updateTime;
}
