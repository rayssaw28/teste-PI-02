package com.greenLogBackend.greenLogSolution.dto;

import java.time.LocalDate;

public record ItinerarioResponse(
        Long id,
        LocalDate data,
        Long rotaId,
        Long caminhaoId,
        String caminhaoPlaca
) {}