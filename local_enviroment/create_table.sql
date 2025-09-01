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
  `estado` VARCHAR(50) NULL,
  `contrasena` VARCHAR(255) NOT NULL
  `rol_id` BIGINT NOT NULL
);

CREATE TABLE autenticacion.`roles` (
  `role_id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `nombre` VARCHAR(50) NOT NULL UNIQUE,
  `descripcion` VARCHAR(255)
);

INSERT INTO autenticacion.`roles` (`nombre`, `descripcion`) VALUES
('ADMINISTRADOR', 'Tiene acceso completo a la aplicación y a la gestión de usuarios.'),
('ASESOR', 'Puede gestionar y asesorar a los clientes.'),
('CLIENTE', 'Puede acceder a su información y servicios personales.');