-- V5__corregir_valores_enum_legacy.sql
-- Migración para corregir valores legacy de enums en productos

-- =====================================================
-- CORREGIR: condicion_conservacion
-- =====================================================
UPDATE productos SET condicion_conservacion = 'REFRIGERADO'
WHERE condicion_conservacion IN ('Mantener refrigerado', 'refrigerado', 'Refrigerado');

UPDATE productos SET condicion_conservacion = 'CONGELADO'
WHERE condicion_conservacion IN ('Mantener congelado', 'congelado', 'Congelado');

UPDATE productos SET condicion_conservacion = 'AMBIENTE'
WHERE condicion_conservacion IN ('Temperatura ambiente', 'ambiente', 'Ambiente', 'TEMPERATURA_AMBIENTE');

-- =====================================================
-- CORREGIR: unidad_medida
-- =====================================================
UPDATE productos SET unidad_medida = 'G'
WHERE unidad_medida IN ('gr', 'g', 'gramos', 'Gramos', 'GR');

UPDATE productos SET unidad_medida = 'KG'
WHERE unidad_medida IN ('kg', 'kilogramos', 'Kilogramos', 'Kg');

UPDATE productos SET unidad_medida = 'ML'
WHERE unidad_medida IN ('ml', 'mililitros', 'Mililitros', 'Ml');

UPDATE productos SET unidad_medida = 'L'
WHERE unidad_medida IN ('l', 'litros', 'Litros');

UPDATE productos SET unidad_medida = 'UNIDAD'
WHERE unidad_medida IN ('unidad', 'un', 'u', 'Unidad');

UPDATE productos SET unidad_medida = 'PORCION'
WHERE unidad_medida IN ('porcion', 'porción', 'Porcion', 'Porción');

UPDATE productos SET unidad_medida = 'DOCENA'
WHERE unidad_medida IN ('docena', 'doc', 'Docena');
