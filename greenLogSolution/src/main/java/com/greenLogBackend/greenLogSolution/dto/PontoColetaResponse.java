package com.greenLogBackend.greenLogSolution.dto;

import com.greenLogBackend.greenLogSolution.enums.TipoResiduo;
import java.util.Set;

public record PontoColetaResponse(
        Long id,
        String nome,
        String endereco,
        String responsavel,
        String contato,
        Set<TipoResiduo> tiposResiduos,
        Long bairroId,
        String bairroNome
) {}