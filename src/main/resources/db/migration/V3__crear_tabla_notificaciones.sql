-- Crear tabla de notificaciones
CREATE TABLE notificaciones (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    titulo VARCHAR(100) NOT NULL,
    mensaje TEXT NOT NULL,
    tipo VARCHAR(20) NOT NULL,
    leida BOOLEAN DEFAULT FALSE NOT NULL,
    fecha_creacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    boleta_id BIGINT,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE
);

-- Índices para mejorar performance
CREATE INDEX idx_notificaciones_usuario ON notificaciones(usuario_id);
CREATE INDEX idx_notificaciones_leida ON notificaciones(usuario_id, leida);
