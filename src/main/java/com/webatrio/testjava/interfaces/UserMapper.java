package com.webatrio.testjava.interfaces;

import com.webatrio.testjava.mapStruct.UserDTO;
import com.webatrio.testjava.models.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toUser(UserDTO userDTO);

    UserDTO toUserDto(User user);
}
