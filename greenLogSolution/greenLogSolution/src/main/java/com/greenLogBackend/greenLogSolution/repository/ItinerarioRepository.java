package com.greenLogBackend.greenLogSolution.repository;

import com.greenLogBackend.greenLogSolution.entity.Itinerario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;

public interface ItinerarioRepository extends JpaRepository<Itinerario, Long> {

    @Query("SELECT CASE WHEN COUNT(i) > 0 THEN true ELSE false END FROM Itinerario i " +
            "WHERE i.caminhao.id = :caminhaoId AND i.data = :data")
    boolean existsByCaminhaoAndData(@Param("caminhaoId") Long caminhaoId, @Param("data") LocalDate data);
}