package com.logitrack.logitrack_backend.controller;

import com.logitrack.logitrack_backend.model.Categoria;
import com.logitrack.logitrack_backend.repository.CategoriaRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {

  private final CategoriaRepository categoriaRepository;

  public CategoriaController(CategoriaRepository categoriaRepository) {
    this.categoriaRepository = categoriaRepository;
  }

  // GET /categorias
  @GetMapping
  public List<Categoria> obtenerCategorias() {
    return categoriaRepository.findAll();
  }

  // GET /categorias/1
  @GetMapping("/{id}")
  public Categoria obtenerCategoria(@PathVariable Long id) {
    return categoriaRepository.findById(id)
      .orElseThrow(() -> new RuntimeException("Categoria no encontrada"));
  }

  // POST /categorias
  @PostMapping
  public Categoria crearCategoria(@RequestBody Categoria categoria) {
    return categoriaRepository.save(categoria);
  }

  // PUT /categorias/1
  @PutMapping("/{id}")
  public Categoria actualizarCategoria(
    @PathVariable Long id,
    @RequestBody Categoria datos) {

    Categoria categoria = categoriaRepository.findById(id)
      .orElseThrow(() -> new RuntimeException("Categoria no encontrada"));

    categoria.setNombre(datos.getNombre());

    return categoriaRepository.save(categoria);
  }

  // DELETE /categorias/1
  @DeleteMapping("/{id}")
  public void eliminarCategoria(@PathVariable Long id) {
    categoriaRepository.deleteById(id);
  }
}