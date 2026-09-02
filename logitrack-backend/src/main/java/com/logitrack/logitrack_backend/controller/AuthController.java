package com.logitrack.logitrack_backend.controller;

import com.logitrack.logitrack_backend.model.Usuario;
import com.logitrack.logitrack_backend.repository.UsuarioRepository;
import com.logitrack.logitrack_backend.security.JwtService;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = {
  "http://localhost:5500",
  "http://127.0.0.1:5500"
})
public class AuthController {

  private final UsuarioRepository usuarioRepository;
  private final JwtService jwtService;
  private final PasswordEncoder passwordEncoder;

  public AuthController(
    UsuarioRepository usuarioRepository,
    JwtService jwtService,
    PasswordEncoder passwordEncoder) {

    this.usuarioRepository = usuarioRepository;
    this.jwtService = jwtService;
    this.passwordEncoder = passwordEncoder;
  }

  @PostMapping("/login")
  public Map<String, String> login(
    @RequestBody Map<String, String> datos) {

    String email = datos.get("email");
    String password = datos.get("password");

    Usuario usuario = usuarioRepository.findByEmail(email)
      .orElseThrow(() ->
        new RuntimeException("Usuario no encontrado"));

    String passwordGuardada = usuario.getPassword();

    boolean contraseñaCorrecta;

    // Si ya está cifrada con BCrypt
    if (passwordGuardada.startsWith("$2a$")
      || passwordGuardada.startsWith("$2b$")
      || passwordGuardada.startsWith("$2y$")) {

      contraseñaCorrecta =
        passwordEncoder.matches(
          password,
          passwordGuardada
        );

    } else {

      // Compatibilidad temporal con contraseñas antiguas
      contraseñaCorrecta =
        passwordGuardada.equals(password);

      // Si coincide, la convertimos inmediatamente a BCrypt
      if (contraseñaCorrecta) {

        usuario.setPassword(
          passwordEncoder.encode(password)
        );

        usuarioRepository.save(usuario);
      }
    }

    if (!contraseñaCorrecta) {
      throw new RuntimeException(
        "Contraseña incorrecta"
      );
    }

    String token = jwtService.generarToken(
      usuario.getEmail(),
      usuario.getRol().toString(),
      Long.valueOf(
        String.valueOf(usuario.getIdusuario())
      )
    );

    return Map.of(
      "token", token,
      "nombre", usuario.getNombre(),
      "idusuario",
      String.valueOf(usuario.getIdusuario()),
      "rol",
      usuario.getRol().toString()
    );
  }

  @PostMapping("/register")
  public Map<String, String> register(
    @RequestBody Usuario usuario) {

    if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
      throw new RuntimeException("El correo ya está registrado");
    }

    usuario.setPassword(
      passwordEncoder.encode(usuario.getPassword())
    );

    Usuario guardado = usuarioRepository.save(usuario);

    String token = jwtService.generarToken(
      guardado.getEmail(),
      guardado.getRol().toString(),
      Long.valueOf(
        String.valueOf(guardado.getIdusuario())
      )
    );

    return Map.of(
      "token", token,
      "nombre", guardado.getNombre(),
      "idusuario",
      String.valueOf(guardado.getIdusuario()),
      "rol",
      guardado.getRol().toString()
    );
  }
}