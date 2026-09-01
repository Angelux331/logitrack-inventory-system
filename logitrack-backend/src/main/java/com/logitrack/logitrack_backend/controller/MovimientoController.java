package com.logitrack.logitrack_backend.controller;

import com.logitrack.logitrack_backend.model.DetalleMovimiento;
import com.logitrack.logitrack_backend.model.Movimiento;
import com.logitrack.logitrack_backend.model.MovimientoRequest;
import com.logitrack.logitrack_backend.repository.MovimientoRepository;
import com.logitrack.logitrack_backend.service.MovimientoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movimientos")
public class MovimientoController {

  private final MovimientoRepository movimientoRepository;
  private final MovimientoService movimientoService;

  public MovimientoController(
    MovimientoRepository movimientoRepository,
    MovimientoService movimientoService) {

    this.movimientoRepository = movimientoRepository;
    this.movimientoService = movimientoService;
  }

  @GetMapping
  public List<Movimiento> obtenerMovimientos() {
    return movimientoRepository.findAll();
  }

  @GetMapping("/{id}")
  public Movimiento obtenerMovimiento(@PathVariable Long id) {

    return movimientoRepository.findById(id)
      .orElseThrow(() ->
        new RuntimeException("Movimiento no encontrado"));
  }

  @PostMapping
  public Movimiento crearMovimiento(
    @RequestBody Movimiento movimiento) {

    return movimientoRepository.save(movimiento);
  }

  @PutMapping("/{id}")
  public Movimiento actualizarMovimiento(
    @PathVariable Long id,
    @RequestBody Movimiento datos) {

    Movimiento movimiento =
      movimientoRepository.findById(id)
        .orElseThrow(() ->
          new RuntimeException(
            "Movimiento no encontrado"));

    movimiento.setTipo(datos.getTipo());
    movimiento.setUsuario(datos.getUsuario());
    movimiento.setBodegaOrigen(datos.getBodegaOrigen());
    movimiento.setBodegaDestino(datos.getBodegaDestino());
    movimiento.setObservacion(datos.getObservacion());

    return movimientoRepository.save(movimiento);
  }

  @DeleteMapping("/{id}")
  public void eliminarMovimiento(@PathVariable Long id) {
    movimientoRepository.deleteById(id);
  }

  @PostMapping("/registrar")
  public DetalleMovimiento registrarMovimiento(
    @RequestBody MovimientoRequest request) {

    return movimientoService.registrarMovimiento(
      request.getMovimiento(),
      request.getDetalle()
    );
  }
}