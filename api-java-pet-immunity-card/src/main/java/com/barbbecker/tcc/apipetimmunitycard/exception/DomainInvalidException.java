package com.barbbecker.tcc.apipetimmunitycard.exception;

public class DomainInvalidException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public DomainInvalidException(String mensagem) {
        super(mensagem);
    }
}
