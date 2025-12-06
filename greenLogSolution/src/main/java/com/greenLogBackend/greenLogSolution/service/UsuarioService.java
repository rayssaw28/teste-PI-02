package com.greenLogBackend.greenLogSolution.service;

import com.greenLogBackend.greenLogSolution.dto.UsuarioRequest;
import com.greenLogBackend.greenLogSolution.dto.UsuarioResponse;
import com.greenLogBackend.greenLogSolution.entity.Usuario;
import com.greenLogBackend.greenLogSolution.mapper.UsuarioMapper;
import com.greenLogBackend.greenLogSolution.repository.UsuarioRepository;
import com.greenLogBackend.greenLogSolution.exception.BusinessException; // Assegure-se de que essa exceção existe
import com.greenLogBackend.greenLogSolution.exception.ResourceNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UsuarioResponse cadastrarUsuario(UsuarioRequest request) {
        if (usuarioRepository.findByLogin(request.login()).isPresent()) {
            throw new BusinessException("O login '" + request.login() + "' já está em uso.");
        }

        Usuario novoUsuario = UsuarioMapper.toEntity(request);

        String senhaCriptografada = passwordEncoder.encode(request.senha());
        novoUsuario.setSenha(senhaCriptografada);

        Usuario usuarioSalvo = usuarioRepository.save(novoUsuario);

        return UsuarioMapper.toResponse(usuarioSalvo);
    }

    @Transactional(readOnly = true)
    public List<UsuarioResponse> listarTodos() {
        return usuarioRepository.findAll().stream()
                .map(UsuarioMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UsuarioResponse buscarPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com id: " + id));
        return UsuarioMapper.toResponse(usuario);
    }

    @Transactional
    public void deletarUsuario(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new ResourceNotFoundException("Usuário não encontrado com id: " + id);
        }
        usuarioRepository.deleteById(id);
    }
}