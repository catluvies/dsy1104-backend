package com.pasteleriamilsabores.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "boletas")
@Data
@NoArgsConstructor
public class Boleta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @CreationTimestamp
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_expiracion")
    private LocalDateTime fechaExpiracion;

    @Column(nullable = false)
    private Double total;

    @Column(columnDefinition = "TEXT")
    private String direccionEntrega;

    @Column(name = "comuna_entrega", length = 100)
    private String comunaEntrega;

    @Column(name = "region_entrega", length = 100)
    private String regionEntrega;

    private Double subtotal;

    private Double costoEnvio;

    private String metodoPago;

    @Column(columnDefinition = "TEXT")
    private String notas;

    @Column(length = 50, nullable = false)
    private String estado;

    @Column(name = "fecha_entrega")
    private java.time.LocalDateTime fechaEntrega;

    @OneToMany(mappedBy = "boleta", cascade = CascadeType.ALL)
    @JsonManagedReference
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<DetalleBoleta> detalles;
}
