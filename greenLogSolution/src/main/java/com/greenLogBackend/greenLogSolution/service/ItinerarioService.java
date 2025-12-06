package com.greenLogBackend.greenLogSolution.service;

import com.greenLogBackend.greenLogSolution.dto.ItinerarioRequest;
import com.greenLogBackend.greenLogSolution.dto.ItinerarioResponse;
import com.greenLogBackend.greenLogSolution.dto.RotaResponse;
import com.greenLogBackend.greenLogSolution.entity.Caminhao;
import com.greenLogBackend.greenLogSolution.entity.Itinerario;
import com.greenLogBackend.greenLogSolution.entity.Rota;
import com.greenLogBackend.greenLogSolution.exception.BusinessException;
import com.greenLogBackend.greenLogSolution.mapper.ItinerarioMapper;
import com.greenLogBackend.greenLogSolution.repository.ItinerarioRepository;
import com.greenLogBackend.greenLogSolution.repository.RotaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItinerarioService {

    private final ItinerarioRepository itinerarioRepository;
    private final RotaRepository rotaRepository; // Acesso direto ao repo para pegar a Entidade Rota

    public ItinerarioService(ItinerarioRepository itinerarioRepository, RotaRepository rotaRepository) {
        this.itinerarioRepository = itinerarioRepository;
        this.rotaRepository = rotaRepository;
    }

    @Transactional(readOnly = true)
    public List<ItinerarioResponse> listarTodos() {
        return itinerarioRepository.findAll().stream()
                .map(ItinerarioMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public ItinerarioResponse agendar(ItinerarioRequest request) {
        Rota rota = rotaRepository.findById(request.rotaId())
                .orElseThrow(() -> new BusinessException("Rota não encontrada com ID: " + request.rotaId()));

        Caminhao caminhaoDaRota = rota.getCaminhao();

        boolean existeConflito = itinerarioRepository.existsByCaminhaoIdAndData(caminhaoDaRota.getId(), request.data());

        if (existeConflito) {
            throw new BusinessException("Conflito de agenda: O caminhão " + caminhaoDaRota.getPlaca() +
                    " já possui um itinerário para o dia " + request.data());
        }

        Itinerario itinerario = ItinerarioMapper.toEntity(request, rota, caminhaoDaRota);

        Itinerario salvo = itinerarioRepository.save(itinerario);
        return ItinerarioMapper.toResponse(salvo);
    }
}