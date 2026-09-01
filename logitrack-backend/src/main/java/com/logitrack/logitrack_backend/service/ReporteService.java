package com.logitrack.logitrack_backend.service;

import com.logitrack.logitrack_backend.model.TipoMovimiento;
import com.logitrack.logitrack_backend.repository.BodegaRepository;
import com.logitrack.logitrack_backend.repository.ProductoRepository;
import com.logitrack.logitrack_backend.repository.InventarioRepository;
import com.logitrack.logitrack_backend.repository.MovimientoRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ReporteService {

  private final BodegaRepository bodegaRepository;
  private final ProductoRepository productoRepository;
  private final InventarioRepository inventarioRepository;
  private final MovimientoRepository movimientoRepository;

  public ReporteService(
    BodegaRepository bodegaRepository,
    ProductoRepository productoRepository,
    InventarioRepository inventarioRepository,
    MovimientoRepository movimientoRepository) {

    this.bodegaRepository = bodegaRepository;
    this.productoRepository = productoRepository;
    this.inventarioRepository = inventarioRepository;
    this.movimientoRepository = movimientoRepository;
  }

  public Map<String, Object> obtenerResumen() {

    Map<String, Object> resumen = new HashMap<>();
    resumen.put("totalBodegas", bodegaRepository.count());
    resumen.put("totalProductos", productoRepository.count());
    resumen.put("totalInventarios", inventarioRepository.count());
    resumen.put("totalMovimientos", movimientoRepository.count());
    resumen.put(
      "entradas",
      movimientoRepository.countByTipo(TipoMovimiento.ENTRADA)
    );

    resumen.put(
      "salidas",
      movimientoRepository.countByTipo(TipoMovimiento.SALIDA)
    );

    resumen.put(
      "transferencias",
      movimientoRepository.countByTipo(TipoMovimiento.TRANSFERENCIA)
    );

    return resumen;
  }
}