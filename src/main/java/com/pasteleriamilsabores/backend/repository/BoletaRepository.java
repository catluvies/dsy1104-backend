package com.pasteleriamilsabores.backend.repository;

import com.pasteleriamilsabores.backend.model.Boleta;
import com.pasteleriamilsabores.backend.model.enums.EstadoBoleta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoletaRepository extends JpaRepository<Boleta, Long> {

    List<Boleta> findByUsuarioId(Long usuarioId);

    int countByUsuarioId(Long usuarioId);

    List<Boleta> findByEstado(EstadoBoleta estado);

    List<Boleta> findByEstadoAndFechaExpiracionBefore(EstadoBoleta estado, java.time.LocalDateTime fecha);
}
