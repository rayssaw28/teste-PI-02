package com.greenLogBackend.greenLogSolution.spec;

import com.greenLogBackend.greenLogSolution.entity.PontoColeta;
import com.greenLogBackend.greenLogSolution.enums.TipoResiduo;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

public class PontoColetaSpecification implements Specification<PontoColeta> {

    private final String key;
    private final String operation;
    private final String value;

    public PontoColetaSpecification(String key, String operation, String value) {
        this.key = key;
        this.operation = operation;
        this.value = value;
    }

    @Override
    public Predicate toPredicate(Root<PontoColeta> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        Path<String> path;
        if (key.contains(".")) {
            String[] split = key.split("\\.");
            path = root.join(split[0]).get(split[1]);
        } else {
            path = root.get(key);
        }

        switch (operation.toUpperCase()) {
            case "=":
                if (key.equalsIgnoreCase("tiposResiduos")) {
                    return builder.isMember(TipoResiduo.valueOf(value.toUpperCase()), root.get("tiposResiduos"));
                }
                return builder.equal(path, value);
            case ":":
            case "LIKE":
                return builder.like(builder.lower(path.as(String.class)), "%" + value.toLowerCase() + "%");
            default:
                return null;
        }
    }
}