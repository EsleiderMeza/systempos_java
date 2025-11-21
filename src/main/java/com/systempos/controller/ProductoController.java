package com.systempos.controller;

import com.systempos.model.Producto;
import com.systempos.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    /**
     * Lista todos los productos
     */
    @GetMapping
    public String listarProductos(Model model) {
        List<Producto> productos = productoService.obtenerTodos();
        model.addAttribute("productos", productos);
        model.addAttribute("producto", new Producto()); // Para el formulario
        return "pages/productos";
    }

    /**
     * Guardar nuevo producto
     */
    @PostMapping("/guardar")
    public String guardarProducto(@ModelAttribute Producto producto,
                                  RedirectAttributes redirectAttributes) {
        try {
            productoService.guardar(producto);
            redirectAttributes.addFlashAttribute("mensaje", "Producto guardado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al guardar producto: " + e.getMessage());
        }
        return "redirect:/productos";
    }

    /**
     * Editar producto (mostrar formulario)
     */
    @GetMapping("/editar/{id}")
    public String editarProducto(@PathVariable Long id, Model model) {
        Producto producto = productoService.obtenerPorId(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        List<Producto> productos = productoService.obtenerTodos();

        model.addAttribute("producto", producto);
        model.addAttribute("productos", productos);
        model.addAttribute("editando", true);

        return "pages/productos";
    }

    /**
     * Actualizar producto
     */
    @PostMapping("/actualizar")
    public String actualizarProducto(@ModelAttribute Producto producto,
                                     RedirectAttributes redirectAttributes) {
        try {
            productoService.guardar(producto);
            redirectAttributes.addFlashAttribute("mensaje", "Producto actualizado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al actualizar producto: " + e.getMessage());
        }
        return "redirect:/productos";
    }

    /**
     * Eliminar producto
     */
    @GetMapping("/eliminar/{id}")
    public String eliminarProducto(@PathVariable Long id,
                                   RedirectAttributes redirectAttributes) {
        try {
            productoService.eliminar(id);
            redirectAttributes.addFlashAttribute("mensaje", "Producto eliminado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar producto: " + e.getMessage());
        }
        return "redirect:/productos";
    }

    /**
     * Buscar producto por código
     */
    @GetMapping("/buscar")
    public String buscarProducto(@RequestParam String codigo, Model model,
                                 RedirectAttributes redirectAttributes) {
        Producto producto = productoService.buscarPorCodigo(codigo)
                .orElse(null);

        if (producto != null) {
            model.addAttribute("producto", producto);
            model.addAttribute("productos", productoService.obtenerTodos());
            return "pages/productos";
        } else {
            redirectAttributes.addFlashAttribute("error", "Producto no encontrado");
            return "redirect:/productos";
        }
    }




}