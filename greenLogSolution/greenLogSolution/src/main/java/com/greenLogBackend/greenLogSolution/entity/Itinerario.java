package com.greenLogBackend.greenLogSolution.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "itinerarios", uniqueConstraints = {
        @UniqueConstraint(name = "uk_itinerario_caminhao_data", columnNames = {"caminhao_id", "data_agendada"})
})
public class Itinerario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "A data é obrigatória")
    @Column(name = "data_agendada", nullable = false)
    private LocalDate data;

    @ManyToOne(optional = false)
    @JoinColumn(name = "rota_id", nullable = false)
    private Rota rota;

    @ManyToOne(optional = false)
    @JoinColumn(name = "caminhao_id", nullable = false)
    private Caminhao caminhao;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }
    public Rota getRota() { return rota; }
    public void setRota(Rota rota) { this.rota = rota; }
    public Caminhao getCaminhao() { return caminhao; }
    public void setCaminhao(Caminhao caminhao) { this.caminhao = caminhao; }
}