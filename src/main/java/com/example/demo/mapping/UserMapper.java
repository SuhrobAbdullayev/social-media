package com.example.demo.mapping;

import com.example.demo.domain.dto.request_dto.users.UserRequestDto;
import com.example.demo.domain.dto.response_dto.users.UserResponseDto;
import com.example.demo.domain.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper extends EntityMapping<User, UserRequestDto, UserResponseDto> {

}
