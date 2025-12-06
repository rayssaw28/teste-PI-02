package com.greenLogBackend.greenLogSolution.repository;

import com.greenLogBackend.greenLogSolution.entity.Caminhao;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CaminhaoRepository extends JpaRepository<Caminhao, Long> {
    Optional<Caminhao> findByPlaca(String placa);
    boolean existsByPlaca(String placa);
}