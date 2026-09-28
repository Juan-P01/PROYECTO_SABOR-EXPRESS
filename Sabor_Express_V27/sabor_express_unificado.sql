CREATE DATABASE IF NOT EXISTS `sabor_express` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci */;
USE `sabor_express`;

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Estructura de tabla: `tipos_documento`
--
DROP TABLE IF EXISTS `tipos_documento`;
CREATE TABLE `tipos_documento` (
  `id_tipo_documento` int(11) NOT NULL AUTO_INCREMENT,
  `descripcion_tipo_documento` varchar(45) NOT NULL,
  `estado` tinyint(1) NOT NULL DEFAULT 1,
  PRIMARY KEY (`id_tipo_documento`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

LOCK TABLES `tipos_documento` WRITE;
INSERT INTO `tipos_documento` VALUES 
(1,'Cédula de Ciudadanía',1),
(2,'Cédula de Extranjería',1),
(3,'Pasaporte',1);
UNLOCK TABLES;

--
-- Estructura de tabla: `roles`
--
DROP TABLE IF EXISTS `roles`;
CREATE TABLE `roles` (
  `id_rol` int(11) NOT NULL AUTO_INCREMENT,
  `tipo_de_rol` varchar(45) NOT NULL,
  `estado` tinyint(1) NOT NULL DEFAULT 1,
  PRIMARY KEY (`id_rol`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

LOCK TABLES `roles` WRITE;
INSERT INTO `roles` VALUES 
(1,'Administrador',1),
(2,'Mesero',1),
(3,'Cocinero',1),
(4,'Cliente',1),
(5,'Repartidor',1),
(6,'Cajero',1);
UNLOCK TABLES;

--
-- Estructura de tabla: `permisos`
--
DROP TABLE IF EXISTS `permisos`;
CREATE TABLE `permisos` (
  `id_permisos` int(11) NOT NULL AUTO_INCREMENT,
  `descripcion` varchar(45) NOT NULL,
  `estado` tinyint(1) NOT NULL DEFAULT 1,
  PRIMARY KEY (`id_permisos`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

LOCK TABLES `permisos` WRITE;
INSERT INTO `permisos` VALUES 
(1,'Gestionar Usuarios',1),
(2,'Crear Pedidos',1),
(3,'Ver Cocina',1),
(4,'Generar Facturas',1),
(5,'Entregar Pedidos',1),
(6,'Gestionar pagos y fidelización',1);
UNLOCK TABLES;

--
-- Estructura de tabla: `roles_has_permisos`
--
DROP TABLE IF EXISTS `roles_has_permisos`;
CREATE TABLE `roles_has_permisos` (
  `roles_id_rol` int(11) NOT NULL,
  `permisos_id_permisos` int(11) NOT NULL,
  `estado` tinyint(1) NOT NULL DEFAULT 1,
  PRIMARY KEY (`roles_id_rol`,`permisos_id_permisos`),
  KEY `fk_roles_has_permisos_permisos1_idx` (`permisos_id_permisos`),
  KEY `fk_roles_has_permisos_roles1_idx` (`roles_id_rol`),
  CONSTRAINT `fk_roles_has_permisos_permisos1` FOREIGN KEY (`permisos_id_permisos`) REFERENCES `permisos` (`id_permisos`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_roles_has_permisos_roles1` FOREIGN KEY (`roles_id_rol`) REFERENCES `roles` (`id_rol`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

LOCK TABLES `roles_has_permisos` WRITE;
INSERT INTO `roles_has_permisos` VALUES 
(1,1,1),(1,2,1),(1,3,1),(1,4,1),(1,5,1),
(2,2,1),
(3,3,1),
(4,2,1),
(5,5,1),
(6,6,1);
UNLOCK TABLES;

--
-- Estructura de tabla: `usuarios`
--
DROP TABLE IF EXISTS `usuarios`;
CREATE TABLE `usuarios` (
  `id_usuario` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(45) NOT NULL,
  `apellido` varchar(45) NOT NULL,
  `direccion` varchar(45) NOT NULL,
  `telefono` varchar(10) NOT NULL,
  `correo` varchar(255) NOT NULL,
  `contrasena` varchar(255) NOT NULL,
  `nro_documento` varchar(10) NOT NULL,
  `ultimo_acceso` datetime NOT NULL,
  `fecha_de_creacion` datetime NOT NULL,
  `fecha_nacimiento` datetime NOT NULL,
  `fecha_vencimiento_clave` datetime NOT NULL,
  `autorizacion_datos` tinyint(4) NOT NULL,
  `roles_id_rol` int(11) NOT NULL,
  `en_linea` tinyint(1) NOT NULL DEFAULT 1,
  `zona_ruta` varchar(120) NOT NULL DEFAULT 'Guaduas',
  `tipos_documento_id_tipo_documento` int(11) NOT NULL,
  `estado` tinyint(1) NOT NULL DEFAULT 1,
  PRIMARY KEY (`id_usuario`),
  UNIQUE KEY `correo_UNIQUE` (`correo`),
  KEY `fk_usuarios_roles1_idx` (`roles_id_rol`),
  KEY `fk_usuarios_tipos_documento1_idx` (`tipos_documento_id_tipo_documento`),
  CONSTRAINT `fk_usuarios_roles1` FOREIGN KEY (`roles_id_rol`) REFERENCES `roles` (`id_rol`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_usuarios_tipos_documento1` FOREIGN KEY (`tipos_documento_id_tipo_documento`) REFERENCES `tipos_documento` (`id_tipo_documento`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

LOCK TABLES `usuarios` WRITE;
INSERT INTO `usuarios` (id_usuario,nombre,apellido,direccion,telefono,correo,contrasena,nro_documento,ultimo_acceso,fecha_de_creacion,fecha_nacimiento,fecha_vencimiento_clave,autorizacion_datos,roles_id_rol,en_linea,zona_ruta,tipos_documento_id_tipo_documento,estado) VALUES
(1,'Carlos','Gómez','Calle 123 #45-67','3001234567','admin@saborexpress.com','$2a$12$eImiTXuWVxfM37uY4JANjOL.8/1R3EThrO/K48.s1mB2Wq/yA1zWy','0101823456','2026-08-19 06:45:48','2024-01-10 08:00:00','1990-05-15 00:00:00','2026-12-31 23:59:59',1,1,1,'Guaduas',1,1),
(2,'Laura','Martínez','Carrera 15 #88-10','3109876543','laura.mesero@saborexpress.com','$2a$12$eImiTXuWVxfM37uY4JANjOL.8/1R3EThrO/K48.s1mB2Wq/yA1zWy','0102045678','2026-08-19 06:45:48','2024-02-01 09:30:00','1998-11-20 00:00:00','2026-12-31 23:59:59',1,2,1,'Guaduas',1,1),
(3,'Andrés','López','Avenida 68 #22-05','3204567890','andres.cliente@gmail.com','$2a$12$eImiTXuWVxfM37uY4JANjOL.8/1R3EThrO/K48.s1mB2Wq/yA1zWy','0052345678','2026-08-19 06:45:48','2024-03-15 14:20:00','1995-03-08 00:00:00','2026-12-31 23:59:59',1,4,1,'Guaduas',1,1),
(4,'Mateo','Ríos','Calle 80 #11-23','3158889900','repartidor@saborexpress.com','$2a$12$eImiTXuWVxfM37uY4JANjOL.8/1R3EThrO/K48.s1mB2Wq/yA1zWy','0103099887','2026-08-19 06:45:48','2024-04-01 10:00:00','1997-07-12 00:00:00','2026-12-31 23:59:59',1,5,1,'Guaduas',1,1),
(5,'soy','chamo','453434534','35345343','chamo@gmail.com','$2a$12$Tu6G/7gMuHbqA0CuYYR5QeU2z9qdKprnuJDVizBVOE6UAfKXszwDC','0215154515','2026-09-04 08:34:46','2026-09-04 13:18:49','2026-09-04 13:18:49','2027-09-04 13:18:49',1,3,1,'Guaduas',1,1),
(6,'Admin','Prueba','Calle 1 #2-3','3000000001','admin.prueba@saborexpress.com','$2a$12$dgu8xCr24bx2oTeZe5uPC.YQaZhCtrAAoG7RPYGfwl3QW7REcqIua','1111111111','2026-09-04 00:00:00','2026-09-04 00:00:00','1990-01-15 00:00:00','2027-09-04 00:00:00',1,1,1,'Guaduas',1,1),
(7,'Mesero','Prueba','Calle 2 #3-4','3000000002','mesero.prueba@saborexpress.com','$2a$12$dgu8xCr24bx2oTeZe5uPC.YQaZhCtrAAoG7RPYGfwl3QW7REcqIua','2222222222','2026-09-04 00:00:00','2026-09-04 00:00:00','1992-02-20 00:00:00','2027-09-04 00:00:00',1,2,1,'Guaduas',1,1),
(8,'Cocinero','Prueba','Calle 3 #4-5','3000000003','cocina.prueba@saborexpress.com','$2a$12$dgu8xCr24bx2oTeZe5uPC.YQaZhCtrAAoG7RPYGfwl3QW7REcqIua','3333333333','2026-09-04 00:00:00','2026-09-04 00:00:00','1994-03-25 00:00:00','2027-09-04 00:00:00',1,3,1,'Guaduas',1,1),
(9,'Cliente','Prueba','Calle 4 #5-6','3000000004','cliente.prueba@saborexpress.com','$2a$12$dgu8xCr24bx2oTeZe5uPC.YQaZhCtrAAoG7RPYGfwl3QW7REcqIua','4444444444','2026-09-04 00:00:00','2026-09-04 00:00:00','1996-04-10 00:00:00','2027-09-04 00:00:00',1,4,1,'Guaduas',1,1),
(10,'Repartidor','Prueba','Calle 5 #6-7','3000000005','repartidor.prueba@saborexpress.com','$2a$12$dgu8xCr24bx2oTeZe5uPC.YQaZhCtrAAoG7RPYGfwl3QW7REcqIua','5555555555','2026-09-04 00:00:00','2026-09-04 00:00:00','1998-05-30 00:00:00','2027-09-04 00:00:00',1,5,1,'Guaduas',1,1);
UNLOCK TABLES;

--
-- Estructura de tabla: `clientes`
--
DROP TABLE IF EXISTS `clientes`;
CREATE TABLE `clientes` (
  `id_cliente` int(11) NOT NULL AUTO_INCREMENT,
  `puntos_fidelizacion` int(11) NOT NULL,
  `usuarios_id_usuario` int(11) NOT NULL,
  `estado` tinyint(1) NOT NULL DEFAULT 1,
  PRIMARY KEY (`id_cliente`),
  KEY `fk_clientes_usuarios1_idx` (`usuarios_id_usuario`),
  CONSTRAINT `fk_clientes_usuarios1` FOREIGN KEY (`usuarios_id_usuario`) REFERENCES `usuarios` (`id_usuario`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

LOCK TABLES `clientes` WRITE;
INSERT INTO `clientes` VALUES 
(1,150,3,1),
(2,0,5,1);
UNLOCK TABLES;

--
-- Estructura de tabla: `estados_mesas`
--
DROP TABLE IF EXISTS `estados_mesas`;
CREATE TABLE `estados_mesas` (
  `id_estado_mesa` int(11) NOT NULL AUTO_INCREMENT,
  `descripcion_estado` varchar(45) NOT NULL,
  `estado` tinyint(1) NOT NULL DEFAULT 1,
  PRIMARY KEY (`id_estado_mesa`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

LOCK TABLES `estados_mesas` WRITE;
INSERT INTO `estados_mesas` VALUES 
(1,'Disponible',1),
(2,'Ocupada',1),
(3,'Reservada',1),
(4,'Mantenimiento',1);
UNLOCK TABLES;

--
-- Estructura de tabla: `mesas`
--
DROP TABLE IF EXISTS `mesas`;
CREATE TABLE `mesas` (
  `id_mesa` int(11) NOT NULL AUTO_INCREMENT,
  `numero` int(11) NOT NULL,
  `estados_mesas_id_estado_mesa` int(11) NOT NULL,
  `estado` tinyint(1) NOT NULL DEFAULT 1,
  PRIMARY KEY (`id_mesa`),
  KEY `fk_mesas_estados_mesas1_idx` (`estados_mesas_id_estado_mesa`),
  CONSTRAINT `fk_mesas_estados_mesas1` FOREIGN KEY (`estados_mesas_id_estado_mesa`) REFERENCES `estados_mesas` (`id_estado_mesa`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

LOCK TABLES `mesas` WRITE;
INSERT INTO `mesas` VALUES 
(1,101,2,1),
(2,102,1,1),
(3,103,1,1),
(4,104,3,1);
UNLOCK TABLES;

-- Mesa virtual de Domicilios (usada por el checkout de domicilios y el modulo de repartidor).
INSERT INTO mesas (numero, estados_mesas_id_estado_mesa, estado)
SELECT 999, 1, 1 WHERE NOT EXISTS (SELECT 1 FROM mesas WHERE numero = 999);

--
-- Estructura de tabla: `estados_pedidos`
--
DROP TABLE IF EXISTS `estados_pedidos`;
CREATE TABLE `estados_pedidos` (
  `id_estado_pedido` int(11) NOT NULL,
  `detalle_estado_pedido` varchar(255) NOT NULL,
  `estado` tinyint(1) NOT NULL DEFAULT 1,
  PRIMARY KEY (`id_estado_pedido`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

LOCK TABLES `estados_pedidos` WRITE;
INSERT INTO `estados_pedidos` VALUES 
(1,'Pendiente de Pago',1),
(2,'En Preparación',1),
(3,'Listo para pago',1),
(4,'Pagado',1),
(5,'Listo para Entrega',1),
(6,'En Camino',1),
(7,'Finalizado',1),
(8,'Cancelado por incidencia',1);
UNLOCK TABLES;

--
-- Catálogo normalizado de categorías para filtrar el menú.
DROP TABLE IF EXISTS `categorias`;
CREATE TABLE `categorias` (
  `id_categoria` INT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(60) NOT NULL,
  `icono` VARCHAR(20) NOT NULL DEFAULT '🍽️',
  `estado` TINYINT(1) NOT NULL DEFAULT 1,
  PRIMARY KEY (`id_categoria`),
  UNIQUE KEY `uq_categoria_nombre` (`nombre`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
INSERT INTO `categorias` (`nombre`,`icono`) VALUES
('Frutas','🍓'),('Helados','🍦'),('Bebidas','🥤'),('Salado','🥪'),('Postres','🍰'),('General','🍓');

--
-- Estructura de tabla: `menus`
-- La categoría NO se almacena como texto en menus.
-- Cada plato apunta a la tabla categorias mediante id_categoria.
DROP TABLE IF EXISTS `menus`;
CREATE TABLE `menus` (
  `id_menu` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(45) NOT NULL,
  `descripcion` varchar(255) NOT NULL,
  `id_categoria` int(11) NOT NULL,
  `imagen` varchar(255) DEFAULT NULL,
  `precio` decimal(10,2) NOT NULL,
  `disponible` tinyint(4) NOT NULL,
  `puntos_fidelizacion` int NOT NULL DEFAULT 0,
  `estado` tinyint(1) NOT NULL DEFAULT 1,
  PRIMARY KEY (`id_menu`),
  KEY `idx_menus_categoria` (`id_categoria`),
  CONSTRAINT `fk_menus_categoria` FOREIGN KEY (`id_categoria`) REFERENCES `categorias` (`id_categoria`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

LOCK TABLES `menus` WRITE;
INSERT INTO `menus` (`id_menu`,`nombre`,`descripcion`,`id_categoria`,`imagen`,`precio`,`disponible`,`puntos_fidelizacion`,`estado`) VALUES
(1,'Hamburguesa Gourmet','200g de carne de res, queso cheddar, tocineta y papas',6,'vista/assets/menu/sandwich-pollo.jpg',28000.00,1,100,1),
(2,'Pizza Margherita','Masa artesanal, salsa de tomate, queso mozzarella y albahaca',6,'vista/assets/menu/waffles-helado.jpg',32000.00,1,120,1),
(3,'Limonada Cerezada','Bebida refrescante natural con cereza 500ml',3,'vista/assets/menu/lulada.jpg',8500.00,1,30,1),
(4,'Cerveza Artesanal','Cerveza IPA 330ml',3,'vista/assets/menu/malteadas.jpg',12000.00,1,40,1);
UNLOCK TABLES;

--
-- Estructura de tabla: `pedidos`
--
DROP TABLE IF EXISTS `pedidos`;
CREATE TABLE `pedidos` (
  `id_pedido` int(11) NOT NULL AUTO_INCREMENT,
  `total` decimal(10,2) NOT NULL,
  `fecha_pedido` datetime NOT NULL,
  `mesas_id_mesa` int(11) NOT NULL,
  `clientes_id_cliente` int(11) NOT NULL,
  `usuarios_id_usuario` int(11) NOT NULL,
  `estados_pedidos_id_estado_pedido` int(11) NOT NULL,
  `repartidor_id_usuario` int(11) DEFAULT NULL,
  `tipo_entrega` varchar(20) NOT NULL DEFAULT 'MESA',
  `direccion_entrega` varchar(255) DEFAULT NULL,
  `metodo_pago` varchar(30) NOT NULL DEFAULT 'EFECTIVO',
  `codigo_entrega` varchar(4) DEFAULT NULL,
  `observaciones_entrega` varchar(500) DEFAULT NULL,
  `fecha_entrega` datetime DEFAULT NULL,
  `estado` tinyint(1) NOT NULL DEFAULT 1,
  PRIMARY KEY (`id_pedido`),
  KEY `fk_pedidos_mesas1_idx` (`mesas_id_mesa`),
  KEY `fk_pedidos_clientes1_idx` (`clientes_id_cliente`),
  KEY `fk_pedidos_usuarios1_idx` (`usuarios_id_usuario`),
  KEY `fk_pedidos_estados_pedidos1_idx` (`estados_pedidos_id_estado_pedido`),
  KEY `fk_pedidos_repartidor_idx` (`repartidor_id_usuario`),
  CONSTRAINT `fk_pedidos_clientes1` FOREIGN KEY (`clientes_id_cliente`) REFERENCES `clientes` (`id_cliente`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_pedidos_estados_pedidos1` FOREIGN KEY (`estados_pedidos_id_estado_pedido`) REFERENCES `estados_pedidos` (`id_estado_pedido`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_pedidos_mesas1` FOREIGN KEY (`mesas_id_mesa`) REFERENCES `mesas` (`id_mesa`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_pedidos_repartidor` FOREIGN KEY (`repartidor_id_usuario`) REFERENCES `usuarios` (`id_usuario`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_pedidos_usuarios1` FOREIGN KEY (`usuarios_id_usuario`) REFERENCES `usuarios` (`id_usuario`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

LOCK TABLES `pedidos` WRITE;
INSERT INTO `pedidos` (`id_pedido`,`total`,`fecha_pedido`,`mesas_id_mesa`,`clientes_id_cliente`,`usuarios_id_usuario`,`estados_pedidos_id_estado_pedido`,`repartidor_id_usuario`,`tipo_entrega`,`direccion_entrega`,`metodo_pago`,`estado`) VALUES
(1,68500.00,'2026-08-19 06:45:48',1,1,2,4,NULL,'MESA',NULL,'EFECTIVO',1);
UNLOCK TABLES;

--
-- Estructura de tabla: `detalles_pedidos`
--
DROP TABLE IF EXISTS `detalles_pedidos`;
CREATE TABLE `detalles_pedidos` (
  `id_detalle_pedido` int(11) NOT NULL AUTO_INCREMENT,
  `cantidad` int(11) NOT NULL,
  `subtotal` decimal(10,2) NOT NULL,
  `precio_unitario` decimal(10,2) NOT NULL,
  `menus_id_menu` int(11) NOT NULL,
  `pedidos_id_pedido` int(11) NOT NULL,
  `estados_pedidos_id_estado_pedido` int(11) NOT NULL,
  `estado` tinyint(1) NOT NULL DEFAULT 1,
  PRIMARY KEY (`id_detalle_pedido`),
  KEY `fk_detalles_pedidos_menus1_idx` (`menus_id_menu`),
  KEY `fk_detalles_pedidos_pedidos1_idx` (`pedidos_id_pedido`),
  KEY `fk_detalles_pedidos_estados_pedidos1_idx` (`estados_pedidos_id_estado_pedido`),
  CONSTRAINT `fk_detalles_pedidos_estados_pedidos1` FOREIGN KEY (`estados_pedidos_id_estado_pedido`) REFERENCES `estados_pedidos` (`id_estado_pedido`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_detalles_pedidos_menus1` FOREIGN KEY (`menus_id_menu`) REFERENCES `menus` (`id_menu`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_detalles_pedidos_pedidos1` FOREIGN KEY (`pedidos_id_pedido`) REFERENCES `pedidos` (`id_pedido`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

LOCK TABLES `detalles_pedidos` WRITE;
INSERT INTO `detalles_pedidos` VALUES 
(1,2,56000.00,28000.00,1,1,4,1),
(2,1,12500.00,12500.00,4,1,4,1);
UNLOCK TABLES;

--
-- Estructura de tabla: `facturas`
--
DROP TABLE IF EXISTS `facturas`;
CREATE TABLE `facturas` (
  `id_factura` int(11) NOT NULL AUTO_INCREMENT,
  `numero_factura` varchar(45) NOT NULL,
  `total_factura` decimal(10,2) NOT NULL,
  `fecha_factura` date NOT NULL,
  `pedidos_id_pedido` int(11) NOT NULL,
  `clientes_id_cliente` int(11) NOT NULL,
  `estado` tinyint(1) NOT NULL DEFAULT 1,
  PRIMARY KEY (`id_factura`),
  KEY `fk_facturas_pedidos1_idx` (`pedidos_id_pedido`),
  KEY `fk_facturas_clientes1_idx` (`clientes_id_cliente`),
  CONSTRAINT `fk_facturas_clientes1` FOREIGN KEY (`clientes_id_cliente`) REFERENCES `clientes` (`id_cliente`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_facturas_pedidos1` FOREIGN KEY (`pedidos_id_pedido`) REFERENCES `pedidos` (`id_pedido`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

LOCK TABLES `facturas` WRITE;
INSERT INTO `facturas` VALUES 
(1,'FACT-0001',68500.00,'2026-08-19',1,1,1);
UNLOCK TABLES;

--
-- Estructura de tabla: `metodos_pago`
--
DROP TABLE IF EXISTS `metodos_pago`;
CREATE TABLE `metodos_pago` (
  `id_metodo_pago` int(11) NOT NULL AUTO_INCREMENT,
  `tipo_pago` varchar(45) NOT NULL,
  `estado` tinyint(1) NOT NULL DEFAULT 1,
  PRIMARY KEY (`id_metodo_pago`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

LOCK TABLES `metodos_pago` WRITE;
INSERT INTO `metodos_pago` VALUES 
(1,'Efectivo',1),
(2,'Tarjeta de Crédito/Débito',1),
(3,'Transferencia QR',1),(4,'Pago demo',1);
UNLOCK TABLES;

--
-- Estructura de tabla: `pagos`
--
DROP TABLE IF EXISTS `pagos`;
CREATE TABLE `pagos` (
  `id_pago` int(11) NOT NULL AUTO_INCREMENT,
  `monto` decimal(10,2) NOT NULL,
  `confirmado` tinyint(4) NOT NULL,
  `fecha_pago` datetime NOT NULL,
  `metodos_pago_id_metodo_pago` int(11) NOT NULL,
  `facturas_id_factura` int(11) NOT NULL,
  `estado` tinyint(1) NOT NULL DEFAULT 1,
  PRIMARY KEY (`id_pago`),
  KEY `fk_pagos_metodos_pago1_idx` (`metodos_pago_id_metodo_pago`),
  KEY `fk_pagos_facturas1_idx` (`facturas_id_factura`),
  CONSTRAINT `fk_pagos_facturas1` FOREIGN KEY (`facturas_id_factura`) REFERENCES `facturas` (`id_factura`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_pagos_metodos_pago1` FOREIGN KEY (`metodos_pago_id_metodo_pago`) REFERENCES `metodos_pago` (`id_metodo_pago`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

LOCK TABLES `pagos` WRITE;
INSERT INTO `pagos` VALUES 
(1,68500.00,1,'2026-08-19 06:45:48',2,1,1);
UNLOCK TABLES;

--
-- Estructura de tabla: `historiales_puntos`
--
DROP TABLE IF EXISTS `historiales_puntos`;
CREATE TABLE `historiales_puntos` (
  `id_historial_puntos` int(11) NOT NULL AUTO_INCREMENT,
  `puntos` int(11) NOT NULL,
  `fecha_movimiento` datetime NOT NULL,
  `tipo_de_movimiento` varchar(45) NOT NULL,
  `puntos_restantes` int(11) NOT NULL,
  `clientes_id_cliente` int(11) NOT NULL,
  `estado` tinyint(1) NOT NULL DEFAULT 1,
  PRIMARY KEY (`id_historial_puntos`),
  KEY `fk_historiales_puntos_clientes1_idx` (`clientes_id_cliente`),
  CONSTRAINT `fk_historiales_puntos_clientes1` FOREIGN KEY (`clientes_id_cliente`) REFERENCES `clientes` (`id_cliente`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

LOCK TABLES `historiales_puntos` WRITE;
INSERT INTO `historiales_puntos` VALUES 
(1,50,'2026-08-19 06:45:48','Acumulación por compra',150,1,1);
UNLOCK TABLES;

--
-- Incidencias de domicilios
--
DROP TABLE IF EXISTS `incidencias_domicilio`;
CREATE TABLE `incidencias_domicilio` (
  `id_incidencia` int NOT NULL AUTO_INCREMENT,
  `pedido_id` int NOT NULL,
  `repartidor_id_usuario` int NOT NULL,
  `motivo` varchar(100) NOT NULL,
  `detalle` varchar(500) DEFAULT NULL,
  `fecha` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `estado` tinyint(1) NOT NULL DEFAULT 1,
  PRIMARY KEY (`id_incidencia`),
  KEY `idx_incidencia_pedido` (`pedido_id`),
  KEY `idx_incidencia_repartidor` (`repartidor_id_usuario`),
  CONSTRAINT `fk_incidencia_pedido` FOREIGN KEY (`pedido_id`) REFERENCES `pedidos` (`id_pedido`),
  CONSTRAINT `fk_incidencia_repartidor` FOREIGN KEY (`repartidor_id_usuario`) REFERENCES `usuarios` (`id_usuario`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Estructura de tabla: `notificaciones`
--

DROP TABLE IF EXISTS `notificaciones`;
CREATE TABLE `notificaciones` (
  `id_notificacion` int(11) NOT NULL AUTO_INCREMENT,
  `titulo` varchar(45) NOT NULL,
  `mensaje` text NOT NULL,
  `fecha_envio` datetime NOT NULL,
  `leido` tinyint(1) NOT NULL,
  `usuarios_id_usuario` int(11) NOT NULL,
  `estado` tinyint(1) NOT NULL DEFAULT 1,
  PRIMARY KEY (`id_notificacion`),
  KEY `fk_notificaciones_usuarios1_idx` (`usuarios_id_usuario`),
  CONSTRAINT `fk_notificaciones_usuarios1` FOREIGN KEY (`usuarios_id_usuario`) REFERENCES `usuarios` (`id_usuario`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

LOCK TABLES `notificaciones` WRITE;
INSERT INTO `notificaciones` VALUES 
(1,'Bienvenido','Gracias por registrarte en Sabor Express','2026-08-19 06:45:48',1,3,1);
UNLOCK TABLES;

/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- ================================================================
-- Sabor Express · Base de datos unificada · Flujo final y reservas
-- Este archivo es autocontenido: no requiere ejecutar migraciones adicionales.
-- ================================================================
USE `sabor_express`;

-- Configuración del negocio.
CREATE TABLE IF NOT EXISTS `configuracion_negocio` (
  `clave` VARCHAR(45) NOT NULL,
  `valor` VARCHAR(255) NOT NULL,
  `estado` TINYINT(1) NOT NULL DEFAULT 1,
  PRIMARY KEY (`clave`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


INSERT INTO `configuracion_negocio` (`clave`,`valor`,`estado`) VALUES ('direccion_base_repartidor','Cra. 3 #3-34, Guaduas, Cundinamarca',1) ON DUPLICATE KEY UPDATE valor=VALUES(valor),estado=1;

-- Mesa virtual exclusiva para domicilios.
INSERT INTO `mesas` (`numero`,`estados_mesas_id_estado_mesa`,`estado`)
SELECT 999,1,1
WHERE NOT EXISTS (SELECT 1 FROM `mesas` WHERE `numero`=999);

-- Cliente técnico para comandas creadas por el mesero.
INSERT INTO `usuarios`
(`nombre`,`apellido`,`direccion`,`telefono`,`correo`,`contrasena`,`nro_documento`,
 `ultimo_acceso`,`fecha_de_creacion`,`fecha_nacimiento`,`fecha_vencimiento_clave`,
 `autorizacion_datos`,`roles_id_rol`,`tipos_documento_id_tipo_documento`,`estado`)
SELECT 'Cliente','Salón','Restaurante','0000000000','cliente.salon@saborexpress.local',
'$2a$12$Mixhi/pl/a5PGMcchd590epMI0H7Bm98OYkvQSto/tETtl2G.IN/i','9000000001',
CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,'2000-01-01',DATE_ADD(CURRENT_TIMESTAMP,INTERVAL 1 YEAR),
1,4,1,1
WHERE NOT EXISTS (SELECT 1 FROM `usuarios` WHERE `correo`='cliente.salon@saborexpress.local');

INSERT INTO `clientes` (`puntos_fidelizacion`,`usuarios_id_usuario`,`estado`)
SELECT 0,u.id_usuario,1 FROM `usuarios` u
WHERE u.correo='cliente.salon@saborexpress.local'
AND NOT EXISTS (SELECT 1 FROM `clientes` c WHERE c.usuarios_id_usuario=u.id_usuario);

-- Usuarios demo: contraseña 123456.
INSERT INTO `usuarios`
(`nombre`,`apellido`,`direccion`,`telefono`,`correo`,`contrasena`,`nro_documento`,
 `ultimo_acceso`,`fecha_de_creacion`,`fecha_nacimiento`,`fecha_vencimiento_clave`,
 `autorizacion_datos`,`roles_id_rol`,`tipos_documento_id_tipo_documento`,`estado`)
SELECT 'Admin','Demo','Sabor Express','3000000101','admin.demo@saborexpress.com',
'$2a$12$Mixhi/pl/a5PGMcchd590epMI0H7Bm98OYkvQSto/tETtl2G.IN/i','9000000101',
CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,'1990-01-01',DATE_ADD(CURRENT_TIMESTAMP,INTERVAL 1 YEAR),1,1,1,1
WHERE NOT EXISTS (SELECT 1 FROM `usuarios` WHERE `correo`='admin.demo@saborexpress.com');

INSERT INTO `usuarios`
(`nombre`,`apellido`,`direccion`,`telefono`,`correo`,`contrasena`,`nro_documento`,
 `ultimo_acceso`,`fecha_de_creacion`,`fecha_nacimiento`,`fecha_vencimiento_clave`,
 `autorizacion_datos`,`roles_id_rol`,`tipos_documento_id_tipo_documento`,`estado`)
SELECT 'Mesero','Demo','Sabor Express','3000000102','mesero.demo@saborexpress.com',
'$2a$12$Mixhi/pl/a5PGMcchd590epMI0H7Bm98OYkvQSto/tETtl2G.IN/i','9000000102',
CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,'1991-01-01',DATE_ADD(CURRENT_TIMESTAMP,INTERVAL 1 YEAR),1,2,1,1
WHERE NOT EXISTS (SELECT 1 FROM `usuarios` WHERE `correo`='mesero.demo@saborexpress.com');

INSERT INTO `usuarios`
(`nombre`,`apellido`,`direccion`,`telefono`,`correo`,`contrasena`,`nro_documento`,
 `ultimo_acceso`,`fecha_de_creacion`,`fecha_nacimiento`,`fecha_vencimiento_clave`,
 `autorizacion_datos`,`roles_id_rol`,`tipos_documento_id_tipo_documento`,`estado`)
SELECT 'Cocinero','Demo','Sabor Express','3000000103','cocinero.demo@saborexpress.com',
'$2a$12$Mixhi/pl/a5PGMcchd590epMI0H7Bm98OYkvQSto/tETtl2G.IN/i','9000000103',
CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,'1992-01-01',DATE_ADD(CURRENT_TIMESTAMP,INTERVAL 1 YEAR),1,3,1,1
WHERE NOT EXISTS (SELECT 1 FROM `usuarios` WHERE `correo`='cocinero.demo@saborexpress.com');

INSERT INTO `usuarios`
(`nombre`,`apellido`,`direccion`,`telefono`,`correo`,`contrasena`,`nro_documento`,
 `ultimo_acceso`,`fecha_de_creacion`,`fecha_nacimiento`,`fecha_vencimiento_clave`,
 `autorizacion_datos`,`roles_id_rol`,`tipos_documento_id_tipo_documento`,`estado`)
SELECT 'Cliente','Demo','Sabor Express','3000000104','cliente.demo@saborexpress.com',
'$2a$12$Mixhi/pl/a5PGMcchd590epMI0H7Bm98OYkvQSto/tETtl2G.IN/i','9000000104',
CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,'1993-01-01',DATE_ADD(CURRENT_TIMESTAMP,INTERVAL 1 YEAR),1,4,1,1
WHERE NOT EXISTS (SELECT 1 FROM `usuarios` WHERE `correo`='cliente.demo@saborexpress.com');

INSERT INTO `clientes` (`puntos_fidelizacion`,`usuarios_id_usuario`,`estado`)
SELECT 0,u.id_usuario,1 FROM `usuarios` u
WHERE u.correo='cliente.demo@saborexpress.com'
AND NOT EXISTS (SELECT 1 FROM `clientes` c WHERE c.usuarios_id_usuario=u.id_usuario);

INSERT INTO `usuarios`
(`nombre`,`apellido`,`direccion`,`telefono`,`correo`,`contrasena`,`nro_documento`,
 `ultimo_acceso`,`fecha_de_creacion`,`fecha_nacimiento`,`fecha_vencimiento_clave`,
 `autorizacion_datos`,`roles_id_rol`,`tipos_documento_id_tipo_documento`,`estado`)
SELECT 'Repartidor','Demo','Sabor Express','3000000105','repartidor.demo@saborexpress.com',
'$2a$12$Mixhi/pl/a5PGMcchd590epMI0H7Bm98OYkvQSto/tETtl2G.IN/i','9000000105',
CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,'1994-01-01',DATE_ADD(CURRENT_TIMESTAMP,INTERVAL 1 YEAR),1,5,1,1
WHERE NOT EXISTS (SELECT 1 FROM `usuarios` WHERE `correo`='repartidor.demo@saborexpress.com');

INSERT INTO `usuarios`
(`nombre`,`apellido`,`direccion`,`telefono`,`correo`,`contrasena`,`nro_documento`,
 `ultimo_acceso`,`fecha_de_creacion`,`fecha_nacimiento`,`fecha_vencimiento_clave`,
 `autorizacion_datos`,`roles_id_rol`,`tipos_documento_id_tipo_documento`,`estado`)
SELECT 'Cajero','Demo','Sabor Express','3000000106','cajero.demo@saborexpress.com',
'$2a$12$Mixhi/pl/a5PGMcchd590epMI0H7Bm98OYkvQSto/tETtl2G.IN/i','9000000106',
CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,'1995-01-01',DATE_ADD(CURRENT_TIMESTAMP,INTERVAL 1 YEAR),1,6,1,1
WHERE NOT EXISTS (SELECT 1 FROM `usuarios` WHERE `correo`='cajero.demo@saborexpress.com');

-- Reservas de mesas.
CREATE TABLE IF NOT EXISTS `reservas` (
  `id_reserva` INT NOT NULL AUTO_INCREMENT,
  `fecha_reserva` DATE NOT NULL,
  `hora_reserva` TIME NOT NULL,
  `cantidad_personas` INT NOT NULL,
  `estado_reserva` VARCHAR(20) NOT NULL DEFAULT 'CONFIRMADA',
  `clientes_id_cliente` INT NOT NULL,
  `mesas_id_mesa` INT NOT NULL,
  `observaciones` VARCHAR(255) NULL,
  `fecha_creacion` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `estado` TINYINT(1) NOT NULL DEFAULT 1,
  PRIMARY KEY (`id_reserva`),
  KEY `idx_reservas_fecha_mesa` (`fecha_reserva`,`mesas_id_mesa`),
  KEY `idx_reservas_cliente` (`clientes_id_cliente`),
  CONSTRAINT `fk_reservas_cliente` FOREIGN KEY (`clientes_id_cliente`) REFERENCES `clientes` (`id_cliente`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_reservas_mesa` FOREIGN KEY (`mesas_id_mesa`) REFERENCES `mesas` (`id_mesa`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Datos semilla de reserva.
INSERT INTO `reservas` (`fecha_reserva`,`hora_reserva`,`cantidad_personas`,`estado_reserva`,`clientes_id_cliente`,`mesas_id_mesa`,`observaciones`)
SELECT DATE_ADD(CURRENT_DATE,INTERVAL 2 DAY),'19:00:00',2,'CONFIRMADA',c.id_cliente,m.id_mesa,'Reserva demo'
FROM `clientes` c CROSS JOIN `mesas` m
WHERE c.usuarios_id_usuario=(SELECT id_usuario FROM usuarios WHERE correo='cliente.demo@saborexpress.com' LIMIT 1)
  AND m.numero=103
  AND NOT EXISTS (SELECT 1 FROM reservas r WHERE r.fecha_reserva=DATE_ADD(CURRENT_DATE,INTERVAL 2 DAY) AND r.hora_reserva='19:00:00' AND r.mesas_id_mesa=m.id_mesa);

-- El flujo oficial es: 1 Recibido en Cocina -> 2 En Preparación -> 5 Listo.
-- En Mesa: 5 Listo -> 3 Servido - Pendiente de Pago -> 4 Pagado.
-- A Domicilio: 5 Listo -> 6 En Camino -> 7 Entregado.
UPDATE `estados_pedidos` SET `detalle_estado_pedido`='Recibido en Cocina' WHERE `id_estado_pedido`=1;
UPDATE `estados_pedidos` SET `detalle_estado_pedido`='En Preparación' WHERE `id_estado_pedido`=2;
UPDATE `estados_pedidos` SET `detalle_estado_pedido`='Servido - Pendiente de Pago' WHERE `id_estado_pedido`=3;
UPDATE `estados_pedidos` SET `detalle_estado_pedido`='Listo' WHERE `id_estado_pedido`=5;
UPDATE `estados_pedidos` SET `detalle_estado_pedido`='Finalizado' WHERE `id_estado_pedido`=7;


-- Programa de canje de puntos configurado por el administrador.
CREATE TABLE IF NOT EXISTS `canjeables` (
  `id_canjeable` INT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(100) NOT NULL,
  `descripcion` VARCHAR(255) NULL,
  `menus_id_menu` INT NULL,
  `puntos_requeridos` INT NOT NULL,
  `activo` TINYINT(1) NOT NULL DEFAULT 1,
  PRIMARY KEY (`id_canjeable`),
  KEY `idx_canjeables_menu` (`menus_id_menu`),
  CONSTRAINT `fk_canjeables_menu` FOREIGN KEY (`menus_id_menu`) REFERENCES `menus` (`id_menu`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE IF NOT EXISTS `canjes` (
  `id_canje` INT NOT NULL AUTO_INCREMENT,
  `clientes_id_cliente` INT NOT NULL,
  `canjeables_id_canjeable` INT NOT NULL,
  `puntos_usados` INT NOT NULL,
  `fecha_canje` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `estado` TINYINT(1) NOT NULL DEFAULT 1,
  PRIMARY KEY (`id_canje`),
  KEY `idx_canjes_cliente` (`clientes_id_cliente`),
  CONSTRAINT `fk_canjes_cliente` FOREIGN KEY (`clientes_id_cliente`) REFERENCES `clientes` (`id_cliente`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_canjes_canjeable` FOREIGN KEY (`canjeables_id_canjeable`) REFERENCES `canjeables` (`id_canjeable`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

INSERT INTO `canjeables` (`nombre`,`descripcion`,`menus_id_menu`,`puntos_requeridos`)
SELECT 'Fresas con crema gratis','Canje por una porción de Fresas con crema',id_menu,300 FROM menus WHERE nombre='Fresas con Crema' LIMIT 1;


-- ================================================================
-- DATOS DEMO PARA REPORTES
-- Pedidos históricos pagados para probar filtros semanal/mensual.
-- ================================================================
INSERT INTO `pedidos` (`id_pedido`,`total`,`fecha_pedido`,`mesas_id_mesa`,`clientes_id_cliente`,`usuarios_id_usuario`,`estados_pedidos_id_estado_pedido`,`repartidor_id_usuario`,`tipo_entrega`,`direccion_entrega`,`metodo_pago`,`estado`) VALUES
(20,28000.00,DATE_SUB(CURRENT_TIMESTAMP,INTERVAL 3 DAY),2,2,2,4,NULL,'MESA',NULL,'EFECTIVO',1),
(21,64000.00,DATE_SUB(CURRENT_TIMESTAMP,INTERVAL 5 DAY),3,2,2,4,NULL,'MESA',NULL,'EFECTIVO',1),
(22,17000.00,DATE_SUB(CURRENT_TIMESTAMP,INTERVAL 8 DAY),2,2,2,4,NULL,'MESA',NULL,'EFECTIVO',1),
(23,56000.00,DATE_SUB(CURRENT_TIMESTAMP,INTERVAL 10 DAY),3,2,2,4,NULL,'MESA',NULL,'EFECTIVO',1),
(24,32000.00,DATE_SUB(CURRENT_TIMESTAMP,INTERVAL 15 DAY),2,2,2,4,NULL,'MESA',NULL,'EFECTIVO',1),
(25,88000.00,DATE_SUB(CURRENT_TIMESTAMP,INTERVAL 20 DAY),3,2,2,4,NULL,'MESA',NULL,'EFECTIVO',1),
(26,36500.00,DATE_SUB(CURRENT_TIMESTAMP,INTERVAL 28 DAY),2,2,2,4,NULL,'MESA',NULL,'EFECTIVO',1),
(27,96000.00,DATE_SUB(CURRENT_TIMESTAMP,INTERVAL 40 DAY),3,2,2,4,NULL,'MESA',NULL,'EFECTIVO',1);

INSERT INTO `detalles_pedidos` (`id_detalle_pedido`,`cantidad`,`subtotal`,`precio_unitario`,`menus_id_menu`,`pedidos_id_pedido`,`estados_pedidos_id_estado_pedido`,`estado`) VALUES
(20,1,28000.00,28000.00,1,20,4,1),
(21,2,64000.00,32000.00,2,21,4,1),
(22,2,17000.00,8500.00,3,22,4,1),
(23,2,56000.00,28000.00,1,23,4,1),
(24,1,32000.00,32000.00,2,24,4,1),
(25,2,56000.00,28000.00,1,25,4,1),
(26,1,32000.00,32000.00,2,25,4,1),
(27,1,36500.00,36500.00,1,26,4,1),
(28,3,84000.00,28000.00,1,27,4,1),
(29,1,12000.00,12000.00,4,27,4,1);

INSERT INTO `facturas` (`id_factura`,`numero_factura`,`total_factura`,`fecha_factura`,`pedidos_id_pedido`,`clientes_id_cliente`,`estado`) VALUES
(20,'FACT-DEMO-20',28000.00,DATE(DATE_SUB(CURRENT_TIMESTAMP,INTERVAL 3 DAY)),20,2,1),
(21,'FACT-DEMO-21',64000.00,DATE(DATE_SUB(CURRENT_TIMESTAMP,INTERVAL 5 DAY)),21,2,1),
(22,'FACT-DEMO-22',17000.00,DATE(DATE_SUB(CURRENT_TIMESTAMP,INTERVAL 8 DAY)),22,2,1),
(23,'FACT-DEMO-23',56000.00,DATE(DATE_SUB(CURRENT_TIMESTAMP,INTERVAL 10 DAY)),23,2,1),
(24,'FACT-DEMO-24',32000.00,DATE(DATE_SUB(CURRENT_TIMESTAMP,INTERVAL 15 DAY)),24,2,1),
(25,'FACT-DEMO-25',88000.00,DATE(DATE_SUB(CURRENT_TIMESTAMP,INTERVAL 20 DAY)),25,2,1),
(26,'FACT-DEMO-26',36500.00,DATE(DATE_SUB(CURRENT_TIMESTAMP,INTERVAL 28 DAY)),26,2,1),
(27,'FACT-DEMO-27',96000.00,DATE(DATE_SUB(CURRENT_TIMESTAMP,INTERVAL 40 DAY)),27,2,1);

INSERT INTO `pagos` (`id_pago`,`monto`,`confirmado`,`fecha_pago`,`metodos_pago_id_metodo_pago`,`facturas_id_factura`,`estado`) VALUES
(20,28000.00,1,DATE_SUB(CURRENT_TIMESTAMP,INTERVAL 3 DAY),1,20,1),
(21,64000.00,1,DATE_SUB(CURRENT_TIMESTAMP,INTERVAL 5 DAY),1,21,1),
(22,17000.00,1,DATE_SUB(CURRENT_TIMESTAMP,INTERVAL 8 DAY),1,22,1),
(23,56000.00,1,DATE_SUB(CURRENT_TIMESTAMP,INTERVAL 10 DAY),1,23,1),
(24,32000.00,1,DATE_SUB(CURRENT_TIMESTAMP,INTERVAL 15 DAY),1,24,1),
(25,88000.00,1,DATE_SUB(CURRENT_TIMESTAMP,INTERVAL 20 DAY),1,25,1),
(26,36500.00,1,DATE_SUB(CURRENT_TIMESTAMP,INTERVAL 28 DAY),1,26,1),
(27,96000.00,1,DATE_SUB(CURRENT_TIMESTAMP,INTERVAL 40 DAY),1,27,1);


-- V12: nombre operativo del estado 3 para hacer explícito que el pedido queda en caja.
UPDATE estados_pedidos SET detalle_estado_pedido='Listo para pago' WHERE id_estado_pedido=3;

-- V12: normaliza los iconos de categorías existentes al conjunto visual a color.
UPDATE categorias SET icono='🍓' WHERE nombre='Frutas';
UPDATE categorias SET icono='🍦' WHERE nombre='Helados';
UPDATE categorias SET icono='🥤' WHERE nombre='Bebidas';
UPDATE categorias SET icono='🥪' WHERE nombre='Salado';
UPDATE categorias SET icono='🍰' WHERE nombre='Postres';
UPDATE categorias SET icono='🍓' WHERE nombre='General';


-- ================================================================
-- Reglas de domicilio V14
-- ================================================================
ALTER TABLE pedidos ADD INDEX idx_pedidos_domicilio_estado (tipo_entrega, estados_pedidos_id_estado_pedido, repartidor_id_usuario);
UPDATE usuarios SET zona_ruta='Guaduas', en_linea=1 WHERE roles_id_rol=5;
