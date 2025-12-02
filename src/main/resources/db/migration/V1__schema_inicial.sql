-- V1__schema_inicial.sql

-- Tabla de Usuarios
CREATE TABLE usuarios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    apellido VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    rol VARCHAR(20) NOT NULL, -- Enum: ROLE_ADMIN, ROLE_VENDEDOR, ROLE_CLIENTE
    rut VARCHAR(15) NOT NULL UNIQUE,
    telefono VARCHAR(15),
    direccion VARCHAR(255),
    comuna VARCHAR(100),
    region VARCHAR(100),
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    fecha_creacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de Categorías
CREATE TABLE categorias (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE,
    descripcion VARCHAR(500),
    activa BOOLEAN NOT NULL DEFAULT TRUE
);

-- Tabla de Productos
CREATE TABLE productos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    sku VARCHAR(50) UNIQUE,
    nombre VARCHAR(150) NOT NULL,
    descripcion VARCHAR(1000),
    imagen_url VARCHAR(255),
    categoria_id BIGINT NOT NULL,
    precio DOUBLE NOT NULL,
    stock INT NOT NULL,
    ingredientes TEXT,
    cantidad_medida DOUBLE,
    unidad_medida VARCHAR(20), -- Enum: GR, ML, UNIDAD
    porciones INT,
    duracion_dias INT,
    condicion_conservacion VARCHAR(20), -- Enum: REFRIGERADO, TEMPERATURA_AMBIENTE, CONGELADO
    alergenos VARCHAR(500),
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    fecha_creacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_modificacion DATETIME,
    FOREIGN KEY (categoria_id) REFERENCES categorias(id)
);

-- Tabla de Boletas
CREATE TABLE boletas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    fecha_creacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_expiracion DATETIME,
    total DOUBLE NOT NULL,
    subtotal DOUBLE,
    costo_envio DOUBLE,
    tipo_entrega VARCHAR(20), -- Enum: RETIRO, DELIVERY
    horario_entrega VARCHAR(20), -- Enum: H_09_11, H_11_13, etc.
    direccion_entrega TEXT,
    comuna_entrega VARCHAR(100),
    region_entrega VARCHAR(100),
    metodo_pago VARCHAR(20), -- Enum: TRANSFERENCIA, EFECTIVO
    notas TEXT,
    estado VARCHAR(20), -- Enum: PENDIENTE, PAGADA, etc.
    fecha_entrega DATETIME,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);

-- Tabla de Detalle de Boleta
CREATE TABLE detalle_boleta (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    boleta_id BIGINT NOT NULL,
    producto_id BIGINT NOT NULL,
    cantidad INT NOT NULL,
    precio_unitario DOUBLE NOT NULL,
    subtotal DOUBLE NOT NULL,
    FOREIGN KEY (boleta_id) REFERENCES boletas(id),
    FOREIGN KEY (producto_id) REFERENCES productos(id)
);
