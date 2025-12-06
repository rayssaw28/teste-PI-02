package com.greenLogBackend.greenLogSolution.entity;

import com.greenLogBackend.greenLogSolution.enums.TipoResiduo;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "rotas")
public class Rota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "caminhao_id", nullable = false)
    private Caminhao caminhao;

    @Column(nullable = false)
    private Double distanciaTotal;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoResiduo tipoResiduo;

    @ManyToMany
    @JoinTable(
            name = "rota_bairros",
            joinColumns = @JoinColumn(name = "rota_id"),
            inverseJoinColumns = @JoinColumn(name = "bairro_id")
    )
    @OrderColumn(name = "ordem_visita")
    private List<Bairro> sequenciaBairros;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Caminhao getCaminhao() { return caminhao; }
    public void setCaminhao(Caminhao caminhao) { this.caminhao = caminhao; }
    public Double getDistanciaTotal() { return distanciaTotal; }
    public void setDistanciaTotal(Double distanciaTotal) { this.distanciaTotal = distanciaTotal; }
    public TipoResiduo getTipoResiduo() { return tipoResiduo; }
    public void setTipoResiduo(TipoResiduo tipoResiduo) { this.tipoResiduo = tipoResiduo; }
    public List<Bairro> getSequenciaBairros() { return sequenciaBairros; }
    public void setSequenciaBairros(List<Bairro> sequenciaBairros) { this.sequenciaBairros = sequenciaBairros; }
}