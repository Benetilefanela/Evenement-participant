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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InscriptionService implements InscriptionInterface {

    @Autowired
    private IParticipantInterface iParticipantInterface;
    @Autowired
    private IEvenementInterface iEvenementInterface;
    private final ParticipantRepository participantRepository;
    private final EvenementRepository evenementRepository;
    private final EvenementMapper evenementMapper;
    private final ParticipantMapper participantMapper;
    Logger logger = LoggerFactory.getLogger(InscriptionService.class);
    @Override
    public ParticipantDTO inscrir(ParticipantDTO participantDTO){
        try {

            if(participantDTO.getEvenements()!=null){
                participantDTO.getEvenements().stream().
                    forEach(evenementDTO -> {
                         verifierSiLeParticipantNestPasInscritAvantLInscription(participantDTO.getId(), evenementDTO.getId());
                    });
                Participant participant = participantRepository.findById(participantDTO.getId()).orElseThrow(()-> new ParticipantException("Une erreur s'est produite lors de la récupération du participant"));
                return participantMapper.toParticipantDto(participant);
            }
            throw new EvenementException("Aucun événement n'est renseigné rattachée au participant");
        }catch (EvenementException | ParticipantException e ){
            logger.error(e.getMessage(),e);
        }
        return participantDTO;
    }

    @Override
    public EvenementDTO verifierSiLeParticipantNestPasInscritAvantLInscription(int idPart, int idEvent) {
        try {
            if(participantRepository.findByIdAndEvenementsId(idPart, idEvent).isEmpty()){
                System.out.println("Evenenemt : "+idEvent);
                Participant participant = participantRepository.findById(idPart)
                        .orElseThrow(()-> new ParticipantException("Une erreur s'est produite lors de la récupération du participant"));
                Evenement evenement = evenementRepository.findById(idEvent)
                        .orElseThrow(()-> new EvenementException("Une erreur s'est produite lors de la récuperation de l'évènement"));

                participant.getEvenements().add(evenement);
                evenement.getParticipants().add(participant);

                evenementRepository.save(evenement);
                return evenementMapper.toEvenementDto(evenement);
            }
        }catch (EvenementException | ParticipantException e ){
            logger.error(e.getMessage(),e);
        }
        return null;
    }

    @Override
    @Transactional
    public List<EvenementDTO> annulerInscription(int idPart, int idEvent) throws EvenementException, ParticipantException {
        Evenement evenement = evenementRepository.findById(idEvent)
                .orElseThrow(()-> new EvenementException("Une erreur s'est produite lors de la récuperation de l'évènement"));
        iEvenementInterface.retirerUnParticipant(idEvent,idPart);
        return iEvenementInterface.afficherLesEvenementsParIdParticipant(idPart);
        //return participantRepository.findByEvenementsId(idEvent).stream().map(participantMapper::toParticipantDto).toList();
    }
}
