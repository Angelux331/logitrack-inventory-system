package com.logitrack.logitrack_backend.controller;

import com.logitrack.logitrack_backend.model.Auditoria;
import com.logitrack.logitrack_backend.service.AuditoriaService;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auditorias")
public class AuditoriaController {

  private final AuditoriaService auditoriaService;

  public AuditoriaController(AuditoriaService auditoriaService) {
    this.auditoriaService = auditoriaService;
  }

  @GetMapping
  public List<Auditoria> obtenerTodas() {
    return auditoriaService.obtenerTodas();
  }

  @PostMapping
  public Auditoria registrar(@RequestBody Auditoria auditoria) {
    return auditoriaService.registrarAuditoria(auditoria);
  }
}