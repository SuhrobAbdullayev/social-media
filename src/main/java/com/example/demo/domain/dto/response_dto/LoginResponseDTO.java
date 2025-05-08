package com.example.demo.domain.dto.response_dto;

import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
public class LoginResponseDTO extends UserBaseDTO {
    private String token;
    private String refreshToken;
}
