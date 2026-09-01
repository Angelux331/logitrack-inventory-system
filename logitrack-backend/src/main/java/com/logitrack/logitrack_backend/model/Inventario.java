package com.logitrack.logitrack_backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "inventario")
public class Inventario {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_inventario")
  private Long idInventario;

  @ManyToOne
  @JoinColumn(name = "id_bodega")
  private Bodega bodega;

  @ManyToOne
  @JoinColumn(name = "id_producto")
  private Producto producto;

  private Integer stock;

  @Column(name = "fecha_actualizacion")
  private LocalDateTime fechaActualizacion;

  public Long getIdInventario() {
    return idInventario;
  }

  public Bodega getBodega() {
    return bodega;
  }

  public Producto getProducto() {
    return producto;
  }

  public Integer getStock() {
    return stock;
  }

  public LocalDateTime getFechaActualizacion() {
    return fechaActualizacion;
  }

  public void setBodega(Bodega bodega) {
    this.bodega = bodega;
  }

  public void setProducto(Producto producto) {
    this.producto = producto;
  }

  public void setStock(Integer stock) {
    this.stock = stock;
  }

  public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
    this.fechaActualizacion = fechaActualizacion;
  }
}