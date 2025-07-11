drop database if exists KinalFamiliarDB;
create database KinalFamiliarDB;
use KinalFamiliarDB;

create table Usuarios(
	idUsuario int auto_increment,
    nombreUsuario varchar(64) not null,
    apellidoUsuario varchar(64) not null,
    correoUsuario varchar(128) not null unique,
	username varchar(32) not null unique,
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




