package com.example.edicionlimitada.demo.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.edicionlimitada.demo.model.Cliente;
import com.example.edicionlimitada.demo.service.ClienteService;

@RestController
@RequestMapping("/clientes")
@CrossOrigin(origins = "*") //llamadas desde android
public class ClienteController {


    private final ClienteService service;

    public ClienteController(ClienteService service){
        this.service = service;
    }

    @GetMapping
    public List<Cliente> getAll(){
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Cliente getById(@PathVariable String id) {
        return service.getById(id);
    }

    @PostMapping
    public Cliente create(@RequestBody Cliente c){
        return service.createCliente(c);
    }

    @PutMapping("/{id}")
    public Cliente update(@PathVariable String id, @RequestBody Cliente c){
        return service.updateCliente(id, c);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id){
        service.deleteCliente(id);
    }


    @GetMapping("/search")
public ResponseEntity<?> findByEmail(@RequestParam String email) {
    email = email.trim().toLowerCase();

    return service.findByEmail(email)
            .<ResponseEntity<?>>map(cliente -> ResponseEntity.ok(cliente))
            .orElseGet(() -> {
                // Map manual (compatible con Java 8)
                java.util.Map<String, String> error = new java.util.HashMap<>();
                error.put("error", "Cliente no encontrado");
                return ResponseEntity.status(404).body(error);
            });
}


}
