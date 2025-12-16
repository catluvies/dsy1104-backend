package com.pasteleriamilsabores.backend.repository;

import com.pasteleriamilsabores.backend.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    List<Producto> findByCategoriaId(Long categoriaId);

    List<Producto> findByActivoTrue();

    // Productos activos cuya categoría también esté activa
    List<Producto> findByActivoTrueAndCategoriaActivaTrue();

    // Productos por categoría, solo si el producto está activo
    List<Producto> findByCategoriaIdAndActivoTrue(Long categoriaId);
}
