package co.yixiang.yshop.module.desk.controller.admin.shopdesk.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Schema(description = "管理后台 - 桌号导入 Response VO")
@Data
@Builder
public class DeskImportRespVO {

    @Schema(description = "创建成功的桌号数组", required = true)
    private List<String> createDeskNames;

    @Schema(description = "更新成功的桌号数组", required = true)
    private List<String> updateDeskNames;

    @Schema(description = "导入失败的桌号集合,key 为用户名，value 为失败原因", required = true)
    private Map<String, String> failureDeskNames;

}
