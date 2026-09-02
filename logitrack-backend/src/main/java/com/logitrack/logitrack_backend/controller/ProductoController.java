package com.logitrack.logitrack_backend.controller;

import com.logitrack.logitrack_backend.model.Auditoria;
import com.logitrack.logitrack_backend.model.Producto;
import com.logitrack.logitrack_backend.model.TipoOperacion;
import com.logitrack.logitrack_backend.model.Usuario;
import com.logitrack.logitrack_backend.repository.ProductoRepository;
import com.logitrack.logitrack_backend.service.AuditoriaService;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/productos")
public class ProductoController {

  private final ProductoRepository productoRepository;
  private final AuditoriaService auditoriaService;

  public ProductoController(
    ProductoRepository productoRepository,
    AuditoriaService auditoriaService) {

    this.productoRepository = productoRepository;
    this.auditoriaService = auditoriaService;
  }

  @GetMapping
  public List<Producto> obtenerProductos() {
    return productoRepository.findAll();
  }

  @GetMapping("/{id}")
  public Producto obtenerProducto(@PathVariable Long id) {
    return productoRepository.findById(id)
      .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
  }

  @PostMapping
  public Producto crearProducto(@RequestBody Producto producto) {

    Producto productoGuardado =
      productoRepository.save(producto);

    Auditoria auditoria = new Auditoria();

    auditoria.setTipoOperacion(TipoOperacion.INSERT);
    auditoria.setEntidad("Producto");
    auditoria.setEntidadId(productoGuardado.getIdProducto());

    auditoria.setValoresNuevos(
      "{\"nombre\":\"" + productoGuardado.getNombre()
        + "\",\"precio\":\"" + productoGuardado.getPrecio()
        + "\",\"descripcion\":\"" + productoGuardado.getDescripcion()
        + "\"}"
    );

    Usuario usuario = new Usuario();
    usuario.setIdusuario(1L);
    auditoria.setUsuario(usuario);

    auditoriaService.registrarAuditoria(auditoria);

    return productoGuardado;
  }

  @PutMapping("/{id}")
  public Producto actualizarProducto(
    @PathVariable Long id,
    @RequestBody Producto datos) {

    Producto producto = productoRepository.findById(id)
      .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

    String valoresAnteriores =
      "{\"nombre\":\"" + producto.getNombre()
        + "\",\"precio\":\"" + producto.getPrecio()
        + "\",\"descripcion\":\"" + producto.getDescripcion()
        + "\"}";

    producto.setNombre(datos.getNombre());
    producto.setCategoria(datos.getCategoria());
    producto.setPrecio(datos.getPrecio());
    producto.setDescripcion(datos.getDescripcion());
    producto.setActivo(datos.getActivo());

    Producto productoActualizado =
      productoRepository.save(producto);

    Auditoria auditoria = new Auditoria();

    auditoria.setTipoOperacion(TipoOperacion.UPDATE);
    auditoria.setEntidad("Producto");
    auditoria.setEntidadId(productoActualizado.getIdProducto());

    auditoria.setValoresAnteriores(valoresAnteriores);

    auditoria.setValoresNuevos(
      "{\"nombre\":\"" + productoActualizado.getNombre()
        + "\",\"precio\":\"" + productoActualizado.getPrecio()
        + "\",\"descripcion\":\"" + productoActualizado.getDescripcion()
        + "\"}"
    );

    Usuario usuario = new Usuario();
    usuario.setIdusuario(1L);
    auditoria.setUsuario(usuario);

    auditoriaService.registrarAuditoria(auditoria);

    return productoActualizado;
  }

  @DeleteMapping("/{id}")
  public void eliminarProducto(@PathVariable Long id) {

    Producto producto = productoRepository.findById(id)
      .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

    Auditoria auditoria = new Auditoria();

    auditoria.setTipoOperacion(TipoOperacion.DELETE);
    auditoria.setEntidad("Producto");
    auditoria.setEntidadId(producto.getIdProducto());

    auditoria.setValoresAnteriores(
      "{\"nombre\":\"" + producto.getNombre()
        + "\",\"precio\":\"" + producto.getPrecio()
        + "\",\"descripcion\":\"" + producto.getDescripcion()
        + "\"}"
    );

    Usuario usuario = new Usuario();
    usuario.setIdusuario(1L);
    auditoria.setUsuario(usuario);

    productoRepository.deleteById(id);

    auditoriaService.registrarAuditoria(auditoria);
  }
}