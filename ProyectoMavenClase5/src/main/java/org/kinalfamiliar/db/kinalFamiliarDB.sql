DROP DATABASE IF EXISTS KinalFamiliarDB;
CREATE DATABASE KinalFamiliarDB;
USE KinalFamiliarDB;

CREATE TABLE Usuarios(
    idUsuario INT AUTO_INCREMENT,
    nombreUsuario VARCHAR(64) NOT NULL,
    apellidoUsuario VARCHAR(64) NOT NULL,
    correoUsuario VARCHAR(128) NOT NULL UNIQUE,
    contrasenaUsuario VARCHAR(255) NOT NULL,
    estadoUsuario ENUM('Activo', 'Inactivo') NOT NULL DEFAULT 'Activo',
    fechaRegistro DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT PK_usuarios PRIMARY KEY(idUsuario)
);

CREATE TABLE Categorias(
    idCategoria INT AUTO_INCREMENT,
    nombreCategoria VARCHAR(64) NOT NULL,
    CONSTRAINT PK_categoria PRIMARY KEY(idCategoria)
);

CREATE TABLE Productos(
    idProducto INT AUTO_INCREMENT,
    nombreProducto VARCHAR(128) NOT NULL,
    cantidadProducto INT NOT NULL,
    precioProducto DECIMAL(10, 2) NOT NULL,
    estadoProducto ENUM("Disponible", "Indisponible"),
    idCategoria INT NOT NULL,
    CONSTRAINT FK_productos_categorias FOREIGN KEY(idCategoria) REFERENCES Categorias(idCategoria),
    CONSTRAINT PK_productos PRIMARY KEY(idProducto)
);

CREATE TABLE Carritos(
    idCarrito INT AUTO_INCREMENT,
    estado ENUM('Activo', 'Inactivo') NOT NULL DEFAULT 'Activo',
    fechaCreacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    idUsuario INT NOT NULL,
    CONSTRAINT FK_carritos_usuarios FOREIGN KEY(idUsuario) REFERENCES Usuarios(idUsuario),
    CONSTRAINT PK_carritos PRIMARY KEY(idCarrito)
);

CREATE TABLE DetalleCarritos(
    idDetalleCarrito INT AUTO_INCREMENT,
    idCarrito INT NOT NULL,
    idProducto INT NOT NULL,
    cantidad INT NOT NULL,
    CONSTRAINT FK_detalleCarritos_carritos FOREIGN KEY(idCarrito) REFERENCES Carritos(idCarrito),
    CONSTRAINT FK_detalleCarritos_productos FOREIGN KEY(idProducto) REFERENCES Productos(idProducto),
    CONSTRAINT PK_detalleCarritos PRIMARY KEY(idDetalleCarrito)
);

CREATE TABLE Facturas (
    idFactura INT AUTO_INCREMENT,
    fechaFactura DATETIME DEFAULT CURRENT_TIMESTAMP,
    estadoFactura ENUM("Activa", "Cancelada"),
    idCarrito INT NOT NULL,
    idUsuario INT NOT NULL,
    CONSTRAINT FK_facturas_carritos FOREIGN KEY(idCarrito) REFERENCES Carritos(idCarrito),
    CONSTRAINT FK_facturas_usuarios FOREIGN KEY(idUsuario) REFERENCES Usuarios(idUsuario),
    CONSTRAINT PK_facturas PRIMARY KEY(idFactura)
);

CREATE TABLE DetalleFacturas (
    idDetalleFactura INT AUTO_INCREMENT,
    cantidadPedida INT NOT NULL,
    precioUnitario DECIMAL(10, 2) NOT NULL,
    idFactura INT NOT NULL,
    idProducto INT NOT NULL,
    CONSTRAINT FK_detalleFacturas_facturas FOREIGN KEY(idFactura) REFERENCES Facturas(idFactura),
    CONSTRAINT FK_detalleFacturas_productos FOREIGN KEY(idProducto) REFERENCES Productos(idProducto),
    CONSTRAINT PK_detallefacturas PRIMARY KEY(idDetalleFactura)
);