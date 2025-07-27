package com.credito.api.service;

import com.credito.api.model.Credito;
import com.credito.api.repository.CreditoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CreditoService {

    @Autowired
    private CreditoRepository creditoRepository;

    @Autowired
    private KafkaProducerService kafkaProducerService;


    public List<Credito> buscarTodos() {
        List<Credito> creditos = this.creditoRepository.findAll();
        notificarConsultaRealizada("buscarTodos", null);
        return creditos;
    }

    public List<Credito> filtrarPorNumeroNfse(String numeroNfse) {
        List<Credito> creditos = this.creditoRepository.findByNumeroNfseLike(numeroNfse + "%");
        notificarConsultaRealizada("filtrarPorNumeroNfse", numeroNfse);
        return creditos;
    }

    public Credito buscarPorNumeroCredito(String numeroCredito) {
        if (numeroCredito == null) {
            throw new IllegalArgumentException("Número de crédito é obrigatório.");
        }
        Credito credito = this.creditoRepository.findCreditoByNumeroCredito(numeroCredito);
        notificarConsultaRealizada("buscarPorNumeroCredito", numeroCredito);
        return credito;
    }

    private void notificarConsultaRealizada(String tipoConsulta, String parametro) {
        Map<String, Object> evento = new HashMap<>();
        evento.put("tipoConsulta", tipoConsulta);
        evento.put("parametro", parametro);
        evento.put("dataHora", java.time.LocalDateTime.now().toString());

        kafkaProducerService.enviarEventoConsultaCredito(evento);
    }

}
