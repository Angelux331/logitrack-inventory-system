package com.logitrack.logitrack_backend.repository;

import com.logitrack.logitrack_backend.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
}