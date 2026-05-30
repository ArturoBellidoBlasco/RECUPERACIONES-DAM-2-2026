package com.arturo.tienda.service;


import com.arturo.tienda.exceptions.ProveedorNotFoundException;
import com.arturo.tienda.exceptions.ProveedorYaExistenteException;
import com.arturo.tienda.models.Proveedor;
import com.arturo.tienda.repository.ProveedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProveedorService {

    @Autowired
    private ProveedorRepository proveedorRepository;

    public List<Proveedor> getAll() {
        return proveedorRepository.findAll();
    }

    public Optional<Proveedor> getById(String id) {
       Optional<Proveedor> proveedor = Optional.of(proveedorRepository.findById(id).orElseThrow(() -> new ProveedorNotFoundException("Proveedor con ID: " + id + " no encontrado.")));
       return proveedor;
    }

    public Proveedor saveProveedor(Proveedor proveedor) {

        Optional<Proveedor> proveedor1 = proveedorRepository.findById(proveedor.getId());
        if (proveedor1.isPresent()) {
            throw new ProveedorYaExistenteException("Proveedor con ID: " + proveedor.getId() + " ya existe en la BD");
        }

        return proveedorRepository.save(proveedor);
    }

    public Proveedor updateProveedor(String id, Proveedor proveedor) {
        Optional<Proveedor> proveedor1 = proveedorRepository.findById(id);
        if (proveedor1.isPresent()) {
            Proveedor proveedor2 = proveedor1.get();
            proveedor2.setNombre(proveedor.getNombre());
            proveedor2.setPais(proveedor.getPais());
            proveedor2.setTelefono(proveedor.getTelefono());
            proveedorRepository.save(proveedor2);
            return proveedor2;
        }
        throw new ProveedorNotFoundException("Proveedor con ID: " + id + " no encontrado.");
    }

    public void deleteProveedor(String id) {
        Optional<Proveedor> proveedor = proveedorRepository.findById(id);
        if (proveedor.isPresent()) {
            proveedorRepository.delete(proveedor.get());
        }
        throw new ProveedorNotFoundException("Producto con ID: " + id + " no encontrado.");
    }
}
