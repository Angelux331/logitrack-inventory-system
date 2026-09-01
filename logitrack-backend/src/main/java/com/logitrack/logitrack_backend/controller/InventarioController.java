package com.logitrack.logitrack_backend.controller;

import com.logitrack.logitrack_backend.model.Inventario;
import com.logitrack.logitrack_backend.repository.InventarioRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inventario")
public class InventarioController {

  private final InventarioRepository inventarioRepository;

  public InventarioController(InventarioRepository inventarioRepository) {
    this.inventarioRepository = inventarioRepository;
  }

  @GetMapping
  public List<Inventario> obtenerInventario() {
    return inventarioRepository.findAll();
  }

  @GetMapping("/{id}")
  public Inventario obtenerInventarioPorId(@PathVariable Long id) {
    return inventarioRepository.findById(id)
      .orElseThrow(() -> new RuntimeException("Inventario no encontrado"));
  }

  @GetMapping("/stock-bajo")
  public List<Inventario> obtenerStockBajo(
    @RequestParam Integer umbral) {

    return inventarioRepository.findByStockLessThan(umbral);
  }

  @PostMapping
  public Inventario crearInventario(@RequestBody Inventario inventario) {
    return inventarioRepository.save(inventario);
  }

  @PutMapping("/{id}")
  public Inventario actualizarInventario(
    @PathVariable Long id,
    @RequestBody Inventario datos) {

    Inventario inventario = inventarioRepository.findById(id)
      .orElseThrow(() -> new RuntimeException("Inventario no encontrado"));

    inventario.setBodega(datos.getBodega());
    inventario.setProducto(datos.getProducto());
    inventario.setStock(datos.getStock());

    return inventarioRepository.save(inventario);
  }

  @DeleteMapping("/{id}")
  public void eliminarInventario(@PathVariable Long id) {
    inventarioRepository.deleteById(id);
  }
}