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

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private com.pasteleriamilsabores.backend.model.enums.TipoEntrega tipoEntrega;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private com.pasteleriamilsabores.backend.model.enums.HorarioEntrega horarioEntrega;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private com.pasteleriamilsabores.backend.model.enums.MetodoPago metodoPago;

    @Column(columnDefinition = "TEXT")
    private String notas;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private com.pasteleriamilsabores.backend.model.enums.EstadoBoleta estado;

    @Column(name = "fecha_entrega")
    private java.time.LocalDateTime fechaEntrega;

    @OneToMany(mappedBy = "boleta", cascade = CascadeType.ALL)
    @JsonManagedReference
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<DetalleBoleta> detalles;

    @Column(name = "comprobante_url")
    private String comprobanteUrl;

    @Column(name = "fecha_comprobante")
    private LocalDateTime fechaComprobante;
}
