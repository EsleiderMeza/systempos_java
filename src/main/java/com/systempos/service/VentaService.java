package com.systempos.service;

import com.systempos.model.*;
import com.systempos.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class VentaService {

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private DetalleVentaRepository detalleVentaRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Registra una nueva venta
     * @Transactional asegura que TODO se guarde o TODO falle (atomicidad)
     */
    @Transactional
    public Venta registrarVenta(Venta venta, List<DetalleVenta> detalles) {

        // 1. Guardar la venta principal
        Venta ventaGuardada = ventaRepository.save(venta);

        // 2. Guardar cada detalle y actualizar stock
        for (DetalleVenta detalle : detalles) {
            // Asociar detalle con la venta
            detalle.setVenta(ventaGuardada);

            // Guardar detalle
            detalleVentaRepository.save(detalle);

            // Actualizar stock del producto
            Producto producto = detalle.getProducto();
            int nuevoStock = producto.getStock() - detalle.getCantidad();

            if (nuevoStock < 0) {
                throw new RuntimeException("Stock insuficiente para: " + producto.getNombre());
            }

            producto.setStock(nuevoStock);
            productoRepository.save(producto);
        }

        return ventaGuardada;
    }

    /**
     * Calcula el subtotal de un detalle
     */
    public BigDecimal calcularSubtotal(BigDecimal precio, Integer cantidad) {
        return precio.multiply(BigDecimal.valueOf(cantidad));
    }

    /**
     * Calcula el impuesto (19% IVA por ejemplo)
     */
    public BigDecimal calcularImpuesto(BigDecimal subtotal) {
        BigDecimal tasaImpuesto = new BigDecimal("0.19"); // 19%
        return subtotal.multiply(tasaImpuesto);
    }

    /**
     * Calcula el total con impuesto
     */
    public BigDecimal calcularTotal(BigDecimal subtotal, BigDecimal impuesto) {
        return subtotal.add(impuesto);
    }

    /**
     * Obtiene todas las ventas
     */
    public List<Venta> obtenerTodasLasVentas() {
        return ventaRepository.findAll();
    }

    /**
     * Busca una venta por ID
     */
    public Venta obtenerVentaPorId(Long id) {
        return ventaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada"));
    }
}