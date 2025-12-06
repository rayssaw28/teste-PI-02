package com.greenLogBackend.greenLogSolution.controller;

import com.greenLogBackend.greenLogSolution.dto.PontoColetaRequest;
import com.greenLogBackend.greenLogSolution.dto.PontoColetaResponse;
import com.greenLogBackend.greenLogSolution.service.PontoColetaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pontos-coleta")
// @CrossOrigin("*") // Configurar via SecurityConfig é mais seguro, mas mantive comentado se precisar rápido
public class PontoColetaController {

    private final PontoColetaService pontoColetaService;

    public PontoColetaController(PontoColetaService pontoColetaService) {
        this.pontoColetaService = pontoColetaService;
    }

    @PostMapping
    public ResponseEntity<PontoColetaResponse> criar(@RequestBody @Valid PontoColetaRequest request) {
        PontoColetaResponse novoPonto = pontoColetaService.criarPonto(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoPonto);
    }

    @GetMapping
    public ResponseEntity<List<PontoColetaResponse>> listar() {
        return ResponseEntity.ok(pontoColetaService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PontoColetaResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(pontoColetaService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PontoColetaResponse> atualizar(@PathVariable Long id, @RequestBody @Valid PontoColetaRequest request) {
        return ResponseEntity.ok(pontoColetaService.atualizarPonto(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        pontoColetaService.deletarPonto(id);
        return ResponseEntity.noContent().build();
    }
}