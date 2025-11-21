package com.systempos.api;

import com.systempos.model.Usuario;
import com.systempos.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class RestApiController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> credentials) {
        Map<String, Object> response = new HashMap<>();

        try {
            String usuario = credentials.get("usuario");
            String clave = credentials.get("clave");

            Optional<Usuario> usuarioOpt = usuarioRepository.findByUsuario(usuario);

            if (usuarioOpt.isPresent()) {
                Usuario user = usuarioOpt.get();

                if (passwordEncoder.matches(clave, user.getClave())) {
                    response.put("success", true);
                    response.put("mensaje", "Login exitoso");
                    response.put("usuario", user.getUsuario());
                    response.put("rol", user.getRol());
                    return ResponseEntity.ok(response);
                }
            }

            response.put("success", false);
            response.put("mensaje", "Usuario o contraseña incorrectos");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("mensaje", "Error en el servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}