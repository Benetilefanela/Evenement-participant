package com.webatrio.testjava.controllers;

import com.webatrio.testjava.exceptions.EvenementException;
import com.webatrio.testjava.exceptions.ParticipantException;
import com.webatrio.testjava.interfaces.*;
import com.webatrio.testjava.mapStruct.EvenementDTO;
import com.webatrio.testjava.mapStruct.ParticipantDTO;
import com.webatrio.testjava.models.Evenement;
import com.webatrio.testjava.models.Participant;
import com.webatrio.testjava.repositories.EvenementRepository;
import com.webatrio.testjava.repositories.ParticipantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.w3c.dom.events.EventException;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/inscription")
public class InscriptionController {

    @Autowired
    private IParticipantInterface iParticipantInterface;
    @Autowired
    private IEvenementInterface iEvenementInterface;
    @Autowired
    private InscriptionInterface inscriptionInterface;
    @Autowired
    private ParticipantMapper participantMapper;
    @Autowired
    private EvenementMapper evenementMapper;
    @Autowired
    private ParticipantRepository participantRepository;
    @Autowired
    private EvenementRepository evenementRepository;

    @GetMapping("/evenement/{id}")
    @PreAuthorize("hasRole('ROLE_PARTICIPANT')")
    public ResponseEntity<?> inscrireParticipant(@PathVariable ("id") int idEvent,Principal principal) throws EvenementException, ParticipantException {
        Participant participant = participantRepository.findByEmail(principal.getName())
                .orElseThrow(()-> new ParticipantException("Une erreur s'est produite lors de la recupération"));
        ParticipantDTO dto = participantMapper.toParticipantDto(participant);

        System.out.println(evenementRepository.findById(idEvent).isPresent());
        if(evenementRepository.findById(idEvent).isPresent() && evenementRepository.findByIdAndParticipantsId(idEvent, participant.getId()).isEmpty()){
            Evenement evenement = evenementRepository.findById(idEvent).orElseThrow(()-> new EvenementException("Une s'est produite lors de la recupératio"));
            dto.getEvenements().add(evenementMapper.toEvenementDto(evenement));
            return ResponseEntity.ok(inscriptionInterface.inscrir(dto));
        }
        throw new EvenementException("Une erreur s'est produite avec de l'inscrption ");
    }

    @GetMapping("/mes-inscriptions")
    @PreAuthorize("hasRole('ROLE_PARTICIPANT')")
    public ResponseEntity<List<?>> mesInscriptions(Principal principal) throws ParticipantException {
        Participant participant = participantRepository.findByEmail(principal.getName())
                .orElseThrow(()-> new ParticipantException("Une erreur s'est produite lors de la recupération"));
        return ResponseEntity.ok(iEvenementInterface.afficherLesEvenementsParIdParticipant(participant.getId()));
    }

    @DeleteMapping("/annuler")
    @PreAuthorize("hasRole('ROLE_PARTICIPANT')")
    public ResponseEntity<List<?>> annulerInscription(@PathVariable("idEvent") int idEvent,Principal principal) throws EvenementException, ParticipantException {
        Participant participant = participantRepository.findByEmail(principal.getName())
                .orElseThrow(()-> new ParticipantException("Une erreur s'est produite lors de la recupération"));
        return ResponseEntity.ok(inscriptionInterface.annulerInscription(participant.getId(), idEvent));
    }

}
