package com.diskfood.diskfoodapi.domain.service;

import com.diskfood.diskfoodapi.domain.exception.CozinhaNaoEncontradaException;
import com.diskfood.diskfoodapi.domain.exception.EntidadeEmUsoException;
import com.diskfood.diskfoodapi.domain.model.Cozinha;
import com.diskfood.diskfoodapi.domain.repository.CozinhaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CadastroCozinhaService {

    public static final String COZINHA_NÃO_EXISTE = "Cozinha de código %d não existe";
    public static final String COZINHA_EM_USO = "Cozinha de código %d não pode ser removida, está em uso";
    @Autowired
    private CozinhaRepository cozinhaRepository;

    @Transactional
    public Cozinha salvar(Cozinha cozinha){
        return cozinhaRepository.save(cozinha);
    }

    public void excluir(Long id){
        try {
            cozinhaRepository.deleteById(id);
        }catch (EmptyResultDataAccessException e){
            throw new CozinhaNaoEncontradaException(id);
        }catch(DataIntegrityViolationException e){
            throw new EntidadeEmUsoException(String.format(COZINHA_EM_USO, id));
        }

    }

    @Transactional
    public Cozinha buscarPorId(Long id){
       return cozinhaRepository.findById(id)
                .orElseThrow(() -> new CozinhaNaoEncontradaException(id));
    }

}
