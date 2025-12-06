package com.greenLogBackend.greenLogSolution.dto;

import jakarta.validation.constraints.NotBlank;

public record BairroRequest(
        @NotBlank(message = "O nome do bairro é obrigatório")
        String nome
) {}