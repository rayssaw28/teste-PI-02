package com.greenLogBackend.greenLogSolution.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.List;

@Entity
@Table(name = "bairros", uniqueConstraints = {
        @UniqueConstraint(name = "uk_bairro_nome", columnNames = "nome")
})
public class Bairro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome do bairro é obrigatório")
    @Column(nullable = false, unique = true)
    private String nome;

    @OneToMany(mappedBy = "bairro", fetch = FetchType.LAZY)
    @JsonIgnoreProperties("bairro")
    private List<PontoColeta> pontosColeta;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public List<PontoColeta> getPontosColeta() { return pontosColeta; }
    public void setPontosColeta(List<PontoColeta> pontosColeta) { this.pontosColeta = pontosColeta; }
}