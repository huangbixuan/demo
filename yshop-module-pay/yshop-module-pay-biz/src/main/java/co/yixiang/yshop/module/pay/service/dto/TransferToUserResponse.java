package co.yixiang.yshop.module.pay.service.dto;

import co.yixiang.yshop.module.pay.enums.TransferBillStatus;
import co.yixiang.yshop.module.pay.service.tramsfer.TransferToUserService;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class TransferToUserResponse {
    @SerializedName("out_bill_no")
    private String outBillNo;

    @SerializedName("transfer_bill_no")
    private String transferBillNo;

    @SerializedName("create_time")
    private String createTime;

    @SerializedName("state")
    private TransferBillStatus state;

    @SerializedName("package_info")
    private String packageInfo;
}
