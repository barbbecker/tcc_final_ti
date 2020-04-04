package com.barbbecker.tcc.apipetimmunitycard.controller;

import com.barbbecker.tcc.apipetimmunitycard.dto.PetDto;
import com.barbbecker.tcc.apipetimmunitycard.dto.VaccineDto;
import com.barbbecker.tcc.apipetimmunitycard.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "api")
public class PetController {

    private PetService petService;

    @Autowired
    public PetController(PetService petService) {
        this.petService = petService;
    }

    @GetMapping(value = "/pet/person/{id}")
    public ResponseEntity<List<PetDto>> getPets(@PathVariable("id") Integer id) {
        List<PetDto> pet = petService.findAllPets(id);
        return new ResponseEntity<>(pet, HttpStatus.OK);
    }

    @GetMapping(value = "/pet/{id}/vaccine")
    public ResponseEntity<List<VaccineDto>> getVaccines(@PathVariable("id") Integer id) {
        List<VaccineDto> vaccine = petService.findAllVaccines(id);
        return new ResponseEntity<>(vaccine, HttpStatus.OK);
    }

    @GetMapping(value = "/pet/{id}")
    public ResponseEntity<PetDto> getPetById(@PathVariable("id") Integer id) {
        PetDto pet = petService.findById(id);
        return new ResponseEntity<PetDto>(pet, HttpStatus.OK);
    }

    @PostMapping(value = "/pet")
    public ResponseEntity<?> save(@RequestBody @Valid PetDto petDto) {
        this.petService.save(petDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(value = "/pet/{numberChip}")
    public ResponseEntity<?> removePetByNumberChip(@PathVariable("numberChip") String numberChip) {
        this.petService.deleteByNumberChip(numberChip);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(value = "/pet/{id}")
    public ResponseEntity<?> removePetById(@PathVariable("id") Integer id) {
        this.petService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(value = "/pet")
    public ResponseEntity<?> updatePet(@RequestBody PetDto petDto) {
        petService.update(petDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/pet/{id}/save-vaccine")
    public ResponseEntity<?> saveVaccine(@PathVariable("id") Integer id,
                                            @RequestBody VaccineDto vaccineDto) {
        this.petService.saveVaccine(id, vaccineDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/pet/health")
    public ResponseEntity<?> health() {
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
