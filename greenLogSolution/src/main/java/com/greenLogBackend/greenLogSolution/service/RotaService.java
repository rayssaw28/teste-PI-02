package com.greenLogBackend.greenLogSolution.service;

import com.greenLogBackend.greenLogSolution.dto.RotaRequest;
import com.greenLogBackend.greenLogSolution.dto.RotaResponse;
import com.greenLogBackend.greenLogSolution.entity.Bairro;
import com.greenLogBackend.greenLogSolution.entity.Caminhao;
import com.greenLogBackend.greenLogSolution.entity.Rota;
import com.greenLogBackend.greenLogSolution.entity.Rua;
import com.greenLogBackend.greenLogSolution.exception.BusinessException;
import com.greenLogBackend.greenLogSolution.exception.ResourceNotFoundException;
import com.greenLogBackend.greenLogSolution.mapper.RotaMapper;
import com.greenLogBackend.greenLogSolution.repository.BairroRepository;
import com.greenLogBackend.greenLogSolution.repository.CaminhaoRepository;
import com.greenLogBackend.greenLogSolution.repository.RotaRepository;
import com.greenLogBackend.greenLogSolution.repository.RuaRepository;
import jakarta.annotation.PostConstruct;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    private final CaminhaoRepository caminhaoRepository;

    private Graph<Long, DefaultWeightedEdge> grafoCache;

    public RotaService(RotaRepository rotaRepository,
                       BairroRepository bairroRepository,
                       RuaRepository ruaRepository,
                       CaminhaoRepository caminhaoRepository) {
        this.rotaRepository = rotaRepository;
        this.bairroRepository = bairroRepository;
        this.ruaRepository = ruaRepository;
        this.caminhaoRepository = caminhaoRepository;
    }


    @PostConstruct
    public void inicializarGrafo() {
        this.grafoCache = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);

        List<Bairro> bairros = bairroRepository.findAll();
        bairros.forEach(b -> grafoCache.addVertex(b.getId()));

        List<Rua> ruas = ruaRepository.findAll();
        for (Rua rua : ruas) {
            Long origemId = rua.getOrigem().getId();
            Long destinoId = rua.getDestino().getId();

            if (grafoCache.containsVertex(origemId) && grafoCache.containsVertex(destinoId)) {
                DefaultWeightedEdge edge = grafoCache.addEdge(origemId, destinoId);
                if (edge != null) {
                    grafoCache.setEdgeWeight(edge, rua.getDistanciaKm());
                }
            }
        }
    }

    public void atualizarGrafo() {
        this.inicializarGrafo();
    }

    @Transactional
    public RotaResponse calcularESalvarRota(RotaRequest request) {
        Caminhao caminhao = caminhaoRepository.findById(request.caminhaoId())
                .orElseThrow(() -> new ResourceNotFoundException("Caminhão não encontrado com id: " + request.caminhaoId()));

        if (!caminhao.getTiposResiduos().contains(request.tipoResiduo())) {
            throw new BusinessException("O caminhão selecionado não suporta o tipo de resíduo: " + request.tipoResiduo());
        }

        if (request.bairroIds() == null || request.bairroIds().size() < 2) {
            throw new BusinessException("A rota deve conter pelo menos 2 bairros (origem e destino).");
        }

        DijkstraShortestPath<Long, DefaultWeightedEdge> dijkstra = new DijkstraShortestPath<>(grafoCache);
        List<Long> sequenciaIds = request.bairroIds();

        double distanciaTotal = 0.0;
        List<Bairro> sequenciaBairrosCalculada = new ArrayList<>();

        sequenciaBairrosCalculada.add(buscarBairroPorId(sequenciaIds.get(0)));

        for (int i = 0; i < sequenciaIds.size() - 1; i++) {
            Long origem = sequenciaIds.get(i);
            Long destino = sequenciaIds.get(i + 1);

            GraphPath<Long, DefaultWeightedEdge> caminho = dijkstra.getPath(origem, destino);

            if (caminho == null) {
                throw new BusinessException("Não existe conexão viável entre o bairro ID " + origem + " e o bairro ID " + destino);
            }

            distanciaTotal += caminho.getWeight();

            List<Long> caminhoIds = caminho.getVertexList();
            for (int j = 1; j < caminhoIds.size(); j++) {
                sequenciaBairrosCalculada.add(buscarBairroPorId(caminhoIds.get(j)));
            }
        }

        Rota rota = RotaMapper.toEntity(request, caminhao, sequenciaBairrosCalculada, distanciaTotal);
        Rota rotaSalva = rotaRepository.save(rota);

        return RotaMapper.toResponse(rotaSalva);
    }

    @Transactional(readOnly = true)
    public List<RotaResponse> listarTodas() {
        return rotaRepository.findAll().stream()
                .map(RotaMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public RotaResponse buscarPorId(Long id) {
        Rota rota = rotaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rota não encontrada com id: " + id));
        return RotaMapper.toResponse(rota);
    }

    private Bairro buscarBairroPorId(Long id) {
        return bairroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bairro não encontrado no banco de dados: " + id));
    }
}