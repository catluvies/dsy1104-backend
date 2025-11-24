package com.pasteleriamilsabores.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
@AllArgsConstructor
@Builder
public class Boleta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime fecha;

    @Column(nullable = false)
    private Double total;

    @Column(columnDefinition = "TEXT")
    private String direccionEntrega;

    @Column(length = 100)
    private String region;

    private Double subtotal;

    private Double costoEnvio;

    private String metodoPago;

    @Column(columnDefinition = "TEXT")
    private String notasAdicionales;

    @Column(length = 50, nullable = false)
    private String estado;

    @OneToMany(mappedBy = "boleta", cascade = CascadeType.ALL)
    @JsonManagedReference
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<DetalleBoleta> detalles;
}
