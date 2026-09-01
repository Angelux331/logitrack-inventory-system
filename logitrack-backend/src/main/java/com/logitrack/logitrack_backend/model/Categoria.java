package com.logitrack.logitrack_backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "categorias")
public class Categoria {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_categoria")
  private Long idCategoria;

  private String nombre;

  public Long getIdCategoria() {
    return idCategoria;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }
}