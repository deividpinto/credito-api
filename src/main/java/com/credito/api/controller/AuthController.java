package com.credito.api.controller;

import com.credito.api.model.Usuario;
import com.credito.api.model.dto.AuthRequestDTO;
import com.credito.api.model.dto.AuthResponseDTO;
import com.credito.api.model.dto.UsuarioDTO;
import com.credito.api.security.JwtTokenUtil;
import com.credito.api.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticação", description = "Endpoints para autenticação de usuários")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final UsuarioService usuarioService;

    public AuthController(
            AuthenticationManager authenticationManager,
            JwtTokenUtil jwtTokenUtil,
            UsuarioService usuarioService
    ) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.usuarioService = usuarioService;
    }

    @Operation(summary = "Autenticar usuário",
            description = "Realiza a autenticação do usuário com suas credenciais e retorna o token JWT")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Autenticação realizada com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponseDTO.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
                                              "usuario": {
                                                "id": 1,
                                                "nome": "João Silva",
                                                "email": "joao.silva@exemplo.com",
                                                "perfil": "USUARIO",
                                                "dataCadastro": "2024-07-27T10:30:00"
                                              },
                                              "message": null
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Credenciais inválidas",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponseDTO.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "token": null,
                                              "usuario": null,
                                              "message": "E-mail ou senha inválidos."
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Requisição inválida")
    })
    @PostMapping()
    public ResponseEntity<?> auth(@RequestBody AuthRequestDTO authRequestDTO) {
        AuthResponseDTO authResponseDTO = new AuthResponseDTO();
        try {
            Authentication authentication =
                    authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    authRequestDTO.getEmail(),
                                    authRequestDTO.getSenha())
                    );
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            final String token = jwtTokenUtil.generateToken(userDetails);
            Usuario usuario = usuarioService.findByEmail(authRequestDTO.getEmail());
            UsuarioDTO usuarioDTO = new UsuarioDTO();
            usuarioDTO.setId(usuario.getId());
            usuarioDTO.setNome(usuario.getNome());
            usuarioDTO.setEmail(usuario.getEmail());
            usuarioDTO.setPerfil(usuario.getPerfil());
            usuarioDTO.setDataCadastro(usuario.getDataCadastro());
            authResponseDTO.setUsuario(usuarioDTO);
            authResponseDTO.setToken(token);
            return ResponseEntity.ok(authResponseDTO);
        } catch (BadCredentialsException | UsernameNotFoundException e) {
            authResponseDTO.setMessage("E-mail ou senha inválidos.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(authResponseDTO);
        }
    }
}
