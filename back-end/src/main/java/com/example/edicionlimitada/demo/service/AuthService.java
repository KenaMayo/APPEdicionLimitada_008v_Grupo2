package com.example.edicionlimitada.demo.service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.edicionlimitada.demo.model.Cliente;
import com.example.edicionlimitada.demo.repository.ClienteRepository;

@Service
public class AuthService {

    private final ClienteRepository repo;
    private final PasswordEncoder passwordEncoder;

    public AuthService(ClienteRepository repo, PasswordEncoder passwordEncoder) {
        this.repo = repo;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<Cliente> login(String email, String rawPassword) {
        Optional<Cliente> opt = repo.findByEmail(email.toLowerCase().trim());
        if (opt.isEmpty()) return Optional.empty();

        Cliente cliente = opt.get();
        if (passwordEncoder.matches(rawPassword, cliente.getContrasena())) {
            cliente.setContrasena(null);
            return Optional.of(cliente);
        }

        return Optional.empty();
    }
}
