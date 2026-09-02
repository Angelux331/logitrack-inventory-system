package com.logitrack.logitrack_backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "bodegas")
public class Bodega {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_bodega")
  private Long idBodega;

  private String nombre;

  private String ubicacion;

  private Integer capacidad;

  @ManyToOne
  @JoinColumn(name = "encargado_id")
  private Usuario encargado;

  private Boolean activo;

  public Long getIdBodega() {
    return idBodega;
  }

  public String getNombre() {
    return nombre;
  }

  public String getUbicacion() {
    return ubicacion;
  }

  public Integer getCapacidad() {
    return capacidad;
  }

  public Usuario getEncargado() {
    return encargado;
  }

  public Boolean getActivo() {
    return activo;
  }

  public void setIdBodega(Long idBodega) {
    this.idBodega = idBodega;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public void setUbicacion(String ubicacion) {
    this.ubicacion = ubicacion;
  }

  public void setCapacidad(Integer capacidad) {
    this.capacidad = capacidad;
  }

  public void setEncargado(Usuario encargado) {
    this.encargado = encargado;
  }

  public void setActivo(Boolean activo) {
    this.activo = activo;
  }
}