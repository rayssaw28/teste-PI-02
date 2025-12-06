package com.greenLogBackend.greenLogSolution.entity;

import com.greenLogBackend.greenLogSolution.enums.TipoResiduo;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.Set;

@Entity
@Table(name = "caminhoes", uniqueConstraints = {
        @UniqueConstraint(name = "uk_caminhao_placa", columnNames = "placa")
})
public class Caminhao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "A placa é obrigatória")
    @Pattern(regexp = "^[A-Z]{3}[0-9][A-Z0-9][0-9]{2}$", message = "A placa deve seguir o padrão (Ex: ABC1234 ou ABC1D23)")
    @Column(nullable = false, length = 8)
    private String placa;

    @NotBlank(message = "O modelo é obrigatório")
    @Column(nullable = false)
    private String modelo; // ADICIONADO: Importante para identificar o veículo

    @NotBlank(message = "O motorista é obrigatório")
    @Column(nullable = false)
    private String motorista;

    @Positive(message = "A capacidade deve ser maior que zero")
    @Column(nullable = false)
    private Double capacidadeCarga;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "caminhao_residuos", joinColumns = @JoinColumn(name = "caminhao_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_residuo")
    @NotEmpty(message = "O caminhão deve aceitar pelo menos um tipo de resíduo")
    private Set<TipoResiduo> tiposResiduos;

    @PrePersist
    @PreUpdate
    private void normalize() {
        if (placa != null) placa = placa.trim().toUpperCase().replace("-", "");
        if (modelo != null) modelo = modelo.trim();
        if (motorista != null) motorista = motorista.trim();
    }

    // Getters e Setters OBRIGATÓRIOS para o Mapper funcionar
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getPlaca() { return placa; }
    public void setPlaca(String placa) { this.placa = placa; }

    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }

    public String getMotorista() { return motorista; }
    public void setMotorista(String motorista) { this.motorista = motorista; }

    public Double getCapacidadeCarga() { return capacidadeCarga; }
    public void setCapacidadeCarga(Double capacidadeCarga) { this.capacidadeCarga = capacidadeCarga; }

    public Set<TipoResiduo> getTiposResiduos() { return tiposResiduos; }
    public void setTiposResiduos(Set<TipoResiduo> tiposResiduos) { this.tiposResiduos = tiposResiduos; }
}