package com.webatrio.testjava.controllers;

import com.webatrio.testjava.exceptions.EvenementException;
import com.webatrio.testjava.exceptions.ParticipantException;
import com.webatrio.testjava.interfaces.IParticipantInterface;
import com.webatrio.testjava.mapStruct.ParticipantDTO;
import com.webatrio.testjava.models.Participant;
import com.webatrio.testjava.repositories.ParticipantRepository;
import com.webatrio.testjava.services.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/participant")
public class ParticipantController {

    @Autowired
    private ParticipantRepository participantRepository;

    private IParticipantInterface iParticipantInterface;

    @PostMapping("/add")
    public ResponseEntity<?> creation(@RequestBody ParticipantDTO participantDTO, BindingResult result) {
        if(!result.hasErrors()){
            ParticipantDTO dto = iParticipantInterface.creation(participantDTO);
            return ResponseEntity.ok(dto);
        }else{
            return ResponseEntity.badRequest().body("Une erreur s'est produite lors de la création du participant");
        }
    }

    @GetMapping("/evenement/{id}")
    public ResponseEntity<List<?>> afficherLesParticipantParEvenement(@PathVariable("id") int id ){
        List<ParticipantDTO> participants = iParticipantInterface.afficherParticipantParEvenement(id);
        return ResponseEntity.ok(participants);
    }

    @GetMapping("/number/{nb}/page/{page}")
    @PreAuthorize("hasRole('ROLE_ORGANISATEUR')")
    public ResponseEntity<List<?>> afficherTousLesParticipants(@PathVariable("nb") int nb, @PathVariable("page") int page){
        return ResponseEntity.ok(iParticipantInterface.afficherParticipantParPage(page,nb));
    }

    @DeleteMapping("/{id}")
    public void supprimerParticipant(@PathVariable("id") int id ){
        iParticipantInterface.supprimerParticipant(id);
    }

}
