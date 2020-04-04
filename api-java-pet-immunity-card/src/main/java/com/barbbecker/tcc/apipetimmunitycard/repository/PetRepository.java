package com.barbbecker.tcc.apipetimmunitycard.repository;

import com.barbbecker.tcc.apipetimmunitycard.domain.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PetRepository extends JpaRepository<Pet, Integer> {

    @Query("SELECT p FROM Pet p WHERE p.numberChip = :numberChip")
    Optional<Pet> findByNumberChip(@Param("numberChip") String numberChip);

    @Query("SELECT p FROM Pet p LEFT JOIN FETCH p.vaccines WHERE p.id = :id")
    Optional<Pet> findPetWithVaccines(@Param("id") Integer id);

    List<Pet> findAllByPersonId(@Param("personId") Integer personId);
}
