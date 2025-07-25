package com.credito.api.service;

import com.credito.api.model.Credito;
import com.credito.api.repository.CreditoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class CreditoServiceTest {

    @Mock
    private CreditoRepository creditoRepository;

    @InjectMocks
    private CreditoService creditoService;

    @Test
    void buscarTodos_deveRetornarListaVazia_quandoNaoExistirRegistros() {
        when(creditoRepository.findAll()).thenReturn(Collections.emptyList());

        List<Credito> resultado = creditoService.buscarTodos();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(creditoRepository, times(1)).findAll();
    }

    @Test
    void buscarTodos_deveRetornarLista_quandoExistirRegistros() {
        Credito credito1 = this.criarCredito("123456", "7891011", "2024-02-25",
                "1500.75", "ISSQN", true, "5.0",
                "30000.00", "5000.00", "25000.00");
        Credito credito2 = this.criarCredito("789012", "7891011", "2024-02-26",
                "1200.50", "ISSQN", false, "4.5",
                "25000.00", "4000.00", "21000.00");
        List<Credito> creditosMock = Arrays.asList(credito1, credito2);
        when(creditoRepository.findAll()).thenReturn(creditosMock);

        List<Credito> resultado = creditoService.buscarTodos();

        assertNotNull(resultado);
        assertTrue((long) resultado.size() > 0);
        verify(creditoRepository, times(1)).findAll();
    }

    @Test
    void filtrarPorNumeroNfse_deveRetornarListaDeCreditos_quandoNfseExistir() {
        String numeroNfse = "7891011";
        Credito credito1 = this.criarCredito("123456", "7891011", "2024-02-25",
                "1500.75", "ISSQN", true, "5.0",
                "30000.00", "5000.00", "25000.00");
        Credito credito2 = this.criarCredito("789012", "7891011", "2024-02-26",
                "1200.50", "ISSQN", false, "4.5",
                "25000.00", "4000.00", "21000.00");
        List<Credito> creditosMock = Arrays.asList(credito1, credito2);

        when(creditoRepository.findByNumeroNfseLike(numeroNfse)).thenReturn(creditosMock);

        List<Credito> resultado = creditoService.filtrarPorNumeroNfse(numeroNfse);

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("123456", resultado.get(0).getNumeroCredito());
        assertEquals("789012", resultado.get(1).getNumeroCredito());
        verify(creditoRepository, times(1)).findByNumeroNfseLike(numeroNfse);
    }

    @Test
    void filtrarPorNumeroNfse_deveRetornarListaVazia_quandoNfseNaoExistir() {
        String numeroNfse = "NF999";
        when(creditoRepository.findByNumeroNfseLike(numeroNfse)).thenReturn(Collections.emptyList());

        List<Credito> resultado = creditoService.filtrarPorNumeroNfse(numeroNfse);

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(creditoRepository, times(1)).findByNumeroNfseLike(numeroNfse);
    }

    @Test
    void buscarPorNumeroCredito_deveRetornarCredito_quandoCreditoExistir() {
        String numeroCredito = "123456";
        Credito creditoMock = this.criarCredito("123456", "7891011", "2024-02-25",
                "1500.75", "ISSQN", true, "5.0",
                "30000.00", "5000.00", "25000.00");
        when(creditoRepository.findCreditoByNumeroCredito(numeroCredito)).thenReturn(creditoMock);

        Credito resultado = creditoService.buscarPorNumeroCredito(numeroCredito);

        assertNotNull(resultado);
        assertEquals(numeroCredito, resultado.getNumeroCredito());
        assertEquals("7891011", resultado.getNumeroNfse());
        assertEquals(new BigDecimal("1500.75"), resultado.getValorIssqn());
        verify(creditoRepository, times(1)).findCreditoByNumeroCredito(numeroCredito);
    }

    @Test
    void buscarPorNumeroCredito_deveRetornarNull_quandoCreditoNaoExistir() {
        String numeroCredito = "2322341";
        when(creditoRepository.findCreditoByNumeroCredito(numeroCredito)).thenReturn(null);

        Credito resultado = creditoService.buscarPorNumeroCredito(numeroCredito);

        assertNull(resultado);
        verify(creditoRepository, times(1)).findCreditoByNumeroCredito(numeroCredito);
    }

    @Test
    void buscarPorNumeroCredito_deveLancarExcecao_quandoParametroInvalido() {
        String numeroCreditoInvalido = null;

        assertThrows(IllegalArgumentException.class, () -> {
            creditoService.buscarPorNumeroCredito(numeroCreditoInvalido);
        });

        verify(creditoRepository, never()).findCreditoByNumeroCredito(any());
    }

    private Credito criarCredito(String numeroCredito, String numeroNfse, String dataConstituicao,
                                 String valorIssqn, String tipoCredito, boolean simplesNacional,
                                 String aliquota, String valorFaturado, String valorDeducao,
                                 String baseCalculo) {
        return new Credito(numeroCredito, numeroNfse, LocalDate.parse(dataConstituicao),
                new BigDecimal(valorIssqn), tipoCredito, simplesNacional, new BigDecimal(aliquota),
                new BigDecimal(valorFaturado), new BigDecimal(valorDeducao), new BigDecimal(baseCalculo));
    }
}
