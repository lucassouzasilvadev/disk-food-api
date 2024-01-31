package com.diskfood.diskfoodapi.domain.service;

import com.diskfood.diskfoodapi.domain.exception.EntidadeEmUsoException;
import com.diskfood.diskfoodapi.domain.exception.EntidadeNaoEncontradaException;
import com.diskfood.diskfoodapi.domain.exception.EstadoNaoEncontradoException;
import com.diskfood.diskfoodapi.domain.model.Estado;
import com.diskfood.diskfoodapi.domain.repository.EstadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CadastroEstadoService {

    private static final String ESTADO_NAO_EXISTE = "Estado de código %d não existe";
    public static final String ESTADO_ESTA_EM_USO = "Estado de código %d não pode ser removido, está em uso";
    @Autowired
    private EstadoRepository estadoRepository;

    public List<Estado> listar(){
        return estadoRepository.findAll();
    }

    public Estado buscar(Long id){
        return estadoRepository.findById(id)
                .orElseThrow(() -> new EstadoNaoEncontradoException(id));
    }

    @Transactional
    public Estado salvar(Estado estado){
        return estadoRepository.save(estado);
    }

    @Transactional
    public void excluir(Long id){
        try{
            estadoRepository.deleteById(id);
        }catch (EmptyResultDataAccessException e){
            throw new EstadoNaoEncontradoException(id);
        }catch (DataIntegrityViolationException e){
            throw new EntidadeEmUsoException(String.format(ESTADO_ESTA_EM_USO, id));
        }
    }

}
