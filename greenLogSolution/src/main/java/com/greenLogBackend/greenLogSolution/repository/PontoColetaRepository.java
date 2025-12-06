package com.greenLogBackend.greenLogSolution.repository;

import com.greenLogBackend.greenLogSolution.entity.PontoColeta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor; // Import novo
import java.util.List;

public interface PontoColetaRepository extends JpaRepository<PontoColeta, Long>, JpaSpecificationExecutor<PontoColeta> {

    List<PontoColeta> findByBairroNome(String nomeBairro);
    boolean existsByNome(String nome);
}