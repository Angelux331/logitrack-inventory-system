DROP DATABASE IF EXISTS LogiTrack_DB;

CREATE database LogiTrack_DB;

USE LogiTrack_DB;

DROP USER IF EXISTS 'logiTrack'@'localhost';
CREATE USER 'logiTrack'@'localhost'
IDENTIFIED BY 'logiTrack123!';

GRANT ALL PRIVILEGES
ON LogiTrack_DB.*
TO 'logiTrack'@'localhost';

FLUSH PRIVILEGES;


CREATE TABLE if not exists usuarios (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    rol ENUM('ADMIN','EMPLEADO') NOT NULL,
    activo BOOLEAN DEFAULT TRUE,
    fecha_creacion DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE if not exists bodegas (
    id_bodega INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(120) NOT NULL,
    ubicacion VARCHAR(200) NOT NULL,
    capacidad INT NOT NULL,
    encargado_id INT,
    activo BOOLEAN DEFAULT TRUE,

    FOREIGN KEY (encargado_id)
        REFERENCES usuarios(id_usuario)
);

CREATE TABLE if not exists categorias (
    id_categoria INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE if not exists productos (
    id_producto INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL,
    id_categoria INT NOT NULL,
    precio DECIMAL(10,2) NOT NULL,
    descripcion VARCHAR(255),
    activo BOOLEAN DEFAULT TRUE,

    FOREIGN KEY (id_categoria)
        REFERENCES categorias(id_categoria)
);

CREATE TABLE if not exists inventario (
    id_inventario INT AUTO_INCREMENT PRIMARY KEY,
    id_bodega INT NOT NULL,
    id_producto INT NOT NULL,
    stock INT NOT NULL DEFAULT 0,
    fecha_actualizacion DATETIME DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP,

    UNIQUE(id_bodega, id_producto),

    FOREIGN KEY (id_bodega)
        REFERENCES bodegas(id_bodega),

    FOREIGN KEY (id_producto)
        REFERENCES productos(id_producto)
);

CREATE TABLE if not exists movimientos (
    id_movimiento INT AUTO_INCREMENT PRIMARY KEY,

    fecha DATETIME DEFAULT CURRENT_TIMESTAMP,

    tipo ENUM(
        'ENTRADA',
        'SALIDA',
        'TRANSFERENCIA'
    ) NOT NULL,

    usuario_id INT NOT NULL,

    bodega_origen_id INT NULL,

    bodega_destino_id INT NULL,

    observacion VARCHAR(300),

    FOREIGN KEY (usuario_id)
        REFERENCES usuarios(id_usuario),

    FOREIGN KEY (bodega_origen_id)
        REFERENCES bodegas(id_bodega),

    FOREIGN KEY (bodega_destino_id)
        REFERENCES bodegas(id_bodega)
);

CREATE TABLE if not exists detalle_movimiento (
    id_detalle INT AUTO_INCREMENT PRIMARY KEY,

    movimiento_id INT NOT NULL,

    producto_id INT NOT NULL,

    cantidad INT NOT NULL,

    precio_unitario DECIMAL(10,2) NOT NULL,

    FOREIGN KEY (movimiento_id)
        REFERENCES movimientos(id_movimiento)
        ON DELETE CASCADE,

    FOREIGN KEY (producto_id)
        REFERENCES productos(id_producto)
);

CREATE TABLE if not exists auditorias (
    id_auditoria BIGINT AUTO_INCREMENT PRIMARY KEY,

    tipo_operacion ENUM(
        'INSERT',
        'UPDATE',
        'DELETE'
    ) NOT NULL,

    fecha_hora DATETIME DEFAULT CURRENT_TIMESTAMP,

    usuario_id INT NOT NULL,

    entidad VARCHAR(100) NOT NULL,

    entidad_id INT NOT NULL,

    valores_anteriores JSON,

    valores_nuevos JSON,

    FOREIGN KEY (usuario_id)
        REFERENCES usuarios(id_usuario)
);



