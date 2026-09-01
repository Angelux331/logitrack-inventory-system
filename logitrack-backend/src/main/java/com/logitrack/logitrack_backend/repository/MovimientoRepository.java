package com.logitrack.logitrack_backend.repository;

import com.logitrack.logitrack_backend.model.Movimiento;
import com.logitrack.logitrack_backend.model.TipoMovimiento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovimientoRepository
  extends JpaRepository<Movimiento, Long> {

  long countByTipo(TipoMovimiento tipo);
}