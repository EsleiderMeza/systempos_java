package com.systempos.controller;

import com.systempos.model.*;
import com.systempos.service.VentaService;
import com.systempos.service.ProductoService;
import com.systempos.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/ventas")
public class VentaController {

    @Autowired
    private VentaService ventaService;

    @Autowired
    private ProductoRepository productoRepository;

    /**
     * Muestra la página principal de ventas
     */
    @GetMapping
    public String mostrarVentas(Model model) {
        // Lista de ventas realizadas
        List<Venta> ventas = ventaService.obtenerTodasLasVentas();
        model.addAttribute("ventas", ventas);

        // Lista de productos disponibles para vender
        List<Producto> productos = productoRepository.findAll();
        model.addAttribute("productos", productos);

        return "pages/ventas";
    }

    /**
     * Registra una nueva venta (procesa el formulario)
     */
    @PostMapping("/registrar")
    public String registrarVenta(
            @RequestParam(required = false) Long clienteId,
            @RequestParam List<Long> productosIds,
            @RequestParam List<Integer> cantidades,
            @RequestParam String metodoPago,
            @RequestParam String tipoComprobante,
            RedirectAttributes redirectAttributes) {

        try {
            // Validar que haya productos
            if (productosIds == null || productosIds.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Debe agregar al menos un producto");
                return "redirect:/ventas";
            }

            // Crear la venta
            Venta venta = new Venta();
            venta.setPaymentMethod(metodoPago);
            venta.setComprobanteType(tipoComprobante);

            // Crear los detalles
            List<DetalleVenta> detalles = new ArrayList<>();
            BigDecimal subtotalGeneral = BigDecimal.ZERO;

            for (int i = 0; i < productosIds.size(); i++) {
                Long productoId = productosIds.get(i);
                Integer cantidad = cantidades.get(i);

                // Buscar el producto
                Producto producto = productoRepository.findById(productoId)
                        .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

                // Crear detalle
                DetalleVenta detalle = new DetalleVenta();
                detalle.setProducto(producto);
                detalle.setCantidad(cantidad);
                detalle.setPrecio(producto.getPrecioVenta());

                // Calcular subtotal del detalle
                BigDecimal subtotal = ventaService.calcularSubtotal(producto.getPrecioVenta(), cantidad);
                detalle.setSubtotal(subtotal);

                detalles.add(detalle);
                subtotalGeneral = subtotalGeneral.add(subtotal);
            }

            // Calcular impuesto y total
            BigDecimal impuesto = ventaService.calcularImpuesto(subtotalGeneral);
            BigDecimal total = ventaService.calcularTotal(subtotalGeneral, impuesto);

            venta.setTax(impuesto);
            venta.setTotal(total);

            // Guardar la venta
            ventaService.registrarVenta(venta, detalles);

            redirectAttributes.addFlashAttribute("mensaje", "Venta registrada exitosamente");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al registrar venta: " + e.getMessage());
        }

        return "redirect:/ventas";
    }

    /**
     * Ver detalle de una venta
     */
    @GetMapping("/{id}")
    public String verDetalleVenta(@PathVariable Long id, Model model) {
        Venta venta = ventaService.obtenerVentaPorId(id);
        model.addAttribute("venta", venta);
        return "pages/venta-detalle";
    }
}