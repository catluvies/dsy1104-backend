package com.pasteleriamilsabores.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "productos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(length = 1000)
    private String descripcion;

    @Column(length = 255)
    private String imagenUrl;

    @ManyToOne
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    @Column(columnDefinition = "TEXT")
    private String tamaños;

    @Column(length = 500)
    private String notas;

    @Column(nullable = false)
    private Boolean activo;

    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL)
    private List<DetalleBoleta> detallesBoleta;
}
