package com.example.demo.controller;

import com.example.demo.controller.VM.LoginVM;
import com.example.demo.domain.dto.request_dto.users.UserRequestDto;
import com.example.demo.domain.dto.response_dto.ResponseDTO;
import com.example.demo.domain.dto.response_dto.users.UserResponseDto;
import com.example.demo.jwt_utils.JwtTokenProvider;
import com.example.demo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/auth")
@Tag(name = "Foydalanuvchi", description = "Foydalunvhilarni ro'yxatga olish va login qilish qismi")
@RequiredArgsConstructor
@Slf4j
public class UserJwtController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @Operation(summary = "Foydalanuvchini ro'yxatga olish uchun api", description = "Foydalanuvchini ro'yxatga olish uchun api")
    @PostMapping("/register")
    public ResponseEntity<ResponseDTO<UserResponseDto>> register(@RequestBody UserRequestDto userRequestDto, @RequestParam(required = false) Long userId) {
        return new ResponseEntity<>(userService.createOrUpdateUser(userRequestDto, userId), HttpStatus.CREATED);
    }

    @Operation(summary = "Foydalanuvchi login qilish uchun API", description = "Foydalanuvchi login qilish uchun API")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginVM loginVM) {
        return new ResponseEntity<>(userService.login(loginVM), HttpStatus.CREATED);
    }

    @Operation(summary = "Foydalanuvchi logout qilish uchun API", description = "Foydalanuvchi logout qilish uchun API")
    @PostMapping("/logout")
    public ResponseEntity<?> logout(Principal principal) {
        userService.logout(principal.getName());
        return ResponseEntity.ok("Logged out successfully");
    }

    @Operation(summary = "Token eskirganda Refresh Token qilish uchun API", description = "param qilib token yuboriladi")
    @PostMapping("/refreshToken")
    public ResponseEntity<?> refreshToken(@RequestParam("refreshToken") String refreshToken) {
        return ResponseEntity.ok(jwtTokenProvider.refreshToken(refreshToken));
    }
}
