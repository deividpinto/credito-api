package com.credito.api.controller;

import com.credito.api.model.Usuario;
import com.credito.api.model.dto.AuthRequestDTO;
import com.credito.api.model.dto.AuthResponseDTO;
import com.credito.api.security.JwtTokenUtil;
import com.credito.api.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenUtil jwtTokenUtil;

    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private AuthController authController;

    private AuthRequestDTO authRequestDTO;
    private Usuario usuario;
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        authRequestDTO = new AuthRequestDTO();
        authRequestDTO.setEmail("teste@email.com");
        authRequestDTO.setSenha("senha123");

        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("Usuário Teste");
        usuario.setEmail("teste@email.com");
        usuario.setPerfil("admin");
        usuario.setDataCadastro(LocalDateTime.now());

        UserDetails userDetails = new User("teste@email.com", "senha123", new ArrayList<>());
        authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
    }

    @Test
    @DisplayName("Deve realizar login com sucesso")
    void auth_deveRealizarLoginComSucesso() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(jwtTokenUtil.generateToken(any(UserDetails.class)))
                .thenReturn("token-jwt-teste");
        when(usuarioService.findByEmail("teste@email.com"))
                .thenReturn(usuario);

        ResponseEntity<?> response = authController.auth(authRequestDTO);
        AuthResponseDTO authResponseDTO = (AuthResponseDTO) response.getBody();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(authResponseDTO);
        assertEquals("token-jwt-teste", authResponseDTO.getToken());
        assertEquals(usuario.getId(), authResponseDTO.getUsuario().getId());
        assertEquals(usuario.getEmail(), authResponseDTO.getUsuario().getEmail());
        assertEquals(usuario.getNome(), authResponseDTO.getUsuario().getNome());
        assertEquals(usuario.getPerfil(), authResponseDTO.getUsuario().getPerfil());
    }

    @Test
    @DisplayName("Deve retornar erro quando credenciais são inválidas")
    void auth_deveRetornarErroQuandoCredenciaisInvalidas() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("E-mail ou senha inválidos."));

        ResponseEntity<?> response = authController.auth(authRequestDTO);
        AuthResponseDTO authResponseDTO = (AuthResponseDTO) response.getBody();

        assertNotNull(response);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(authResponseDTO);
        assertEquals("E-mail ou senha inválidos.", authResponseDTO.getMessage());
    }
}
