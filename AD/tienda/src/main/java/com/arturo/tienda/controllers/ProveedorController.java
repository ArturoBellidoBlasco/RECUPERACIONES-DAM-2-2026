package com.arturo.tienda.controllers;

import com.arturo.tienda.models.Producto;
import com.arturo.tienda.models.Proveedor;
import com.arturo.tienda.service.ProductoService;
import com.arturo.tienda.service.ProveedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/proveedores")
public class ProveedorController {

    @Autowired
    private ProveedorService proveedorService;

    @GetMapping
    public ResponseEntity<List<Proveedor>> obtenerTodos(){
        return ResponseEntity.ok(proveedorService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Proveedor>> obtenerPorId(@PathVariable String id){
        return ResponseEntity.ok(proveedorService.getById(id));
    }

    @PostMapping()
    public ResponseEntity<Proveedor> guardarProveedor(@RequestBody Proveedor proveedor){
        return ResponseEntity.status(HttpStatus.CREATED).body(proveedorService.saveProveedor(proveedor));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Proveedor> actualizarProveedor(@RequestBody Proveedor proveedor, @PathVariable String id){
        return ResponseEntity.status(HttpStatus.CREATED).body(proveedorService.updateProveedor(id, proveedor));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Proveedor> eliminarProveedor(@PathVariable String id){
        proveedorService.deleteProveedor(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
