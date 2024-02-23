package com.webatrio.testjava.services;

import com.webatrio.testjava.exceptions.EvenementException;
import com.webatrio.testjava.exceptions.ParticipantException;
import com.webatrio.testjava.interfaces.*;
import com.webatrio.testjava.mapStruct.EvenementDTO;
import com.webatrio.testjava.mapStruct.ParticipantDTO;
import com.webatrio.testjava.models.Evenement;
import com.webatrio.testjava.models.Participant;
import com.webatrio.testjava.repositories.EvenementRepository;
import com.webatrio.testjava.repositories.ParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.BooleanSupplier;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class InscriptionServiceTest {

    @Autowired
    private IParticipantInterface iParticipantInterface;
    @Autowired
    private IEvenementInterface iEvenementInterface;
    @Autowired
    private InscriptionInterface inscriptionInterface;
    @Autowired
    private ParticipantRepository participantRepository;
    @Autowired
    private EvenementRepository evenementRepository;
    @Autowired
    private EvenementMapper evenementMapper;
    @Autowired
    private ParticipantMapper participantMapper;

    Logger logger = LoggerFactory.getLogger(InscriptionServiceTest.class);

    @Test
    public void inscrirUnParticipantAUnEvenement() throws ParticipantException, EvenementException {
        Random random = new Random();
        int idPart = random.nextInt( participantRepository.findAll().size() - 1 + 1) + 1;
        Participant participant = participantRepository.findById(idPart)
                .orElseThrow(()-> new ParticipantException("Une erreur s'est produite lors de la récupération du participant : " +idPart));
        ParticipantDTO dto = participantMapper.toParticipantDto(participant);


        int idEvent = random.nextInt(evenementRepository.findAll().size()- 1 + 1) + 1;
        Evenement evenement = evenementRepository.findById(idEvent)
                .orElseThrow(()-> new EvenementException("Une erreur s'est produite lors de la récupération du participant"));

        EvenementDTO evenementDTO = evenementMapper.toEvenementDto(evenement);
        dto.getEvenements().add(evenementDTO);
        inscriptionInterface.inscrir(dto);

        participant = participantRepository.findById(idPart)
                .orElseThrow(()-> new ParticipantException("Une erreur s'est produite lors de la récupération du participant"));
        System.out.println(participant.getEvenements().size());
        assertNotNull(participant.getEvenements());

    }

    @Test
    public void afficherLesParticipantInscritAUnEvenement(){
        iParticipantInterface.afficherParticipantParEvenement(2).stream().forEach(participantDTO -> System.out.println(participantDTO.getPrenom()));
        assertNotEquals(iParticipantInterface.afficherParticipantParEvenement(2).size(), participantRepository.findAll().size());
    }

    @Test
    public void afficherLesEvenementsInscritParUnParticipant(){
        logger.info("Lecture des evenements où le participant est inscrit");
        iEvenementInterface.afficherLesEvenementsParIdParticipant(2).stream()
                .forEach(evenementDTO -> System.out.println("Lieu : "+evenementDTO.getLieu()+" descr: "+evenementDTO.getDescription()));
        assertNotEquals(iEvenementInterface.afficherLesEvenementsParIdParticipant(2).size(),evenementRepository.findAll().size());
    }

    @Test
    public void annulerLInscriptionDUnParticipantAUnEvenement() throws ParticipantException, EvenementException {
        Random random = new Random();
        int idPart = random.nextInt( participantRepository.findAll().size() - 1 + 1) + 1;
        int idEvent = random.nextInt(evenementRepository.findAll().size()- 1 + 1) + 1;
        Participant participant = participantRepository.findById(idPart)
                .orElseThrow(()-> new ParticipantException("Une erreur s'est produite lors de la récupération du participant : " +idPart));
        ParticipantDTO dto = participantMapper.toParticipantDto(participant);
        Evenement evenement = evenementRepository.findById(idEvent)
                .orElseThrow(()-> new EvenementException("Une erreur s'est produite lors de la récupération du participant"));

        EvenementDTO evenementDTO = evenementMapper.toEvenementDto(evenement);
        dto.getEvenements().add(evenementDTO);

        inscriptionInterface.inscrir(dto);

        inscriptionInterface.annulerInscription(idPart,idEvent);

        assertFalse(participantRepository.findByIdAndEvenementsId(idPart,idEvent).isEmpty());

    }


}
