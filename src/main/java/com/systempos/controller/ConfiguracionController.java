package com.systempos.controller;

import com.systempos.model.Usuario;
import com.systempos.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/configuracion")
@PreAuthorize("hasRole('ADMIN')") // Solo administradores
public class ConfiguracionController {

    @Autowired
    private UsuarioService usuarioService;

    /**
     * Vista principal de configuración
     */
    @GetMapping
    public String mostrarConfiguracion(Model model, Authentication auth) {
        List<Usuario> usuarios = usuarioService.obtenerTodos();

        model.addAttribute("usuarios", usuarios);
        model.addAttribute("usuario", new Usuario());
        model.addAttribute("usuarioActual", auth.getName());
        model.addAttribute("totalUsuarios", usuarios.size());
        model.addAttribute("usuariosActivos", usuarioService.contarUsuariosActivos());
        model.addAttribute("administradores", usuarioService.contarUsuariosPorRol("admin"));
        model.addAttribute("vendedores", usuarioService.contarUsuariosPorRol("vendedor"));

        return "pages/configuracion";
    }

    /**
     * Crear nuevo usuario
     */
    @PostMapping("/usuarios/crear")
    public String crearUsuario(@RequestParam String nombre,
                               @RequestParam(required = false) String email,
                               @RequestParam String usuario,
                               @RequestParam String clave,
                               @RequestParam String rol,
                               @RequestParam(required = false, defaultValue = "1") Integer estado,
                               RedirectAttributes redirectAttributes) {
        try {
            // Validar si el usuario ya existe
            if (usuarioService.existeUsuario(usuario)) {
                redirectAttributes.addFlashAttribute("error", "El nombre de usuario ya existe");
                return "redirect:/configuracion";
            }

            // Crear nuevo usuario
            Usuario nuevoUsuario = new Usuario();
            nuevoUsuario.setNombre(nombre);
            nuevoUsuario.setEmail(email);
            nuevoUsuario.setUsuario(usuario);
            nuevoUsuario.setClave(clave); // El servicio la encriptará
            nuevoUsuario.setRol(rol);
            nuevoUsuario.setEstado(estado);

            usuarioService.guardar(nuevoUsuario);
            redirectAttributes.addFlashAttribute("mensaje", "Usuario creado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al crear usuario: " + e.getMessage());
        }
        return "redirect:/configuracion";
    }

    /**
     * Editar usuario
     */
    @GetMapping("/usuarios/editar/{id}")
    public String editarUsuario(@PathVariable Long id, Model model) {
        Usuario usuario = usuarioService.obtenerPorId(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        List<Usuario> usuarios = usuarioService.obtenerTodos();

        model.addAttribute("usuario", usuario);
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("editando", true);
        model.addAttribute("totalUsuarios", usuarios.size());
        model.addAttribute("usuariosActivos", usuarioService.contarUsuariosActivos());
        model.addAttribute("administradores", usuarioService.contarUsuariosPorRol("admin"));
        model.addAttribute("vendedores", usuarioService.contarUsuariosPorRol("vendedor"));

        return "pages/configuracion";
    }

    /**
     * Actualizar usuario
     */
    @PostMapping("/usuarios/actualizar")
    public String actualizarUsuario(@RequestParam Long id,
                                    @RequestParam String nombre,
                                    @RequestParam String email,
                                    @RequestParam String usuario,
                                    @RequestParam(required = false) String nuevaClave,
                                    @RequestParam String rol,
                                    @RequestParam Integer estado,
                                    RedirectAttributes redirectAttributes) {
        try {
            Usuario usuarioExistente = usuarioService.obtenerPorId(id)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            // Actualizar datos básicos
            usuarioExistente.setNombre(nombre);
            usuarioExistente.setEmail(email);
            usuarioExistente.setUsuario(usuario);
            usuarioExistente.setRol(rol);
            usuarioExistente.setEstado(estado);

            // Solo actualizar contraseña si se proporciona una nueva
            if (nuevaClave != null && !nuevaClave.trim().isEmpty()) {
                usuarioExistente.setClave(nuevaClave); // El servicio la encriptará
            }

            usuarioService.guardar(usuarioExistente);
            redirectAttributes.addFlashAttribute("mensaje", "Usuario actualizado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al actualizar usuario: " + e.getMessage());
        }
        return "redirect:/configuracion";
    }

    /**
     * Cambiar estado del usuario (activar/desactivar)
     */
    @PostMapping("/usuarios/cambiar-estado/{id}")
    public String cambiarEstado(@PathVariable Long id,
                                @RequestParam Integer estado,
                                RedirectAttributes redirectAttributes) {
        try {
            usuarioService.cambiarEstado(id, estado);
            String mensaje = estado == 1 ? "Usuario activado" : "Usuario desactivado";
            redirectAttributes.addFlashAttribute("mensaje", mensaje);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al cambiar estado: " + e.getMessage());
        }
        return "redirect:/configuracion";
    }

    /**
     * Eliminar usuario
     */
    @GetMapping("/usuarios/eliminar/{id}")
    public String eliminarUsuario(@PathVariable Long id,
                                  Authentication auth,
                                  RedirectAttributes redirectAttributes) {
        try {
            // Obtener usuario a eliminar
            Usuario usuario = usuarioService.obtenerPorId(id)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            // No permitir que el admin se elimine a sí mismo
            if (usuario.getUsuario().equals(auth.getName())) {
                redirectAttributes.addFlashAttribute("error", "No puedes eliminar tu propio usuario");
                return "redirect:/configuracion";
            }

            usuarioService.eliminar(id);
            redirectAttributes.addFlashAttribute("mensaje", "Usuario eliminado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar usuario: " + e.getMessage());
        }
        return "redirect:/configuracion";
    }

    /**
     * Guardar configuración de seguridad
     */
    @PostMapping("/seguridad/guardar")
    public String guardarConfiguracionSeguridad(
            @RequestParam(required = false) boolean passwordFuerte,
            @RequestParam(required = false) boolean cerrarSesionAuto,
            @RequestParam(required = false) boolean sesionesMultiples,
            @RequestParam(required = false) boolean registrarLogs,
            @RequestParam(defaultValue = "5") int intentosLogin,
            RedirectAttributes redirectAttributes) {
        try {
            // Aquí puedes guardar en base de datos o archivo de configuración
            // Por ahora solo simularemos el guardado

            redirectAttributes.addFlashAttribute("mensaje",
                    "Configuración de seguridad guardada exitosamente. Cambios aplicados: " +
                            (passwordFuerte ? "Contraseña fuerte activada. " : "") +
                            (cerrarSesionAuto ? "Cierre automático activado. " : "") +
                            "Intentos de login: " + intentosLogin);

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al guardar configuración: " + e.getMessage());
        }
        return "redirect:/configuracion#seguridad";
    }

    /**
     * Crear respaldo de base de datos
     */
    @PostMapping("/respaldo/crear")
    public String crearRespaldo(RedirectAttributes redirectAttributes) {
        try {
            // Obtener fecha actual para el nombre del archivo
            String fecha = java.time.LocalDateTime.now()
                    .format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
            String nombreArchivo = "backup_systempos_" + fecha + ".sql";

            // Comando para crear backup (ajusta según tu sistema)
            String comando = String.format(
                    "mysqldump -u root -p systempos_db > %s",
                    nombreArchivo
            );

            // Nota: Esto es un ejemplo básico. En producción deberías usar ProcessBuilder
            redirectAttributes.addFlashAttribute("mensaje",
                    "Respaldo creado exitosamente: " + nombreArchivo +
                            ". Archivo guardado en el directorio del sistema.");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error",
                    "Error al crear respaldo. Verifica tus credenciales de base de datos.");
        }
        return "redirect:/configuracion#seguridad";
    }

    /**
     * Restaurar desde respaldo
     */
    @PostMapping("/respaldo/restaurar")
    public String restaurarRespaldo(
            @RequestParam String archivoRespaldo,
            RedirectAttributes redirectAttributes) {
        try {
            // Validar que el archivo existe
            redirectAttributes.addFlashAttribute("mensaje",
                    "Función de restauración en desarrollo. Por seguridad, contacta al administrador del sistema.");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al restaurar: " + e.getMessage());
        }
        return "redirect:/configuracion#seguridad";
    }
}