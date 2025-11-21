package com.systempos.controller;

import com.systempos.model.Proveedor;
import com.systempos.service.ProveedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/proveedores")
public class ProveedorController {

    @Autowired
    private ProveedorService proveedorService;

    /**
     * Lista todos los proveedores
     */
    @GetMapping
    public String listarProveedores(Model model) {
        List<Proveedor> proveedores = proveedorService.obtenerTodos();
        model.addAttribute("proveedores", proveedores);
        model.addAttribute("proveedor", new Proveedor());
        return "pages/proveedores";
    }

    /**
     * Guardar nuevo proveedor
     */
    @PostMapping("/guardar")
    public String guardarProveedor(@ModelAttribute Proveedor proveedor,
                                   RedirectAttributes redirectAttributes) {
        try {
            proveedorService.guardar(proveedor);
            redirectAttributes.addFlashAttribute("mensaje", "Proveedor guardado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al guardar proveedor: " + e.getMessage());
        }
        return "redirect:/proveedores";
    }

    /**
     * Editar proveedor
     */
    @GetMapping("/editar/{id}")
    public String editarProveedor(@PathVariable Long id, Model model) {
        Proveedor proveedor = proveedorService.obtenerPorId(id)
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));

        List<Proveedor> proveedores = proveedorService.obtenerTodos();

        model.addAttribute("proveedor", proveedor);
        model.addAttribute("proveedores", proveedores);
        model.addAttribute("editando", true);

        return "pages/proveedores";
    }

    /**
     * Actualizar proveedor
     */
    @PostMapping("/actualizar")
    public String actualizarProveedor(@ModelAttribute Proveedor proveedor,
                                      RedirectAttributes redirectAttributes) {
        try {
            proveedorService.guardar(proveedor);
            redirectAttributes.addFlashAttribute("mensaje", "Proveedor actualizado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al actualizar proveedor: " + e.getMessage());
        }
        return "redirect:/proveedores";
    }

    /**
     * Eliminar proveedor
     */
    @GetMapping("/eliminar/{id}")
    public String eliminarProveedor(@PathVariable Long id,
                                    RedirectAttributes redirectAttributes) {
        try {
            proveedorService.eliminar(id);
            redirectAttributes.addFlashAttribute("mensaje", "Proveedor eliminado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar proveedor: " + e.getMessage());
        }
        return "redirect:/proveedores";
    }
}