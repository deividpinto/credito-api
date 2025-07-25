package com.credito.api.service;

import com.credito.api.model.Usuario;
import com.credito.api.repository.UsuarioRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    @Test
    @DisplayName("Deve retornar null quando nao existir um usuário com email informado")
    void findByEmail_deveRetornarNullQuandoUsuarioNaoExistir() {
        when(usuarioRepository.findByEmail("teste@email.com")).thenReturn(Optional.empty());

        Usuario usuario = usuarioService.findByEmail("teste@email.com");

        assertNull(usuario);
        verify(usuarioRepository, times(1)).findByEmail("teste@email.com");
    }

    @Test
    @DisplayName("Deve retornar usuario de acordo com email informado")
    void findByEmail_deveRetornarUsuarioQuandoExistir() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("Usuário Teste");
        usuario.setEmail("teste@email.com");
        usuario.setPerfil("admin");
        usuario.setDataCadastro(LocalDateTime.now());
        when(usuarioRepository.findByEmail("teste@email.com")).thenReturn(Optional.of(usuario));

        Usuario resultado = usuarioService.findByEmail("teste@email.com");

        assertNotNull(resultado);
        assertEquals("teste@email.com", resultado.getEmail());
        verify(usuarioRepository, times(1)).findByEmail("teste@email.com");
    }
}
