package com.credito.api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "credito", schema = "financeiro")
@Getter
@Setter
public class Credito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "numero_credito")
    private String numeroCredito;

    @Column(name = "numero_nfse")
    private String numeroNfse;

    @Column(name = "data_constituicao")
    private LocalDate dataConstituicao;

    @Column(name = "valor_issqn")
    private BigDecimal valorIssqn;

    @Column(name = "tipo_credito")
    private String tipoCredito;

    @Column(name = "simples_nacional")
    private boolean simplesNacional;

    @Column(name = "aliquota")
    private BigDecimal aliquota;

    @Column(name = "valor_faturado")
    private BigDecimal valorFaturado;

    @Column(name = "valor_deducao")
    private BigDecimal valorDeducao;

    @Column(name = "base_calculo")
    private BigDecimal baseCalculo;

    public Credito() {
    }

    public Credito(String numeroCredito, String numeroNfse, LocalDate dataConstituicao,
                   BigDecimal valorIssqn, String tipoCredito, boolean simplesNacional,
                   BigDecimal aliquota, BigDecimal valorFaturado, BigDecimal valorDeducao,
                   BigDecimal baseCalculo) {
        this.numeroCredito = numeroCredito;
        this.numeroNfse = numeroNfse;
        this.dataConstituicao = dataConstituicao;
        this.valorIssqn = valorIssqn;
        this.tipoCredito = tipoCredito;
        this.simplesNacional = simplesNacional;
        this.aliquota = aliquota;
        this.valorFaturado = valorFaturado;
        this.valorDeducao = valorDeducao;
        this.baseCalculo = baseCalculo;
    }

}

