package com.barbbecker.tcc.apipetimmunitycard.domain;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "vaccine")
public class Vaccine extends BaseDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vaccine_id")
    private Integer id;

    @NotNull(message = "Nome da vacina não pode ser nulo")
    @NotEmpty(message = "Nome da vacina não pode ser vazio")
    @Column(name = "name_vaccine")
    private String name;

    @NotNull(message = "Nome da vet não pode ser nulo")
    @NotEmpty(message = "Nome da vet não pode ser vazio")
    @Column(name = "name_veterinary")
    private String nameVet;

    @NotNull
    @Column(name = "date_application")
    private LocalDate dateApplication;

    @NotNull
    @Column(name = "date_reapplication")
    private LocalDate dateReapplication;

    @NotNull(message = "O link da vacina não pode ser nulo")
    @Column(name = "link_vaccine")
    private String linkVaccine;

    @ManyToOne
    @JoinColumn(name = "pet_id")
    private Pet pet;


    @SuppressWarnings("unused")
    private Vaccine() {
    }

    public Vaccine(String name, String nameVet, LocalDate dateReapplication, String linkVaccine) {
        this.name = name;
        this.nameVet = nameVet;
        this.dateApplication = LocalDate.now();
        this.dateReapplication = dateReapplication;
        this.linkVaccine = linkVaccine;
        validateDomain();
    }

    public Vaccine(Integer id, String name, String nameVet, LocalDate dateReapplication, String linkVaccine) {
        this(name, nameVet ,dateReapplication, linkVaccine);
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getNameVet() {
        return nameVet;
    }

    public LocalDate getDateApplication() {
        return dateApplication;
    }

    public LocalDate getDateReapplication() {
        return dateReapplication;
    }

    public String getLinkVaccine() {
        return linkVaccine;
    }

    void infoPet(Pet pet) {
        this.pet = pet;
    }
}

