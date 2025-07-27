package com.credito.api.controller;

import com.credito.api.model.Credito;
import com.credito.api.service.CreditoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/creditos", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Créditos", description = "API para gerenciamento de créditos")
public class CreditoController {

    @Autowired
    private CreditoService creditoService;

    @Operation(summary = "Listar todos os créditos",
            description = "Retorna uma lista com todos os créditos cadastrados no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de créditos retornada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autorizado"),
            @ApiResponse(responseCode = "403", description = "Acesso proibido"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping()
    public List<Credito> buscarTodos() {
        return this.creditoService.buscarTodos();
    }

    @Operation(summary = "Buscar créditos por NFSE",
            description = "Retorna uma lista de créditos associados ao número de NFSE informado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Créditos encontrados com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autorizado"),
            @ApiResponse(responseCode = "403", description = "Acesso proibido"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/{numeroNfse}")
    public List<Credito> buscarPorNfse(@PathVariable("numeroNfse") String numeroNfse) {
        return this.creditoService.filtrarPorNumeroNfse(numeroNfse);
    }

    @Operation(summary = "Buscar crédito por número",
            description = "Retorna um crédito específico com base no número do crédito informado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Crédito encontrado com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autorizado"),
            @ApiResponse(responseCode = "403", description = "Acesso proibido"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/credito/{numeroCredito}")
    public Credito buscarPorNumeroCredito(@PathVariable("numeroCredito") String numeroCredito) {
        return this.creditoService.buscarPorNumeroCredito(numeroCredito);
    }
}
