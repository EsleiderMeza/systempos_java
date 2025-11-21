package com.systempos.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ventas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal total;

    @Column(precision = 12, scale = 2)
    private BigDecimal tax = BigDecimal.ZERO;

    @Column(name = "payment_method", length = 50)
    private String paymentMethod = "efectivo";

    @Column(name = "comprobante_type", length = 50)
    private String comprobanteType = "Ticket";

    @Column(name = "creado_en")
    private LocalDateTime creadoEn;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL)
    private List<DetalleVenta> detalles = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        creadoEn = LocalDateTime.now();
        createdAt = LocalDateTime.now();
    }
}