package com.logitrack.logitrack_backend.controller;

import com.logitrack.logitrack_backend.model.Bodega;
import com.logitrack.logitrack_backend.repository.BodegaRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bodegas")
public class BodegaController {

  private final BodegaRepository bodegaRepository;

  public BodegaController(BodegaRepository bodegaRepository) {
    this.bodegaRepository = bodegaRepository;
  }

  // OBTENER TODAS
  @GetMapping
  public List<Bodega> obtenerBodegas() {
    return bodegaRepository.findAll();
  }

  // OBTENER UNA
  @GetMapping("/{id}")
  public Bodega obtenerBodega(@PathVariable Long id) {
    return bodegaRepository.findById(id)
      .orElseThrow(() -> new RuntimeException("Bodega no encontrada"));
  }

  // CREAR
  @PostMapping
  public Bodega crearBodega(@RequestBody Bodega bodega) {
    return bodegaRepository.save(bodega);
  }

  // ACTUALIZAR
  @PutMapping("/{id}")
  public Bodega actualizarBodega(
    @PathVariable Long id,
    @RequestBody Bodega datos) {

    Bodega bodega = bodegaRepository.findById(id)
      .orElseThrow(() -> new RuntimeException("Bodega no encontrada"));

    bodega.setNombre(datos.getNombre());
    bodega.setUbicacion(datos.getUbicacion());
    bodega.setCapacidad(datos.getCapacidad());
    bodega.setActivo(datos.getActivo());

    return bodegaRepository.save(bodega);
  }

  // ELIMINAR
  @DeleteMapping("/{id}")
  public void eliminarBodega(@PathVariable Long id) {
    bodegaRepository.deleteById(id);
  }
}