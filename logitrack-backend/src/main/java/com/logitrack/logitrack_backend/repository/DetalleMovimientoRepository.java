package com.logitrack.logitrack_backend.repository;

import com.logitrack.logitrack_backend.model.DetalleMovimiento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DetalleMovimientoRepository
  extends JpaRepository<DetalleMovimiento, Long> {
}