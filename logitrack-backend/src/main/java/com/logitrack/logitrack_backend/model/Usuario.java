package com.logitrack.logitrack_backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;

@Entity
@Table(name = "usuarios")
public class Usuario {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_usuario")
  private Long idusuario;

  private String nombre;

  private String apellido;

  private String email;

  private String password;

  private String rol;

  private Boolean activo;

  public Long getIdusuario() {
    return idusuario;
  }

  public String getNombre() {
    return nombre;
  }

  public String getApellido() {
    return apellido;
  }

  public String getEmail() {
    return email;
  }

  public String getPassword() {
    return password;
  }

  public String getRol() {
    return rol;
  }

  public Boolean getActivo() {
    return activo;
  }

  public void setIdusuario(Long idusuario) {
    this.idusuario = idusuario;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public void setApellido(String apellido) {
    this.apellido = apellido;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public void setRol(String rol) {
    this.rol = rol;
  }

  public void setActivo(Boolean activo) {
    this.activo = activo;
  }
}

