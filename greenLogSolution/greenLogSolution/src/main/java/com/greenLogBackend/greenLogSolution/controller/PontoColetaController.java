package com.greenLogBackend.greenLogSolution.controller;

import com.greenLogBackend.greenLogSolution.entity.PontoColeta;
import com.greenLogBackend.greenLogSolution.service.PontoColetaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pontos-coleta")
@CrossOrigin(origins = "*")
public class PontoColetaController {

    private final PontoColetaService pontoColetaService;

    public PontoColetaController(PontoColetaService pontoColetaService) {
        this.pontoColetaService = pontoColetaService;
    }

    @GetMapping
    public ResponseEntity<List<PontoColeta>> listar() {
        return ResponseEntity.ok(pontoColetaService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PontoColeta> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(pontoColetaService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<PontoColeta> criar(@Valid @RequestBody PontoColeta ponto) {
        PontoColeta novoPonto = pontoColetaService.salvar(ponto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoPonto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        pontoColetaService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}