package com.greenLogBackend.greenLogSolution.service;

import com.greenLogBackend.greenLogSolution.entity.*;
import com.greenLogBackend.greenLogSolution.enums.TipoResiduo;
import com.greenLogBackend.greenLogSolution.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RotaService {

    private final RotaRepository rotaRepository;
    private final BairroRepository bairroRepository;
    private final RuaRepository ruaRepository;
    private final CaminhaoService caminhaoService;
    private final PontoColetaService pontoColetaService;

    public RotaService(RotaRepository rotaRepository, BairroRepository bairroRepository,
                       RuaRepository ruaRepository, CaminhaoService caminhaoService,
                       PontoColetaService pontoColetaService) {
        this.rotaRepository = rotaRepository;
        this.bairroRepository = bairroRepository;
        this.ruaRepository = ruaRepository;
        this.caminhaoService = caminhaoService;
        this.pontoColetaService = pontoColetaService;
    }

    public List<Rota> listarTodas() {
        return rotaRepository.findAll();
    }

    public Rota buscarPorId(Long id) {
        return rotaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Rota não encontrada."));
    }

    /**
     * Calcula a rota mais curta (Dijkstra) e salva no banco.
     */
    @Transactional
    public Rota calcularESalvarRota(Long caminhaoId, Long pontoOrigemId, Long pontoDestinoId, TipoResiduo tipoResiduo) {

        // 1. Busca e Validação das Entidades
        Caminhao caminhao = caminhaoService.buscarPorId(caminhaoId);
        PontoColeta origem = pontoColetaService.buscarPorId(pontoOrigemId);
        PontoColeta destino = pontoColetaService.buscarPorId(pontoDestinoId);

        // 2. Regra de Negócio: O caminhão aceita este tipo de resíduo?
        if (!caminhao.getTiposResiduos().contains(tipoResiduo)) {
            throw new IllegalArgumentException("O caminhão selecionado não está habilitado para transportar: " + tipoResiduo);
        }

        // 3. Montagem do Grafo (Bairros como Vértices, Ruas como Arestas com Peso)
        Graph<Bairro, DefaultWeightedEdge> grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);

        List<Bairro> bairros = bairroRepository.findAll();
        List<Rua> ruas = ruaRepository.findAll();

        // Adiciona todos os bairros como vértices
        bairros.forEach(grafo::addVertex);

        // Adiciona as ruas como arestas ponderadas (peso = distância)
        for (Rua rua : ruas) {
            // Só adiciona a aresta se ambos os bairros (origem e destino) estiverem no grafo
            if (grafo.containsVertex(rua.getOrigem()) && grafo.containsVertex(rua.getDestino())) {
                DefaultWeightedEdge edge = grafo.addEdge(rua.getOrigem(), rua.getDestino());
                if (edge != null) {
                    grafo.setEdgeWeight(edge, rua.getDistanciaKm());
                }
            }
        }

        // 4. Execução do Algoritmo de Dijkstra
        DijkstraShortestPath<Bairro, DefaultWeightedEdge> dijkstra = new DijkstraShortestPath<>(grafo);

        // Nota: O getBairro() vem da entidade PontoColeta
        GraphPath<Bairro, DefaultWeightedEdge> caminho = dijkstra.getPath(origem.getBairro(), destino.getBairro());

        if (caminho == null) {
            throw new IllegalStateException("Não foi encontrado um caminho conectado entre "
                    + origem.getBairro().getNome() + " e " + destino.getBairro().getNome());
        }

        // 5. Persistência da Rota Calculada
        Rota novaRota = new Rota();
        novaRota.setCaminhao(caminhao);
        novaRota.setTipoResiduo(tipoResiduo);
        novaRota.setDistanciaTotal(caminho.getWeight());
        novaRota.setSequenciaBairros(caminho.getVertexList()); // Salva a lista ordenada de bairros visitados

        return rotaRepository.save(novaRota);
    }
}