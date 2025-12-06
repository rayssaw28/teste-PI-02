package com.greenLogBackend.greenLogSolution.controller;

import com.greenLogBackend.greenLogSolution.dto.LoginRequest; // Importe o novo DTO
import com.greenLogBackend.greenLogSolution.dto.UsuarioRequest;
import com.greenLogBackend.greenLogSolution.dto.UsuarioResponse;
import com.greenLogBackend.greenLogSolution.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UsuarioService usuarioService;
    private final AuthenticationManager authenticationManager;

    public AuthController(UsuarioService usuarioService, AuthenticationManager authenticationManager) {
        this.usuarioService = usuarioService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/register")
    public ResponseEntity<UsuarioResponse> registrar(@RequestBody @Valid UsuarioRequest request) {
        UsuarioResponse novoUsuario = usuarioService.cadastrarUsuario(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoUsuario);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest request) {
        try {
            UsernamePasswordAuthenticationToken dadosLogin =
                    new UsernamePasswordAuthenticationToken(request.login(), request.senha());

            Authentication auth = authenticationManager.authenticate(dadosLogin);

            return ResponseEntity.ok().body("Autenticação realizada com sucesso para o usuário: " + auth.getName());

        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Falha na autenticação: Credenciais inválidas.");
        }
    }
}