package com.greenLogBackend.greenLogSolution.controller;

import com.greenLogBackend.greenLogSolution.dto.RotaRequest;
import com.greenLogBackend.greenLogSolution.dto.RotaResponse;
import com.greenLogBackend.greenLogSolution.service.RotaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rotas")
public class RotaController {

    private final RotaService rotaService;

    public RotaController(RotaService rotaService) {
        this.rotaService = rotaService;
    }

    @GetMapping
    public ResponseEntity<List<RotaResponse>> listarTodas() {
        return ResponseEntity.ok(rotaService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RotaResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(rotaService.buscarPorId(id));
    }

    @PostMapping("/calcular")
    public ResponseEntity<RotaResponse> calcularRota(@RequestBody @Valid RotaRequest request) {
        RotaResponse novaRota = rotaService.calcularESalvarRota(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaRota);
    }

    @PostMapping("/atualizar-grafo")
    public ResponseEntity<Void> atualizarGrafo() {
        rotaService.atualizarGrafo();
        return ResponseEntity.noContent().build();
    }
}