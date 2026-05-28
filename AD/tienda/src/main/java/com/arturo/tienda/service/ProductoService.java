package com.arturo.tienda.service;

import com.arturo.tienda.exceptions.ProductoNotFoundException;
import com.arturo.tienda.exceptions.ProductoYaExistenteException;
import com.arturo.tienda.models.Producto;
import com.arturo.tienda.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    public List<Producto> getAll() {
        return productoRepository.findAll();
    }

    public Optional<Producto> getById(String id) {
       Optional<Producto> producto = Optional.of(productoRepository.findById(id).orElseThrow(() -> new ProductoNotFoundException("Producto con ID: " + id + " no encontrado.")));
       return producto;
    }

    public Producto saveProducto(Producto producto) {

        Optional<Producto> producto1 = productoRepository.findById(producto.getId());
        if (producto1.isPresent()) {
            throw new ProductoYaExistenteException("Producto con ID: " + producto.getId() + " ya existe en la BD");
        }

        return productoRepository.save(producto);
    }
}
