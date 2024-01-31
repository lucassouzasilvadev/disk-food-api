package com.diskfood.diskfoodapi.domain.service;

import com.diskfood.diskfoodapi.domain.exception.CidadeNaoEncontradaException;
import com.diskfood.diskfoodapi.domain.exception.EntidadeEmUsoException;
import com.diskfood.diskfoodapi.domain.model.Cidade;
import com.diskfood.diskfoodapi.domain.model.Estado;
import com.diskfood.diskfoodapi.domain.repository.CidadeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CadastroCidadeService {

    public static final String CIDADE_NAO_EXISTE = "Cidade de código %d não existe";
    public static final String CIDADE_NAO_ENCONTRADA = "Cidade de código %d não foi encontrada";

    private static final String MSG_CIDADE_EM_USO
            = "Cidade de código %d não pode ser removida, pois está em uso";
    @Autowired
    private CidadeRepository cidadeRepository;

    @Autowired
    private CadastroEstadoService cadastroEstadoService;

    public List<Cidade> listar(){
        return cidadeRepository.findAll();
    }

    public Cidade buscar(Long id){
       return cidadeRepository.findById(id)
               .orElseThrow(() -> new CidadeNaoEncontradaException(id));
    }

    @Transactional
    public Cidade salvar(Cidade cidade){
        Long estadoId = cidade.getEstado().getId();
        Estado estado = cadastroEstadoService.buscar(estadoId);
        cidade.setEstado(estado);
        return cidadeRepository.save(cidade);
    }

    @Transactional
    public void excluir(Long id){
        try{
            cidadeRepository.deleteById(id);
        }catch (EmptyResultDataAccessException exception){
            throw new CidadeNaoEncontradaException(id);
        }catch (DataIntegrityViolationException exception) {
            throw new EntidadeEmUsoException(String.format(MSG_CIDADE_EM_USO, id));
        }
    }
}
