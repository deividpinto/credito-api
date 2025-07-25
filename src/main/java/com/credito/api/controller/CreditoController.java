package com.credito.api.controller;

import com.credito.api.model.Credito;
import com.credito.api.service.CreditoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/creditos", produces = MediaType.APPLICATION_JSON_VALUE)
public class CreditoController {

    @Autowired
    private CreditoService creditoService;

    @GetMapping()
    public List<Credito> buscarTodos() {
        return this.creditoService.buscarTodos();
    }

    @GetMapping("/{numeroNfse}")
    public List<Credito> buscarPorNfse(@PathVariable("numeroNfse") String numeroNfse) {
        return this.creditoService.filtrarPorNumeroNfse(numeroNfse);
    }

    @GetMapping("/credito/{numeroCredito}")
    public Credito buscarPorNumeroCredito(@PathVariable("numeroCredito") String numeroCredito) {
        return this.creditoService.buscarPorNumeroCredito(numeroCredito);
    }
}
