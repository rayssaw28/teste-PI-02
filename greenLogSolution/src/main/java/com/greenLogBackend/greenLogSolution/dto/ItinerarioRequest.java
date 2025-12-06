package com.greenLogBackend.greenLogSolution.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record ItinerarioRequest(
        @NotNull(message = "A data é obrigatória")
        @FutureOrPresent(message = "A data deve ser hoje ou no futuro")
        LocalDate data,

        @NotNull(message = "O ID da rota é obrigatório")
        Long rotaId,

        @NotNull(message = "O ID do caminhão é obrigatório")
        Long caminhaoId
) {}