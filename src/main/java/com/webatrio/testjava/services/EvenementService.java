package com.webatrio.testjava.services;

import com.webatrio.testjava.exceptions.EvenementException;
import com.webatrio.testjava.exceptions.ParticipantException;
import com.webatrio.testjava.interfaces.EvenementMapper;
import com.webatrio.testjava.interfaces.IEvenementInterface;
import com.webatrio.testjava.mapStruct.EvenementDTO;
import com.webatrio.testjava.models.Evenement;
import com.webatrio.testjava.models.Participant;
import com.webatrio.testjava.repositories.EvenementRepository;
import com.webatrio.testjava.repositories.ParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class EvenementService implements IEvenementInterface {

    private final EvenementRepository evenementRepository;
    private final ParticipantRepository participantRepository;
    private final EvenementMapper evenementMapper;

    Logger logger = LoggerFactory.getLogger(EvenementService.class);

    @Override
    public Evenement traitementDeLEvenement(EvenementDTO evenementDTO){
        try {

            Evenement evenement = evenementMapper.toEvenement(evenementDTO);
            evenement.setDescription(verificationDelaDescription(evenement.getDescription()));
            evenement.setLieu(verificationLieu(evenement.getLieu()));
            evenement.setCapaciteMaximale(verificationCapaciteMaximale(evenement.getCapaciteMaximale()));
            evenement.setDateDebut(verificationDateDebut(evenement.getDateDebut()));
            evenement.setDateFin(verificationDateFin(evenement.getDateDebut(),evenement.getDateFin()));
            return evenement;
        }catch (EvenementException e){
            logger.error(e.getMessage());
        }
        return null;
    }
    @Override
    public EvenementDTO creationEvenement(EvenementDTO evenementDTO){
        try {
            logger.info("Tentaive de création d'un événement");
            Evenement evenement = traitementDeLEvenement(evenementDTO);
            if(evenementRepository.findByDescriptionIgnoreCaseAndLieuIgnoreCaseAndDateDebutEqualsAndDateFinEquals(evenement.getDescription(),
                    evenement.getLieu(),evenement.getDateDebut(),evenement.getDateFin()).isEmpty()){
                evenement.setDescription(evenement.getDescription().toLowerCase());

                evenementRepository.save(evenement);

                logger.info("Fin de la création de l'événement avec succès");

                return evenementMapper.toEvenementDto(evenement);
            }else{
                throw new EvenementException("Un événement portant la même déscription au même lieu et à la même date existe déjà");
            }
        }catch (EvenementException e){
            logger.error(e.getMessage(),e);
        }
        return null;

    }
    @Override
    public EvenementDTO modifierUnEvenement(EvenementDTO updateEvenementDTO, int id) {
        Evenement updateEvent =traitementDeLEvenement(updateEvenementDTO);
        try {
            logger.info("Tentative de modification d'un événement ");
            Optional<Evenement> evenement = Optional.ofNullable(evenementRepository.findById(id)
                    .orElseThrow(() -> new EvenementException("Une erreur s'est produite lors la récupération de l'evenement")));


            if(evenementRepository.findByDescriptionIgnoreCaseAndLieuIgnoreCaseAndDateDebutEqualsAndDateFinEquals(updateEvent.getDescription(),
                    updateEvent.getLieu(),updateEvent.getDateDebut(),updateEvent.getDateFin()).isEmpty()){
                updateEvent.setId(id);
                evenementRepository.save(updateEvent);

                Evenement event = evenementRepository.findById(id)
                        .orElseThrow(() -> new EvenementException("Une erreur s'est produite lors la récupération de l'evenement"));
                logger.info("Fin de la mise à jour de l'événemnt");
                return evenementMapper.toEvenementDto(event);
            }

        } catch (EvenementException e) {
            logger.error(e.getMessage(),e);
        }
        return updateEvenementDTO;
    }
    @Override
    public void annulerUnEvenement(int id){
        try {
            Optional<Evenement> evenement = Optional.ofNullable(evenementRepository.findById(id).
                    orElseThrow(() -> new EvenementException("Une erreur s'est produite lors la récupération de l'evenement")));
            evenementRepository.deleteById(id);
            logger.info("Annulation de l'événement a été éffectuée avec succès");
        }catch (EvenementException e){
            logger.error(e.getMessage(),e);
        }

    }

    @Override
    public EvenementDTO retirerUnParticipant(int idEvent, int idPart) {
        try {
            Participant participant = participantRepository.findById(idPart)
                    .orElseThrow(()-> new ParticipantException("Une erreur s'est produite lors de la récupération du participant"));

            Evenement evenement = evenementRepository.findById(idEvent)
                    .orElseThrow(()-> new EvenementException("Une erreur s'est produite lors de la récuperation de l'évènement"));
            if(participantRepository.findByIdAndEvenementsId(idPart,idEvent).isPresent()){
                Set<Participant> participants = participantRepository.findByEvenementsId(idEvent);
                if(participants!=null) participants.remove(participant);
                evenement.setParticipants(participants);
                evenementRepository.save(evenement);
                return evenementMapper.toEvenementDto(evenement);
            }else{
                throw new EvenementException("Le participant n'est pas inscrit à l'évènement");
            }
        }catch (EvenementException | ParticipantException e){
            logger.error(e.getMessage(),e);
        }

        return null;
    }

    @Override
    public List<EvenementDTO> afficherLesEvenementsParIdParticipant(int id){
        List<EvenementDTO> dtos = new ArrayList<>();
        for (Evenement evenement : evenementRepository.findByParticipantsId(id)){
            EvenementDTO dto = evenementMapper.toEvenementDto(evenement);
            dtos.add(dto);
        }
        return dtos;
    }
    @Override
    public List<EvenementDTO> afficherLesEvenementAVenir(){
        Date now = new Date();
        List<EvenementDTO> dtos = new ArrayList<>();
        for (Evenement evenement : evenementRepository.findByDateDebutAfter(now)){
            EvenementDTO dto = evenementMapper.toEvenementDto(evenement);
            dtos.add(dto);
        }
        return dtos;
    }
    @Override
    public Optional<EvenementDTO> findById(int id ) throws EvenementException {
        Optional<Evenement> evenement = Optional.ofNullable(evenementRepository.findById(id).orElseThrow(() -> new EvenementException("")));
        EvenementDTO evenementDTO = evenementMapper.toEvenementDto(evenement.get());
        return Optional.ofNullable(evenementDTO);
    }
    @Override
    public List<EvenementDTO> findByLieuIgnoreCase(String lieu){
        List<EvenementDTO> dtos = new ArrayList<>();
        for (Evenement evenement : evenementRepository.findByLieuIgnoreCase(lieu)){
            EvenementDTO dto = evenementMapper.toEvenementDto(evenement);
            dtos.add(dto);
        }
        return dtos;
    }
    @Override
    public List<EvenementDTO> findAll(){
        List<EvenementDTO> evenementDTOS = new ArrayList<>();
        for (Evenement evenement : evenementRepository.findAll()){
            EvenementDTO dto = evenementMapper.toEvenementDto(evenement);
            evenementDTOS.add(dto);
        }
        return evenementDTOS;
    }
    @Override
    public String verificationDelaDescription(String description) throws EvenementException{
        if(!description.isEmpty()){
            return description;
        } throw
                new EvenementException("Impossible de poursuivre la création l'évenement car aucune déscription ne renseignée");
    }
    @Override
    public String verificationLieu(String lieu) throws EvenementException{
        if(!lieu.isEmpty()){
            return lieu;
        }throw new EvenementException("Impossible de poursuivre la création l'évenement car aucun lieu ne renseigné");
    }
    @Override
    public int verificationCapaciteMaximale(int capacite) throws EvenementException{
        if(capacite > 0){
            return capacite;
        }throw new EvenementException("Impossible de poursuivre la création de l'événment car la capacité maximale ne renseignée");
    }
    @Override
    public Date verificationDateDebut(Date date) throws EvenementException{
        Date now = new Date();
        if(date != null && date.after(now)){
            return date;
        }throw new EvenementException("Une erreur s'est produite car la date de début de l'événmént n'est pas correcte");
    }
    @Override
    public Date verificationDateFin(Date debut, Date fin) throws EvenementException{
        if(fin != null && fin.after(debut)){
            return fin;
        }throw new EvenementException("Une erreur s'est produite lors de la validation de la date de fin ");
    }

}
