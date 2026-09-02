package com.logitrack.logitrack_backend.controller;

import com.logitrack.logitrack_backend.model.Auditoria;
import com.logitrack.logitrack_backend.model.Categoria;
import com.logitrack.logitrack_backend.model.TipoOperacion;
import com.logitrack.logitrack_backend.model.Usuario;
import com.logitrack.logitrack_backend.repository.CategoriaRepository;
import com.logitrack.logitrack_backend.service.AuditoriaService;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {

  private final CategoriaRepository categoriaRepository;
  private final AuditoriaService auditoriaService;

  public CategoriaController(
    CategoriaRepository categoriaRepository,
    AuditoriaService auditoriaService) {

    this.categoriaRepository = categoriaRepository;
    this.auditoriaService = auditoriaService;
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

    Categoria categoriaGuardada =
      categoriaRepository.save(categoria);

    Auditoria auditoria = new Auditoria();

    auditoria.setTipoOperacion(TipoOperacion.INSERT);
    auditoria.setEntidad("Categoria");
    auditoria.setEntidadId(categoriaGuardada.getIdCategoria());

    auditoria.setValoresNuevos(
      "{\"nombre\":\"" + categoriaGuardada.getNombre() + "\"}"
    );

    Usuario usuario = new Usuario();
    usuario.setIdusuario(1L);
    auditoria.setUsuario(usuario);

    auditoriaService.registrarAuditoria(auditoria);

    return categoriaGuardada;
  }

  // PUT /categorias/1
  @PutMapping("/{id}")
  public Categoria actualizarCategoria(
    @PathVariable Long id,
    @RequestBody Categoria datos) {

    Categoria categoria = categoriaRepository.findById(id)
      .orElseThrow(() -> new RuntimeException("Categoria no encontrada"));

    String valoresAnteriores =
      "{\"nombre\":\"" + categoria.getNombre() + "\"}";

    categoria.setNombre(datos.getNombre());

    Categoria categoriaActualizada =
      categoriaRepository.save(categoria);

    Auditoria auditoria = new Auditoria();

    auditoria.setTipoOperacion(TipoOperacion.UPDATE);
    auditoria.setEntidad("Categoria");
    auditoria.setEntidadId(categoriaActualizada.getIdCategoria());

    auditoria.setValoresAnteriores(valoresAnteriores);

    auditoria.setValoresNuevos(
      "{\"nombre\":\"" + categoriaActualizada.getNombre() + "\"}"
    );

    Usuario usuario = new Usuario();
    usuario.setIdusuario(1L);
    auditoria.setUsuario(usuario);

    auditoriaService.registrarAuditoria(auditoria);

    return categoriaActualizada;
  }

  // DELETE /categorias/1
  @DeleteMapping("/{id}")
  public void eliminarCategoria(@PathVariable Long id) {

    Categoria categoria = categoriaRepository.findById(id)
      .orElseThrow(() -> new RuntimeException("Categoria no encontrada"));

    Auditoria auditoria = new Auditoria();

    auditoria.setTipoOperacion(TipoOperacion.DELETE);
    auditoria.setEntidad("Categoria");
    auditoria.setEntidadId(categoria.getIdCategoria());

    auditoria.setValoresAnteriores(
      "{\"nombre\":\"" + categoria.getNombre() + "\"}"
    );

    Usuario usuario = new Usuario();
    usuario.setIdusuario(1L);
    auditoria.setUsuario(usuario);

    categoriaRepository.deleteById(id);

    auditoriaService.registrarAuditoria(auditoria);
  }
}