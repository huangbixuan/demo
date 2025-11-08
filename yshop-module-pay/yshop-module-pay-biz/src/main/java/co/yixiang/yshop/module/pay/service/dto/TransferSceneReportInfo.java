package co.yixiang.yshop.module.pay.service.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class TransferSceneReportInfo {
    @SerializedName("info_type")
    private String infoType;

    @SerializedName("info_content")
    private String infoContent;
}
