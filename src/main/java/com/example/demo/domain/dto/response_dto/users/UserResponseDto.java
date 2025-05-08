package com.example.demo.domain.dto.response_dto.users;

import com.example.demo.domain.entity.Role;
import lombok.Data;

import java.util.Set;

@Data
public class UserResponseDto {

    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private Set<Role> roles;
}
