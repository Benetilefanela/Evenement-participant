package com.webatrio.testjava.impl;

import com.webatrio.testjava.interfaces.ParticipantMapper;
import com.webatrio.testjava.interfaces.UserMapper;
import com.webatrio.testjava.mapStruct.UserDTO;
import com.webatrio.testjava.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserMapperImpl implements UserMapper {

    @Autowired
    private ParticipantMapper participantMapper;
    @Override
    public User toUser(UserDTO userDTO) {
        if(userDTO == null){
            return null;
        }
        User.UserBuilder user = User.builder();
        user.username(userDTO.getUsername());
        user.password(userDTO.getPassword());
        user.id(userDTO.getId());
        if(userDTO.getRole()!= null){
            user.role(userDTO.getRole());
        }
        if(userDTO.getParticipant()!= null){
            user.participant(participantMapper.toParticipant(userDTO.getParticipant()));
        }
        return user.build();
    }

    @Override
    public UserDTO toUserDto(User user) {
        if(user ==  null){
            return null;
        }

        UserDTO.UserDTOBuilder userDTO = UserDTO.builder();
        userDTO.id(user.getId());
        userDTO.username(user.getUsername());
        userDTO.password(user.getPassword());
        if(user.getRole() != null){
            userDTO.role(user.getRole());
        }
        if(user.getParticipant() != null){
            userDTO.participant(participantMapper.toParticipantDto(user.getParticipant()));
        }
        return userDTO.build();
    }
}
