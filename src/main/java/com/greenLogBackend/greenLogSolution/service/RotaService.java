package com.greenLogBackend.greenLogSolution.service;

import com.greenLogBackend.greenLogSolution.entity.*;
import com.greenLogBackend.greenLogSolution.enums.TipoResiduo;
import com.greenLogBackend.greenLogSolution.repository.*;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Padrão Singleton:
 * O  Spring garante que apenas uma instância desta classe Service
 * exista no contexto da aplicação, sendo reutilizada em todas as injeções de dependência.
 */

@Service
public class RotaService {

    private final RotaRepository rotaRepository;
    private final BairroRepository bairroRepository;
    private final RuaRepository ruaRepository;
    private final CaminhaoService caminhaoService;
    private final PontoColetaService pontoColetaService;

    private Graph<Bairro, DefaultWeightedEdge> grafoCache;

    public RotaService(RotaRepository rotaRepository, BairroRepository bairroRepository,
                       RuaRepository ruaRepository, CaminhaoService caminhaoService,
                       PontoColetaService pontoColetaService) {
        this.rotaRepository = rotaRepository;
        this.bairroRepository = bairroRepository;
        this.ruaRepository = ruaRepository;
        this.caminhaoService = caminhaoService;
        this.pontoColetaService = pontoColetaService;
    }

    @PostConstruct
    public void inicializarGrafo() {
        System.out.println(">>> Construindo grafo da cidade na memória...");
        this.grafoCache = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);

        List<Bairro> bairros = bairroRepository.findAll();
        List<Rua> ruas = ruaRepository.findAll();

        bairros.forEach(grafoCache::addVertex);

        for (Rua rua : ruas) {
            if (grafoCache.containsVertex(rua.getOrigem()) && grafoCache.containsVertex(rua.getDestino())) {
                DefaultWeightedEdge edge = grafoCache.addEdge(rua.getOrigem(), rua.getDestino());
                if (edge != null) {
                    grafoCache.setEdgeWeight(edge, rua.getDistanciaKm());
                }
            }
        }
        System.out.println(">>> Grafo construído com sucesso! Vértices: " + grafoCache.vertexSet().size());
    }

    public void atualizarGrafo() {
        this.inicializarGrafo();
    }

    public List<Rota> listarTodas() {
        return rotaRepository.findAll();
    }

    public Rota buscarPorId(Long id) {
        return rotaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Rota não encontrada."));
    }

    @Transactional
    public Rota calcularESalvarRota(Long caminhaoId, Long pontoOrigemId, Long pontoDestinoId, TipoResiduo tipoResiduo) {
        Caminhao caminhao = caminhaoService.buscarPorId(caminhaoId);
        PontoColeta destino = pontoColetaService.buscarPorId(pontoDestinoId);


        Bairro bairroOrigem;
        if (pontoOrigemId != null) {
            bairroOrigem = pontoColetaService.buscarPorId(pontoOrigemId).getBairro();
        } else {
            bairroOrigem = bairroRepository.findByNome("Centro")
                    .orElseThrow(() -> new EntityNotFoundException("Garagem (Centro) não encontrada!"));
        }

        Bairro bairroDestino = destino.getBairro();

        if (!caminhao.getTiposResiduos().contains(tipoResiduo)) {
            throw new IllegalArgumentException("Caminhão incompatível com o resíduo: " + tipoResiduo);
        }

        DijkstraShortestPath<Bairro, DefaultWeightedEdge> dijkstra = new DijkstraShortestPath<>(grafoCache);
        GraphPath<Bairro, DefaultWeightedEdge> caminho = dijkstra.getPath(bairroOrigem, bairroDestino);

        if (caminho == null) {
            throw new IllegalStateException("Não existe rota conectada entre " + bairroOrigem.getNome() + " e " + bairroDestino.getNome());
        }

        Rota novaRota = new Rota();
        novaRota.setCaminhao(caminhao);
        novaRota.setTipoResiduo(tipoResiduo);
        novaRota.setDistanciaTotal(caminho.getWeight());
        novaRota.setSequenciaBairros(caminho.getVertexList());

        return rotaRepository.save(novaRota);
    }
}