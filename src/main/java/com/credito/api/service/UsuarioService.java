package com.credito.api.service;

import com.credito.api.model.Usuario;
import com.credito.api.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    @Autowired
    UsuarioRepository usuarioRepository;

    public Usuario findByEmail(String email) {
        return this.usuarioRepository.findByEmail(email).orElse(null);
    }
}
