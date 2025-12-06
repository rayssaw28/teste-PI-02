package com.greenLogBackend.greenLogSolution.service;

import com.greenLogBackend.greenLogSolution.entity.Bairro;
import com.greenLogBackend.greenLogSolution.entity.PontoColeta;
import com.greenLogBackend.greenLogSolution.entity.Rua;
import com.greenLogBackend.greenLogSolution.enums.TipoResiduo;
import com.greenLogBackend.greenLogSolution.repository.BairroRepository;
import com.greenLogBackend.greenLogSolution.repository.PontoColetaRepository;
import com.greenLogBackend.greenLogSolution.repository.RuaRepository;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Padrão Facade (Fachada):
 * Esta classe atua como uma fachada para o subsistema de importação de dados,
 * encapsulando a complexidade de leitura de arquivos, parsing de CSV e persistência
 * de múltiplas entidades (Bairro, Rua, PontoColeta) em um único método simplificado.
 */


@Service
public class CsvImportService {

    private final BairroRepository bairroRepository;
    private final RuaRepository ruaRepository;
    private final PontoColetaRepository pontoColetaRepository;

    private final RotaService rotaService;

    private final Map<Long, Bairro> bairroCache = new HashMap<>();

    public CsvImportService(BairroRepository bairroRepository,
                            RuaRepository ruaRepository,
                            PontoColetaRepository pontoColetaRepository,
                            RotaService rotaService) {
        this.bairroRepository = bairroRepository;
        this.ruaRepository = ruaRepository;
        this.pontoColetaRepository = pontoColetaRepository;
        this.rotaService = rotaService;
    }

    @Transactional
    public void importarDados(String pathBairros, String pathRuas, String pathPontos) {
        try {
            System.out.println(">>> Iniciando importação de dados...");

            importarBairros(pathBairros);

            importarRuas(pathRuas);

            importarPontosColeta(pathPontos);

            System.out.println("Dados persistidos no Banco de Dados");

            rotaService.atualizarGrafo();
            System.out.println(" Grafo de rotas atualizado com sucesso");
            System.out.println("Importação concluída");

        } catch (Exception e) {
            System.err.println("ERRO NA IMPORTAÇÃO CSV: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void importarBairros(String fileName) throws IOException {
        ClassPathResource resource = new ClassPathResource(fileName);
        if (!resource.exists()) {
            System.out.println("Arquivo não encontrado: " + fileName);
            return;
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
            String linha;
            boolean primeiraLinha = true;
            while ((linha = br.readLine()) != null) {
                if (primeiraLinha) { primeiraLinha = false; continue; }

                String[] dados = linha.split(",");
                if (dados.length < 2) continue;

                try {
                    Long csvId = Long.parseLong(dados[0].trim());
                    String nome = dados[1].trim();

                    Bairro bairro = bairroRepository.findByNome(nome).orElseGet(() -> {
                        Bairro novo = new Bairro();
                        novo.setNome(nome);
                        return bairroRepository.save(novo);
                    });

                    bairroCache.put(csvId, bairro);

                } catch (NumberFormatException e) {
                    System.err.println("Erro ao processar linha de bairro: " + linha);
                }
            }
        }
        System.out.println("Bairros processados. Cache size: " + bairroCache.size());
    }

    private void importarRuas(String fileName) throws IOException {
        ClassPathResource resource = new ClassPathResource(fileName);
        if (!resource.exists()) return;

        try (BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
            String linha;
            boolean primeiraLinha = true;
            while ((linha = br.readLine()) != null) {
                if (primeiraLinha) { primeiraLinha = false; continue; }

                String[] dados = linha.split(",");
                if (dados.length < 4) continue;

                try {
                    Long origemId = Long.parseLong(dados[1].trim());
                    Long destinoId = Long.parseLong(dados[2].trim());
                    Double distancia = Double.parseDouble(dados[3].trim());

                    Bairro origem = bairroCache.get(origemId);
                    Bairro destino = bairroCache.get(destinoId);

                    if (origem != null && destino != null) {
                        if (!ruaRepository.existsConexao(origem, destino)) {
                            Rua rua = new Rua();
                            rua.setOrigem(origem);
                            rua.setDestino(destino);
                            rua.setDistanciaKm(distancia);
                            ruaRepository.save(rua);
                        }
                    } else {
                        System.err.println("Rua ignorada (Bairro desconhecido). OrigemID: " + origemId + ", DestinoID: " + destinoId);
                    }
                } catch (Exception e) {
                    System.err.println("Erro ao processar rua: " + linha);
                }
            }
        }
        System.out.println("Ruas importadas.");
    }

    private void importarPontosColeta(String fileName) throws IOException {
        ClassPathResource resource = new ClassPathResource(fileName);
        if (!resource.exists()) return;

        try (BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
            String linha;
            boolean primeiraLinha = true;
            while ((linha = br.readLine()) != null) {
                if (primeiraLinha) { primeiraLinha = false; continue; }

                String[] dados = linha.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

                if (dados.length < 9) continue;

                try {
                    Long bairroIdCsv = Long.parseLong(dados[1].trim());
                    String nome = dados[2].replace("\"", "").trim();
                    String responsavel = dados[3].replace("\"", "").trim();
                    String contato = dados[4].replace("\"", "").trim();
                    String endereco = dados[6].replace("\"", "").trim();
                    String tiposRaw = dados[8].replace("\"", "").trim();

                    if (!pontoColetaRepository.existsByNome(nome)) {
                        PontoColeta ponto = new PontoColeta();
                        ponto.setNome(nome);
                        ponto.setResponsavel(responsavel);
                        ponto.setContato(contato);
                        ponto.setEndereco(endereco);

                        ponto.setCpfResponsavel("00000000000");

                        Bairro bairro = bairroCache.get(bairroIdCsv);
                        if (bairro == null) {
                            bairro = bairroRepository.findById(bairroIdCsv).orElse(null);
                        }

                        if (bairro == null) {
                            System.err.println("Bairro ID " + bairroIdCsv + " não encontrado para o ponto " + nome);
                            continue;
                        }
                        ponto.setBairro(bairro);

                        Set<TipoResiduo> residuos = new HashSet<>();
                        String[] tipos = tiposRaw.split("[,;]");

                        for (String t : tipos) {
                            try {
                                String tipoLimpo = limparTexto(t);
                                if (!tipoLimpo.isEmpty()) {
                                    residuos.add(TipoResiduo.valueOf(tipoLimpo));
                                }
                            } catch (IllegalArgumentException e) {
                                System.err.println("Tipo de resíduo desconhecido: " + t);
                            }
                        }

                        if(residuos.isEmpty()) residuos.add(TipoResiduo.ORGANICO); // Fallback

                        ponto.setTiposResiduos(residuos);
                        pontoColetaRepository.save(ponto);
                    }
                } catch (Exception e) {
                    System.err.println("Erro ao importar ponto de coleta: " + linha + " -> " + e.getMessage());
                }
            }
        }
        System.out.println("Pontos de coleta importados.");
    }

    private String limparTexto(String texto) {
        if (texto == null) return "";
        String normalizado = Normalizer.normalize(texto, Normalizer.Form.NFD);
        String semAcento = normalizado.replaceAll("\\p{M}", "");
        return semAcento.trim().toUpperCase();
    }
}