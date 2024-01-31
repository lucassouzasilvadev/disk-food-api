package com.diskfood.diskfoodapi.domain.repository;

import com.diskfood.diskfoodapi.domain.model.Restaurante;

import java.math.BigDecimal;
import java.util.List;

public interface RestauranteRepositoryQueries {
    List<Restaurante> consultarPorNomeEFrete(String nome, BigDecimal taxaFreteIncial, BigDecimal taxaFreteFinal);
    List<Restaurante> findComFreteGratis(String nome);
}
