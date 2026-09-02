package com.logitrack.logitrack_backend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {

  private static final String SECRET =
    "LogiTrackSuperSecretKey2026JWT123456789";

  private final SecretKey key =
    Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));

  public String generarToken(String email, String rol, Long idUsuario) {

    return Jwts.builder()
      .subject(email)
      .claim("rol", rol)
      .claim("id", idUsuario)
      .issuedAt(new Date())
      .expiration(new Date(System.currentTimeMillis() + 86400000))
      .signWith(key)
      .compact();
  }

  public Claims extraerClaims(String token) {

    return Jwts.parser()
      .verifyWith(key)
      .build()
      .parseSignedClaims(token)
      .getPayload();
  }

  public String extraerEmail(String token) {
    return extraerClaims(token).getSubject();
  }

  public String extraerRol(String token) {
    return extraerClaims(token).get("rol", String.class);
  }

  public Long extraerId(String token) {
    return extraerClaims(token).get("id", Long.class);
  }

  public boolean esValido(String token) {
    try {
      extraerClaims(token);
      return true;
    } catch (Exception e) {
      return false;
    }
  }
}