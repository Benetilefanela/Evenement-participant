package com.webatrio.testjava.interfaces;

import com.webatrio.testjava.exceptions.EvenementException;
import com.webatrio.testjava.exceptions.ParticipantException;
import com.webatrio.testjava.mapStruct.EvenementDTO;
import com.webatrio.testjava.models.Evenement;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Component
public interface IEvenementInterface {
    Evenement traitementDeLEvenement(EvenementDTO evenement) throws EvenementException;
    EvenementDTO creationEvenement(EvenementDTO evenement);
    EvenementDTO modifierUnEvenement(EvenementDTO updateEvenement, int id);
    void annulerUnEvenement(int id);

    EvenementDTO retirerUnParticipant(int idEvent, int idPart) throws ParticipantException, EvenementException;
    List<EvenementDTO> afficherLesEvenementsParIdParticipant(int id);

   List<EvenementDTO> afficherLesEvenementAVenir();

    Optional<EvenementDTO> findById(int id )throws EvenementException;

    List<EvenementDTO> findByLieuIgnoreCase(String lieu);
    List<EvenementDTO> findAll();

    String verificationDelaDescription(String description)throws EvenementException;

    String verificationLieu(String lieu)throws EvenementException;

    int verificationCapaciteMaximale(int capacite)throws EvenementException;

    Date verificationDateDebut(Date date)throws EvenementException;

    Date verificationDateFin(Date debut, Date fin)throws EvenementException;


}
