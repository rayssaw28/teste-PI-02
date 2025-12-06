package com.greenLogBackend.greenLogSolution.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record RuaRequest(
        @NotNull(message = "O ID do bairro de origem é obrigatório")
        Long origemId,

        @NotNull(message = "O ID do bairro de destino é obrigatório")
        Long destinoId,

        @Positive(message = "A distância deve ser maior que zero")
        Double distanciaKm
) {}