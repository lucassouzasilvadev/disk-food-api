package com.diskfood.diskfoodapi.domain.service;

import com.diskfood.diskfoodapi.domain.exception.EntidadeNaoEncontradaException;
import com.diskfood.diskfoodapi.domain.exception.RestauranteNaoEncontradoException;
import com.diskfood.diskfoodapi.domain.model.Cozinha;
import com.diskfood.diskfoodapi.domain.model.Restaurante;
import com.diskfood.diskfoodapi.domain.repository.CozinhaRepository;
import com.diskfood.diskfoodapi.domain.repository.RestauranteRepository;
import com.fasterxml.jackson.databind.util.BeanUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CadastroRestauranteService {

    public static final String RESTAURANTE_NAO_EXISTE = "Não existe cadastro de restaurante com código %d";
    @Autowired
    private RestauranteRepository restauranteRepository;

    @Autowired
    private CozinhaRepository cozinhaRepository;

    @Autowired
    private CadastroCozinhaService cadastroCozinhaService;



    public List<Restaurante> listar(){
        return restauranteRepository.findAll();
    }

    public Restaurante buscar(Long id){
        return restauranteRepository.findById(id)
                .orElseThrow(()-> new RestauranteNaoEncontradoException(id));
    }

    @Transactional
    public Restaurante salvar (Restaurante restaurante){
        Long cozinhaId = restaurante.getCozinha().getId();
        Cozinha cozinha = cadastroCozinhaService.buscarPorId(cozinhaId);
        restaurante.setCozinha(cozinha);
        return restauranteRepository.save(restaurante);
    }

    @Transactional
    public Restaurante atualizar(Restaurante restaurante, Long id){
        Restaurante restauranteEncontrado = buscar(id);
        Cozinha cozinha = cadastroCozinhaService.buscarPorId(restaurante.getCozinha().getId());
        restaurante.setCozinha(cozinha);
        BeanUtils.copyProperties(restaurante, restauranteEncontrado, "id", "formasDePagamento", "endereco", "dataCadastro", "produtos");
        return restauranteRepository.save(restauranteEncontrado);
    }

}
