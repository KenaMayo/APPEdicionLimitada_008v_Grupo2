package com.example.edicionlimitada.demo.model;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;




@Document(collection = "cliente")
@Getter
@Setter
public class Cliente {
    
    @Id
    @JsonProperty("_id")
    private String id;
    private String nombre;
    private String email;
    private String contrasena;
    private String comuna;
    private String region;

}
