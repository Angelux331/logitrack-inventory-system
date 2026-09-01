package com.logitrack.logitrack_backend.repository;

import com.logitrack.logitrack_backend.model.Inventario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InventarioRepository extends JpaRepository<Inventario, Long> {

  List<Inventario> findByStockLessThan(Integer umbral);

  Optional<Inventario> findByBodega_IdBodegaAndProducto_IdProducto(
    Long idBodega,
    Long idProducto
  );
}