package com.greenLogBackend.greenLogSolution.service;

import com.greenLogBackend.greenLogSolution.dto.CaminhaoRequest;
import com.greenLogBackend.greenLogSolution.dto.CaminhaoResponse;
import com.greenLogBackend.greenLogSolution.entity.Caminhao;
import com.greenLogBackend.greenLogSolution.exception.BusinessException;
import com.greenLogBackend.greenLogSolution.exception.ResourceNotFoundException;
import com.greenLogBackend.greenLogSolution.mapper.CaminhaoMapper;
import com.greenLogBackend.greenLogSolution.repository.CaminhaoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CaminhaoService {

    private final CaminhaoRepository caminhaoRepository;

    public CaminhaoService(CaminhaoRepository caminhaoRepository) {
        this.caminhaoRepository = caminhaoRepository;
    }

    @Transactional(readOnly = true)
    public List<CaminhaoResponse> listarTodos() {
        return caminhaoRepository.findAll().stream()
                .map(CaminhaoMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CaminhaoResponse buscarPorId(Long id) {
        Caminhao caminhao = caminhaoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Caminhão não encontrado com ID: " + id));
        return CaminhaoMapper.toResponse(caminhao);
    }

    @Transactional
    public CaminhaoResponse salvar(CaminhaoRequest request) {
        if (caminhaoRepository.existsByPlaca(request.placa())) {
            throw new BusinessException("Já existe um caminhão cadastrado com a placa " + request.placa());
        }

        Caminhao novoCaminhao = CaminhaoMapper.toEntity(request);
        Caminhao caminhaoSalvo = caminhaoRepository.save(novoCaminhao);

        return CaminhaoMapper.toResponse(caminhaoSalvo);
    }

    @Transactional
    public CaminhaoResponse atualizar(Long id, CaminhaoRequest request) {
        Caminhao caminhaoExistente = caminhaoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Caminhão não encontrado com ID: " + id));

        if (!caminhaoExistente.getPlaca().equals(request.placa()) && caminhaoRepository.existsByPlaca(request.placa())) {
            throw new BusinessException("Já existe outro caminhão cadastrado com a placa " + request.placa());
        }

        CaminhaoMapper.copyToEntity(request, caminhaoExistente);
        return CaminhaoMapper.toResponse(caminhaoRepository.save(caminhaoExistente));
    }

    @Transactional
    public void excluir(Long id) {
        if (!caminhaoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Caminhão não encontrado para exclusão com ID: " + id);
        }
        caminhaoRepository.deleteById(id);
    }
}