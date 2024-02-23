package com.webatrio.testjava.models;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RequestLogin {

    private String username;
    private String password;

    private Role role;
}
