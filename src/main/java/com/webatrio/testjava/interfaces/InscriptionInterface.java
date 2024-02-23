package com.webatrio.testjava.interfaces;

import com.webatrio.testjava.exceptions.EvenementException;
import com.webatrio.testjava.exceptions.ParticipantException;
import com.webatrio.testjava.mapStruct.EvenementDTO;
import com.webatrio.testjava.mapStruct.ParticipantDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface InscriptionInterface {

    ParticipantDTO inscrir(ParticipantDTO participant) throws EvenementException;

    EvenementDTO verifierSiLeParticipantNestPasInscritAvantLInscription(int idPart, int idEvent) throws ParticipantException, EvenementException;
    List<EvenementDTO> annulerInscription(int idPart, int idEvent) throws EvenementException, ParticipantException;


}
