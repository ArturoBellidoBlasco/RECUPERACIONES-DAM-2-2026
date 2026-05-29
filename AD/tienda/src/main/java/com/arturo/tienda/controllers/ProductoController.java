package com.arturo.tienda.controllers;

import com.arturo.tienda.models.Producto;
import com.arturo.tienda.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @GetMapping
    public ResponseEntity<List<Producto>> obtenerTodos(){
        return ResponseEntity.ok(productoService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Producto>> obtenerPorId(@PathVariable String id){
        return ResponseEntity.ok(productoService.getById(id));
    }

    @PostMapping()
    public ResponseEntity<Producto> guardarProducto(@RequestBody Producto producto){
        return ResponseEntity.status(HttpStatus.CREATED).body(productoService.saveProducto(producto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizarProducto(@RequestBody Producto producto, @PathVariable String id){
        return ResponseEntity.status(HttpStatus.CREATED).body(productoService.updateProducto(id, producto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Producto> eliminarProducto(@PathVariable String id){
        productoService.deleteProducto(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
