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

    public Producto updateProducto(String id, Producto producto) {
        Optional<Producto> producto1 = productoRepository.findById(id);
        if (producto1.isPresent()) {
            Producto producto2 = producto1.get();
            producto2.setNombre(producto.getNombre());
            producto2.setPrecio(producto.getPrecio());
            producto2.setCategoria(producto.getCategoria());
            producto2.setMarca(producto.getMarca());
            producto2.setStock(producto.getStock());
            producto2.setEspecificaciones(producto.getEspecificaciones());
            producto2.setOpiniones(producto.getOpiniones());
            producto2.setTags(producto.getTags());
            productoRepository.save(producto2);
            return producto2;
        }
        throw new ProductoNotFoundException("Producto con ID: " + id + " no encontrado.");
    }

    public void deleteProducto(String id) {
        Optional<Producto> producto = productoRepository.findById(id);
        if (producto.isPresent()) {
            productoRepository.delete(producto.get());
        }
        throw  new ProductoNotFoundException("Producto con ID: " + id + " no encontrado.");
    }
}
