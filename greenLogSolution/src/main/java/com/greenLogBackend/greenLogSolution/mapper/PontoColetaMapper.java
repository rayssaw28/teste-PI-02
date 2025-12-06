package com.greenLogBackend.greenLogSolution.mapper;

import com.greenLogBackend.greenLogSolution.dto.PontoColetaRequest;
import com.greenLogBackend.greenLogSolution.dto.PontoColetaResponse;
import com.greenLogBackend.greenLogSolution.entity.Bairro;
import com.greenLogBackend.greenLogSolution.entity.PontoColeta;

public final class PontoColetaMapper {

    private PontoColetaMapper() {}

    public static PontoColeta toEntity(PontoColetaRequest req, Bairro bairro) {
        PontoColeta pc = new PontoColeta();
        pc.setNome(req.nome());
        pc.setEndereco(req.endereco());
        pc.setResponsavel(req.responsavel());
        pc.setCpfResponsavel(req.cpfResponsavel());
        pc.setContato(req.contato());
        pc.setTiposResiduos(req.tiposResiduos());
        pc.setBairro(bairro);
        return pc;
    }

    public static void copyToEntity(PontoColetaRequest req, PontoColeta pc, Bairro bairro) {
        pc.setNome(req.nome());
        pc.setEndereco(req.endereco());
        pc.setResponsavel(req.responsavel());
        pc.setCpfResponsavel(req.cpfResponsavel());
        pc.setContato(req.contato());
        pc.setTiposResiduos(req.tiposResiduos());
        pc.setBairro(bairro);
    }

    public static PontoColetaResponse toResponse(PontoColeta pc) {
        return new PontoColetaResponse(
                pc.getId(),
                pc.getNome(),
                pc.getEndereco(),
                pc.getResponsavel(),
                pc.getContato(),
                pc.getTiposResiduos(),
                pc.getBairro().getId(),
                pc.getBairro().getNome()
        );
    }
}