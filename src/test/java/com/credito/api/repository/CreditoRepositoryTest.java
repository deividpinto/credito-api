package com.credito.api.repository;

import com.credito.api.model.Credito;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
public class CreditoRepositoryTest {

    @Autowired
    private CreditoRepository creditoRepository;

    @BeforeEach
    void setUp() {
        creditoRepository.deleteAll();

        Credito credito1 = new Credito("123456", "7891011", LocalDate.parse("2024-02-25"),
                new BigDecimal("1500.75"), "ISSQN", true, new BigDecimal("5.0"),
                new BigDecimal("30000.00"), new BigDecimal("5000.00"), new BigDecimal("25000.00"));
        Credito credito2 = new Credito("789012", "7891011", LocalDate.parse("2024-02-26"),
                new BigDecimal("1200.50"), "ISSQN", false, new BigDecimal("4.5"),
                new BigDecimal("25000.00"), new BigDecimal("4000.00"), new BigDecimal("21000.00"));

        creditoRepository.saveAll(List.of(credito1, credito2));
    }

    @Test
    void findByNumeroNfse_deveRetornarCreditosCorrespondentes() {
        List<Credito> result = creditoRepository.findByNumeroNfseLike("7891011");

        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(c -> c.getNumeroNfse().equals("7891011")));
    }

    @Test
    void findCreditoByNumeroCredito_deveRetornarCreditoCorreto() {
        Credito credito = creditoRepository.findCreditoByNumeroCredito("123456");

        assertNotNull(credito);
        assertEquals("7891011", credito.getNumeroNfse());
        assertEquals(new BigDecimal("1500.75"), credito.getValorIssqn());
    }

    @Test
    void findByNumeroNfse_deveRetornarListaVaziaParaNfseInexistente() {
        List<Credito> result = creditoRepository.findByNumeroNfseLike("123456");

        assertTrue(result.isEmpty());
    }

    @Test
    void findCreditoByNumeroCredito_deveRetornarVazioParaCreditoInexistente() {
        Credito credito = creditoRepository.findCreditoByNumeroCredito("999");

        assertNull(credito);
    }

    @Test
    void save_devePersistirNovoCredito() {
        Credito novoCredito = new Credito("789013", "7891015", LocalDate.parse("2024-02-27"),
                new BigDecimal("1300.50"), "ISSQN", false, new BigDecimal("2.5"),
                new BigDecimal("27000.00"), new BigDecimal("2500.00"), new BigDecimal("23000.00"));

        Credito saved = creditoRepository.save(novoCredito);
        Credito credito = creditoRepository.findCreditoByNumeroCredito("789013");

        assertNotNull(credito);
        assertEquals("7891015", credito.getNumeroNfse());
        assertEquals(new BigDecimal("1300.50"), credito.getValorIssqn());
    }
}
