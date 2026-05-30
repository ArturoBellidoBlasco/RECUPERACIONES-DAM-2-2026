package com.arturo.tienda.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "proveedor")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Proveedor {
    @Id
    private String id;
    private String nombre;
    private String pais;
    private String telefono;
}
