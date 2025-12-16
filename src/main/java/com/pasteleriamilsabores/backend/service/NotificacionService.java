package com.pasteleriamilsabores.backend.service;

import com.pasteleriamilsabores.backend.dto.NotificacionDTO;
import com.pasteleriamilsabores.backend.exception.ResourceNotFoundException;
import com.pasteleriamilsabores.backend.model.Notificacion;
import com.pasteleriamilsabores.backend.model.Usuario;
import com.pasteleriamilsabores.backend.model.enums.TipoNotificacion;
import com.pasteleriamilsabores.backend.repository.NotificacionRepository;
import com.pasteleriamilsabores.backend.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificacionService {

    private final NotificacionRepository notificacionRepository;
    private final UsuarioRepository usuarioRepository;

    public List<NotificacionDTO> listarPorUsuario(Long usuarioId) {
        return notificacionRepository.findByUsuarioIdOrderByFechaCreacionDesc(usuarioId)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public List<NotificacionDTO> listarNoLeidasPorUsuario(Long usuarioId) {
        return notificacionRepository.findByUsuarioIdAndLeidaFalseOrderByFechaCreacionDesc(usuarioId)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public long contarNoLeidas(Long usuarioId) {
        return notificacionRepository.countByUsuarioIdAndLeidaFalse(usuarioId);
    }

    @Transactional
    public NotificacionDTO marcarComoLeida(long notificacionId) {
        Notificacion notificacion = notificacionRepository.findById(notificacionId)
                .orElseThrow(() -> new ResourceNotFoundException("Notificación no encontrada"));
        notificacion.setLeida(true);
        return convertirADTO(notificacionRepository.save(notificacion));
    }

    @Transactional
    public void marcarTodasComoLeidas(long usuarioId) {
        List<Notificacion> noLeidas = notificacionRepository.findByUsuarioIdAndLeidaFalseOrderByFechaCreacionDesc(usuarioId);
        noLeidas.forEach(n -> n.setLeida(true));
        notificacionRepository.saveAll(noLeidas);
    }

    @Transactional
    public void eliminar(long notificacionId) {
        if (!notificacionRepository.existsById(notificacionId)) {
            throw new ResourceNotFoundException("Notificación no encontrada");
        }
        notificacionRepository.deleteById(notificacionId);
    }

    @Transactional
    public NotificacionDTO crearNotificacion(long usuarioId, String titulo, String mensaje, TipoNotificacion tipo, Long boletaId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        Notificacion notificacion = new Notificacion();
        notificacion.setUsuario(usuario);
        notificacion.setTitulo(titulo);
        notificacion.setMensaje(mensaje);
        notificacion.setTipo(tipo);
        notificacion.setLeida(false);
        notificacion.setBoletaId(boletaId);

        return convertirADTO(notificacionRepository.save(notificacion));
    }

    private NotificacionDTO convertirADTO(Notificacion notificacion) {
        return new NotificacionDTO(
                notificacion.getId(),
                notificacion.getUsuario().getId(),
                notificacion.getTitulo(),
                notificacion.getMensaje(),
                notificacion.getTipo(),
                notificacion.getLeida(),
                notificacion.getFechaCreacion(),
                notificacion.getBoletaId()
        );
    }
}
