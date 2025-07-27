
package com.credito.api.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO de resposta para autenticação")
public class AuthResponseDTO {

    @Schema(description = "Token JWT de autenticação",
            example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String token;

    @Schema(description = "Dados do usuário autenticado")
    private UsuarioDTO usuario;

    @Schema(description = "Mensagem de resposta quando possui um erro",
            example = "Dados inválidos.")
    private String message;
}
