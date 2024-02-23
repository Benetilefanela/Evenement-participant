package com.webatrio.testjava.repositories;

import com.webatrio.testjava.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    Optional<Role> findByNomIgnoreCase(String nom);
}
