package com.arturo.tienda.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

@Document(collection = "productos")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Producto {

    @Id
    private String id;

    private String nombre;
    private String categoria;
    private String marca;
    private Double precio;
    private Integer stock;
    private Map<String, Object> especificaciones;
    private List<Opinion> opiniones;
    private List<String> tags;
}
