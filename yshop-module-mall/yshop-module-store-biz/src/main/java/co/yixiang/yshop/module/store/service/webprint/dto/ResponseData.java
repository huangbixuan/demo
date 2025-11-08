package co.yixiang.yshop.module.store.service.webprint.dto;

import lombok.Data;

@Data
public class ResponseData {
    private Integer error;
    private String errorDescription;
    private Long timestamp;
    private String body;
}
