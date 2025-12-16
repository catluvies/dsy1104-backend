package com.pasteleriamilsabores.backend.dto;

import com.pasteleriamilsabores.backend.model.enums.TipoNotificacion;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Datos de una notificación")
public class NotificacionDTO {

    @Schema(description = "ID de la notificación", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "ID del usuario", example = "1")
    private Long usuarioId;

    @Schema(description = "Título de la notificación", example = "Pedido confirmado")
    private String titulo;

    @Schema(description = "Mensaje de la notificación", example = "Tu pedido #123 ha sido confirmado")
    private String mensaje;

    @Schema(description = "Tipo de notificación", example = "EXITO")
    private TipoNotificacion tipo;

    @Schema(description = "Si la notificación ha sido leída", example = "false")
    private Boolean leida;

    @Schema(description = "Fecha de creación", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime fechaCreacion;

    @Schema(description = "ID de la boleta relacionada (opcional)", example = "123")
    private Long boletaId;
}
