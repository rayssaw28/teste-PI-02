package com.greenLogBackend.greenLogSolution.service;

import com.greenLogBackend.greenLogSolution.dto.RuaResponse;
import com.greenLogBackend.greenLogSolution.mapper.RuaMapper;
import com.greenLogBackend.greenLogSolution.repository.RuaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RuaService {

    private final RuaRepository ruaRepository;

    public RuaService(RuaRepository ruaRepository) {
        this.ruaRepository = ruaRepository;
    }

    @Transactional(readOnly = true)
    public List<RuaResponse> listarTodas() {
        return ruaRepository.findAll().stream()
                .map(RuaMapper::toResponse)
                .collect(Collectors.toList());
    }
}