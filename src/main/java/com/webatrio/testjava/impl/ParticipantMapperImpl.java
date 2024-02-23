package com.webatrio.testjava.impl;

import com.webatrio.testjava.interfaces.EvenementMapper;
import com.webatrio.testjava.interfaces.ParticipantMapper;
import com.webatrio.testjava.mapStruct.EvenementDTO;
import com.webatrio.testjava.mapStruct.ParticipantDTO;
import com.webatrio.testjava.models.Evenement;
import com.webatrio.testjava.models.Participant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ParticipantMapperImpl implements ParticipantMapper {

    @Autowired
    private EvenementMapper evenementMapper;

    @Override
    public Participant toParticipant(ParticipantDTO participantDTO) {
        if(participantDTO == null) {
            return null;
        }

        Participant.ParticipantBuilder participant = Participant.builder();
        participant.id(participantDTO.getId());
        participant.email(participantDTO.getEmail());
        participant.prenom(participantDTO.getPrenom());
        participant.nom(participantDTO.getNom());

        System.out.println(participantDTO.getEvenements().size());

        if(participantDTO.getEvenements()!= null){
            Set<Evenement> evenements = participantDTO.getEvenements().stream()
                    .map(evenementDTO -> evenementMapper.toEvenement(evenementDTO)).collect(Collectors.toSet());
            participant.evenements(evenements);
        }
        return participant.build();
    }

    @Override
    public ParticipantDTO toParticipantDto(Participant participant){
        if(participant == null){
            return null;
        }
        ParticipantDTO.ParticipantDTOBuilder participantDto = ParticipantDTO.builder();
        participantDto.id(participant.getId());
        participantDto.nom(participant.getNom());
        participantDto.prenom(participant.getPrenom());
        participantDto.email(participant.getEmail());

        if(participant.getEvenements() != null){
            Set<EvenementDTO> evenements = participant.getEvenements().stream()
                    .map(evenement -> evenementMapper.toEvenementDto(evenement)).collect(Collectors.toSet());
            participantDto.evenements(evenements);
        }

        return participantDto.build();
    }
}
