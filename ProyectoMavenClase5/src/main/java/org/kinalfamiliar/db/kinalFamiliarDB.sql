drop database if exists KinalFamiliarDB;
create database KinalFamiliarDB;
use KinalFamiliarDB;

/*
create table Usuarios(
	idUsuario int auto_increment,
    nombreUsuario varchar(64) not null,
    apellidoUsuario varchar(64) not null,
    correoUsuario varchar(128) not null,
    contrasaniaUsuario varchar(16) not null,
    constraint PK_usuarios primary key(idUsuarios)
);
*/

create table Categorias(
	idCategoria int auto_increment,
    nombreCategoria varchar(64) not null,
    constraint PK_categoria primary key(idCategoria)
);

create table Productos(
	idProducto int auto_increment,
    idCategoria int not null,
    nombreProducto varchar(128) not null,
    precioProducto decimal(10,2) not null,
    descripcionProducto varchar(256) not null,
    constraint FK_productos_categorias foreign key(idCategoria)
		references Categorias(idCategoria),
    constraint PK_productos primary key(idProducto)
);

delimiter //
	create procedure sp_InsertarCategoria(
		in p_nombreCategoria varchar(64)
		)
        begin
			insert into Categorias(nombreCategoria)
				values(p_nombreCategoria);
        end//
delimiter ;

delimiter //
	create procedure sp_InsertarProducto(
		in p_nombreProducto varchar(128),
        in p_precioProducto decimal(10,2),
        in p_descripcionProducto varchar(256),
        in p_idCategoria int
		)
        begin
			insert into Productos(nombreProducto, precioProducto, descripcionProducto, idCategoria)
				values(p_nombreProducto, p_precioProducto, p_descripcionProducto, p_idCategoria);
        end//
delimiter ;

call sp_InsertarCategoria(nombreCategoria);
call sp_InsertarProducto(nombreProducto, precioProducto, descripcionProducto, idCategoria);