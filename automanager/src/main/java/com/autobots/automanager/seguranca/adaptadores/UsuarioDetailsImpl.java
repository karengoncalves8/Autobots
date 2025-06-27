package com.autobots.automanager.seguranca.adaptadores;

import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.enums.PerfilUsuario;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.autobots.automanager.entidades.Credencial;
import com.autobots.automanager.entidades.CredencialCodigoBarra;
import com.autobots.automanager.entidades.CredencialSenha;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@AllArgsConstructor
@Data
@NoArgsConstructor
@SuppressWarnings("serial")
public class UsuarioDetailsImpl implements UserDetails {
    private Usuario usuario;
    private Credencial credencial;


    @Override
    public String getUsername() {
        if (credencial instanceof CredencialSenha) {
            return ((CredencialSenha) credencial).getNomeUsuario();
        } else if (credencial instanceof CredencialCodigoBarra) {
            return String.valueOf(((CredencialCodigoBarra) credencial).getCodigo());
        } else {
            return null;
        }
    }

    @Override
    public String getPassword() {
        if (credencial instanceof CredencialSenha) {
            return ((CredencialSenha) credencial).getSenha();
        } else {
            return "";
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for(PerfilUsuario perfil : this.usuario.getPerfis()){
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + perfil.toString());
				authorities.add(authority);
        }
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return !credencial.isInativo(); }

    public Usuario getUsuario() {
        return usuario;
    }
}
