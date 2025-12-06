package com.greenLogBackend.greenLogSolution.config;

import com.greenLogBackend.greenLogSolution.service.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final UserDetailsServiceImpl userDetailsService;

    public SecurityConfig(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    /**
     * Padrão Strategy:
     * Define o BCrypt como a estratégia concreta para o algoritmo de codificação de senhas,
     * permitindo que a implementação mude sem afetar quem consome o serviço de autenticação.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Padrão Builder:
     * O objeto HttpSecurity utiliza o padrão Builder para configurar a cadeia de filtros de segurança
     * de forma fluente (encadeamento de métodos como .csrf(), .cors(), .authorizeHttpRequests()).
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()));

        http
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // API Stateless
                .httpBasic(Customizer.withDefaults())
                .authorizeHttpRequests(auth -> auth

                        .requestMatchers("/h2-console/**", "/swagger-ui/**", "/v3/api-docs/**").permitAll()

                        .requestMatchers(HttpMethod.POST, "/api/caminhoes/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/caminhoes/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/pontos-coleta/**").hasAuthority("ROLE_ADMIN")

                        .requestMatchers("/api/rotas/calcular").hasAnyAuthority("ROLE_ADMIN", "ROLE_GESTOR")
                        .requestMatchers("/api/itinerarios/agendar").hasAnyAuthority("ROLE_ADMIN", "ROLE_GESTOR")

                        .requestMatchers(HttpMethod.GET, "/api/**").authenticated()

                        .anyRequest().authenticated()
                );

        return http.build();
    }

    /**
     * Configuração de CORS (Cross-Origin Resource Sharing):
     * Define as regras para permitir que o Frontend (Angular) acesse este Backend.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200"));

        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD", "TRACE", "CONNECT"));

        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "x-auth-token"));

        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}