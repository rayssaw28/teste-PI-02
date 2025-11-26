package com.greenLogBackend.greenLogSolution.service;

import com.greenLogBackend.greenLogSolution.entity.Itinerario;
import com.greenLogBackend.greenLogSolution.entity.Rota;
import com.greenLogBackend.greenLogSolution.repository.ItinerarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class ItinerarioService {

    private final ItinerarioRepository itinerarioRepository;
    private final RotaService rotaService;

    public ItinerarioService(ItinerarioRepository itinerarioRepository, RotaService rotaService) {
        this.itinerarioRepository = itinerarioRepository;
        this.rotaService = rotaService;
    }

    public List<Itinerario> listarTodos() {
        return itinerarioRepository.findAll();
    }

    @Transactional
    public Itinerario agendar(Long rotaId, LocalDate data) {
        Rota rota = rotaService.buscarPorId(rotaId);

        boolean existeConflito = itinerarioRepository.existsByCaminhaoIdAndData(rota.getCaminhao().getId(), data);

        if (existeConflito) {
            throw new IllegalArgumentException("Conflito de agenda: O caminhão já possui um itinerário para o dia " + data);
        }

        Itinerario itinerario = new Itinerario();
        itinerario.setRota(rota);
        itinerario.setCaminhao(rota.getCaminhao());
        itinerario.setData(data);

        return itinerarioRepository.save(itinerario);
    }
}
