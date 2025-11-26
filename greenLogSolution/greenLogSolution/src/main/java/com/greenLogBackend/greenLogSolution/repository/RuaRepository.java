package com.greenLogBackend.greenLogSolution.repository;

import com.greenLogBackend.greenLogSolution.entity.Bairro;
import com.greenLogBackend.greenLogSolution.entity.Rua;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface RuaRepository extends JpaRepository<Rua, Long> {

    Optional<Rua> findByOrigemAndDestino(Bairro origem, Bairro destino);

    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END FROM Rua r " +
            "WHERE r.origem = :origem AND r.destino = :destino")
    boolean existsConexao(@Param("origem") Bairro origem, @Param("destino") Bairro destino);
}