package com.logitrack.logitrack_backend.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "detalle_movimiento")
public class DetalleMovimiento {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_detalle")
  private Long idDetalle;

  @ManyToOne
  @JoinColumn(name = "movimiento_id")
  private Movimiento movimiento;

  @ManyToOne
  @JoinColumn(name = "producto_id")
  private Producto producto;

  private Integer cantidad;

  @Column(name = "precio_unitario")
  private BigDecimal precioUnitario;

  public Long getIdDetalle() {
    return idDetalle;
  }

  public Movimiento getMovimiento() {
    return movimiento;
  }

  public Producto getProducto() {
    return producto;
  }

  public Integer getCantidad() {
    return cantidad;
  }

  public BigDecimal getPrecioUnitario() {
    return precioUnitario;
  }

  public void setMovimiento(Movimiento movimiento) {
    this.movimiento = movimiento;
  }

  public void setProducto(Producto producto) {
    this.producto = producto;
  }

  public void setCantidad(Integer cantidad) {
    this.cantidad = cantidad;
  }

  public void setPrecioUnitario(BigDecimal precioUnitario) {
    this.precioUnitario = precioUnitario;
  }
}