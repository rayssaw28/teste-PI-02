package com.greenLogBackend.greenLogSolution.service;

import com.greenLogBackend.greenLogSolution.dto.BairroResponse;
import com.greenLogBackend.greenLogSolution.mapper.BairroMapper;
import com.greenLogBackend.greenLogSolution.repository.BairroRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BairroService {

    private final BairroRepository bairroRepository;

    public BairroService(BairroRepository bairroRepository) {
        this.bairroRepository = bairroRepository;
    }

    @Transactional(readOnly = true)
    public List<BairroResponse> listarTodos() {
        return bairroRepository.findAll().stream()
                .map(BairroMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public boolean existePorId(Long id) {
        return bairroRepository.existsById(id);
    }
}