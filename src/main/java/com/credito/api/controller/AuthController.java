package com.credito.api.controller;

import com.credito.api.model.Usuario;
import com.credito.api.model.dto.AuthRequestDTO;
import com.credito.api.model.dto.AuthResponseDTO;
import com.credito.api.model.dto.UsuarioDTO;
import com.credito.api.security.JwtTokenUtil;
import com.credito.api.service.UsuarioService;
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
