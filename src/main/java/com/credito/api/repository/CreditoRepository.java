package com.credito.api.repository;

import com.credito.api.model.Credito;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CreditoRepository extends JpaRepository<Credito, Long> {

    Credito findCreditoByNumeroCredito(String numeroCredito);

    List<Credito> findByNumeroNfseLike(String numeroNfse);
}
