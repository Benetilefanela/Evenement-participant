package com.webatrio.testjava.interfaces;

import com.webatrio.testjava.mapStruct.UserDTO;
import com.webatrio.testjava.models.RequestLogin;
import org.springframework.stereotype.Component;

@Component
public interface AuthenticationInterface {

    String login(RequestLogin requestLogin) throws Exception;

    UserDTO creationUser(UserDTO userDTO);

}
