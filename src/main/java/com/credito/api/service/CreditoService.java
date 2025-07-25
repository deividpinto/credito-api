package com.credito.api.service;

import com.credito.api.model.Credito;
import com.credito.api.repository.CreditoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CreditoService {

    @Autowired
    private CreditoRepository creditoRepository;

    public List<Credito> buscarTodos() {
        return this.creditoRepository.findAll();
    }

    public List<Credito> filtrarPorNumeroNfse(String numeroNfse) {
        return this.creditoRepository.findByNumeroNfseLike(numeroNfse + "%");
    }

    public Credito buscarPorNumeroCredito(String numeroCredito) {
        if (numeroCredito == null) {
            throw new IllegalArgumentException("Número de crédito é obrigatório.");
        }
        return this.creditoRepository.findCreditoByNumeroCredito(numeroCredito);
    }
}
