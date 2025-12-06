package com.greenLogBackend.greenLogSolution.repository;

import com.greenLogBackend.greenLogSolution.entity.Bairro;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface BairroRepository extends JpaRepository<Bairro, Long> {
    Optional<Bairro> findByNome(String nome);
    boolean existsByNome(String nome);
}