package com.barbbecker.tcc.apipetimmunitycard.domain;

import org.hibernate.validator.constraints.br.CPF;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@Table(name = "person")
public class Person extends BaseDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "person_id")
    private Integer id;

    @NotNull(message = "Nome da pessoa nao pode ser nulo")
    @NotEmpty(message = "Nome da pessoa nao pode ser vazio")
    @Column(name = "name_person")
    private String name;

    @NotNull(message = "Endereço nao pode ser nulo")
    @NotEmpty(message = "Endereço nao pode ser vazio")
    @Column(name = "address")
    private String address;

    @NotNull(message = "Numero do endereço nao pode ser nulo")
    @NotEmpty(message = "Numero do endereço nao pode ser vazio")
    @Column(name = "number_address")
    private String number;

    @NotNull(message = "Numero de telefone nao pode ser nulo")
    @NotEmpty(message = "Numero de telefone nao pode ser vazio")
    @Column(name = "phone_number")
    private String phoneNumber;

    @NotNull(message = "CPF nao pode ser nulo")
    @NotEmpty(message = "CPF nao pode ser vazio")
    @CPF
    @Column(name = "cpf")
    private String cpf;

    @NotNull(message = "Email nao pode ser nulo")
    @NotEmpty(message = "Email nao pode ser vazio")
    @Email
    @Column(unique = true)
    private String email;

    @NotNull(message = "Senha nao pode ser nula")
    @NotEmpty(message = "Senha nao pode ser vazia")
    @Column(name = "password_person")
    private String password;

    @SuppressWarnings("unused")
    private Person() {

    }

    public Person(String name, String address, String number, String phoneNumber, String cpf, String email, String password) {
        this.name = name;
        this.address = address;
        this.number = number;
        this.phoneNumber = phoneNumber;
        this.cpf = cpf;
        this.email = email;
        this.password = password;
        validateDomain();
    }

    public Person(Integer id, String name, String address, String number, String phoneNumber, String cpf, String email, String password) {
        this(name, address, number, phoneNumber, cpf, email, password);
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getNumber() {
        return number;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getCpf() {
        return cpf;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(id, person.id) &&
                Objects.equals(name, person.name) &&
                Objects.equals(address, person.address) &&
                Objects.equals(number, person.number) &&
                Objects.equals(phoneNumber, person.phoneNumber) &&
                Objects.equals(cpf, person.cpf) &&
                Objects.equals(email, person.email) &&
                Objects.equals(password, person.password);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((cpf == null) ? 0 : cpf.hashCode());
        result = prime * result + ((address == null) ? 0 : address.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((number == null) ? 0 : number.hashCode());
        result = prime * result + ((phoneNumber == null) ? 0 : phoneNumber.hashCode());
        result = prime * result + ((email == null) ? 0 : email.hashCode());
        result = prime * result + ((password == null) ? 0 : password.hashCode());

        return result;
    }

}
