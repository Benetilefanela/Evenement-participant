package com.webatrio.testjava.mapStruct;

import com.webatrio.testjava.models.Role;
import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Data
public class UserDTO {

    private int id;
    private String username;
    private String password;

    private Role role;

    private ParticipantDTO participant;
}
