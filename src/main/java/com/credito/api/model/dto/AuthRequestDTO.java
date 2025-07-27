package com.credito.api.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO de requisição de autenticação")
public class AuthRequestDTO {
    @Schema(description = "Email do usuário", example = "usuario@exemplo.com")
    String email;

    @Schema(description = "Senha do usuário", example = "123456")
    String senha;
}
