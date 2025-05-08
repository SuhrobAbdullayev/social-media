package com.example.demo.service;


import com.example.demo.controller.VM.LoginVM;
import com.example.demo.domain.dto.request_dto.users.UserRequestDto;
import com.example.demo.domain.dto.response_dto.LoginResponseDTO;
import com.example.demo.domain.dto.response_dto.ResponseDTO;
import com.example.demo.domain.dto.response_dto.users.UserResponseDto;

public interface UserService {

    ResponseDTO<LoginResponseDTO> login(LoginVM loginVM);

    ResponseDTO<UserResponseDto> createOrUpdateUser(UserRequestDto userRequestDto, Long userId);

    Boolean checkUsername(String username);

    Boolean checkPassword(String password);

    void logout(String username);
}
