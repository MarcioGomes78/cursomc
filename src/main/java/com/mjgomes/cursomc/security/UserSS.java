package com.mjgomes.cursomc.security;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.mjgomes.cursomc.enums.Perfil;

// Adapta o usuário do domínio (Cliente) para o contrato UserDetails exigido pelo Spring Security,
// usado tanto no login (JWTAuthenticationFilter) quanto na autorização (JWTAuthorizationFilter).
public class UserSS implements UserDetails {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private String email;
    private String senha;
    private Collection<? extends GrantedAuthority> authorities;

    public UserSS(){}

    // Converte o conjunto de Perfil do usuário em GrantedAuthority, formato que o Spring Security entende.
    public UserSS(Integer id, String email, String senha, Set<Perfil> perfils) {
        this.id = id;
        this.email = email;
        this.senha = senha;
        this.authorities = perfils.stream().map(x -> new SimpleGrantedAuthority(x.getDescricao())).collect(Collectors.toList());
    }

    public Integer getId() {
        return id;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return senha;
    }

    // O email é usado como username para fins de autenticação.
    @Override
    public String getUsername() {
        return email;
    }

    // Flags de conta exigidas pela interface UserDetails. Como o sistema não implementa
    // expiração/bloqueio de conta nem de credenciais, todas retornam true (sempre habilitado).
    @Override
    public boolean isAccountNonExpired(){
        return true;
    }

    @Override
    public boolean isAccountNonLocked(){
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    // Verifica se o usuário autenticado possui o perfil informado, usado nas checagens de autorização dos services.
    public boolean hasRole(Perfil perfil) {
        return getAuthorities().contains(new SimpleGrantedAuthority(perfil.getDescricao()));
    }
}
