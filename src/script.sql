-- Eliminar tablas si existen
DROP TABLE IF EXISTS `Bonos`;
DROP TABLE IF EXISTS `Reservas`;
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
  -- La antigüedad se calcula mediante una consulta, no como columna generada
  PRIMARY KEY (`idUsuario`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- Tabla: Pistas
CREATE TABLE IF NOT EXISTS `Pistas` (
  `idPista` INT(11) NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(100) NOT NULL UNIQUE,
  `estado` BOOLEAN NOT NULL,
  `tipo` BOOLEAN NOT NULL,
  `tamano` ENUM('minibasket', 'adultos', '3vs3') NOT NULL,
  `numMaxJugadores` INT(11) NOT NULL,
  PRIMARY KEY (`idPista`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- Tabla: Materiales
CREATE TABLE IF NOT EXISTS `Materiales` (
  `idMaterial` INT(11) NOT NULL AUTO_INCREMENT,
  `tipo` ENUM('pelotas', 'canastas', 'conos') NOT NULL,
  `uso` BOOLEAN NOT NULL,
  `estado` ENUM('disponible', 'reservado', 'mal estado') NOT NULL,
  `cantidadMaxima` INT(11) NOT NULL,
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
  `tipoReserva` ENUM('infantil', 'familiar', 'adultos') NOT NULL,
  PRIMARY KEY (`idReserva`),
  KEY `usuarioId` (`usuarioId`),
  KEY `pistaId` (`pistaId`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- Tabla: Bonos
CREATE TABLE IF NOT EXISTS `Bonos` (
  `idBono` INT(11) NOT NULL AUTO_INCREMENT,
  `usuarioId` INT(11) NOT NULL,
  `fechaInicio` DATE NOT NULL,
  `fechaCaducidad` DATE NOT NULL,
  `tipoPista` ENUM('minibasket', 'adultos', '3vs3') NOT NULL,
  PRIMARY KEY (`idBono`),
  KEY `usuarioId` (`usuarioId`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- Tabla intermedia: Material_Pista (relación N:M)
DROP TABLE IF EXISTS `Material_Pista`;
CREATE TABLE IF NOT EXISTS `Material_Pista` (
  `idPista` INT(11) NOT NULL,
  `idMaterial` INT(11) NOT NULL,
  PRIMARY KEY (`idPista`, `idMaterial`),
  KEY `idPista` (`idPista`),
  KEY `idMaterial` (`idMaterial`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
