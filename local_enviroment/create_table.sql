CREATE TABLE `usuarios` (
  `usuario_id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `documento_id` BIGINT NOT NULL UNIQUE,
  `nombres` VARCHAR(100) NOT NULL,
  `apellidos` VARCHAR(100) NOT NULL,
  `fecha_nacimiento` DATETIME(6) NULL,
  `direccion` VARCHAR(100) NULL,
  `telefono` VARCHAR(100) NULL,
  `correo_electronico` VARCHAR(100) NOT NULL UNIQUE,
  `salario_base` DECIMAL(10, 2) NULL,
  `estado` VARCHAR(50) NULL
);