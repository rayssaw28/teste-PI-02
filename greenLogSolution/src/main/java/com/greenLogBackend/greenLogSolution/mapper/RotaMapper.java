package com.greenLogBackend.greenLogSolution.mapper;

import com.greenLogBackend.greenLogSolution.dto.RotaRequest;
import com.greenLogBackend.greenLogSolution.dto.RotaResponse;
import com.greenLogBackend.greenLogSolution.entity.Bairro;
import com.greenLogBackend.greenLogSolution.entity.Caminhao;
import com.greenLogBackend.greenLogSolution.entity.Rota;
import java.util.List;

public final class RotaMapper {

    private RotaMapper() {}

    public static Rota toEntity(RotaRequest req, Caminhao caminhao, List<Bairro> sequenciaBairros, Double distanciaTotal) {
        Rota rota = new Rota();
        rota.setCaminhao(caminhao);
        rota.setTipoResiduo(req.tipoResiduo());
        rota.setSequenciaBairros(sequenciaBairros);
        rota.setDistanciaTotal(distanciaTotal);
        return rota;
    }

    public static RotaResponse toResponse(Rota rota) {
        return new RotaResponse(
                rota.getId(),
                rota.getCaminhao().getId(),
                rota.getCaminhao().getPlaca(),
                rota.getDistanciaTotal(),
                rota.getTipoResiduo(),
                rota.getSequenciaBairros().stream()
                        .map(BairroMapper::toResponse)
                        .toList()
        );
    }
}