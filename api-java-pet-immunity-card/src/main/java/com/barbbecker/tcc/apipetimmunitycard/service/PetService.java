package com.barbbecker.tcc.apipetimmunitycard.service;

import com.barbbecker.tcc.apipetimmunitycard.domain.Person;
import com.barbbecker.tcc.apipetimmunitycard.domain.Pet;
import com.barbbecker.tcc.apipetimmunitycard.domain.Vaccine;
import com.barbbecker.tcc.apipetimmunitycard.dto.PetDto;
import com.barbbecker.tcc.apipetimmunitycard.dto.VaccineDto;
import com.barbbecker.tcc.apipetimmunitycard.exception.ServiceException;
import com.barbbecker.tcc.apipetimmunitycard.repository.PersonRepository;
import com.barbbecker.tcc.apipetimmunitycard.repository.PetRepository;
import com.barbbecker.tcc.apipetimmunitycard.repository.VaccineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PetService {

    private PetRepository petRepository;
    private PersonRepository personRepository;
    private VaccineRepository vaccineRepository;


    @Autowired
    public PetService(PetRepository petRepository, PersonRepository personRepository, VaccineRepository vaccineRepository) {
        this.petRepository = petRepository;
        this.personRepository = personRepository;
        this.vaccineRepository = vaccineRepository;
    }

    public void deleteAll() {
        this.petRepository.deleteAll();
    }

    public void save(PetDto petDto) {
        Optional<Person> personFound = personRepository.findByCpf(petDto.getCpf());
        String name = petDto.getName();
        String breed = petDto.getBreed();
        LocalDate birthDate = petDto.getBirthDate();
        String numberChip = petDto.getNumberChip();
        String linkImage = petDto.getLinkImage();
        Person person = personFound.get();

        Pet pet = new Pet(name, breed, birthDate, numberChip, linkImage, person);
        this.petRepository.saveAndFlush(pet);
    }

    private PetDto createPetDto(Pet pet) {
        PetDto petDto = new PetDto();
        petDto.setId(pet.getId());
        petDto.setName(pet.getName());
        petDto.setBreed(pet.getBreed());
        petDto.setBirthDate(pet.getBirthDate());
        petDto.setNumberChip(pet.getNumberChip());
        petDto.setLinkImage(pet.getLinkImage());
        petDto.setNamePerson(pet.getPerson().getName());
        petDto.setCpf(pet.getPerson().getCpf());
        petDto.setVaccines(createVaccines(pet.getVaccines()));

        return petDto;
    }

    private List<VaccineDto> createVaccines(List<Vaccine> vaccines) {
        List<VaccineDto> returnVaccine = new ArrayList<>();

        for (Vaccine vaccine : vaccines) {
            VaccineDto vaccineDto = new VaccineDto();
            vaccineDto.setId(vaccine.getId());
            vaccineDto.setName(vaccine.getName());
            vaccineDto.setNameVet(vaccine.getNameVet());
            vaccineDto.setDateApplication(vaccine.getDateApplication());
            vaccineDto.setDateReapplication(vaccine.getDateReapplication());
            vaccineDto.setLinkVaccine(vaccine.getLinkVaccine());
            returnVaccine.add(vaccineDto);
        }

        return returnVaccine;
    }

    public PetDto findById(Integer id) {
        Optional<Pet> pet = petRepository.findById(id);
        if (pet.isPresent()) {
            PetDto petDto = createPetDto(pet.get());
            return petDto;
        }
        throw new ServiceException("Pet não encontrado");
    }

    public void delete(Integer id) {
        this.petRepository.deleteById(id);
    }

    public Pet update(PetDto petDto) {
        Optional<Person> personFound = personRepository.findByCpf(petDto.getCpf());

        Integer id = petDto.getId();
        String name = petDto.getName();
        String breed = petDto.getBreed();
        LocalDate birthDate = petDto.getBirthDate();
        String numberChip = petDto.getNumberChip();
        String linkImage = petDto.getLinkImage();
        Person person = personFound.get();

        Pet pet = new Pet(id, name, breed, birthDate, numberChip, linkImage, person);
        return this.petRepository.saveAndFlush(pet);
    }

    public void deleteByNumberChip(String numberChip) {
        Optional<Pet> pet = petRepository.findByNumberChip(numberChip);
        if (pet.isPresent()) {
            petRepository.deleteById(pet.get().getId());
        }
        throw new ServiceException("Esse número de chip não existe");
    }

    public List<PetDto> findAllPets(Integer id) {
        Optional<Person> personFound = this.personRepository.findById(id);
        List<PetDto> everyPets = new ArrayList<PetDto>();

        if (personFound.isPresent()) {
            List<Pet> pets = petRepository.findAllByPersonId(id);

            for (Pet pet : pets) {
                PetDto petDto = createPetDto(pet);
                everyPets.add(petDto);
            }
        }
        return everyPets;
    }

    public List<VaccineDto> findAllVaccines(Integer id) {
        Optional<Pet> petFound = this.petRepository.findPetWithVaccines(id);
        List<VaccineDto> everyVaccine = new ArrayList<VaccineDto>();

        if (petFound.isPresent()) {
            List<Vaccine> vaccines = petFound.get().getVaccines();

            for (Vaccine vaccine : vaccines) {
                VaccineDto vaccineDto = createVaccine(vaccine);
                everyVaccine.add(vaccineDto);
            }
        }
        return everyVaccine;
    }

    public void saveVaccine(Integer id, VaccineDto vaccineDto) {
        Optional<Pet> petFound = this.petRepository.findPetWithVaccines(id);
        if (petFound.isPresent()) {
            Pet pet = petFound.get();
            String nameVaccine = vaccineDto.getName();
            String nameVet = vaccineDto.getNameVet();
            LocalDate dateReapplication = vaccineDto.getDateReapplication();
            String linkVaccine = vaccineDto.getLinkVaccine();
            Vaccine vaccine = new Vaccine(nameVaccine, nameVet, dateReapplication, linkVaccine);
            pet.addVaccines(vaccine);
            this.petRepository.saveAndFlush(pet);
        }
    }

    private VaccineDto createVaccine(Vaccine vaccine) {
        VaccineDto vaccineDto = new VaccineDto();
        vaccineDto.setId(vaccine.getId());
        vaccineDto.setName(vaccine.getName());
        vaccineDto.setNameVet(vaccine.getNameVet());
        vaccineDto.setDateApplication(vaccine.getDateApplication());
        vaccineDto.setDateReapplication(vaccine.getDateReapplication());
        vaccineDto.setLinkVaccine(vaccine.getLinkVaccine());

        return vaccineDto;
    }

}
