package co.yixiang.yshop.module.desk.controller.app.shopdesk.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class AppDeskDueVO {

    private Long id;

    @Schema(description = "类别名称")
    private String name;

    @Schema(description = "内容列表")
    private List<AppShopDeskVO> childrens;
}
