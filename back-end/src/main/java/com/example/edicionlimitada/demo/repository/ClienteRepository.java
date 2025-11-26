package com.example.edicionlimitada.demo.repository;


import com.example.edicionlimitada.demo.model.Cliente;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ClienteRepository extends MongoRepository<Cliente, String>{

    Optional<Cliente> findByEmail(String email);

}
