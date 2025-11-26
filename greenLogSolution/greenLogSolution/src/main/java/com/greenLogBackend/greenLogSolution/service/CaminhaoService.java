package com.greenLogBackend.greenLogSolution.service;

import com.greenLogBackend.greenLogSolution.entity.Caminhao;
import com.greenLogBackend.greenLogSolution.repository.CaminhaoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CaminhaoService {

    private final CaminhaoRepository caminhaoRepository;

    public CaminhaoService(CaminhaoRepository caminhaoRepository) {
        this.caminhaoRepository = caminhaoRepository;
    }

    public List<Caminhao> listarTodos() {
        return caminhaoRepository.findAll();
    }

    public Caminhao buscarPorId(Long id) {
        return caminhaoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Caminhão não encontrado com ID: " + id));
    }

    @Transactional
    public Caminhao salvar(Caminhao caminhao) {
        if (caminhao.getId() == null && caminhaoRepository.existsByPlaca(caminhao.getPlaca())) {
            throw new IllegalArgumentException("Já existe um caminhão cadastrado com a placa " + caminhao.getPlaca());
        }
        return caminhaoRepository.save(caminhao);
    }

    @Transactional
    public void excluir(Long id) {
        if (!caminhaoRepository.existsById(id)) {
            throw new EntityNotFoundException("Caminhão não encontrado para exclusão.");
        }
        caminhaoRepository.deleteById(id);
    }
}