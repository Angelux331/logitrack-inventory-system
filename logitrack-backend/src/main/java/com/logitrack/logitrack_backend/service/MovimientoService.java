package com.logitrack.logitrack_backend.service;

import com.logitrack.logitrack_backend.model.*;
import com.logitrack.logitrack_backend.repository.InventarioRepository;
import com.logitrack.logitrack_backend.repository.MovimientoRepository;
import com.logitrack.logitrack_backend.repository.DetalleMovimientoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class MovimientoService {

  private final MovimientoRepository movimientoRepository;
  private final DetalleMovimientoRepository detalleRepository;
  private final InventarioRepository inventarioRepository;

  public MovimientoService(
    MovimientoRepository movimientoRepository,
    DetalleMovimientoRepository detalleRepository,
    InventarioRepository inventarioRepository) {

    this.movimientoRepository = movimientoRepository;
    this.detalleRepository = detalleRepository;
    this.inventarioRepository = inventarioRepository;
  }

  @Transactional
  public DetalleMovimiento registrarMovimiento(
    Movimiento movimiento,
    DetalleMovimiento detalle) {

    if (movimiento.getFecha() == null) {
      movimiento.setFecha(LocalDateTime.now());
    }

    Movimiento movimientoGuardado =
      movimientoRepository.save(movimiento);

    detalle.setMovimiento(movimientoGuardado);

    actualizarInventario(movimiento, detalle);

    return detalleRepository.save(detalle);
  }

  private void actualizarInventario(
    Movimiento movimiento,
    DetalleMovimiento detalle) {

    Long idProducto = detalle.getProducto().getIdProducto();
    Integer cantidad = detalle.getCantidad();

    if (cantidad <= 0) {
      throw new RuntimeException(
        "La cantidad debe ser mayor que cero"
      );
    }

    switch (movimiento.getTipo()) {

      case ENTRADA:

        Inventario inventarioEntrada =
          obtenerInventario(
            movimiento.getBodegaDestino().getIdBodega(),
            idProducto
          );

        inventarioEntrada.setStock(
          inventarioEntrada.getStock() + cantidad
        );

        inventarioRepository.save(inventarioEntrada);

        break;

      case SALIDA:

        Inventario inventarioSalida =
          obtenerInventario(
            movimiento.getBodegaOrigen().getIdBodega(),
            idProducto
          );

        if (inventarioSalida.getStock() < cantidad) {
          throw new RuntimeException(
            "Stock insuficiente"
          );
        }

        inventarioSalida.setStock(
          inventarioSalida.getStock() - cantidad
        );

        inventarioRepository.save(inventarioSalida);

        break;

      case TRANSFERENCIA:

        Inventario inventarioOrigen =
          obtenerInventario(
            movimiento.getBodegaOrigen().getIdBodega(),
            idProducto
          );

        if (inventarioOrigen.getStock() < cantidad) {
          throw new RuntimeException(
            "Stock insuficiente en la bodega de origen"
          );
        }

        Inventario inventarioDestino =
          obtenerInventario(
            movimiento.getBodegaDestino().getIdBodega(),
            idProducto
          );

        inventarioOrigen.setStock(
          inventarioOrigen.getStock() - cantidad
        );

        inventarioDestino.setStock(
          inventarioDestino.getStock() + cantidad
        );

        inventarioRepository.save(inventarioOrigen);
        inventarioRepository.save(inventarioDestino);

        break;
    }
  }

  private Inventario obtenerInventario(
    Long idBodega,
    Long idProducto) {

    return inventarioRepository
      .findByBodega_IdBodegaAndProducto_IdProducto(
        idBodega,
        idProducto
      )
      .orElseThrow(() ->
        new RuntimeException(
          "No existe inventario para esa bodega y producto"
        )
      );
  }
}