package com.pasteleriamilsabores.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

@Entity
@Table(name = "productos")
@Data
@NoArgsConstructor
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, length = 50)
    private String sku;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(length = 1000)
    private String descripcion;

    @Column(length = 255)
    private String imagenUrl;

    @ManyToOne
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    @Column(nullable = false)
    private Double precio;

    @Column(nullable = false)
    private Integer stock;

    @Column(columnDefinition = "TEXT")
    private String ingredientes;

    private String formatoVenta;
    private Integer porciones;
    private String peso;
    private String duracion;
    private String tiempoPreparacion;

    @Column(length = 500)
    private String notas;

    @Column(nullable = false)
    private Boolean activo;

    @CreationTimestamp
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private java.time.LocalDateTime fechaCreacion;

    @org.hibernate.annotations.UpdateTimestamp
    @Column(name = "fecha_modificacion")
    private java.time.LocalDateTime fechaModificacion;

    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL)
    @JsonIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<DetalleBoleta> detallesBoleta;
}
