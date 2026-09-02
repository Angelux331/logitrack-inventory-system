package com.logitrack.logitrack_backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "auditorias")
public class Auditoria {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_auditoria")
  private Long idAuditoria;

  @Enumerated(EnumType.STRING)
  @Column(name = "tipo_operacion")
  private TipoOperacion tipoOperacion;

  @Column(name = "fecha_hora")
  private LocalDateTime fechaHora;

  @ManyToOne
  @JoinColumn(name = "usuario_id")
  private Usuario usuario;

  private String entidad;

  @Column(name = "entidad_id")
  private Long entidadId;

  @Column(name = "valores_anteriores", columnDefinition = "JSON")
  private String valoresAnteriores;

  @Column(name = "valores_nuevos", columnDefinition = "JSON")
  private String valoresNuevos;


  public Long getIdAuditoria() {
    return idAuditoria;
  }

  public void setIdAuditoria(Long idAuditoria) {
    this.idAuditoria = idAuditoria;
  }

  public TipoOperacion getTipoOperacion() {
    return tipoOperacion;
  }

  public void setTipoOperacion(TipoOperacion tipoOperacion) {
    this.tipoOperacion = tipoOperacion;
  }

  public LocalDateTime getFechaHora() {
    return fechaHora;
  }

  public void setFechaHora(LocalDateTime fechaHora) {
    this.fechaHora = fechaHora;
  }

  public Usuario getUsuario() {
    return usuario;
  }

  public void setUsuario(Usuario usuario) {
    this.usuario = usuario;
  }

  public String getEntidad() {
    return entidad;
  }

  public void setEntidad(String entidad) {
    this.entidad = entidad;
  }

  public Long getEntidadId() {
    return entidadId;
  }

  public void setEntidadId(Long entidadId) {
    this.entidadId = entidadId;
  }

  public String getValoresAnteriores() {
    return valoresAnteriores;
  }

  public void setValoresAnteriores(String valoresAnteriores) {
    this.valoresAnteriores = valoresAnteriores;
  }

  public String getValoresNuevos() {
    return valoresNuevos;
  }

  public void setValoresNuevos(String valoresNuevos) {
    this.valoresNuevos = valoresNuevos;
  }
}