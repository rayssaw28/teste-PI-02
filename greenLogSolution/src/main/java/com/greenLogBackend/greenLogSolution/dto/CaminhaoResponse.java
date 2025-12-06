package com.greenLogBackend.greenLogSolution.dto;

import com.greenLogBackend.greenLogSolution.enums.TipoResiduo;
import java.util.Set;

public record CaminhaoResponse(
        Long id,
        String placa,
        String modelo,
        String motorista,
        Double capacidadeCarga,
        Set<TipoResiduo> tiposResiduos
) {}