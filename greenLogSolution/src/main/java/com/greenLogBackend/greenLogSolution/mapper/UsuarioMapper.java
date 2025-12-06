package com.greenLogBackend.greenLogSolution.mapper;

import com.greenLogBackend.greenLogSolution.dto.UsuarioRequest;
import com.greenLogBackend.greenLogSolution.dto.UsuarioResponse;
import com.greenLogBackend.greenLogSolution.entity.Usuario;

public final class UsuarioMapper {

    private UsuarioMapper() {}

    public static Usuario toEntity(UsuarioRequest req) {
        return new Usuario(req.login(), req.senha(), req.perfil());
    }

    public static UsuarioResponse toResponse(Usuario usuario) {
        return new UsuarioResponse(
                usuario.getId(),
                usuario.getUsername(),
                usuario.getPerfil()
        );
    }
}