package com.greenLogBackend.greenLogSolution.dto;

import com.greenLogBackend.greenLogSolution.enums.TipoResiduo;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record RotaRequest(
        @NotNull(message = "O ID do caminhão é obrigatório")
        Long caminhaoId,

        @NotNull(message = "O tipo de resíduo a ser coletado é obrigatório")
        TipoResiduo tipoResiduo,

        @NotEmpty(message = "A lista de bairros não pode estar vazia")
        List<Long> bairroIds
) {}