package com.greenLogBackend.greenLogSolution.mapper;

import com.greenLogBackend.greenLogSolution.dto.RuaRequest;
import com.greenLogBackend.greenLogSolution.dto.RuaResponse;
import com.greenLogBackend.greenLogSolution.entity.Bairro;
import com.greenLogBackend.greenLogSolution.entity.Rua;

public final class RuaMapper {

    private RuaMapper() {}

    public static Rua toEntity(RuaRequest req, Bairro origem, Bairro destino) {
        Rua rua = new Rua();
        rua.setOrigem(origem);
        rua.setDestino(destino);
        rua.setDistanciaKm(req.distanciaKm());
        return rua;
    }

    public static RuaResponse toResponse(Rua rua) {
        return new RuaResponse(
                rua.getId(),
                rua.getOrigem().getId(),
                rua.getOrigem().getNome(),
                rua.getDestino().getId(),
                rua.getDestino().getNome(),
                rua.getDistanciaKm()
        );
    }
}