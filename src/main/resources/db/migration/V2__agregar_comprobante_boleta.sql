-- Agregar campos para comprobante de transferencia
ALTER TABLE boletas ADD COLUMN comprobante_url VARCHAR(500);
ALTER TABLE boletas ADD COLUMN fecha_comprobante DATETIME;
