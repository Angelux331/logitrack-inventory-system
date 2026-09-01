package com.logitrack.logitrack_backend.model;

public class MovimientoRequest {

  private Movimiento movimiento;
  private DetalleMovimiento detalle;

  public Movimiento getMovimiento() {
    return movimiento;
  }

  public DetalleMovimiento getDetalle() {
    return detalle;
  }

  public void setMovimiento(Movimiento movimiento) {
    this.movimiento = movimiento;
  }

  public void setDetalle(DetalleMovimiento detalle) {
    this.detalle = detalle;
  }
}