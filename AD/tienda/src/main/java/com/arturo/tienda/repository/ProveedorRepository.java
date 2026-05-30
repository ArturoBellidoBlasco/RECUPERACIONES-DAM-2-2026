package com.arturo.tienda.repository;

import com.arturo.tienda.models.Producto;
import com.arturo.tienda.models.Proveedor;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProveedorRepository extends MongoRepository<Proveedor, String> {

}
