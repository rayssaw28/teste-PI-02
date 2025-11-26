package com.greenLogBackend.greenLogSolution.service;

import com.greenLogBackend.greenLogSolution.entity.PontoColeta;
import com.greenLogBackend.greenLogSolution.repository.PontoColetaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PontoColetaService {

    private final PontoColetaRepository pontoColetaRepository;

    public PontoColetaService(PontoColetaRepository pontoColetaRepository) {
        this.pontoColetaRepository = pontoColetaRepository;
    }

    public List<PontoColeta> listarTodos() {
        return pontoColetaRepository.findAll();
    }

    public PontoColeta buscarPorId(Long id) {
        return pontoColetaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ponto de coleta não encontrado. ID: " + id));
    }

    @Transactional
    public PontoColeta salvar(PontoColeta ponto) {
        // Regra: Nome do ponto deve ser único
        if (ponto.getId() == null && pontoColetaRepository.existsByNome(ponto.getNome())) {
            throw new IllegalArgumentException("Já existe um ponto de coleta com o nome '" + ponto.getNome() + "'");
        }
        return pontoColetaRepository.save(ponto);
    }

    @Transactional
    public void excluir(Long id) {
        if (!pontoColetaRepository.existsById(id)) {
            throw new EntityNotFoundException("Ponto de coleta não encontrado.");
        }
        pontoColetaRepository.deleteById(id);
    }
}