package com.greenLogBackend.greenLogSolution;

import com.greenLogBackend.greenLogSolution.entity.Usuario;
import com.greenLogBackend.greenLogSolution.enums.PerfilUsuario;
import com.greenLogBackend.greenLogSolution.repository.UsuarioRepository;
import com.greenLogBackend.greenLogSolution.service.CsvImportService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final CsvImportService csvImportService;

    public DataInitializer(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, CsvImportService csvImportService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.csvImportService = csvImportService;
    }

    @Override
    public void run(String... args) throws Exception {
        if (usuarioRepository.findByLogin("admin").isEmpty()) {
            Usuario admin = new Usuario();
            admin.setLogin("admin");
            admin.setSenha(passwordEncoder.encode("admin123"));
            admin.setPerfil(PerfilUsuario.ROLE_ADMIN);
            usuarioRepository.save(admin);
            System.out.println(">>> Usuário 'admin' criado <<<");
        }

        String pathBairros = "dados/nome_bairros.csv";
        String pathRuas = "dados/ruas_conexoes.csv";
        String pathPontos = "dados/pontos_coleta.csv";

        csvImportService.importarDados(pathBairros, pathRuas, pathPontos);
    }
}