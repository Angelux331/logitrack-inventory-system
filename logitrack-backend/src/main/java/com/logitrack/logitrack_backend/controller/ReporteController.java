package com.logitrack.logitrack_backend.controller;

import com.logitrack.logitrack_backend.service.ReporteService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/reportes")
public class ReporteController {

  private final ReporteService reporteService;

  public ReporteController(ReporteService reporteService) {
    this.reporteService = reporteService;
  }

  @GetMapping("/resumen")
  public Map<String, Object> obtenerResumen() {
    return reporteService.obtenerResumen();
  }
}