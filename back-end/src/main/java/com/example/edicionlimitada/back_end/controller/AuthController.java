package com.example.edicionlimitada.back_end.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.edicionlimitada.back_end.model.Cliente;
import com.example.edicionlimitada.back_end.service.AuthService;
import com.example.edicionlimitada.back_end.service.ClienteService;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;
    private final ClienteService clienteService;

    public AuthController(AuthService authService, ClienteService clienteService){
        this.authService = authService;
        this.clienteService = clienteService;
    }

    // Registro: crea usuario (ya hasheado en ClienteService.createCliente)
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Cliente cliente){
        Cliente creado = clienteService.createCliente(cliente);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    // Login móvil
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body){
        String email = body.getOrDefault("email", "").trim().toLowerCase();
        String contrasena = body.getOrDefault("contrasena", "").trim();

        return authService.login(email, contrasena)
                .<ResponseEntity<?>>map(u -> ResponseEntity.ok(u))
                .orElseGet(() ->
                        ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                .body(Map.of("error", "Credenciales incorrectas"))
                );
    }
}
