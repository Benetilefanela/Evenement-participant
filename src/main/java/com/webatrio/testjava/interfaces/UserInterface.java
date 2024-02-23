package com.webatrio.testjava.interfaces;

import com.webatrio.testjava.mapStruct.UserDTO;
import org.springframework.stereotype.Component;

@Component
public interface UserInterface {

    UserDTO creationUser(UserDTO userDTO);

}
