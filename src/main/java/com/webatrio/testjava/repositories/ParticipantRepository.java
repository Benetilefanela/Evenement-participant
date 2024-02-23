package com.webatrio.testjava.repositories;

import com.webatrio.testjava.models.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Integer> {

    Optional<Participant> findByEmailAndEvenementsId(String email, int id );

    Set<Participant> findByEvenementsId(int id);

    Optional<Participant> findByEmail(String email);

    Optional<Participant> findByIdAndEvenementsId(int idParticipant, int IdEvenement);
}
