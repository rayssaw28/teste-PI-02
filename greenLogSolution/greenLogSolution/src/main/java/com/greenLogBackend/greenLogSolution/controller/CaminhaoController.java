package com.greenLogBackend.greenLogSolution.controller;

import com.greenLogBackend.greenLogSolution.entity.Caminhao;
import com.greenLogBackend.greenLogSolution.service.CaminhaoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/caminhoes")
@CrossOrigin(origins = "*")
public class CaminhaoController {

    private final CaminhaoService caminhaoService;

    public CaminhaoController(CaminhaoService caminhaoService) {
        this.caminhaoService = caminhaoService;
    }

    @GetMapping
    public ResponseEntity<List<Caminhao>> listar() {
        return ResponseEntity.ok(caminhaoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Caminhao> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(caminhaoService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Caminhao> criar(@Valid @RequestBody Caminhao caminhao) {
        Caminhao novoCaminhao = caminhaoService.salvar(caminhao);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoCaminhao);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        caminhaoService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}