package com.webatrio.testjava.controllers;

import com.webatrio.testjava.interfaces.AuthenticationInterface;
import com.webatrio.testjava.mapStruct.UserDTO;
import com.webatrio.testjava.models.RequestLogin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/auth")
public class AuthentificationController {

    @Autowired
    private AuthenticationInterface authenticationInterface;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody RequestLogin requestLogin) throws Exception {
        String token = authenticationInterface.login(requestLogin);
        if(!token.isEmpty()){
            return ResponseEntity.ok(token);
        }else{
            return ResponseEntity.badRequest().body("Username ou Mot de passe incorrect ");
        }
    }

    @PostMapping("/creation")
    public ResponseEntity<?> creationUser(@RequestBody UserDTO userDTO){
        UserDTO dto = authenticationInterface.creationUser(userDTO);
        if(dto.getId() != 0){
            return ResponseEntity.ok(dto);
        }else{
            return ResponseEntity.internalServerError().body("Une erruer s'est produite lors de la création du compte ");
        }
    }
}
