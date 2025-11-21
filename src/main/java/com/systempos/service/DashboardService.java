package com.systempos.service;

import com.systempos.model.Venta;
import com.systempos.repository.VentaRepository;
import com.systempos.repository.ProductoRepository;
import com.systempos.repository.ClienteRepository;
import com.systempos.repository.ProveedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class DashboardService {

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ProveedorRepository proveedorRepository;

    public List<Venta> getVentasHoy() {
        LocalDateTime inicioDelDia = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime finDelDia = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        return ventaRepository.findByCreatedAtBetween(inicioDelDia, finDelDia);
    }

    public long getTotalProductos() {
        return productoRepository.count();
    }

    public long getTotalClientes() {
        return clienteRepository.count();
    }

    public long getTotalProveedores() {
        return proveedorRepository.count();
    }

    public double getTotalVentasHoy() {
        List<Venta> ventas = getVentasHoy();
        return ventas.stream()
                .mapToDouble(v -> v.getTotal().doubleValue())
                .sum();
    }
}