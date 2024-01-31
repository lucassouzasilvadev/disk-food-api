package com.diskfood.diskfoodapi.domain.repository;

import com.diskfood.diskfoodapi.domain.model.FormaPagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FormaDePagamentoRepository extends JpaRepository<FormaPagamento, Long> {

}
