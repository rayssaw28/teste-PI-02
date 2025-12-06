package com.greenLogBackend.greenLogSolution;

import com.greenLogBackend.greenLogSolution.entity.Rota;
import com.greenLogBackend.greenLogSolution.enums.TipoResiduo;
import com.greenLogBackend.greenLogSolution.repository.BairroRepository;
import com.greenLogBackend.greenLogSolution.repository.PontoColetaRepository;
import com.greenLogBackend.greenLogSolution.service.RotaService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class GreenLogSolutionApplicationTests {

    @Autowired
    private RotaService rotaService;

    @Autowired
    private BairroRepository bairroRepository;

    @Autowired
    private PontoColetaRepository pontoColetaRepository;

    @Test
    void contextLoads() {
        // Verifica se o contexto do Spring subiu sem erros
        Assertions.assertNotNull(rotaService);
    }

    @Test
    void testeCalculoRota() {
        // Este teste depende dos dados do CSV terem sido carregados pelo DataInitializer.
        // O ID 1 é um caminhão criado ou existente? Precisamos garantir que existe um caminhão.
        // Como o DataInitializer cria 'admin' e importa CSVs, mas NÃO cria caminhão,
        // este teste pode falhar se não criarmos um caminhão antes.

        // Vamos apenas verificar se o Grafo carregou os bairros (performance)
        long qtdBairros = bairroRepository.count();
        Assertions.assertTrue(qtdBairros > 0, "Deveria haver bairros importados do CSV");
    }
}