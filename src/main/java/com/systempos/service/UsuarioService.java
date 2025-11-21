package com.systempos.service;

import com.systempos.model.Usuario;
import com.systempos.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Usuario> obtenerTodos() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> obtenerPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    public Usuario guardar(Usuario usuario) {
        // Si es un usuario nuevo o se cambió la contraseña, encriptarla
        if (usuario.getId() == null || !usuario.getClave().startsWith("$2a$")) {
            usuario.setClave(passwordEncoder.encode(usuario.getClave()));
        }
        return usuarioRepository.save(usuario);
    }

    public void eliminar(Long id) {
        usuarioRepository.deleteById(id);
    }

    public Usuario cambiarEstado(Long id, Integer nuevoEstado) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        usuario.setEstado(nuevoEstado);
        return usuarioRepository.save(usuario);
    }

    public Usuario cambiarRol(Long id, String nuevoRol) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        usuario.setRol(nuevoRol);
        return usuarioRepository.save(usuario);
    }

    public boolean existeUsuario(String username) {
        return usuarioRepository.findByUsuario(username).isPresent();
    }

    public long contarUsuariosActivos() {
        return usuarioRepository.findAll().stream()
                .filter(u -> u.getEstado() == 1)
                .count();
    }

    public long contarUsuariosPorRol(String rol) {
        return usuarioRepository.findAll().stream()
                .filter(u -> u.getRol().equalsIgnoreCase(rol))
                .count();
    }
}