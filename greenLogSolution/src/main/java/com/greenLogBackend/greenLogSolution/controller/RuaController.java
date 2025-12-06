package com.greenLogBackend.greenLogSolution.controller;

import com.greenLogBackend.greenLogSolution.dto.RuaResponse;
import com.greenLogBackend.greenLogSolution.service.RuaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/ruas")
public class RuaController {

    private final RuaService ruaService;

    public RuaController(RuaService ruaService) {
        this.ruaService = ruaService;
    }

    @GetMapping
    public ResponseEntity<List<RuaResponse>> listarTodas() {
        return ResponseEntity.ok(ruaService.listarTodas());
    }
}