-- Eliminar tablas si existen
DROP TABLE IF EXISTS `Bonos`;
DROP TABLE IF EXISTS `Reservas`;
DROP TABLE IF EXISTS `Material_Pista`;
DROP TABLE IF EXISTS `Materiales`;
DROP TABLE IF EXISTS `Pistas`;
DROP TABLE IF EXISTS `Usuarios`;

-- Tabla: Usuarios
CREATE TABLE IF NOT EXISTS `Usuarios` (
  `idUsuario` INT(11) NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(100) NOT NULL,
  `apellidos` VARCHAR(100) NOT NULL,
  `fechaNacimiento` DATE NOT NULL,
  `fechaInscripcion` DATE NOT NULL,
  `correoElectronico` VARCHAR(100) NOT NULL UNIQUE,
  PRIMARY KEY (`idUsuario`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- Tabla: Pistas
CREATE TABLE IF NOT EXISTS `Pistas` (
  `idPista` INT(11) NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(100) NOT NULL UNIQUE,
  `estado` BOOLEAN NOT NULL,
  `tipo` BOOLEAN NOT NULL,
  `tamano` VARCHAR(20) NOT NULL,  -- Cambiado de ENUM a VARCHAR
  `numMaxJugadores` INT(11) NOT NULL,
  PRIMARY KEY (`idPista`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- Tabla: Materiales
CREATE TABLE IF NOT EXISTS `Materiales` (
  `idMaterial` INT(11) NOT NULL AUTO_INCREMENT,
  `tipo` VARCHAR(20) NOT NULL,  -- Cambiado de ENUM a VARCHAR
  `uso` BOOLEAN NOT NULL,
  `estado` VARCHAR(20) NOT NULL,  -- Cambiado de ENUM a VARCHAR
  PRIMARY KEY (`idMaterial`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- Tabla: Reservas
CREATE TABLE IF NOT EXISTS `Reservas` (
  `idReserva` INT(11) NOT NULL AUTO_INCREMENT,
  `usuarioId` INT(11) NOT NULL,
  `fechaHora` DATETIME NOT NULL,
  `duracion` INT(11) NOT NULL,
  `pistaId` INT(11) NOT NULL,
  `precio` FLOAT NOT NULL,
  `descuento` BOOLEAN NOT NULL,
  `tipoReserva` VARCHAR(20) NOT NULL,  -- Cambiado de ENUM a VARCHAR
  PRIMARY KEY (`idReserva`),
  KEY `usuarioId` (`usuarioId`),
  KEY `pistaId` (`pistaId`),
  CONSTRAINT `fk_reservas_usuario` FOREIGN KEY (`usuarioId`) REFERENCES `Usuarios`(`idUsuario`),
  CONSTRAINT `fk_reservas_pista` FOREIGN KEY (`pistaId`) REFERENCES `Pistas`(`idPista`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- Tabla: Bonos
CREATE TABLE IF NOT EXISTS `Bonos` (
  `idBono` INT(11) NOT NULL AUTO_INCREMENT,
  `usuarioId` INT(11) NOT NULL,
  `fechaInicio` DATE NOT NULL,
  `fechaCaducidad` DATE NOT NULL,
  `tipoPista` VARCHAR(20) NOT NULL,  -- Cambiado de ENUM a VARCHAR
  PRIMARY KEY (`idBono`),
  KEY `usuarioId` (`usuarioId`),
  CONSTRAINT `fk_bonos_usuario` FOREIGN KEY (`usuarioId`) REFERENCES `Usuarios`(`idUsuario`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- Tabla intermedia: Material_Pista (relación N:M)
CREATE TABLE IF NOT EXISTS `Material_Pista` (
  `nombrePista` VARCHAR(100) NOT NULL,
  `idMaterial` INT(11) NOT NULL,
  PRIMARY KEY (`nombrePista`, `idMaterial`),
  KEY `nombrePista` (`nombrePista`),
  KEY `idMaterial` (`idMaterial`),
  CONSTRAINT `fk_material_pista` FOREIGN KEY (`nombrePista`) REFERENCES `Pistas`(`nombre`),
  CONSTRAINT `fk_pista_material` FOREIGN KEY (`idMaterial`) REFERENCES `Materiales`(`idMaterial`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
