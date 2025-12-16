-- Agregar campo imagen_url a categorias
ALTER TABLE categorias ADD COLUMN imagen_url VARCHAR(255);

-- Crear tabla de variantes de productos
CREATE TABLE producto_variantes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    producto_id BIGINT NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    porciones INT NOT NULL,
    precio DOUBLE NOT NULL,
    stock INT NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT fk_variante_producto FOREIGN KEY (producto_id) REFERENCES productos(id) ON DELETE CASCADE
);

-- Índice para buscar variantes por producto
CREATE INDEX idx_variante_producto ON producto_variantes(producto_id);

-- Agregar campo variante_id a detalle_boleta (opcional, para soportar compras con variantes)
ALTER TABLE detalle_boleta ADD COLUMN variante_id BIGINT;
ALTER TABLE detalle_boleta ADD CONSTRAINT fk_detalle_variante
    FOREIGN KEY (variante_id) REFERENCES producto_variantes(id) ON DELETE SET NULL;
