package com.greenLogBackend.greenLogSolution.dto;

import com.greenLogBackend.greenLogSolution.enums.TipoResiduo;
import java.util.List;

public record RotaResponse(
        Long id,
        Long caminhaoId,
        String caminhaoPlaca,
        Double distanciaTotal,
        TipoResiduo tipoResiduo,
        List<BairroResponse> sequenciaBairros
) {}