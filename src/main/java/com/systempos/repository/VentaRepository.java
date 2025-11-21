package com.systempos.repository;

import com.systempos.model.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {
    List<Venta> findByCreatedAtBetween(LocalDateTime inicio, LocalDateTime fin);
}