package com.greenLogBackend.greenLogSolution.service;

import com.greenLogBackend.greenLogSolution.entity.PontoColeta;
import com.greenLogBackend.greenLogSolution.repository.PontoColetaRepository;
import com.greenLogBackend.greenLogSolution.spec.PontoColetaSpecification;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ConsultaAvancadaService {

    private final PontoColetaRepository repository;

    public ConsultaAvancadaService(PontoColetaRepository repository) {
        this.repository = repository;
    }

    public List<PontoColeta> executarConsulta(String comando) {
        Specification<PontoColeta> spec = Specification.where(null);


        Pattern pattern = Pattern.compile("(\\w+(\\.\\w+)?)\\s*(=|:)\\s*\"([^\"]+)\"");
        Matcher matcher = pattern.matcher(comando + ","); // Hackzinho para fechar o loop se tiver multiplos

        while (matcher.find()) {
            String key = matcher.group(1);
            String op = matcher.group(3);
            String val = matcher.group(4);

            PontoColetaSpecification novaSpec = new PontoColetaSpecification(key, op, val);
            spec = spec.and(novaSpec);
        }

        return repository.findAll(spec);
    }
}