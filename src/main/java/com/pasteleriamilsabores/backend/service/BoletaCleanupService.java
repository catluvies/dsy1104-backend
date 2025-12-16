package com.pasteleriamilsabores.backend.service;

import com.pasteleriamilsabores.backend.model.Boleta;
import com.pasteleriamilsabores.backend.model.DetalleBoleta;
import com.pasteleriamilsabores.backend.model.Producto;
import com.pasteleriamilsabores.backend.model.enums.EstadoBoleta;
import com.pasteleriamilsabores.backend.model.enums.TipoNotificacion;
import com.pasteleriamilsabores.backend.repository.BoletaRepository;
import com.pasteleriamilsabores.backend.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BoletaCleanupService {

    private final BoletaRepository boletaRepository;
    private final ProductoRepository productoRepository;
    private final NotificacionService notificacionService;

    // Se ejecuta cada hora (3600000 ms)
    @Scheduled(fixedRate = 3600000)
    @Transactional
    public void cancelarBoletasPendientesAntiguas() {
        log.info("Iniciando limpieza de boletas pendientes antiguas...");

        // Buscar boletas PENDIENTES cuya fecha de expiración ya pasó
        LocalDateTime ahora = LocalDateTime.now();

        List<Boleta> boletasVencidas = boletaRepository.findByEstadoAndFechaExpiracionBefore(
                EstadoBoleta.PENDIENTE, ahora);

        for (Boleta boleta : boletasVencidas) {
            log.info("Cancelando boleta ID: {} por falta de pago (creada: {})", boleta.getId(),
                    boleta.getFechaCreacion());

            // 1. Devolver Stock
            for (DetalleBoleta detalle : boleta.getDetalles()) {
                Producto producto = detalle.getProducto();
                producto.setStock(producto.getStock() + detalle.getCantidad());
                productoRepository.save(producto);
                log.debug("Stock devuelto para producto ID {}: +{}", producto.getId(), detalle.getCantidad());
            }

            // 2. Cambiar estado a CANCELADA
            boleta.setEstado(EstadoBoleta.CANCELADA);
            String notasActuales = boleta.getNotas() != null ? boleta.getNotas() : "";
            boleta.setNotas(notasActuales + " [AUTO-CANCELADA: Expiró tiempo de pago]");
            boletaRepository.save(boleta);

            // 3. Notificar al cliente
            notificacionService.crearNotificacion(
                    boleta.getUsuario().getId(),
                    "Pedido cancelado",
                    "Tu pedido #" + boleta.getId() + " ha sido cancelado automáticamente por expiración del tiempo de pago.",
                    TipoNotificacion.ADVERTENCIA,
                    boleta.getId()
            );
        }

        if (!boletasVencidas.isEmpty()) {
            log.info("Se cancelaron {} boletas vencidas.", boletasVencidas.size());
        } else {
            log.info("No se encontraron boletas vencidas.");
        }
    }
}
