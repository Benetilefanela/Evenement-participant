package com.webatrio.testjava.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "evenement")
public class Evenement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "evenement_id")
    private int id;
    private String description;
    private Date dateDebut;
    private Date dateFin;
    private String lieu;
    @NotNull
    private int capaciteMaximale;

    @ManyToMany(mappedBy = "evenements",fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonIgnoreProperties("{evenements}")
    private Set<Participant> participants = new HashSet<>();


}
