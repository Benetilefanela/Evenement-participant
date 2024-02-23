package com.webatrio.testjava.controllers;

import com.webatrio.testjava.exceptions.EvenementException;
import com.webatrio.testjava.mapStruct.EvenementDTO;
import com.webatrio.testjava.models.Evenement;
import com.webatrio.testjava.repositories.EvenementRepository;
import com.webatrio.testjava.services.EvenementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/evenement")
public class EvenementController {

    @Autowired
    private EvenementRepository evenementRepository;

    @Autowired
    private EvenementService evenementService;


    @PostMapping("/add")
    @PreAuthorize("hasRole('ROLE_ORGANISATEUR')")
    public ResponseEntity<?> creation(@RequestBody EvenementDTO evenement, BindingResult result) {
        if(!result.hasErrors()){
            EvenementDTO dto = evenementService.creationEvenement(evenement);
            return ResponseEntity.ok(dto);
        }else{
            return ResponseEntity.badRequest().body("Une erreur s'est produite lors la création de l'événement ");
        }
    }

    @GetMapping("/")
    public ResponseEntity<List<?>>afficherTousLesEvenements(){
        List<EvenementDTO>evenements = evenementService.findAll();
        return ResponseEntity.ok(evenements);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> afficherEvenementparId(@PathVariable("id") int id) throws EvenementException {
        if(evenementRepository.findById(id).isPresent()){
            EvenementDTO dto = evenementService.findById(id).orElseThrow(()-> new EvenementException("Une erreur s'est produite lors de la recuperationb"));
            return ResponseEntity.ok(dto);
        }
        return null;
    }

    @GetMapping("/participant/{id}")
    public ResponseEntity<List<?>> affciherLesEvenementParIdParticipant(@PathVariable("id") int id ){
        List<EvenementDTO> dtos = evenementService.afficherLesEvenementsParIdParticipant(id);
        return ResponseEntity.ok(dtos);
    }
    @GetMapping("/lieu/{lieu}")
    public ResponseEntity<?> afficherLesEvenemenParLieu(@PathVariable("lieu") String lieu){
        List<EvenementDTO> dtos = new ArrayList<>();
        if(!evenementRepository.findByLieuIgnoreCase(lieu).isEmpty()){
            dtos = evenementService.findByLieuIgnoreCase(lieu);
            return ResponseEntity.ok(dtos);
        }
        return null;
    }

    @GetMapping("/avenir")
    public ResponseEntity<List<?>> afficherLesEvenementAVenir(){
        List<EvenementDTO> dtos = evenementService.afficherLesEvenementAVenir();
        return ResponseEntity.ok(dtos);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ROLE_ORGANISATEUR')")
    public EvenementDTO modifier(@RequestBody EvenementDTO evenement, @PathVariable("id") int id ){

        return evenementService.modifierUnEvenement(evenement, id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ORGANISATEUR')")
    public void annulerUnEvenement(@PathVariable("id") int id){
        evenementService.annulerUnEvenement(id);
    }


}
