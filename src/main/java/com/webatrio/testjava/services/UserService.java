package com.webatrio.testjava.services;

import com.webatrio.testjava.interfaces.UserInterface;
import com.webatrio.testjava.mapStruct.UserDTO;
import com.webatrio.testjava.repositories.ParticipantRepository;
import com.webatrio.testjava.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserInterface {

    private final UserRepository userRepository;
    private final ParticipantRepository participantRepository;
    @Override
    public UserDTO creationUser(UserDTO userDTO) {

        return null;
    }
}
