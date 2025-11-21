package com.systempos.controller;

import com.systempos.model.Producto;
import com.systempos.service.InventarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/inventario")
public class InventarioController {

    @Autowired
    private InventarioService inventarioService;

    /**
     * Vista principal del inventario
     */
    @GetMapping
    public String mostrarInventario(Model model) {
        List<Producto> inventarioCompleto = inventarioService.obtenerInventarioCompleto();
        List<Producto> stockBajo = inventarioService.obtenerProductosStockBajo();
        List<Producto> sinStock = inventarioService.obtenerProductosSinStock();

        double valorTotal = inventarioService.calcularValorTotalInventario();
        double valorPotencial = inventarioService.calcularValorPotencialVentas();

        model.addAttribute("inventario", inventarioCompleto);
        model.addAttribute("stockBajo", stockBajo);
        model.addAttribute("sinStock", sinStock);
        model.addAttribute("valorTotal", valorTotal);
        model.addAttribute("valorPotencial", valorPotencial);
        model.addAttribute("totalProductos", inventarioCompleto.size());

        return "pages/inventario";
    }

    /**
     * Actualizar stock de un producto
     */
    @PostMapping("/actualizar-stock")
    public String actualizarStock(@RequestParam Long productoId,
                                  @RequestParam Integer nuevoStock,
                                  RedirectAttributes redirectAttributes) {
        try {
            inventarioService.actualizarStock(productoId, nuevoStock);
            redirectAttributes.addFlashAttribute("mensaje", "Stock actualizado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al actualizar stock: " + e.getMessage());
        }
        return "redirect:/inventario";
    }

    /**
     * Agregar stock a un producto
     */
    @PostMapping("/agregar-stock")
    public String agregarStock(@RequestParam Long productoId,
                               @RequestParam Integer cantidad,
                               RedirectAttributes redirectAttributes) {
        try {
            inventarioService.agregarStock(productoId, cantidad);
            redirectAttributes.addFlashAttribute("mensaje", "Stock agregado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al agregar stock: " + e.getMessage());
        }
        return "redirect:/inventario";
    }

    /**
     * Vista de productos con stock bajo
     */
    @GetMapping("/stock-bajo")
    public String verStockBajo(Model model) {
        List<Producto> stockBajo = inventarioService.obtenerProductosStockBajo();
        model.addAttribute("productos", stockBajo);
        model.addAttribute("titulo", "Productos con Stock Bajo");
        return "pages/inventario-alertas";
    }

    /**
     * Vista de productos sin stock
     */
    @GetMapping("/sin-stock")
    public String verSinStock(Model model) {
        List<Producto> sinStock = inventarioService.obtenerProductosSinStock();
        model.addAttribute("productos", sinStock);
        model.addAttribute("titulo", "Productos sin Stock");
        return "pages/inventario-alertas";
    }
}