package com.logitrack.logitrack_backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "movimientos")
public class Movimiento {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_movimiento")
  private Long idMovimiento;

  private LocalDateTime fecha;

  @Enumerated(EnumType.STRING)
  private TipoMovimiento tipo;

  @ManyToOne
  @JoinColumn(name = "usuario_id")
  private Usuario usuario;

  @ManyToOne
  @JoinColumn(name = "bodega_origen_id")
  private Bodega bodegaOrigen;

  @ManyToOne
  @JoinColumn(name = "bodega_destino_id")
  private Bodega bodegaDestino;

  private String observacion;

  public Long getIdMovimiento() {
    return idMovimiento;
  }

  public LocalDateTime getFecha() {
    return fecha;
  }

  public TipoMovimiento getTipo() {
    return tipo;
  }

  public Usuario getUsuario() {
    return usuario;
  }

  public Bodega getBodegaOrigen() {
    return bodegaOrigen;
  }

  public Bodega getBodegaDestino() {
    return bodegaDestino;
  }

  public String getObservacion() {
    return observacion;
  }

  public void setFecha(LocalDateTime fecha) {
    this.fecha = fecha;
  }

  public void setTipo(TipoMovimiento tipo) {
    this.tipo = tipo;
  }

  public void setUsuario(Usuario usuario) {
    this.usuario = usuario;
  }

  public void setBodegaOrigen(Bodega bodegaOrigen) {
    this.bodegaOrigen = bodegaOrigen;
  }

  public void setBodegaDestino(Bodega bodegaDestino) {
    this.bodegaDestino = bodegaDestino;
  }

  public void setObservacion(String observacion) {
    this.observacion = observacion;
  }
}