package com.greenLogBackend.greenLogSolution.dto;

import com.greenLogBackend.greenLogSolution.enums.PerfilUsuario;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UsuarioRequest(
        @NotBlank(message = "O login é obrigatório")
        String login,

        @NotBlank(message = "A senha é obrigatória")
        @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres")
        String senha,

        @NotNull(message = "O perfil do usuário é obrigatório")
        PerfilUsuario perfil
) {}