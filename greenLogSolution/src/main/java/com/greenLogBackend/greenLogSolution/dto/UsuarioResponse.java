package com.greenLogBackend.greenLogSolution.dto;

import com.greenLogBackend.greenLogSolution.enums.PerfilUsuario;

public record UsuarioResponse(
        Long id,
        String login,
        PerfilUsuario perfil
) {}