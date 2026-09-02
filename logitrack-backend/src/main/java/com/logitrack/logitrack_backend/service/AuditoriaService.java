package com.logitrack.logitrack_backend.service;

import com.logitrack.logitrack_backend.model.Auditoria;
import com.logitrack.logitrack_backend.repository.AuditoriaRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AuditoriaService {

  private final AuditoriaRepository auditoriaRepository;

  public AuditoriaService(AuditoriaRepository auditoriaRepository) {
    this.auditoriaRepository = auditoriaRepository;
  }

  public Auditoria registrarAuditoria(Auditoria auditoria) {

    if (auditoria.getFechaHora() == null) {
      auditoria.setFechaHora(LocalDateTime.now());
    }

    return auditoriaRepository.save(auditoria);
  }

  public List<Auditoria> obtenerTodas() {
    return auditoriaRepository.findAll();
  }
}