package com.pasteleriamilsabores.backend.model;

import com.pasteleriamilsabores.backend.model.enums.UnidadMedida;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "producto_variantes")
@Data
@NoArgsConstructor
public class ProductoVariante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    @Column(nullable = false)
    private Integer cantidad;

    @Enumerated(EnumType.STRING)
    @Column(name = "unidad_medida", nullable = false, length = 20)
    private UnidadMedida unidadMedida;

    @Column(nullable = false)
    private Double precio;

    @Column(nullable = false)
    private Integer stock;

    @Column(nullable = false)
    private Boolean activo = true;

    /**
     * Genera el nombre para mostrar basado en cantidad y unidad de medida.
     * Ej: "Para 12 personas", "750ml", "6 unidades"
     */
    public String getNombreDisplay() {
        if (unidadMedida == null || cantidad == null) {
            return "";
        }
        return switch (unidadMedida) {
            case PORCION -> "Para " + cantidad + " personas";
            case ML -> cantidad + "ml";
            case L -> cantidad + "L";
            case G -> cantidad + "g";
            case KG -> cantidad + "kg";
            case UNIDAD -> cantidad + (cantidad == 1 ? " unidad" : " unidades");
            case DOCENA -> cantidad + (cantidad == 1 ? " docena" : " docenas");
        };
    }
}
