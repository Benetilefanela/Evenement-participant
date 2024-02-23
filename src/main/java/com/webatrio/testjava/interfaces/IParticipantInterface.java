package com.webatrio.testjava.interfaces;

import com.webatrio.testjava.exceptions.EvenementException;
import com.webatrio.testjava.exceptions.ParticipantException;
import com.webatrio.testjava.mapStruct.ParticipantDTO;
import com.webatrio.testjava.models.Evenement;
import com.webatrio.testjava.models.Participant;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public interface IParticipantInterface {

     ParticipantDTO creation(ParticipantDTO participantDTO);

     void annulerParticipant(int idParticipant, int idEvenement);

     List<ParticipantDTO> afficherParticipantParEvenement(int idEv);

     Participant traitementParticipant(ParticipantDTO participantDTO) throws ParticipantException, EvenementException;

     Participant verifierSiParticipantExist(Participant participant) throws ParticipantException ;

     List<ParticipantDTO> afficherParticipantParPage(int page, int nombre);

     String verificationEmail(String email) throws ParticipantException;
     String verificationNom(String nom) throws ParticipantException;
     String verificationPrenom(String prenom) throws ParticipantException;

     void supprimerParticipant(int id);
}
