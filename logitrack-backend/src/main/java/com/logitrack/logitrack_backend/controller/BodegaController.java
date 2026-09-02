package com.logitrack.logitrack_backend.controller;

import com.logitrack.logitrack_backend.model.Auditoria;
import com.logitrack.logitrack_backend.model.Bodega;
import com.logitrack.logitrack_backend.model.TipoOperacion;
import com.logitrack.logitrack_backend.model.Usuario;
import com.logitrack.logitrack_backend.repository.BodegaRepository;
import com.logitrack.logitrack_backend.service.AuditoriaService;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bodegas")
public class BodegaController {

  private final BodegaRepository bodegaRepository;
  private final AuditoriaService auditoriaService;

  public BodegaController(
    BodegaRepository bodegaRepository,
    AuditoriaService auditoriaService) {

    this.bodegaRepository = bodegaRepository;
    this.auditoriaService = auditoriaService;
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

    Bodega bodegaGuardada =
      bodegaRepository.save(bodega);

    Auditoria auditoria = new Auditoria();

    auditoria.setTipoOperacion(TipoOperacion.INSERT);
    auditoria.setEntidad("Bodega");
    auditoria.setEntidadId(bodegaGuardada.getIdBodega());

    auditoria.setValoresNuevos(
      "{\"nombre\":\"" + bodegaGuardada.getNombre()
        + "\",\"ubicacion\":\"" + bodegaGuardada.getUbicacion()
        + "\",\"capacidad\":\"" + bodegaGuardada.getCapacidad()
        + "\"}"
    );

    Usuario usuario = new Usuario();
    usuario.setIdusuario(1L);
    auditoria.setUsuario(usuario);

    auditoriaService.registrarAuditoria(auditoria);

    return bodegaGuardada;
  }

  // ACTUALIZAR
  @PutMapping("/{id}")
  public Bodega actualizarBodega(
    @PathVariable Long id,
    @RequestBody Bodega datos) {

    Bodega bodega = bodegaRepository.findById(id)
      .orElseThrow(() -> new RuntimeException("Bodega no encontrada"));

    String valoresAnteriores =
      "{\"nombre\":\"" + bodega.getNombre()
        + "\",\"ubicacion\":\"" + bodega.getUbicacion()
        + "\",\"capacidad\":\"" + bodega.getCapacidad()
        + "\"}";

    bodega.setNombre(datos.getNombre());
    bodega.setUbicacion(datos.getUbicacion());
    bodega.setCapacidad(datos.getCapacidad());
    bodega.setActivo(datos.getActivo());

    Bodega bodegaActualizada =
      bodegaRepository.save(bodega);

    Auditoria auditoria = new Auditoria();

    auditoria.setTipoOperacion(TipoOperacion.UPDATE);
    auditoria.setEntidad("Bodega");
    auditoria.setEntidadId(bodegaActualizada.getIdBodega());

    auditoria.setValoresAnteriores(valoresAnteriores);

    auditoria.setValoresNuevos(
      "{\"nombre\":\"" + bodegaActualizada.getNombre()
        + "\",\"ubicacion\":\"" + bodegaActualizada.getUbicacion()
        + "\",\"capacidad\":\"" + bodegaActualizada.getCapacidad()
        + "\"}"
    );

    Usuario usuario = new Usuario();
    usuario.setIdusuario(1L);
    auditoria.setUsuario(usuario);

    auditoriaService.registrarAuditoria(auditoria);

    return bodegaActualizada;
  }

  // ELIMINAR
  @DeleteMapping("/{id}")
  public void eliminarBodega(@PathVariable Long id) {

    Bodega bodega = bodegaRepository.findById(id)
      .orElseThrow(() -> new RuntimeException("Bodega no encontrada"));

    Auditoria auditoria = new Auditoria();

    auditoria.setTipoOperacion(TipoOperacion.DELETE);
    auditoria.setEntidad("Bodega");
    auditoria.setEntidadId(bodega.getIdBodega());

    auditoria.setValoresAnteriores(
      "{\"nombre\":\"" + bodega.getNombre()
        + "\",\"ubicacion\":\"" + bodega.getUbicacion()
        + "\",\"capacidad\":\"" + bodega.getCapacidad()
        + "\"}"
    );

    Usuario usuario = new Usuario();
    usuario.setIdusuario(1L);
    auditoria.setUsuario(usuario);

    bodegaRepository.deleteById(id);

    auditoriaService.registrarAuditoria(auditoria);
  }
}