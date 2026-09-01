package com.logitrack.logitrack_backend.controller;

import com.logitrack.logitrack_backend.model.Usuario;
import com.logitrack.logitrack_backend.repository.UsuarioRepository;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:5500")
public class AuthController {

  private final UsuarioRepository usuarioRepository;

  public AuthController(UsuarioRepository usuarioRepository) {
    this.usuarioRepository = usuarioRepository;
  }

  @PostMapping("/login")
  public Map<String, String> login(@RequestBody Map<String, String> datos) {

    String email = datos.get("email");
    String password = datos.get("password");

    Usuario usuario = usuarioRepository.findByEmail(email)
      .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

    if (!usuario.getPassword().equals(password)) {
      throw new RuntimeException("Contraseña incorrecta");
    }

    return Map.of(
      "token", "token-temporal",
      "nombre", usuario.getNombre()
    );
  }
}