package com.systempos.service;

import com.systempos.model.Producto;
import com.systempos.model.Venta;
import com.systempos.repository.VentaRepository;
import com.systempos.repository.ProductoRepository;
import com.systempos.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReporteService {

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    /**
     * Ventas del día actual
     */
    public List<Venta> obtenerVentasHoy() {
        LocalDateTime inicioDelDia = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime finDelDia = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        return ventaRepository.findByCreatedAtBetween(inicioDelDia, finDelDia);
    }

    /**
     * Ventas por rango de fechas
     */
    public List<Venta> obtenerVentasPorRango(LocalDate fechaInicio, LocalDate fechaFin) {
        LocalDateTime inicio = LocalDateTime.of(fechaInicio, LocalTime.MIN);
        LocalDateTime fin = LocalDateTime.of(fechaFin, LocalTime.MAX);
        return ventaRepository.findByCreatedAtBetween(inicio, fin);
    }

    /**
     * Total de ventas del día
     */
    public double obtenerTotalVentasHoy() {
        return obtenerVentasHoy().stream()
                .mapToDouble(v -> v.getTotal().doubleValue())
                .sum();
    }

    /**
     * Total de ventas por rango
     */
    public double obtenerTotalVentasPorRango(LocalDate fechaInicio, LocalDate fechaFin) {
        return obtenerVentasPorRango(fechaInicio, fechaFin).stream()
                .mapToDouble(v -> v.getTotal().doubleValue())
                .sum();
    }

    /**
     * Ventas del mes actual
     */
    public List<Venta> obtenerVentasMesActual() {
        YearMonth mesActual = YearMonth.now();
        LocalDate primerDia = mesActual.atDay(1);
        LocalDate ultimoDia = mesActual.atEndOfMonth();
        return obtenerVentasPorRango(primerDia, ultimoDia);
    }

    /**
     * Total ventas del mes
     */
    public double obtenerTotalVentasMes() {
        return obtenerVentasMesActual().stream()
                .mapToDouble(v -> v.getTotal().doubleValue())
                .sum();
    }

    /**
     * Ventas por día del mes (para gráficos)
     */
    public Map<Integer, Double> obtenerVentasPorDiaMes() {
        List<Venta> ventasMes = obtenerVentasMesActual();

        return ventasMes.stream()
                .collect(Collectors.groupingBy(
                        v -> v.getCreatedAt().getDayOfMonth(),
                        Collectors.summingDouble(v -> v.getTotal().doubleValue())
                ));
    }

    /**
     * Productos más vendidos
     */
    public Map<String, Integer> obtenerProductosMasVendidos(int limite) {
        List<Venta> todasVentas = ventaRepository.findAll();

        Map<String, Integer> productosCantidad = new HashMap<>();

        for (Venta venta : todasVentas) {
            if (venta.getDetalles() != null) {
                venta.getDetalles().forEach(detalle -> {
                    String nombreProducto = detalle.getProducto().getNombre();
                    productosCantidad.merge(nombreProducto, detalle.getCantidad(), Integer::sum);
                });
            }
        }

        return productosCantidad.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(limite)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }

    /**
     * Productos que generan más ingresos
     */
    public Map<String, Double> obtenerProductosMasRentables(int limite) {
        List<Venta> todasVentas = ventaRepository.findAll();

        Map<String, Double> productosIngresos = new HashMap<>();

        for (Venta venta : todasVentas) {
            if (venta.getDetalles() != null) {
                venta.getDetalles().forEach(detalle -> {
                    String nombreProducto = detalle.getProducto().getNombre();
                    double ingreso = detalle.getSubtotal().doubleValue();
                    productosIngresos.merge(nombreProducto, ingreso, Double::sum);
                });
            }
        }

        return productosIngresos.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .limit(limite)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }

    /**
     * Resumen general de estadísticas
     */
    public Map<String, Object> obtenerResumenGeneral() {
        Map<String, Object> resumen = new HashMap<>();

        // Ventas
        resumen.put("totalVentasHoy", obtenerTotalVentasHoy());
        resumen.put("totalVentasMes", obtenerTotalVentasMes());
        resumen.put("numeroVentasHoy", obtenerVentasHoy().size());
        resumen.put("numeroVentasMes", obtenerVentasMesActual().size());

        // Productos
        resumen.put("totalProductos", productoRepository.count());
        resumen.put("productosStockBajo", productoRepository.findAll().stream()
                .filter(p -> p.getStock() <= p.getStockMinimo())
                .count());

        // Clientes
        resumen.put("totalClientes", clienteRepository.count());

        // Promedio venta
        double promedioVenta = obtenerVentasMesActual().stream()
                .mapToDouble(v -> v.getTotal().doubleValue())
                .average()
                .orElse(0.0);
        resumen.put("promedioVenta", promedioVenta);

        return resumen;
    }

    /**
     * Métodos de pago más utilizados
     */
    public Map<String, Long> obtenerMetodosPagoMasUsados() {
        List<Venta> todasVentas = ventaRepository.findAll();

        return todasVentas.stream()
                .collect(Collectors.groupingBy(
                        Venta::getPaymentMethod,
                        Collectors.counting()
                ));
    }

    /**
     * Ventas por mes del año actual
     */
    public Map<Integer, Double> obtenerVentasPorMesAnioActual() {
        int anioActual = LocalDate.now().getYear();

        Map<Integer, Double> ventasPorMes = new HashMap<>();

        for (int mes = 1; mes <= 12; mes++) {
            YearMonth yearMonth = YearMonth.of(anioActual, mes);
            LocalDate primerDia = yearMonth.atDay(1);
            LocalDate ultimoDia = yearMonth.atEndOfMonth();

            double totalMes = obtenerTotalVentasPorRango(primerDia, ultimoDia);
            ventasPorMes.put(mes, totalMes);
        }

        return ventasPorMes;
    }
}