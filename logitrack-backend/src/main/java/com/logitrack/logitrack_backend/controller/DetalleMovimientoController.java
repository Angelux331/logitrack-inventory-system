package com.logitrack.logitrack_backend.controller;

import com.logitrack.logitrack_backend.model.DetalleMovimiento;
import com.logitrack.logitrack_backend.repository.DetalleMovimientoRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/detalle-movimientos")
public class DetalleMovimientoController {

  private final DetalleMovimientoRepository detalleRepository;

  public DetalleMovimientoController(
    DetalleMovimientoRepository detalleRepository) {
    this.detalleRepository = detalleRepository;
  }

  @GetMapping
  public List<DetalleMovimiento> obtenerDetalles() {
    return detalleRepository.findAll();
  }

  @GetMapping("/{id}")
  public DetalleMovimiento obtenerDetalle(@PathVariable Long id) {
    return detalleRepository.findById(id)
      .orElseThrow(() ->
        new RuntimeException("Detalle no encontrado"));
  }

  @PostMapping
  public DetalleMovimiento crearDetalle(
    @RequestBody DetalleMovimiento detalle) {

    return detalleRepository.save(detalle);
  }

  @PutMapping("/{id}")
  public DetalleMovimiento actualizarDetalle(
    @PathVariable Long id,
    @RequestBody DetalleMovimiento datos) {

    DetalleMovimiento detalle = detalleRepository.findById(id)
      .orElseThrow(() ->
        new RuntimeException("Detalle no encontrado"));

    detalle.setMovimiento(datos.getMovimiento());
    detalle.setProducto(datos.getProducto());
    detalle.setCantidad(datos.getCantidad());
    detalle.setPrecioUnitario(datos.getPrecioUnitario());

    return detalleRepository.save(detalle);
  }

  @DeleteMapping("/{id}")
  public void eliminarDetalle(@PathVariable Long id) {
    detalleRepository.deleteById(id);
  }
}