package com.webatrio.testjava.mapStruct;

import lombok.*;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class EvenementDTO {

    private int id;
    private String description;
    private Date dateDebut;
    private Date dateFin;
    private String lieu;
    private int capaciteMaximale;

    @Builder.Default
    private Set<ParticipantDTO> participant = new HashSet<>();

}
