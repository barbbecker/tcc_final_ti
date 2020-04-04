package com.barbbecker.tcc.apipetimmunitycard.controller;

import com.barbbecker.tcc.apipetimmunitycard.domain.Success;
import com.barbbecker.tcc.apipetimmunitycard.dto.PersonDto;
import com.barbbecker.tcc.apipetimmunitycard.dto.PersonLoginDto;
import com.barbbecker.tcc.apipetimmunitycard.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "api")
public class PersonController {

    private PersonService personService;

    @Autowired
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping(value = "/people")
    public ResponseEntity<List<PersonDto>> getPeople() {
        List<PersonDto> person = personService.findAll();
        return new ResponseEntity<>(person, HttpStatus.OK);
    }

    @GetMapping(value = "/person/{cpf}")
    public ResponseEntity<PersonDto> getPersonByCpf(@PathVariable ("cpf") String cpf) {
        PersonDto person = personService.findByCpf(cpf);
        return new ResponseEntity<PersonDto>(person, HttpStatus.OK);
    }

    @PostMapping(value = "/person")
    public ResponseEntity<?> save(@RequestBody @Valid PersonDto personDto) {
        this.personService.savePerson(personDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(value = "/person/{id}")
    public ResponseEntity<?> deletePersonById(@PathVariable("id") Integer id) {
        this.personService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(value = "/person/{id}")
    public ResponseEntity<?> update(@RequestBody PersonDto personDto) {
        this.personService.update(personDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/login")
    public ResponseEntity<?> login(@RequestBody PersonLoginDto personLoginDto) {
        Success personBoolean = personService.login(personLoginDto);
        return new ResponseEntity<>(personBoolean, HttpStatus.OK);
    }

    @GetMapping(value = "/person/health")
    public ResponseEntity<?> health() {
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
