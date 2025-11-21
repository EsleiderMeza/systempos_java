package com.systempos.service;

import com.systempos.model.Producto;
import com.systempos.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InventarioService {

    @Autowired
    private ProductoRepository productoRepository;

    /**
     * Obtener todos los productos del inventario
     */
    public List<Producto> obtenerInventarioCompleto() {
        return productoRepository.findAll();
    }

    /**
     * Productos con stock bajo (menor o igual al stock mínimo)
     */
    public List<Producto> obtenerProductosStockBajo() {
        return productoRepository.findAll().stream()
                .filter(p -> p.getStock() <= p.getStockMinimo())
                .collect(Collectors.toList());
    }

    /**
     * Productos sin stock (stock = 0)
     */
    public List<Producto> obtenerProductosSinStock() {
        return productoRepository.findAll().stream()
                .filter(p -> p.getStock() == 0)
                .collect(Collectors.toList());
    }

    /**
     * Productos activos
     */
    public List<Producto> obtenerProductosActivos() {
        return productoRepository.findAll().stream()
                .filter(p -> p.getActivo() == 1)
                .collect(Collectors.toList());
    }

    /**
     * Calcular valor total del inventario
     */
    public double calcularValorTotalInventario() {
        return productoRepository.findAll().stream()
                .mapToDouble(p -> p.getPrecioCompra().doubleValue() * p.getStock())
                .sum();
    }

    /**
     * Calcular valor potencial de ventas
     */
    public double calcularValorPotencialVentas() {
        return productoRepository.findAll().stream()
                .mapToDouble(p -> p.getPrecioVenta().doubleValue() * p.getStock())
                .sum();
    }

    /**
     * Obtener productos más costosos en inventario
     */
    public List<Producto> obtenerProductosMasCostosos(int limite) {
        return productoRepository.findAll().stream()
                .sorted((p1, p2) -> p2.getPrecioVenta().compareTo(p1.getPrecioVenta()))
                .limit(limite)
                .collect(Collectors.toList());
    }

    /**
     * Actualizar stock de un producto
     */
    public Producto actualizarStock(Long productoId, Integer nuevoStock) {
        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        producto.setStock(nuevoStock);
        return productoRepository.save(producto);
    }

    /**
     * Agregar stock a un producto
     */
    public Producto agregarStock(Long productoId, Integer cantidad) {
        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        producto.setStock(producto.getStock() + cantidad);
        return productoRepository.save(producto);
    }

    /**
     * Reducir stock de un producto
     */
    public Producto reducirStock(Long productoId, Integer cantidad) {
        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        int nuevoStock = producto.getStock() - cantidad;
        if (nuevoStock < 0) {
            throw new RuntimeException("Stock insuficiente");
        }

        producto.setStock(nuevoStock);
        return productoRepository.save(producto);
    }
}