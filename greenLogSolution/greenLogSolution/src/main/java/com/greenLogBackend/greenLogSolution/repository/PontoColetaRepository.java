package com.greenLogBackend.greenLogSolution.repository;

import com.greenLogBackend.greenLogSolution.entity.PontoColeta;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PontoColetaRepository extends JpaRepository<PontoColeta, Long> {

    List<PontoColeta> findByBairroNome(String nomeBairro);
    boolean existsByNome(String nome);
}