package com.logitrack.logitrack_backend.controller;

import com.logitrack.logitrack_backend.model.Producto;
import com.logitrack.logitrack_backend.repository.ProductoRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/productos")
public class ProductoController {

  private final ProductoRepository productoRepository;

  public ProductoController(ProductoRepository productoRepository) {
    this.productoRepository = productoRepository;
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
    return productoRepository.save(producto);
  }

  @PutMapping("/{id}")
  public Producto actualizarProducto(
    @PathVariable Long id,
    @RequestBody Producto datos) {

    Producto producto = productoRepository.findById(id)
      .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

    producto.setNombre(datos.getNombre());
    producto.setCategoria(datos.getCategoria());
    producto.setPrecio(datos.getPrecio());
    producto.setDescripcion(datos.getDescripcion());
    producto.setActivo(datos.getActivo());

    return productoRepository.save(producto);
  }

  @DeleteMapping("/{id}")
  public void eliminarProducto(@PathVariable Long id) {
    productoRepository.deleteById(id);
  }
}