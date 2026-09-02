package com.logitrack.logitrack_backend.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "productos")
public class Producto {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_producto")
  private Long idProducto;

  private String nombre;

  @ManyToOne
  @JoinColumn(name = "id_categoria")
  private Categoria categoria;

  @Column(precision = 10, scale = 2)
  private BigDecimal precio;

  private String descripcion;

  private Boolean activo;

  public Long getIdProducto() {
    return idProducto;
  }

  public String getNombre() {
    return nombre;
  }

  public Categoria getCategoria() {
    return categoria;
  }

  public BigDecimal getPrecio() {
    return precio;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public Boolean getActivo() {
    return activo;
  }

  public void setIdProducto(Long idProducto) {
    this.idProducto = idProducto;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public void setCategoria(Categoria categoria) {
    this.categoria = categoria;
  }

  public void setPrecio(BigDecimal precio) {
    this.precio = precio;
  }

  public void setDescripcion(String descripcion) {
    this.descripcion = descripcion;
  }

  public void setActivo(Boolean activo) {
    this.activo = activo;
  }
}