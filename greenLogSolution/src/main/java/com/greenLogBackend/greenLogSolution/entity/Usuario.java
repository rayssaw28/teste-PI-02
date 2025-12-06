package com.greenLogBackend.greenLogSolution.entity;

import com.greenLogBackend.greenLogSolution.enums.PerfilUsuario;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "usuarios", uniqueConstraints = {
        @UniqueConstraint(name = "uk_usuario_login", columnNames = "login")
})
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O login é obrigatório")
    @Column(nullable = false, length = 50)
    private String login;

    @NotBlank(message = "A senha é obrigatória")
    @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres")
    @Column(nullable = false)
    private String senha;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PerfilUsuario perfil;

    @PrePersist
    @PreUpdate
    private void normalize() {
        if (login != null) login = login.trim().toLowerCase();
    }

    public Usuario() {}

    public Usuario(String login, String senha, PerfilUsuario perfil) {
        this.login = login;
        this.senha = senha;
        this.perfil = perfil;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.perfil.name()));
    }

    @Override
    public String getPassword() { return this.senha; }
    @Override
    public String getUsername() { return this.login; }
    @Override
    public boolean isAccountNonExpired() { return true; }
    @Override
    public boolean isAccountNonLocked() { return true; }
    @Override
    public boolean isCredentialsNonExpired() { return true; }
    @Override
    public boolean isEnabled() { return true; }

    public void setId(Long id) { this.id = id; }
    public Long getId() { return id; }
    public void setLogin(String login) { this.login = login; }
    public void setSenha(String senha) { this.senha = senha; }
    public PerfilUsuario getPerfil() { return perfil; }
    public void setPerfil(PerfilUsuario perfil) { this.perfil = perfil; }
}