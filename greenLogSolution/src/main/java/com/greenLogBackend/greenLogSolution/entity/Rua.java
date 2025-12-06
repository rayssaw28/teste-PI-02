package com.greenLogBackend.greenLogSolution.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name = "ruas")
public class Rua {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Bairro de origem é obrigatório")
    @ManyToOne
    @JoinColumn(name = "origem_id", nullable = false)
    private Bairro origem;

    @NotNull(message = "Bairro de destino é obrigatório")
    @ManyToOne
    @JoinColumn(name = "destino_id", nullable = false)
    private Bairro destino;

    @Positive(message = "A distância deve ser maior que zero")
    @Column(nullable = false)
    private Double distanciaKm;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Bairro getOrigem() { return origem; }
    public void setOrigem(Bairro origem) { this.origem = origem; }
    public Bairro getDestino() { return destino; }
    public void setDestino(Bairro destino) { this.destino = destino; }
    public Double getDistanciaKm() { return distanciaKm; }
    public void setDistanciaKm(Double distanciaKm) { this.distanciaKm = distanciaKm; }
}