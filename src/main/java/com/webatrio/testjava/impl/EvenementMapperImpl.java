package com.webatrio.testjava.impl;

import com.webatrio.testjava.interfaces.EvenementMapper;
import com.webatrio.testjava.mapStruct.EvenementDTO;
import com.webatrio.testjava.models.Evenement;
import com.webatrio.testjava.repositories.EvenementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EvenementMapperImpl implements EvenementMapper {

    @Autowired
    private EvenementRepository evenementRepository;
    @Override
    public Evenement toEvenement(EvenementDTO evenementDTO) {
        if(evenementDTO == null){
            return null;
        }
        Evenement.EvenementBuilder evenement = Evenement.builder();
        evenement.id(evenementDTO.getId());
        evenement.description(evenementDTO.getDescription());
        evenement.lieu(evenementDTO.getLieu());
        evenement.dateFin(evenementDTO.getDateFin());
        evenement.dateDebut(evenementDTO.getDateDebut());
        evenement.capaciteMaximale(evenementDTO.getCapaciteMaximale());

        return evenement.build();
    }

    @Override
    public EvenementDTO toEvenementDto(Evenement evenement) {
        if(evenement == null){
            return null;
        }

        EvenementDTO.EvenementDTOBuilder evenementDTO = EvenementDTO.builder();
        evenementDTO.id(evenement.getId());
        evenementDTO.description(evenement.getDescription());
        evenementDTO.dateFin(evenement.getDateFin());
        evenementDTO.dateDebut(evenement.getDateDebut());
        evenementDTO.capaciteMaximale(evenement.getCapaciteMaximale());
        evenementDTO.lieu(evenement.getLieu());


        return evenementDTO.build();
    }

    @Override
    public void copy(EvenementDTO evenementDTO, Evenement evenement) {

    }
}
