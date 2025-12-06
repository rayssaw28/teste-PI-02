package com.greenLogBackend.greenLogSolution.controller;

import com.greenLogBackend.greenLogSolution.entity.Rota;
import com.greenLogBackend.greenLogSolution.enums.TipoResiduo;
import com.greenLogBackend.greenLogSolution.service.RotaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rotas")
@CrossOrigin(origins = "*")
public class RotaController {

    private final RotaService rotaService;

    public RotaController(RotaService rotaService) {
        this.rotaService = rotaService;
    }

    @GetMapping
    public ResponseEntity<List<Rota>> listarTodas() {
        return ResponseEntity.ok(rotaService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Rota> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(rotaService.buscarPorId(id));
    }

    @PostMapping("/calcular")
    public ResponseEntity<Rota> calcularRota(
            @RequestParam Long caminhaoId,
            @RequestParam Long origemId,
            @RequestParam Long destinoId,
            @RequestParam TipoResiduo tipoResiduo) {

        Rota rotaCalculada = rotaService.calcularESalvarRota(caminhaoId, origemId, destinoId, tipoResiduo);
        return ResponseEntity.status(HttpStatus.CREATED).body(rotaCalculada);
    }
}