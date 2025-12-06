package com.greenLogBackend.greenLogSolution.controller;

import com.greenLogBackend.greenLogSolution.dto.ItinerarioRequest;
import com.greenLogBackend.greenLogSolution.dto.ItinerarioResponse;
import com.greenLogBackend.greenLogSolution.service.ItinerarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/itinerarios")
public class ItinerarioController {

    private final ItinerarioService itinerarioService;

    public ItinerarioController(ItinerarioService itinerarioService) {
        this.itinerarioService = itinerarioService;
    }

    @GetMapping
    public ResponseEntity<List<ItinerarioResponse>> listarTodos() {
        return ResponseEntity.ok(itinerarioService.listarTodos());
    }

    @PostMapping("/agendar")
    public ResponseEntity<ItinerarioResponse> agendarItinerario(@RequestBody @Valid ItinerarioRequest request) {
        ItinerarioResponse novoItinerario = itinerarioService.agendar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoItinerario);
    }
}