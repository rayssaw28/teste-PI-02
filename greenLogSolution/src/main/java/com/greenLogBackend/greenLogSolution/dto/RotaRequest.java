package com.greenLogBackend.greenLogSolution.dto;

import com.greenLogBackend.greenLogSolution.enums.TipoResiduo;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record RotaRequest(
        @NotNull(message = "O ID do caminhão é obrigatório")
        Long caminhaoId,

        @NotNull(message = "O tipo de resíduo da rota é obrigatório")
        TipoResiduo tipoResiduo,

        @NotNull(message = "A sequência de bairros é obrigatória")
        List<Long> bairroIds
) {}