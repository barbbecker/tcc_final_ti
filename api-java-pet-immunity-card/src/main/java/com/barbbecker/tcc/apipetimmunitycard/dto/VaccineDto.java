package com.barbbecker.tcc.apipetimmunitycard.dto;

import java.time.LocalDate;

public class VaccineDto {

    private Integer id;
    private String name;
    private String nameVet;
    private LocalDate dateApplication;
    private LocalDate dateReapplication;
    private String linkVaccine;

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

    public String getNameVet() {
        return nameVet;
    }

    public void setNameVet(String nameVet) {
        this.nameVet = nameVet;
    }

    public LocalDate getDateApplication() {
        return dateApplication;
    }

    public void setDateApplication(LocalDate dateApplication) {
        this.dateApplication = dateApplication;
    }

    public LocalDate getDateReapplication() {
        return dateReapplication;
    }

    public void setDateReapplication(LocalDate dateReapplication) {
        this.dateReapplication = dateReapplication;
    }

    public String getLinkVaccine() {
        return linkVaccine;
    }

    public void setLinkVaccine(String linkVaccine) {
        this.linkVaccine = linkVaccine;
    }
}
