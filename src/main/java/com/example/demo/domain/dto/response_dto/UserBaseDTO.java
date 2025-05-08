package com.example.demo.domain.dto.response_dto;

import com.example.demo.domain.entity.Role;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Set;

@Data
public class UserBaseDTO {
    @NotNull
    Long id;
    @NotNull
    String username;
    @NotNull
    String firstName;
    @NotNull
    String lastName;
    @NotNull
    Set<Role> roles;
}
