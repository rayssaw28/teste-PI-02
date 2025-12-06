package com.greenLogBackend.greenLogSolution;

import com.greenLogBackend.greenLogSolution.repository.BairroRepository;
import com.greenLogBackend.greenLogSolution.service.CsvImportService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class DataInitializer implements CommandLineRunner {

    private final CsvImportService csvImportService;
    private final BairroRepository bairroRepository;

    public DataInitializer(CsvImportService csvImportService, BairroRepository bairroRepository) {
        this.csvImportService = csvImportService;
        this.bairroRepository = bairroRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (bairroRepository.count() == 0) {
            System.out.println(">>> Banco de dados vazio. Iniciando carga inicial de CSVs...");

            String bairros = "dados/nome_bairros.csv";
            String ruas = "dados/ruas_conexoes.csv";
            String pontos = "dados/pontos_coleta.csv";

            csvImportService.importarDados(bairros, ruas, pontos);
        } else {
            System.out.println(">>> Dados já carregados. Pulando importação CSV.");
        }
    }
}