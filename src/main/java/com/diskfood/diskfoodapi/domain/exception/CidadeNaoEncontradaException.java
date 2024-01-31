package com.diskfood.diskfoodapi.domain.exception;


public class CidadeNaoEncontradaException extends EntidadeNaoEncontradaException {

    public static final String CIDADE_NAO_ENCONTRADA = "Cidade de código %d não foi encontrada";

    public CidadeNaoEncontradaException(String mensagem){
        super(mensagem);
    }

    public CidadeNaoEncontradaException(Long id){
        this(String.format(CIDADE_NAO_ENCONTRADA, id));
    }

}
