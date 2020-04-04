package com.barbbecker.tcc.apipetimmunitycard.repository;

import com.barbbecker.tcc.apipetimmunitycard.domain.Vaccine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VaccineRepository extends JpaRepository<Vaccine, Integer> {

}
