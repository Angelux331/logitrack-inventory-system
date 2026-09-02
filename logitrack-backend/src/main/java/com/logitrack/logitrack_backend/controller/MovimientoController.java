package com.logitrack.logitrack_backend.controller;

import com.logitrack.logitrack_backend.model.Auditoria;
import com.logitrack.logitrack_backend.model.DetalleMovimiento;
import com.logitrack.logitrack_backend.model.Movimiento;
import com.logitrack.logitrack_backend.model.MovimientoRequest;
import com.logitrack.logitrack_backend.model.TipoOperacion;
import com.logitrack.logitrack_backend.model.Usuario;
import com.logitrack.logitrack_backend.repository.MovimientoRepository;
import com.logitrack.logitrack_backend.service.AuditoriaService;
import com.logitrack.logitrack_backend.service.MovimientoService;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movimientos")
public class MovimientoController {

  private final MovimientoRepository movimientoRepository;
  private final MovimientoService movimientoService;
  private final AuditoriaService auditoriaService;

  public MovimientoController(
    MovimientoRepository movimientoRepository,
    MovimientoService movimientoService,
    AuditoriaService auditoriaService) {

    this.movimientoRepository = movimientoRepository;
    this.movimientoService = movimientoService;
    this.auditoriaService = auditoriaService;
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

    Movimiento movimientoGuardado =
      movimientoRepository.save(movimiento);

    Auditoria auditoria = new Auditoria();

    auditoria.setTipoOperacion(TipoOperacion.INSERT);
    auditoria.setEntidad("Movimiento");
    auditoria.setEntidadId(
      movimientoGuardado.getIdMovimiento()
    );

    auditoria.setValoresNuevos(
      "{\"tipo\":\"" + movimientoGuardado.getTipo()
        + "\",\"observacion\":\""
        + movimientoGuardado.getObservacion()
        + "\"}"
    );

    Usuario usuario = new Usuario();
    usuario.setIdusuario(1L);
    auditoria.setUsuario(usuario);

    auditoriaService.registrarAuditoria(auditoria);

    return movimientoGuardado;
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

    String valoresAnteriores =
      "{\"tipo\":\"" + movimiento.getTipo()
        + "\",\"observacion\":\""
        + movimiento.getObservacion()
        + "\"}";

    movimiento.setTipo(datos.getTipo());
    movimiento.setUsuario(datos.getUsuario());
    movimiento.setBodegaOrigen(datos.getBodegaOrigen());
    movimiento.setBodegaDestino(datos.getBodegaDestino());
    movimiento.setObservacion(datos.getObservacion());

    Movimiento movimientoActualizado =
      movimientoRepository.save(movimiento);

    Auditoria auditoria = new Auditoria();

    auditoria.setTipoOperacion(TipoOperacion.UPDATE);
    auditoria.setEntidad("Movimiento");
    auditoria.setEntidadId(
      movimientoActualizado.getIdMovimiento()
    );

    auditoria.setValoresAnteriores(valoresAnteriores);

    auditoria.setValoresNuevos(
      "{\"tipo\":\"" + movimientoActualizado.getTipo()
        + "\",\"observacion\":\""
        + movimientoActualizado.getObservacion()
        + "\"}"
    );

    Usuario usuario = new Usuario();
    usuario.setIdusuario(1L);
    auditoria.setUsuario(usuario);

    auditoriaService.registrarAuditoria(auditoria);

    return movimientoActualizado;
  }

  @DeleteMapping("/{id}")
  public void eliminarMovimiento(@PathVariable Long id) {

    Movimiento movimiento =
      movimientoRepository.findById(id)
        .orElseThrow(() ->
          new RuntimeException(
            "Movimiento no encontrado"));

    Auditoria auditoria = new Auditoria();

    auditoria.setTipoOperacion(TipoOperacion.DELETE);
    auditoria.setEntidad("Movimiento");
    auditoria.setEntidadId(
      movimiento.getIdMovimiento()
    );

    auditoria.setValoresAnteriores(
      "{\"tipo\":\"" + movimiento.getTipo()
        + "\",\"observacion\":\""
        + movimiento.getObservacion()
        + "\"}"
    );

    Usuario usuario = new Usuario();
    usuario.setIdusuario(1L);
    auditoria.setUsuario(usuario);

    movimientoRepository.deleteById(id);

    auditoriaService.registrarAuditoria(auditoria);
  }

  @PostMapping("/registrar")
  public DetalleMovimiento registrarMovimiento(
    @RequestBody MovimientoRequest request) {

    DetalleMovimiento detalle =
      movimientoService.registrarMovimiento(
        request.getMovimiento(),
        request.getDetalle()
      );

    Movimiento movimiento =
      detalle.getMovimiento();

    Auditoria auditoria = new Auditoria();

    auditoria.setTipoOperacion(TipoOperacion.INSERT);
    auditoria.setEntidad("Movimiento");
    auditoria.setEntidadId(
      movimiento.getIdMovimiento()
    );

    String bodegaOrigen =
      movimiento.getBodegaOrigen() != null
        ? String.valueOf(
        movimiento.getBodegaOrigen().getIdBodega())
        : "null";

    String bodegaDestino =
      movimiento.getBodegaDestino() != null
        ? String.valueOf(
        movimiento.getBodegaDestino().getIdBodega())
        : "null";

    auditoria.setValoresNuevos(
      "{\"tipo\":\"" + movimiento.getTipo()
        + "\",\"productoId\":\""
        + detalle.getProducto().getIdProducto()
        + "\",\"cantidad\":\""
        + detalle.getCantidad()
        + "\",\"bodegaOrigen\":\""
        + bodegaOrigen
        + "\",\"bodegaDestino\":\""
        + bodegaDestino
        + "\",\"observacion\":\""
        + (movimiento.getObservacion() != null
        ? movimiento.getObservacion()
        : "")
        + "\"}"
    );

    Usuario usuario = new Usuario();
    usuario.setIdusuario(1L);
    auditoria.setUsuario(usuario);

    auditoriaService.registrarAuditoria(auditoria);

    return detalle;
  }
}