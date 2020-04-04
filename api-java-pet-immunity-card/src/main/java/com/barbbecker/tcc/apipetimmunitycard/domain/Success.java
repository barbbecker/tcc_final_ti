package com.barbbecker.tcc.apipetimmunitycard.domain;

public class Success {

    private Boolean success;
    private String cpf;
    private Integer personId;

    public Success(Boolean success, String cpf, Integer personId){
        this.success = success;
        this.cpf = cpf;
        this.personId = personId;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public Integer getPersonId() {
        return personId;
    }

    public void setPersonId(Integer personId) {
        this.personId = personId;
    }
}
