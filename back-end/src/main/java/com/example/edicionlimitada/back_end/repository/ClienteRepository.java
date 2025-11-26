package com.example.edicionlimitada.back_end.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.edicionlimitada.back_end.model.Cliente;

public interface ClienteRepository extends MongoRepository<Cliente, String>{

    Optional<Cliente> findByEmail(String email);

}
