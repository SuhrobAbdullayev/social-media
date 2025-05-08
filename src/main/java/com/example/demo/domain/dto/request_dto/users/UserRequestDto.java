package com.example.demo.domain.dto.request_dto.users;

import com.example.demo.domain.entity.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Data
public class UserRequestDto {
    @NotNull
    @NotBlank
    @Size(min = 3)
    private String firstName;

    @NotNull
    @NotBlank
    @Size(min = 3)
    private String lastName;

    @NotNull
    @NotBlank
    @Size(min = 4, max = 30)
    private String username;

    @NotNull
    @NotBlank
    @Size(min = 4)
    private String password;

    @NotNull
    @NotBlank
    private Set<Role> roles;
}
