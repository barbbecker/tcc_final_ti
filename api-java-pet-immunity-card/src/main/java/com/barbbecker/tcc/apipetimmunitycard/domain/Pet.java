package com.barbbecker.tcc.apipetimmunitycard.domain;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pet")
public class Pet extends BaseDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pet_id")
    private Integer id;

    @NotNull(message = "Nome do animal nao pode ser nulo")
    @NotEmpty(message = "Nome do animal nao pode ser vazio")
    @Column(name = "name_pet")
    private String name;

    @NotNull(message = "A raça do animal nao deve ser nula")
    @NotEmpty(message = "A raça do animal nao deve ser vazia")
    @Column(name = "breed")
    private String breed;

    @NotNull
    @Column(name = "birth_date")
    private LocalDate birthDate;

    @NotNull(message = "O numero do chip nao pode ser nulo")
    @NotEmpty(message = "O numero do chip nao pode ser vazio")
    @Column(name = "number_chip")
    private String numberChip;

    @NotNull(message = "O link da imagem não pode ser nulo")
    @Column(name = "link_image")
    private String linkImage;

    @ManyToOne
    @JoinColumn(name = "person_id")
    private Person person;

    @Column(name = "vaccine")
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "pet")
    private List<Vaccine> vaccines;

    @SuppressWarnings("unused")
    private Pet() {
    }

    public Pet(String name, String breed, LocalDate birthDate, String numberChip, String linkImage, Person person) {
        this.name = name;
        this.breed = breed;
        this.birthDate = birthDate;
        this.numberChip = numberChip;
        this.linkImage = linkImage;
        this.person = person;
        this.vaccines = new ArrayList<>();
        validateDomain();
    }

    public Pet(Integer id, String name, String breed, LocalDate birthDate, String numberChip, String linkImage, Person person) {
        this(name, breed, birthDate, numberChip, linkImage, person);
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getBreed() {
        return breed;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public String getNumberChip() {
        return numberChip;
    }

    public String getLinkImage() {
        return linkImage;
    }

    public Person getPerson() {
        return person;
    }

    public List<Vaccine> getVaccines() {
        return vaccines;
    }

    public void addVaccines(Vaccine vaccine) {
        vaccine.infoPet(this);
        this.vaccines.add(vaccine);
    }

}
