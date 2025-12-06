package com.greenLogBackend.greenLogSolution.controller;

import com.greenLogBackend.greenLogSolution.dto.CaminhaoRequest;
import com.greenLogBackend.greenLogSolution.dto.CaminhaoResponse;
import com.greenLogBackend.greenLogSolution.service.CaminhaoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/caminhoes")
public class CaminhaoController {

    private final CaminhaoService caminhaoService;

    public CaminhaoController(CaminhaoService caminhaoService) {
        this.caminhaoService = caminhaoService;
    }

    @GetMapping
    public ResponseEntity<List<CaminhaoResponse>> listar() {
        return ResponseEntity.ok(caminhaoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CaminhaoResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(caminhaoService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<CaminhaoResponse> criar(@Valid @RequestBody CaminhaoRequest request) {
        CaminhaoResponse novoCaminhao = caminhaoService.salvar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoCaminhao);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CaminhaoResponse> atualizar(@PathVariable Long id, @Valid @RequestBody CaminhaoRequest request) {
        return ResponseEntity.ok(caminhaoService.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        caminhaoService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}