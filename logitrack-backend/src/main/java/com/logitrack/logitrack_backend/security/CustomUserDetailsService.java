package com.logitrack.logitrack_backend.security;

import com.logitrack.logitrack_backend.model.Usuario;
import com.logitrack.logitrack_backend.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

  private final UsuarioRepository usuarioRepository;

  public CustomUserDetailsService(UsuarioRepository usuarioRepository) {
    this.usuarioRepository = usuarioRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String email)
    throws UsernameNotFoundException {

    Usuario usuario = usuarioRepository.findByEmail(email)
      .orElseThrow(() ->
        new UsernameNotFoundException("Usuario no encontrado"));

    return User.builder()
      .username(usuario.getEmail())
      .password(usuario.getPassword())
      .roles(usuario.getRol().toString())
      .build();
  }
}