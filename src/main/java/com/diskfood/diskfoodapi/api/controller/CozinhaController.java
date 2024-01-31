package com.diskfood.diskfoodapi.api.controller;

import com.diskfood.diskfoodapi.domain.exception.EntidadeEmUsoException;
import com.diskfood.diskfoodapi.domain.exception.EntidadeNaoEncontradaException;
import com.diskfood.diskfoodapi.domain.model.Cozinha;
import com.diskfood.diskfoodapi.domain.model.Restaurante;
import com.diskfood.diskfoodapi.domain.repository.CozinhaRepository;
import com.diskfood.diskfoodapi.domain.service.CadastroCozinhaService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/cozinhas")
public class CozinhaController {

    @Autowired
    private CozinhaRepository cozinhaRepository;

    @Autowired
    private CadastroCozinhaService cadastroCozinha;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Cozinha> listar(){
        return cozinhaRepository.findAll();
    }

    @GetMapping("/primeiro")
    public Optional<Cozinha> restaurantePrimeiro(){
        return cozinhaRepository.buscarPrimeiro();
    }
    @GetMapping("/{id}")
    public Cozinha buscar(@PathVariable Long id){
        return cadastroCozinha.buscarPorId(id);
    }

    @PostMapping
    public ResponseEntity<Cozinha> addCozinha(@RequestBody @Valid Cozinha cozinha){
        Cozinha cozinhaSalva = cadastroCozinha.salvar(cozinha);
        return ResponseEntity.status(HttpStatus.CREATED).body(cozinhaSalva);
    }

    @PutMapping("/{id}")
    public Cozinha atualizar(@PathVariable Long id, @RequestBody @Valid Cozinha cozinha){
        Cozinha cozinhaAtual = cadastroCozinha.buscarPorId(id);
        BeanUtils.copyProperties(cozinha, cozinhaAtual, "id");
        return cadastroCozinha.salvar(cozinhaAtual);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long id){
        cadastroCozinha.excluir(id);
    }

}
