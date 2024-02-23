package com.webatrio.testjava.services;

import com.webatrio.testjava.exceptions.EvenementException;
import com.webatrio.testjava.interfaces.EvenementMapper;
import com.webatrio.testjava.interfaces.IEvenementInterface;
import com.webatrio.testjava.mapStruct.EvenementDTO;
import com.webatrio.testjava.models.Evenement;
import com.webatrio.testjava.repositories.EvenementRepository;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class EvenementServiceTest {

    @Autowired
    private IEvenementInterface iEvenementInterface;
    @Autowired
    private EvenementMapper evenementMapper;

    Logger logger = LoggerFactory.getLogger(EvenementServiceTest.class);
    @Autowired
    private EvenementRepository evenementRepository;

    @Test
    public void creationEvenementTest(){

        List<String> lieux = Arrays.asList("Bercy","Stade de france", "adidas arena", "parc de prince", "velodrome","Opera","saint lazarre","Milan","vatican",
                "les halles");

        lieux.stream().forEach(s -> {
            EvenementDTO evenement = EvenementDTO.builder().
                    description("Description pour l'evenement du test fonctionnel").lieu(s).
                    dateDebut(new Date(2024,06,30,20,00)).dateFin(new Date(2024,07,1,12,00)).capaciteMaximale(20000).build();
            iEvenementInterface.creationEvenement(evenement);
        });

        assertNotNull(evenementRepository.findAll());
    }

    @Test
    public void creationDUnEvenementExistant(){
        assertThrows(EvenementException.class,()->{
            EvenementDTO evenement = EvenementDTO.builder().
                    description("Description pour l'evenement du test fonctionnel").lieu("bercy").
                    dateDebut(new Date(2024,06,30,20,00)).dateFin(new Date(2024,07,1,12,00)).capaciteMaximale(20000).build();
            iEvenementInterface.creationEvenement(evenement);
            throw new EvenementException("Un événement portant la même déscription au même lieu et à la même date existe déjà");
        });
    }

    @Test
    public void modifierEvenementExistantTest() throws EvenementException {

        Random random = new Random();
        int id = random.nextInt(evenementRepository.findAll().size() - 1 + 1) + 1;
        logger.info("On récupre un Id aléaotoire ID : "+id);

        Evenement evenement = evenementRepository.findById(id).
                orElseThrow(()-> new EvenementException("Une erreur s'est produite lors de la recuperartion de l'evenement"));

        EvenementDTO dto = evenementMapper.toEvenementDto(evenement);

        int capacite = 60000;

        String description ="Modification de la déscription pour l'événment de Bercy "+new Date();

        dto.setCapaciteMaximale(capacite);

        dto.setDescription(description);

        iEvenementInterface.modifierUnEvenement(dto,id);

        Evenement update = evenementRepository.findById(id).
                orElseThrow(()-> new EvenementException("Une erreur s'est produite lors de la recuperartion de l'evenement"));

        assertEquals(update.getDescription(),description);
        assertEquals(capacite, update.getCapaciteMaximale());

    }

    @Test
    public void annulerUnEvenement() throws EvenementException {
        Random random = new Random();
        int id = random.nextInt(evenementRepository.findAll().size() - 1 + 1) + 1;
        logger.info("On récupere un Id aléaotoire ID : "+id);

       iEvenementInterface.annulerUnEvenement(id);
    }
    @Test
    public void afficherEvenemtParLieu() throws EvenementException {
        String lieu = "Stade de France";
        assertEquals(iEvenementInterface.findByLieuIgnoreCase(lieu).size(),1);

    }
}
