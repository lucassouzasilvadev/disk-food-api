package com.diskfood.diskfoodapi.domain.exception;


public class RestauranteNaoEncontradoException extends EntidadeNaoEncontradaException {

    public static final String RESTAURANTE_NAO_EXISTE = "Não existe cadastro de restaurante com código %d";

    public RestauranteNaoEncontradoException(String mensagem){
        super(mensagem);
    }

    public RestauranteNaoEncontradoException(Long id){
        this(String.format(RESTAURANTE_NAO_EXISTE, id));
    }

}
