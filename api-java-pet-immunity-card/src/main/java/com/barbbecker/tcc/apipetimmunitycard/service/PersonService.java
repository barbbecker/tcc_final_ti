package com.barbbecker.tcc.apipetimmunitycard.service;

import com.barbbecker.tcc.apipetimmunitycard.domain.Person;
import com.barbbecker.tcc.apipetimmunitycard.domain.Success;
import com.barbbecker.tcc.apipetimmunitycard.dto.PersonDto;
import com.barbbecker.tcc.apipetimmunitycard.dto.PersonLoginDto;
import com.barbbecker.tcc.apipetimmunitycard.exception.ServiceException;
import com.barbbecker.tcc.apipetimmunitycard.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PersonService {

    private PersonRepository personRepository;

    @Autowired
    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public void deleteAll() {
        this.personRepository.deleteAll();
    }

    public void savePerson(PersonDto personDto) {
        String name = personDto.getName();
        String address = personDto.getAddress();
        String number = personDto.getNumber();
        String phoneNumber = personDto.getPhoneNumber();
        String cpf = personDto.getCpf();
        String email = personDto.getEmail();
        String password = personDto.getPassword();

        Person person = new Person(name, address, number, phoneNumber, cpf, email, password);
        this.personRepository.saveAndFlush(person);
    }

    public PersonDto findByCpf(String cpf) {
        Optional<Person> person = personRepository.findByCpf(cpf);
        if (person.isPresent()) {
            PersonDto personDto = createPerson(person.get());
            return personDto;
        }
        throw new ServiceException("Pessoa não encontrada");
    }

    private PersonDto createPerson(Person person) {
        PersonDto personDto = new PersonDto();
        personDto.setId(person.getId());
        personDto.setName(person.getName());
        personDto.setAddress(person.getAddress());
        personDto.setNumber(person.getNumber());
        personDto.setPhoneNumber(person.getPhoneNumber());
        personDto.setCpf(person.getCpf());
        personDto.setEmail(person.getEmail());
        personDto.setPassword(person.getPassword());
        return personDto;
    }

    public void deleteById(Integer id) {
        Optional<Person> person = personRepository.findById(id);
        if (person.isPresent()) {
            personRepository.deleteById(person.get().getId());
        }
    }

    public Person update(PersonDto personDto) {
        Integer id = personDto.getId();
        String name = personDto.getName();
        String address = personDto.getAddress();
        String number = personDto.getNumber();
        String phoneNumber = personDto.getPhoneNumber();
        String cpf = personDto.getCpf();
        String email = personDto.getEmail();
        String password = personDto.getPassword();

        Person person = new Person(id, name, address, number, phoneNumber, cpf, email, password);
        return this.personRepository.saveAndFlush(person);
    }

    public List<PersonDto> findAll() {
        List<PersonDto> everyPerson = new ArrayList<PersonDto>();
        List<Person> people = personRepository.findAll();

        for (Person person : people) {
            PersonDto personDto = createPerson(person);
            everyPerson.add(personDto);
        }
        return everyPerson;
    }

    public Success login(PersonLoginDto personLoginDto) {
        Person personBoolean = personRepository.findByEmailAndPassword(personLoginDto.getEmail(), personLoginDto.getPassword());
        if (personBoolean != null) {
            return new Success(true, personBoolean.getCpf(), personBoolean.getId());
        }
        return new Success(false, "", personBoolean.getId());
    }
}