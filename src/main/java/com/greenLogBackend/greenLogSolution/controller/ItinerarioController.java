package com.greenLogBackend.greenLogSolution.controller;

import com.greenLogBackend.greenLogSolution.entity.Itinerario;
import com.greenLogBackend.greenLogSolution.service.ItinerarioService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/itinerarios")
@CrossOrigin(origins = "*")
public class ItinerarioController {

    private final ItinerarioService itinerarioService;

    public ItinerarioController(ItinerarioService itinerarioService) {
        this.itinerarioService = itinerarioService;
    }

    @GetMapping
    public ResponseEntity<List<Itinerario>> listarTodos() {
        return ResponseEntity.ok(itinerarioService.listarTodos());
    }

    @PostMapping("/agendar")
    public ResponseEntity<Itinerario> agendarItinerario(
            @RequestParam Long rotaId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {

        Itinerario novoItinerario = itinerarioService.agendar(rotaId, data);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoItinerario);
    }
}