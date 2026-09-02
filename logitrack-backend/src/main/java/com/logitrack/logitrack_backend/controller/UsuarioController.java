package com.logitrack.logitrack_backend.controller;

import com.logitrack.logitrack_backend.model.Usuario;
import com.logitrack.logitrack_backend.repository.UsuarioRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin(origins = {
  "http://localhost:5500",
  "http://127.0.0.1:5500"
})
public class UsuarioController {

  private final UsuarioRepository usuarioRepository;
  private final PasswordEncoder passwordEncoder;

  public UsuarioController(
    UsuarioRepository usuarioRepository,
    PasswordEncoder passwordEncoder) {

    this.usuarioRepository = usuarioRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @GetMapping
  @PreAuthorize("hasRole('ADMIN')")
  public List<Usuario> listarUsuarios() {
    return usuarioRepository.findAll();
  }

  @PostMapping
  @PreAuthorize("hasRole('ADMIN')")
  public Usuario crearUsuario(@RequestBody Usuario usuario) {

    usuario.setPassword(
      passwordEncoder.encode(usuario.getPassword())
    );

    return usuarioRepository.save(usuario);
  }
}