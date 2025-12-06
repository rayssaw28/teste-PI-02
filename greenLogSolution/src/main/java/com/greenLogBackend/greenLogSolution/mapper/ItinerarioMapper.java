package com.greenLogBackend.greenLogSolution.mapper;

import com.greenLogBackend.greenLogSolution.dto.ItinerarioRequest;
import com.greenLogBackend.greenLogSolution.dto.ItinerarioResponse;
import com.greenLogBackend.greenLogSolution.entity.Caminhao;
import com.greenLogBackend.greenLogSolution.entity.Itinerario;
import com.greenLogBackend.greenLogSolution.entity.Rota;

public final class ItinerarioMapper {

    private ItinerarioMapper() {}

    public static Itinerario toEntity(ItinerarioRequest req, Rota rota, Caminhao caminhao) {
        Itinerario it = new Itinerario();
        it.setData(req.data());
        it.setRota(rota);
        it.setCaminhao(caminhao);
        return it;
    }

    public static ItinerarioResponse toResponse(Itinerario it) {
        return new ItinerarioResponse(
                it.getId(),
                it.getData(),
                it.getRota().getId(),
                it.getCaminhao().getId(),
                it.getCaminhao().getPlaca()
        );
    }
}