drop database if exists KinalFamiliarDB;
create database KinalFamiliarDB;
use KinalFamiliarDB;

create table Usuarios(
	idUsuario int auto_increment,
    nombreUsuario varchar(64) not null,
    apellidoUsuario varchar(64) not null,
    correoUsuario varchar(128) not null unique,
    contrasenaUsuario varchar(255) not null,    
    estadoUsuario enum('Activo', 'Inactivo') not null default 'Activo',
    fechaRegistro datetime not null default current_timestamp,
    constraint PK_usuarios primary key(idUsuario)
);

create table Categorias(
	idCategoria int auto_increment,
    nombreCategoria varchar(64) not null,
    constraint PK_categoria primary key(idCategoria)
);

create table Productos(
	idProducto int auto_increment,    
    nombreProducto varchar(128) not null,
    cantidadProducto int not null,
    precioProducto decimal(10,2) not null,
    estadoProducto enum("Disponible", "Indisponible"),
    idCategoria int not null,
    constraint FK_productos_categorias foreign key(idCategoria)
		references Categorias(idCategoria),
    constraint PK_productos primary key(idProducto)
);

create table Carritos(
	idCarrito int auto_increment,
    estado enum('Activo', 'Inactivo') not null default 'Activo',
    fechaCreacion datetime default current_timestamp,
    idUsuario int not null,
    constraint FK_carritos_usuarios foreign	key(idUsuario)
		references Usuarios(idUsuario),
    constraint PK_carritos primary key(idCarrito)
);

create table DetalleCarritos(
	idDetalleCarrito int auto_increment,
    idCarrito int not null,
    idProducto int not null,
    cantidad int not null,
    constraint FK_detalleCarritos_carritos foreign key(idCarrito)
		references Carritos(idCarrito),
    constraint FK_detalleCarritos_productos foreign key(idProducto)
		references Productos(idProducto),
    constraint PK_detalleCarritos primary key(idDetalleCarrito)
);

create table Facturas (
	idFactura int auto_increment,
    fechaFactura datetime default current_timestamp,
    estadoFactura enum("Activa", "Cancelada"),
    idCarrito int not null,
    idUsuario int not null,
    constraint FK_facturas_carritos foreign key(idCarrito)
		references Carritos(idCarritos),
    constraint FK_facturas_usuarios foreign key(idUsuario)
		references Usuarios(idUsuario),	
    constraint PK_facturas primary key(idFactura)
);
 
create table DetalleFacturas (
	idDetalleFactura int auto_increment,
    cantidadPedida int not null,
    precioUnitario decimal(10,2) not null,
    idFactura int not null,
    idProducto int not null,
    constraint FK_detalleFacturas_facturas foreign key(idFactura)
		references Facturas(idFactura),
	constraint FK_detalleFacturas_productos foreign key(idProducto)
		references Productos(idProducto),
    constraint PK_detallefacturas primary key(idDetalleFactura)
);
