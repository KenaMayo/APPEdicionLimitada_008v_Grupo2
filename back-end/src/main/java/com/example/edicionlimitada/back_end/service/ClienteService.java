package com.example.edicionlimitada.back_end.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.edicionlimitada.back_end.model.Cliente;
import com.example.edicionlimitada.back_end.repository.ClienteRepository;

@Service
public class ClienteService {

    private final PasswordEncoder passwordEncoder;

    private final ClienteRepository repo;

    public ClienteService(ClienteRepository repo, PasswordEncoder passwordEncoder){
        this.repo = repo;
        this.passwordEncoder = passwordEncoder;
    }

    //get todos
    public List<Cliente> getAll(){
        return repo.findAll();
    }

    //buscar id
    public Cliente getById(String id){
        return repo.findById(id).orElse(null);
    }

    //crear cliente
    public Cliente createCliente (Cliente c){
        c.setEmail(c.getEmail().trim().toLowerCase());
        c.setContrasena(passwordEncoder.encode(c.getContrasena().trim()));
        return repo.save(c);
    }

    //editar cliente
    public Cliente updateCliente(String id, Cliente c) {
    Cliente original = repo.findById(id).orElse(null);
    if (original == null) return null;

    // Mantener el email igual en formato
    if (c.getEmail() != null) {
        original.setEmail(c.getEmail().trim().toLowerCase());
    }

    original.setNombre(c.getNombre());
    original.setRegion(c.getRegion());
    original.setComuna(c.getComuna());

    // Si el cliente envía nueva contraseña → encriptar
    if (c.getContrasena() != null && !c.getContrasena().isBlank()) {
        original.setContrasena(passwordEncoder.encode(c.getContrasena().trim()));
    }

    return repo.save(original);
}


    public void deleteCliente(String id){
        repo.deleteById(id);
    }

    public Optional<Cliente> findByEmail(String email){
    return repo.findByEmail(email);
}


}
