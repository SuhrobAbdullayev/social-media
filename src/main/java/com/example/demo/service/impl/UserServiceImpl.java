package com.example.demo.service.impl;

import com.example.demo.controller.VM.LoginVM;
import com.example.demo.domain.dto.request_dto.users.UserRequestDto;
import com.example.demo.domain.dto.response_dto.LoginResponseDTO;
import com.example.demo.domain.dto.response_dto.ResponseDTO;
import com.example.demo.domain.dto.response_dto.users.UserResponseDto;
import com.example.demo.domain.entity.User;
import com.example.demo.exceptions.UserException;
import com.example.demo.jwt_utils.JwtTokenProvider;
import com.example.demo.mapping.UserMapper;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public ResponseDTO<LoginResponseDTO> login(LoginVM loginVM) {
        User user = userRepository.findByUsername(loginVM.getUsername().trim().toLowerCase());
        if (user == null) {
            throw new UserException("Bu foydalanuvch mavjud emas");
        }
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginVM.getUsername().trim().toLowerCase(), loginVM.getPassword()));
        return jwtTokenProvider.createToken(user.getUsername().trim().toLowerCase(), loginVM.isRememberMe());
    }

    @Override
    @Transactional
    public ResponseDTO<UserResponseDto> createOrUpdateUser(UserRequestDto userRequestDto, Long userId) {
        ResponseDTO<UserResponseDto> responseDTO = new ResponseDTO<>();
        User user = userMapper.toEntity(userRequestDto);
        if (Boolean.TRUE.equals(checkPassword(userRequestDto.getPassword()))) {
            user.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));
        } else {
            log.error("Parol juda qisqa,{}", userRequestDto.getPassword());
            throw new UserException("Parol juda qisqa");
        }
        user.setUsername(userRequestDto.getUsername().trim().toLowerCase());
        if (userId != null) {
            log.info("Foydalanuvchi ma'lumotlari yangilanmoqda...");
            user = userRepository.findById(userId).orElseThrow(() -> new UserException("Foydalanuvchi topilmadi"));
            userMapper.updateFromDto(userRequestDto, user);
            user.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));
            userRepository.save(user);
            responseDTO.setMessage("Foydalanuvchi ma'lumotlari yangilandi");
            log.info("Foydalanuvchi ma'lumotlari yangilandi");
            return responseDTO;
        } else {
            if (Boolean.TRUE.equals(checkUsername(userRequestDto.getUsername()))) {
                log.error("Bu username band, {}", userRequestDto.getUsername());
                throw new UserException("Bu username band");
            }
            responseDTO.setMessage("Foydalanuvchi muvaffaqiyatli qo'shildi");
        }
        responseDTO.setSuccess(true);
        responseDTO.setReason(null);
        responseDTO.setData(userMapper.toDto(userRepository.save(user)));
        return responseDTO;
    }

    @Override
    public Boolean checkUsername(String username) {
        try {
            return userRepository.existsByUsername(username);

        } catch (Exception e) {
            log.error("talaba topilmadi, {}", e.getMessage());
            throw new UserException("talaba topilmadi");
        }
    }

    @Override
    public Boolean checkPassword(String password) {
        return password.length() >= 4;
    }

    @Override
    public void logout(String username) {
        log.info("The process of logout has started");
        try {
            User user = userRepository.findByUsername(username);
            if (user == null) {
                log.error("Foydalanuvchi topilmadi, {}", username);
                throw new UserException("Foydalanuvchi topilmadi");
            }
            user.setRefreshToken(null);
            userRepository.save(user);
        } catch (Exception e) {
            log.error("Foydalanuvchi topilmadi, {}", e.getMessage());
            throw new UserException("Foydalanuvchi topilmadi");
        }
    }
}
