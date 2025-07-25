package com.credito.api.controller;

import com.credito.api.model.Credito;
import com.credito.api.service.CreditoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.MediaType;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class CreditoControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CreditoService creditoService;

    @InjectMocks
    private CreditoController creditoController;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(creditoController).build();
    }

    @Test
    void buscarTodos_deveRetornarListaVazia_quandoNaoTiverRegistros() throws Exception {

        when(creditoService.buscarTodos()).thenReturn(List.of());

        mockMvc.perform(get("/creditos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());

        verify(creditoService, times(1)).buscarTodos();
    }

    @Test
    void buscarTodos_deveRetornarLista_quandoExistirRegistros() throws Exception {
        Credito credito1 = new Credito("123456", "7891011", LocalDate.parse("2024-02-25"),
                new BigDecimal("1500.75"), "ISSQN", true, new BigDecimal("5.0"),
                new BigDecimal("30000.00"), new BigDecimal("5000.00"), new BigDecimal("25000.00"));
        Credito credito2 = new Credito("789012", "7891011", LocalDate.parse("2024-02-26"),
                new BigDecimal("1200.50"), "ISSQN", false, new BigDecimal("4.5"),
                new BigDecimal("25000.00"), new BigDecimal("4000.00"), new BigDecimal("21000.00"));
        List<Credito> creditos = Arrays.asList(credito1, credito2);
        when(creditoService.buscarTodos()).thenReturn(creditos);

        mockMvc.perform(get("/creditos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty());

        verify(creditoService, times(1)).buscarTodos();
    }

    @Test
    void buscarPorNfse_deveRetornarListaDeCreditos_quandoNfseExistir() throws Exception {

        String numeroNfse = "7891011";
        Credito credito1 = new Credito("123456", "7891011", LocalDate.parse("2024-02-25"),
                new BigDecimal("1500.75"), "ISSQN", true, new BigDecimal("5.0"),
                new BigDecimal("30000.00"), new BigDecimal("5000.00"), new BigDecimal("25000.00"));
        Credito credito2 = new Credito("789012", "7891011", LocalDate.parse("2024-02-26"),
                new BigDecimal("1200.50"), "ISSQN", false, new BigDecimal("4.5"),
                new BigDecimal("25000.00"), new BigDecimal("4000.00"), new BigDecimal("21000.00"));
        List<Credito> creditos = Arrays.asList(credito1, credito2);

        when(creditoService.filtrarPorNumeroNfse(numeroNfse)).thenReturn(creditos);


        mockMvc.perform(get("/creditos/{numeroNfse}", numeroNfse)
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].numeroCredito").value("123456"))
                .andExpect(jsonPath("$[0].numeroNfse").value("7891011"))
                .andExpect(jsonPath("$[0].valorIssqn").value(1500.75));

        verify(creditoService, times(1)).filtrarPorNumeroNfse(numeroNfse);
    }

    @Test
    void buscarPorNfse_deveRetornarListaVazia_quandoNfseNaoExistir() throws Exception {

        String numeroNfse = "99999";
        when(creditoService.filtrarPorNumeroNfse(numeroNfse)).thenReturn(List.of());

        mockMvc.perform(get("/creditos/{numeroNfse}", numeroNfse))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());

        verify(creditoService, times(1)).filtrarPorNumeroNfse(numeroNfse);
    }

    @Test
    void buscarPorNumeroCredito_deveRetornarCredito_quandoCreditoExistir() throws Exception {

        String numeroCredito = "123456";
        Credito credito = new Credito("123456", "7891011", LocalDate.parse("2024-02-25"),
                new BigDecimal("1500.75"), "ISSQN", true, new BigDecimal("5.0"),
                new BigDecimal("30000.00"), new BigDecimal("5000.00"), new BigDecimal("25000.00"));

        when(creditoService.buscarPorNumeroCredito(numeroCredito)).thenReturn(credito);

        mockMvc.perform(get("/creditos/credito/{numeroCredito}", numeroCredito))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numeroCredito").value("123456"))
                .andExpect(jsonPath("$.numeroNfse").value("7891011"))
                .andExpect(jsonPath("$.valorIssqn").value(1500.75));

        verify(creditoService, times(1)).buscarPorNumeroCredito(numeroCredito);
    }

    @Test
    void buscarPorNumeroCredito_deveRetornarOk_quandoCreditoNaoExistir() throws Exception {

        String numeroCredito = "9999";
        when(creditoService.buscarPorNumeroCredito(numeroCredito)).thenReturn(null);

        mockMvc.perform(get("/creditos/credito/{numeroCredito}", numeroCredito))
                .andExpect(status().isOk());

        verify(creditoService, times(1)).buscarPorNumeroCredito(numeroCredito);
    }
}
