package com.arturo.tienda.models;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Opinion {
    private String usuario;
    private Integer calificacion;
    private String comentario;
}
