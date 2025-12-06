package com.greenLogBackend.greenLogSolution.controller;

import com.greenLogBackend.greenLogSolution.dto.UsuarioResponse;
import com.greenLogBackend.greenLogSolution.entity.Usuario;
import com.greenLogBackend.greenLogSolution.mapper.UsuarioMapper;
import com.greenLogBackend.greenLogSolution.repository.UsuarioRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UsuarioRepository usuarioRepository;

    public AuthController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping("/me")
    public ResponseEntity<UsuarioResponse> getCurrentUser() {
        // 1. O Spring Security já validou a senha (Basic Auth) antes de chegar aqui.
        // 2. Pegamos o login do usuário que passou na segurança:
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String login = authentication.getName();

        // 3. Buscamos os dados dele no banco
        Usuario usuario = usuarioRepository.findByLogin(login)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // 4. Convertemos para DTO (para não travar o JSON com erro 500)
        UsuarioResponse response = UsuarioMapper.toResponse(usuario);

        return ResponseEntity.ok(response);
    }
}