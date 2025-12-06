package com.greenLogBackend.greenLogSolution.mapper;

import com.greenLogBackend.greenLogSolution.dto.CaminhaoRequest;
import com.greenLogBackend.greenLogSolution.dto.CaminhaoResponse;
import com.greenLogBackend.greenLogSolution.entity.Caminhao;

public final class CaminhaoMapper {

    private CaminhaoMapper() {}

    public static Caminhao toEntity(CaminhaoRequest req) {
        Caminhao c = new Caminhao();
        c.setPlaca(req.placa());
        c.setModelo(req.modelo());
        c.setMotorista(req.motorista());
        c.setCapacidadeCarga(req.capacidadeCarga());
        c.setTiposResiduos(req.tiposResiduos());
        return c;
    }

    public static void copyToEntity(CaminhaoRequest req, Caminhao c) {
        c.setPlaca(req.placa());
        c.setModelo(req.modelo());
        c.setMotorista(req.motorista());
        c.setCapacidadeCarga(req.capacidadeCarga());
        c.setTiposResiduos(req.tiposResiduos());
    }

    public static CaminhaoResponse toResponse(Caminhao c) {
        return new CaminhaoResponse(
                c.getId(),
                c.getPlaca(),
                c.getModelo(),
                c.getMotorista(),
                c.getCapacidadeCarga(),
                c.getTiposResiduos()
        );
    }
}