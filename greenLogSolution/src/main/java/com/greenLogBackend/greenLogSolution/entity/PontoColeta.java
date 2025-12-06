package com.greenLogBackend.greenLogSolution.entity;

import com.greenLogBackend.greenLogSolution.enums.TipoResiduo;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import java.util.Set;

@Entity
@Table(name = "pontos_coleta", uniqueConstraints = {
        @UniqueConstraint(name = "uk_ponto_nome", columnNames = "nome")
})
public class PontoColeta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome do ponto é obrigatório")
    @Column(nullable = false)
    private String nome;

    @NotBlank(message = "O endereço é obrigatório")
    @Column(nullable = false)
    private String endereco;

    @NotBlank(message = "O nome do responsável é obrigatório")
    @Column(nullable = false)
    private String responsavel;

    // REGEX EXIGIDA (Linguagens Formais) - Validação de CPF simples (apenas números)
    @NotBlank(message = "O CPF do responsável é obrigatório")
    @Pattern(regexp = "^\\d{11}$", message = "O CPF deve conter apenas 11 dígitos numéricos")
    @Column(nullable = false, length = 11)
    private String cpfResponsavel;

    @NotBlank(message = "O contato é obrigatório")
    @Column(nullable = false)
    private String contato;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "ponto_residuos", joinColumns = @JoinColumn(name = "ponto_id"))
    @Enumerated(EnumType.STRING)
    @NotEmpty(message = "O ponto deve aceitar pelo menos um tipo de resíduo")
    private Set<TipoResiduo> tiposResiduos;

    @ManyToOne(optional = false)
    @JoinColumn(name = "bairro_id", nullable = false)
    private Bairro bairro;

    @PrePersist
    @PreUpdate
    private void normalize() {
        if (nome != null) nome = nome.trim();
        if (cpfResponsavel != null) cpfResponsavel = cpfResponsavel.replaceAll("\\D", ""); // Remove formatação
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }
    public String getResponsavel() { return responsavel; }
    public void setResponsavel(String responsavel) { this.responsavel = responsavel; }
    public String getCpfResponsavel() { return cpfResponsavel; }
    public void setCpfResponsavel(String cpfResponsavel) { this.cpfResponsavel = cpfResponsavel; }
    public String getContato() { return contato; }
    public void setContato(String contato) { this.contato = contato; }
    public Set<TipoResiduo> getTiposResiduos() { return tiposResiduos; }
    public void setTiposResiduos(Set<TipoResiduo> tiposResiduos) { this.tiposResiduos = tiposResiduos; }
    public Bairro getBairro() { return bairro; }
    public void setBairro(Bairro bairro) { this.bairro = bairro; }
}
