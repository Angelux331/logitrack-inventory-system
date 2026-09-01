package com.logitrack.logitrack_backend.repository;

import com.logitrack.logitrack_backend.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
}