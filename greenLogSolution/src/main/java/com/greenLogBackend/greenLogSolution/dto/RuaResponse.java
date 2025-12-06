package com.greenLogBackend.greenLogSolution.dto;

public record RuaResponse(
        Long id,
        Long origemId,
        String origemNome,
        Long destinoId,
        String destinoNome,
        Double distanciaKm
) {}