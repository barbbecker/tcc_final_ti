package com.barbbecker.tcc.apipetimmunitycard.dto;

import java.time.LocalDate;
import java.util.List;

public class PetDto {

    private Integer id;
    private String name;
    private String breed;
    private LocalDate birthDate;
    private String numberChip;
    private String linkImage;
    private String namePerson;
    private String cpf;

    private List<VaccineDto> vaccines;

//  private VaccineDto vaccine;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getNumberChip() {
        return numberChip;
    }

    public void setNumberChip(String numberChip) {
        this.numberChip = numberChip;
    }

    public String getLinkImage() {
        return linkImage;
    }

    public void setLinkImage(String linkImage) {
        this.linkImage = linkImage;
    }

    public String getNamePerson() {
        return namePerson;
    }

    public void setNamePerson(String namePerson) {
        this.namePerson = namePerson;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public List<VaccineDto> getVaccines() {
        return vaccines;
    }

    public void setVaccines(List<VaccineDto> vaccines) {
        this.vaccines = vaccines;
    }

//    public VaccineDto getVaccine() {
//        return vaccine;
//    }
//
//    public void setVaccine(VaccineDto vaccine) {
//        this.vaccine = vaccine;
//    }
    
}
