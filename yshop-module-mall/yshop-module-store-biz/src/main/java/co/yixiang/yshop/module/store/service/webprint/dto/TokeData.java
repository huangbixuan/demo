package co.yixiang.yshop.module.store.service.webprint.dto;

import lombok.Data;

@Data
public class TokeData {
    private String accessToken;
    private String refreshToken;
    private String machineCode;
    private Long expiresIn;
    private String scope;
}
