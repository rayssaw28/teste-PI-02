package com.greenLogBackend.greenLogSolution.controller;

import com.greenLogBackend.greenLogSolution.entity.Bairro;
import com.greenLogBackend.greenLogSolution.repository.BairroRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/bairros")
@CrossOrigin(origins = "*")
public class BairroController {

    private final BairroRepository bairroRepository;

    public BairroController(BairroRepository bairroRepository) {
        this.bairroRepository = bairroRepository;
    }

    @GetMapping
    public ResponseEntity<List<Bairro>> listarTodos() {
        return ResponseEntity.ok(bairroRepository.findAll());
    }
}