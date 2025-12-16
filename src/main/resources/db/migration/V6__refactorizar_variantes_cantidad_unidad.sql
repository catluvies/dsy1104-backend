-- Migración: Refactorizar variantes de productos
-- Cambio de modelo: nombre + porciones -> cantidad + unidad_medida

-- 1. Agregar columna unidad_medida (por defecto PORCION para datos existentes de tortas)
ALTER TABLE producto_variantes ADD COLUMN unidad_medida VARCHAR(20) NOT NULL DEFAULT 'PORCION';

-- 2. Renombrar columna porciones a cantidad
ALTER TABLE producto_variantes CHANGE COLUMN porciones cantidad INT NOT NULL;

-- 3. Eliminar columna nombre (ya no se usa, se genera dinámicamente)
ALTER TABLE producto_variantes DROP COLUMN nombre;
