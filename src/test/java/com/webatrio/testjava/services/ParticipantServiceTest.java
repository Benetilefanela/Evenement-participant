package com.webatrio.testjava.services;

import com.webatrio.testjava.exceptions.EvenementException;
import com.webatrio.testjava.exceptions.ParticipantException;
import com.webatrio.testjava.interfaces.IEvenementInterface;
import com.webatrio.testjava.interfaces.IParticipantInterface;
import com.webatrio.testjava.mapStruct.EvenementDTO;
import com.webatrio.testjava.mapStruct.ParticipantDTO;
import com.webatrio.testjava.models.Evenement;
import com.webatrio.testjava.models.Participant;
import com.webatrio.testjava.repositories.EvenementRepository;
import com.webatrio.testjava.repositories.ParticipantRepository;
import com.webatrio.testjava.services.EvenementService;
import com.webatrio.testjava.services.ParticipantService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ParticipantServiceTest {

    @Autowired
    private IParticipantInterface iParticipantInterface;
    @Autowired
    private ParticipantRepository participantRepository;
    @Autowired
    private EvenementRepository evenementRepository;

    @Autowired
    private IEvenementInterface iEvenementInterface;



    @Test
    public void creerUnParticipant() throws EvenementException, ParticipantException {
        ParticipantDTO participantDTO = new ParticipantDTO();
        participantDTO.setEmail("test@gmail.comT");
        participantDTO.setNom("benny");
        participantDTO.setPrenom("test");
        ParticipantDTO saveDto = iParticipantInterface.creation(participantDTO);
        assertNotNull(saveDto.getId());
    }
    @Test
    public void creerUnParticipantExistant(){
        ParticipantDTO participantDTO = new ParticipantDTO();
        participantDTO.setEmail("test@gmail.com");
        participantDTO.setNom("benny");
        participantDTO.setPrenom("test");
        assertThrows(ParticipantException.class, ()->{
            iParticipantInterface.creation(participantDTO);
            throw new ParticipantException("Le participant renseigné existe déjà dans la base des donnée");
        });
    }

    @Test
    public void miseAJourParticipant() throws ParticipantException {
        Participant participant = participantRepository.findByEmail("test@gmail.com")
                .orElseThrow(()-> new ParticipantException("Une erreur s'est produite lors de la recupération du participant "));
        participant.setPrenom("benny");
        participant.setNom("web-atrio");

    }

   @Test
   public void afficherLesEvenementAuxQuelsUnParticipantEstInscrit() throws ParticipantException {
       Participant participant = participantRepository.findByEmail("test@gmail.com")
               .orElseThrow(()-> new ParticipantException("Une erreur s'est produite lors de la recupération du participant "));
       List<EvenementDTO> evenements = new ArrayList<>();

       if(!iEvenementInterface.afficherLesEvenementsParIdParticipant(participant.getId()).isEmpty()){
           evenements = iEvenementInterface.afficherLesEvenementsParIdParticipant(participant.getId());
       }

       System.out.println(evenements.size());
       assertFalse(evenements.isEmpty());
   }

}
