package com.systempos.controller;

import com.systempos.model.Venta;
import com.systempos.service.ReporteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/reportes")
public class ReporteController {

    @Autowired
    private ReporteService reporteService;

    /**
     * Vista principal de reportes
     */
    @GetMapping
    public String mostrarReportes(Model model) {
        // Resumen general
        Map<String, Object> resumen = reporteService.obtenerResumenGeneral();
        model.addAttribute("resumen", resumen);

        // Ventas del día
        List<Venta> ventasHoy = reporteService.obtenerVentasHoy();
        model.addAttribute("ventasHoy", ventasHoy);

        // Ventas del mes
        List<Venta> ventasMes = reporteService.obtenerVentasMesActual();
        model.addAttribute("ventasMes", ventasMes);

        // Productos más vendidos
        Map<String, Integer> productosMasVendidos = reporteService.obtenerProductosMasVendidos(10);
        model.addAttribute("productosMasVendidos", productosMasVendidos);

        // Productos más rentables
        Map<String, Double> productosMasRentables = reporteService.obtenerProductosMasRentables(10);
        model.addAttribute("productosMasRentables", productosMasRentables);

        // Ventas por día del mes
        Map<Integer, Double> ventasPorDia = reporteService.obtenerVentasPorDiaMes();
        model.addAttribute("ventasPorDia", ventasPorDia);

        // Métodos de pago
        Map<String, Long> metodosPago = reporteService.obtenerMetodosPagoMasUsados();
        model.addAttribute("metodosPago", metodosPago);

        // Ventas por mes del año
        Map<Integer, Double> ventasPorMes = reporteService.obtenerVentasPorMesAnioActual();
        model.addAttribute("ventasPorMes", ventasPorMes);

        return "pages/reportes";
    }

    /**
     * Reporte por rango de fechas
     */
    @GetMapping("/rango")
    public String reportePorRango(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,
            Model model) {

        List<Venta> ventas = reporteService.obtenerVentasPorRango(fechaInicio, fechaFin);
        double total = reporteService.obtenerTotalVentasPorRango(fechaInicio, fechaFin);

        model.addAttribute("ventas", ventas);
        model.addAttribute("total", total);
        model.addAttribute("fechaInicio", fechaInicio);
        model.addAttribute("fechaFin", fechaFin);

        return "pages/reporte-rango";
    }
}