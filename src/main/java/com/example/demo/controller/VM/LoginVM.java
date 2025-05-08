package com.example.demo.controller.VM;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LoginVM {
    @NotNull
    private String username;
    @NotNull
    private String password;
    @NotNull
    private boolean rememberMe;
}
