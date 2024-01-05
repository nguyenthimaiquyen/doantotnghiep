package com.quyen.hust.model.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class RefreshTokenRequest {

    @NotBlank
    private String refreshToken;


}
