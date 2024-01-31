package com.diskfood.diskfoodapi.domain.exception;


public class EstadoNaoEncontradoException extends EntidadeNaoEncontradaException {

    public static final String ESTADO_ESTA_EM_USO = "Estado de código %d não pode ser removido, está em uso";

    public EstadoNaoEncontradoException(String mensagem){
        super(mensagem);
    }

    public EstadoNaoEncontradoException(Long id){
        this(String.format(ESTADO_ESTA_EM_USO, id));
    }

}
