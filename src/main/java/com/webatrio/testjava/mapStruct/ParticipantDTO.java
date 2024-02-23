package com.webatrio.testjava.mapStruct;

import lombok.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ParticipantDTO {

    private int id;
    private String nom;
    private String prenom;
    private String email;

    @Builder.Default
    private Set<EvenementDTO> evenements = new HashSet<>();

}
