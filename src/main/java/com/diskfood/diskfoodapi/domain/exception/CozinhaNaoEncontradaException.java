package com.diskfood.diskfoodapi.domain.exception;


public class CozinhaNaoEncontradaException extends EntidadeNaoEncontradaException {

    private static final long serialVersionUID = 1L;


    public static final String COZINHA_NAO_EXISTE = "Cozinha de código %d não existe";

    public CozinhaNaoEncontradaException(String mensagem){
        super(mensagem);
    }

    public CozinhaNaoEncontradaException(Long id){
        this(String.format(COZINHA_NAO_EXISTE, id));
    }

}
