package com.webatrio.testjava.services;

import com.webatrio.testjava.exceptions.EvenementException;
import com.webatrio.testjava.exceptions.ParticipantException;
import com.webatrio.testjava.interfaces.IParticipantInterface;
import com.webatrio.testjava.interfaces.ParticipantMapper;
import com.webatrio.testjava.mapStruct.ParticipantDTO;
import com.webatrio.testjava.mapStruct.UserDTO;
import com.webatrio.testjava.models.Evenement;
import com.webatrio.testjava.models.Participant;
import com.webatrio.testjava.repositories.EvenementRepository;
import com.webatrio.testjava.repositories.ParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ParticipantService implements IParticipantInterface {

    private final ParticipantRepository participantRepository;
    private final EvenementRepository evenementRepository;
    Logger logger = LoggerFactory.getLogger(ParticipantService.class);

    private final EvenementService evenementService;
    private final ParticipantMapper participantMapper;
    private final AuthenticationService authenticationService;

    @Override
    public ParticipantDTO creation(ParticipantDTO participantDTO){

        try {
            Participant participant = traitementParticipant(participantDTO);
            if (participantRepository.findByEmail(participant.getEmail()).isPresent()) {
                throw new ParticipantException("Le participant existe déjà dans la base des données");
            }
            participantRepository.save(participant);
            logger.info("Fin de la sauvegarde du participant portant l'ID "+participant.getId());

            UserDTO userDTO = UserDTO.builder().username(participantDTO.getEmail()).build();
            return participantMapper.toParticipantDto(participant);
        }catch (ParticipantException | EvenementException e){
            logger.error(e.getMessage(),e);
        }
        return null;
    }

    @Override
    public void annulerParticipant(int idParticipant, int idEvenement){
        try {
            if(participantRepository.findByIdAndEvenementsId(idParticipant, idEvenement).isPresent()){
                Participant participant = participantRepository.findById(idParticipant)
                        .orElseThrow(()-> new ParticipantException("Une erreur s'est produite lors de la récuperation du participant"));

               // participantRepository.save(participant);

                logger.info("Fin de la suppression de l'évènement ");
            }
        }catch (ParticipantException e){
            logger.error(e.getMessage(),e);
        }

    }
    @Override

    public List<ParticipantDTO> afficherParticipantParEvenement(int idEv){
        List<ParticipantDTO> dtos = new ArrayList<>();
        for (Participant participant : participantRepository.findByEvenementsId(idEv)){
            ParticipantDTO dto = participantMapper.toParticipantDto(participant);
            dtos.add(dto);
        }
        return dtos;
    }
    @Override

    public Participant traitementParticipant(ParticipantDTO participantDTO) throws ParticipantException, EvenementException {

        Participant participant = participantMapper.toParticipant(participantDTO);

        participant = verifierSiParticipantExist(participant);

        participant.setEmail(verificationEmail(participantDTO.getEmail()));
        participant.setNom(verificationNom(participantDTO.getNom()));
        participant.setPrenom(verificationPrenom(participantDTO.getPrenom()));
        /**if(participant.getEvenements()!= null){
            participant.setEvenements(participant.getEvenements().stream().
                    map(evenement -> evenementRepository.findById(evenement.getId())).flatMap(Optional::stream).collect(Collectors.toList()));
        }**/
        return participant;
    }
    @Override

    public Participant verifierSiParticipantExist(Participant participant) throws ParticipantException {
        if (participantRepository.findByEmail(participant.getEmail()).isPresent()){
            return participantRepository.findByEmail(participant.getEmail())
                    .orElseThrow(()-> new ParticipantException("Une erreur s'est produite lors de la récupération du participant"));
        }else{
            return participant;
        }
    }

    @Override
    public List<ParticipantDTO> afficherParticipantParPage(int page, int nombre) {
        PageRequest pageable = PageRequest.of(page,nombre);
        return participantRepository.findAll(pageable).stream()
                .map(participantMapper::toParticipantDto).collect(Collectors.toList());
    }


    @Override

    public String verificationEmail(String email) throws ParticipantException{
        if(!email.isEmpty()){
            return email;
        }throw new ParticipantException("Une erreur lors de la création d'un participant car son email est vide ");
    }
    @Override

    public String verificationNom(String nom) throws ParticipantException{
        if(!nom.isEmpty()){
            return nom;
        }throw new ParticipantException("Une erreur lors de la création d'un participant car son nom n'est pas renseigné");
    }
    @Override
    public String verificationPrenom(String prenom) throws ParticipantException{
        if(!prenom.isEmpty()){
            return prenom;
        }throw new ParticipantException("Une erreur lors de la création d'un participant car son prénom n'est pas renseigné ");
    }

    @Override
    public void supprimerParticipant(int id) {
        try {
            if(participantRepository.findById(id).isPresent()){
                Participant participant = participantRepository.findById(id)
                        .orElseThrow(()-> new ParticipantException("Une erreur s'est produite lors de la récuperation du participant"));

                participant.setEvenements(null);
                participantRepository.delete(participant);

                logger.info("Fin de la suppression du participant  ");
            }
        }catch (ParticipantException e){
            logger.error(e.getMessage(),e);
        }
    }

    public Evenement verificationEvenement(Evenement evenement) throws EvenementException{
        if(evenementRepository.findById(evenement.getId()).isPresent()){
            return evenementRepository.findById(evenement.getId()).orElseThrow(()-> new EvenementException("Une erreur s'est produite lors de la récupération de de l'événement "));
        }
        throw new EvenementException("Impossible de poursuivre car l'evenement est manquant");
    }

}
