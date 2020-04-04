package com.barbbecker.tcc.apipetimmunitycard.repository;

import com.barbbecker.tcc.apipetimmunitycard.domain.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Integer> {

    @Query("SELECT p FROM Person p WHERE p.cpf = :cpf")
    Optional<Person> findByCpf(@Param("cpf") String cpf);

    @Query("SELECT p FROM Person p WHERE p.email = :email and p.password = :password")
    Person findByEmailAndPassword(@Param("email") String email,
                                  @Param ("password") String password);
}
