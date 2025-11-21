package com.systempos.service;

import com.systempos.model.Usuario;
import com.systempos.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByUsuario(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        // Verificar si el usuario está activo
        if (usuario.getEstado() == null || usuario.getEstado() == 0) {
            throw new UsernameNotFoundException("Usuario inactivo");
        }

        return User.builder()
                .username(usuario.getUsuario())
                .password(usuario.getClave())
                .authorities(Collections.singletonList(
                        new SimpleGrantedAuthority("ROLE_" + usuario.getRol().toUpperCase())
                ))
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(usuario.getEstado() == 0)
                .build();
    }
}