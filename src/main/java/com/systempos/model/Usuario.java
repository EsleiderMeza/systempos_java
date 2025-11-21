package com.systempos.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(length = 150)
    private String email;

    @Column(nullable = false, unique = true, length = 50)
    private String usuario;

    @Column(nullable = false)
    private String clave;

    @Column(nullable = false, length = 20)
    private String rol;

    @Column(columnDefinition = "TINYINT DEFAULT 1")
    private Integer estado;

    @Column(name = "creado_en", nullable = false, updatable = false)
    private LocalDateTime creadoEn;

    @PrePersist
    protected void onCreate() {
        creadoEn = LocalDateTime.now();
    }
}