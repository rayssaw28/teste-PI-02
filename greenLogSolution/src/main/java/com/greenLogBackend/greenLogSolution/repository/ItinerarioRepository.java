package com.greenLogBackend.greenLogSolution.repository;

import com.greenLogBackend.greenLogSolution.entity.Itinerario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;

public interface ItinerarioRepository extends JpaRepository<Itinerario, Long> {
    boolean existsByCaminhaoIdAndData(Long caminhaoId, LocalDate data);
}