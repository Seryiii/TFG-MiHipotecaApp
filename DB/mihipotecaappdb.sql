-- phpMyAdmin SQL Dump
-- version 5.1.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 22-02-2023 a las 19:27:20
-- Versión del servidor: 10.4.22-MariaDB
-- Versión de PHP: 8.0.13

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `mihipotecaappdb`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `banco`
--

CREATE TABLE `banco` (
  `idBanco` int(11) NOT NULL,
  `logo` blob NOT NULL,
  `nombre` varchar(50) NOT NULL,
  `paginaWeb` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `comisiones`
--

CREATE TABLE `comisiones` (
  `idComision` int(11) NOT NULL,
  `idhipotecaOfertada` int(11) NOT NULL,
  `idBanco` int(11) NOT NULL,
  `tipoComision` varchar(100) NOT NULL,
  `porcentaje` float NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `gastos`
--

CREATE TABLE `gastos` (
  `idGasto` int(11) NOT NULL,
  `idBanco` int(11) NOT NULL,
  `gestoria` float NOT NULL,
  `notaria` float NOT NULL,
  `registro` float NOT NULL,
  `tasacion` float NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `hipotecaofertada`
--

CREATE TABLE `hipotecaofertada` (
  `idHipotecaOfertada` int(11) NOT NULL,
  `idBanco` int(11) NOT NULL,
  `nombreHipoteca` varchar(50) NOT NULL,
  `plazoMin` int(11) NOT NULL,
  `plazoMax` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `hipotecaofertadafija`
--

CREATE TABLE `hipotecaofertadafija` (
  `idhipotecaOfertadaFija` int(11) NOT NULL,
  `idhipotecaOfertada` int(11) NOT NULL,
  `plazoFijo` tinyint(1) NOT NULL,
  `tin` float NOT NULL,
  `numAños` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `hipotecaofertadamixta`
--

CREATE TABLE `hipotecaofertadamixta` (
  `idhipotecaOfertadaMixta` int(11) NOT NULL,
  `idhipotecaOfertada` int(11) NOT NULL,
  `numAniosMax` int(11) NOT NULL,
  `numAniosMin` int(11) NOT NULL,
  `tin` float NOT NULL,
  `diferencial` float NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `hipotecaofertadavariable`
--

CREATE TABLE `hipotecaofertadavariable` (
  `idHipotecaOfertadaVariable` int(11) NOT NULL,
  `idHipotecaOfertada` int(11) NOT NULL,
  `tin` float NOT NULL,
  `diferencial` float NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `hipotecaseguimiento`
--

CREATE TABLE `hipotecaseguimiento` (
  `idHipotecaSeguimiento` int(11) NOT NULL,
  `idUsuario` int(11) NOT NULL,
  `infoBanco` varchar(50) NOT NULL,
  `precioVivienda` float NOT NULL,
  `ahorro` float NOT NULL,
  `numCuotas` int(11) NOT NULL,
  `comunidad` varchar(50) NOT NULL,
  `tipoVivienda` varchar(50) NOT NULL,
  `antiguedad` varchar(50) NOT NULL,
  `pagoIVA` tinyint(1) NOT NULL,
  `pagoAJD` tinyint(1) NOT NULL,
  `pagoITP` tinyint(1) NOT NULL,
  `gastoTotal` float NOT NULL,
  `anioActual` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `hipotecaseguimientofija`
--

CREATE TABLE `hipotecaseguimientofija` (
  `idHipotecaSeguimientoFija` int(11) NOT NULL,
  `idHipotecaSeguimiento` int(11) NOT NULL,
  `tin` float NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `hipotecaseguimientomixta`
--

CREATE TABLE `hipotecaseguimientomixta` (
  `idHipotecaSeguimientoMixta` int(11) NOT NULL,
  `idHipotecaSeguimiento` int(11) NOT NULL,
  `porcenFijo` float NOT NULL,
  `diferencial` float NOT NULL,
  `numAnios` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `hipotecaseguimientovar`
--

CREATE TABLE `hipotecaseguimientovar` (
  `idHipotecaSeguimientoVar` int(11) NOT NULL,
  `idHipotecaSeguimiento` int(11) NOT NULL,
  `tin` float NOT NULL,
  `diferencial` float NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `impuestos`
--

CREATE TABLE `impuestos` (
  `idImpuesto` int(11) NOT NULL,
  `tipo` varchar(50) DEFAULT NULL,
  `comunidadAutonoma` varchar(50) DEFAULT NULL,
  `porcentaje` float DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuarios`
--

CREATE TABLE `usuarios` (
  `idUsuario` int(11) NOT NULL,
  `nombre` varchar(30) NOT NULL,
  `correo` varchar(50) NOT NULL,
  `password` varchar(50) NOT NULL,
  `premium` tinyint(1) NOT NULL,
  `avatar` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Volcado de datos para la tabla `usuarios`
--

INSERT INTO `usuarios` (`idUsuario`, `nombre`, `correo`, `password`, `premium`, `avatar`) VALUES
(1, 'Jorge', 'jorge@gmail.com', 'hola123', 0, 0);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `vinculaciones`
--

CREATE TABLE `vinculaciones` (
  `idVinculacion` int(11) NOT NULL,
  `idBanco` int(11) NOT NULL,
  `tipo` varchar(100) NOT NULL,
  `costeAnual` float NOT NULL,
  `rebaja` float NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `banco`
--
ALTER TABLE `banco`
  ADD PRIMARY KEY (`idBanco`);

--
-- Indices de la tabla `comisiones`
--
ALTER TABLE `comisiones`
  ADD PRIMARY KEY (`idComision`),
  ADD KEY `idhipotecaOfertada` (`idhipotecaOfertada`),
  ADD KEY `idBanco` (`idBanco`);

--
-- Indices de la tabla `gastos`
--
ALTER TABLE `gastos`
  ADD PRIMARY KEY (`idGasto`),
  ADD KEY `idBanco` (`idBanco`);

--
-- Indices de la tabla `hipotecaofertada`
--
ALTER TABLE `hipotecaofertada`
  ADD PRIMARY KEY (`idHipotecaOfertada`),
  ADD KEY `idBanco` (`idBanco`);

--
-- Indices de la tabla `hipotecaofertadafija`
--
ALTER TABLE `hipotecaofertadafija`
  ADD PRIMARY KEY (`idhipotecaOfertadaFija`),
  ADD KEY `idhipotecaOfertada` (`idhipotecaOfertada`);

--
-- Indices de la tabla `hipotecaofertadamixta`
--
ALTER TABLE `hipotecaofertadamixta`
  ADD PRIMARY KEY (`idhipotecaOfertadaMixta`),
  ADD KEY `idhipotecaOfertada` (`idhipotecaOfertada`);

--
-- Indices de la tabla `hipotecaofertadavariable`
--
ALTER TABLE `hipotecaofertadavariable`
  ADD PRIMARY KEY (`idHipotecaOfertadaVariable`),
  ADD KEY `idHipotecaOfertada` (`idHipotecaOfertada`);

--
-- Indices de la tabla `hipotecaseguimiento`
--
ALTER TABLE `hipotecaseguimiento`
  ADD PRIMARY KEY (`idHipotecaSeguimiento`),
  ADD KEY `idUsuario` (`idUsuario`);

--
-- Indices de la tabla `hipotecaseguimientofija`
--
ALTER TABLE `hipotecaseguimientofija`
  ADD PRIMARY KEY (`idHipotecaSeguimientoFija`),
  ADD KEY `idHipotecaSeguimiento` (`idHipotecaSeguimiento`);

--
-- Indices de la tabla `hipotecaseguimientomixta`
--
ALTER TABLE `hipotecaseguimientomixta`
  ADD PRIMARY KEY (`idHipotecaSeguimientoMixta`),
  ADD KEY `idHipotecaSeguimiento` (`idHipotecaSeguimiento`);

--
-- Indices de la tabla `hipotecaseguimientovar`
--
ALTER TABLE `hipotecaseguimientovar`
  ADD PRIMARY KEY (`idHipotecaSeguimientoVar`),
  ADD KEY `idHipotecaSeguimiento` (`idHipotecaSeguimiento`);

--
-- Indices de la tabla `impuestos`
--
ALTER TABLE `impuestos`
  ADD PRIMARY KEY (`idImpuesto`);

--
-- Indices de la tabla `usuarios`
--
ALTER TABLE `usuarios`
  ADD PRIMARY KEY (`idUsuario`);

--
-- Indices de la tabla `vinculaciones`
--
ALTER TABLE `vinculaciones`
  ADD PRIMARY KEY (`idVinculacion`),
  ADD KEY `idBanco` (`idBanco`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `banco`
--
ALTER TABLE `banco`
  MODIFY `idBanco` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `comisiones`
--
ALTER TABLE `comisiones`
  MODIFY `idComision` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `gastos`
--
ALTER TABLE `gastos`
  MODIFY `idGasto` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `hipotecaofertada`
--
ALTER TABLE `hipotecaofertada`
  MODIFY `idHipotecaOfertada` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `hipotecaofertadafija`
--
ALTER TABLE `hipotecaofertadafija`
  MODIFY `idhipotecaOfertadaFija` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `hipotecaofertadamixta`
--
ALTER TABLE `hipotecaofertadamixta`
  MODIFY `idhipotecaOfertadaMixta` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `hipotecaofertadavariable`
--
ALTER TABLE `hipotecaofertadavariable`
  MODIFY `idHipotecaOfertadaVariable` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `hipotecaseguimiento`
--
ALTER TABLE `hipotecaseguimiento`
  MODIFY `idHipotecaSeguimiento` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `hipotecaseguimientofija`
--
ALTER TABLE `hipotecaseguimientofija`
  MODIFY `idHipotecaSeguimientoFija` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `hipotecaseguimientomixta`
--
ALTER TABLE `hipotecaseguimientomixta`
  MODIFY `idHipotecaSeguimientoMixta` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `hipotecaseguimientovar`
--
ALTER TABLE `hipotecaseguimientovar`
  MODIFY `idHipotecaSeguimientoVar` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `impuestos`
--
ALTER TABLE `impuestos`
  MODIFY `idImpuesto` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `usuarios`
--
ALTER TABLE `usuarios`
  MODIFY `idUsuario` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT de la tabla `vinculaciones`
--
ALTER TABLE `vinculaciones`
  MODIFY `idVinculacion` int(11) NOT NULL AUTO_INCREMENT;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `comisiones`
--
ALTER TABLE `comisiones`
  ADD CONSTRAINT `comisiones_ibfk_1` FOREIGN KEY (`idhipotecaOfertada`) REFERENCES `hipotecaofertada` (`idHipotecaOfertada`),
  ADD CONSTRAINT `comisiones_ibfk_2` FOREIGN KEY (`idBanco`) REFERENCES `banco` (`idBanco`);

--
-- Filtros para la tabla `gastos`
--
ALTER TABLE `gastos`
  ADD CONSTRAINT `gastos_ibfk_1` FOREIGN KEY (`idBanco`) REFERENCES `banco` (`idBanco`);

--
-- Filtros para la tabla `hipotecaofertada`
--
ALTER TABLE `hipotecaofertada`
  ADD CONSTRAINT `hipotecaofertada_ibfk_1` FOREIGN KEY (`idBanco`) REFERENCES `banco` (`idBanco`);

--
-- Filtros para la tabla `hipotecaofertadafija`
--
ALTER TABLE `hipotecaofertadafija`
  ADD CONSTRAINT `hipotecaofertadafija_ibfk_1` FOREIGN KEY (`idhipotecaOfertada`) REFERENCES `hipotecaofertada` (`idHipotecaOfertada`);

--
-- Filtros para la tabla `hipotecaofertadamixta`
--
ALTER TABLE `hipotecaofertadamixta`
  ADD CONSTRAINT `hipotecaofertadamixta_ibfk_1` FOREIGN KEY (`idhipotecaOfertada`) REFERENCES `hipotecaofertada` (`idHipotecaOfertada`);

--
-- Filtros para la tabla `hipotecaofertadavariable`
--
ALTER TABLE `hipotecaofertadavariable`
  ADD CONSTRAINT `hipotecaofertadavariable_ibfk_1` FOREIGN KEY (`idHipotecaOfertada`) REFERENCES `hipotecaofertada` (`idHipotecaOfertada`);

--
-- Filtros para la tabla `hipotecaseguimiento`
--
ALTER TABLE `hipotecaseguimiento`
  ADD CONSTRAINT `hipotecaseguimiento_ibfk_1` FOREIGN KEY (`idUsuario`) REFERENCES `usuarios` (`idUsuario`);

--
-- Filtros para la tabla `hipotecaseguimientofija`
--
ALTER TABLE `hipotecaseguimientofija`
  ADD CONSTRAINT `hipotecaseguimientofija_ibfk_1` FOREIGN KEY (`idHipotecaSeguimiento`) REFERENCES `hipotecaseguimiento` (`idHipotecaSeguimiento`);

--
-- Filtros para la tabla `hipotecaseguimientomixta`
--
ALTER TABLE `hipotecaseguimientomixta`
  ADD CONSTRAINT `hipotecaseguimientomixta_ibfk_1` FOREIGN KEY (`idHipotecaSeguimiento`) REFERENCES `hipotecaseguimiento` (`idHipotecaSeguimiento`);

--
-- Filtros para la tabla `hipotecaseguimientovar`
--
ALTER TABLE `hipotecaseguimientovar`
  ADD CONSTRAINT `hipotecaseguimientovar_ibfk_1` FOREIGN KEY (`idHipotecaSeguimiento`) REFERENCES `hipotecaseguimiento` (`idHipotecaSeguimiento`);

--
-- Filtros para la tabla `vinculaciones`
--
ALTER TABLE `vinculaciones`
  ADD CONSTRAINT `vinculaciones_ibfk_1` FOREIGN KEY (`idBanco`) REFERENCES `banco` (`idBanco`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
