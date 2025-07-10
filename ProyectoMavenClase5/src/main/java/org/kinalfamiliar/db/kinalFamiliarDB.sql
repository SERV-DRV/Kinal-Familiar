drop database if exists KinalFamiliarDB;
create database KinalFamiliarDB;
use KinalFamiliarDB;

/*
create table Usuarios(
	idUsuario int auto_increment,
    nombreUsuario varchar(64) not null,
    apellidoUsuario varchar(64) not null,
    correoUsuario varchar(128) not null,
    contrasenaUsuario varchar(16) not null,
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
    nombreProducto varchar(128) not null,
    cantidadProducto int not null,
    precioProducto decimal(10,2) not null,
    estadoProducto enum("Disponible", "Indisponible"),
    idCategoria int not null,
    constraint FK_productos_categorias foreign key(idCategoria)
		references Categorias(idCategoria),
    constraint PK_productos primary key(idProducto)
);

-- ________________________________ CRUD ________________________________ --

-- ________________________ Categorias ________________________ --

-- Insertar Categorias
delimiter //
	create procedure sp_InsertarCategoria(
		in p_nombreCategoria varchar(64)
        )
		begin
			insert into Categorias(nombreCategoria)
				values(p_nombreCategoria);
        end//
delimiter ;

-- Listar Categorias
delimiter //
	create procedure sp_ListarCategorias()
		begin
			select 
				CT.idCategoria as ID,
				CT.nombreCategoria as Nombre
			from Categorias CT;
        end//
delimiter ;

-- Actualizar Categorias
delimiter //
	create procedure sp_ActualizarCategoria(
		in p_idCategoria int,
		in p_nombreCategoria varchar(64)
        )
		begin
			update Categorias CT
				set
					CT.nombreCategoria = p_nombreCategoria
			where CT.idCategoria = p_idCategoria;
        end//
delimiter ;

-- Eliminar Categorias
delimiter //
	create procedure sp_EliminarCategoria(in p_idCategoria int)
		begin
			delete from Categorias
				where idCategoria = p_idCategoria;
        end//
delimiter ;

-- Buscar Categorias
delimiter //
	create procedure sp_BuscarCategoria(in p_idCategoria int)
		begin
			select 
				CT.idCategoria as ID,
				CT.nombreCategoria as Nombre
			from Categorias CT
				where CT.idCategoria = p_idCategoria;
        end//
delimiter ;


-- ________________________ Productos ________________________ -- 

-- Insertar Productos
delimiter //
	create procedure sp_InsertarProducto(
		in p_nombreProducto varchar(128),
		in p_cantidadProducto int,
		in p_precioProducto decimal(10,2),
		in p_estadoProducto enum("Disponible", "Indisponible"),
		in p_idCategoria int
        )
		begin
			insert into Productos(nombreProducto, cantidadProducto, 
				precioProducto, estadoProducto, idCategoria)
				values(p_nombreProducto, p_cantidadProducto, 
					p_precioProducto, p_estadoProducto, p_idCategoria);
        end//
delimiter ;

-- Listar Productos
delimiter //
	create procedure sp_ListarProductos()
		begin
			select 
				PD.idProducto as ID,
				PD.nombreProducto as Nombre,
				PD.cantidadProducto as Cantidad,
				PD.precioProducto as Precio,
				PD.estadoProducto as Estado,
				PD.idCategoria as CategoriaID
			from Productos PD;
        end//
delimiter ;

-- Actualizar Productos
delimiter //
	create procedure sp_ActualizarProducto(
		in p_idProducto int,
		in p_nombreProducto varchar(128),
		in p_cantidadProducto int,
		in p_precioProducto decimal(10,2),
		in p_estadoProducto enum("Disponible", "Indisponible"),
		in p_idCategoria int
        )
		begin
			update Productos PD
				set
					PD.nombreProducto = p_nombreProducto,
					PD.cantidadProducto = p_cantidadProducto,
					PD.precioProducto = p_precioProducto,
					PD.estadoProducto = p_estadoProducto,
					PD.idCategoria = p_idCategoria
			where PD.idProducto = p_idProducto;
        end//
delimiter ;

-- Eliminar Productos
delimiter //
	create procedure sp_EliminarProducto(in p_idProducto int)
		begin
			delete from Productos
				where idProducto = p_idProducto;
        end//
delimiter ;

-- Buscar Productos
delimiter //
	create procedure sp_BuscarProducto(in p_idProducto int)
		begin
			select 
				PD.idProducto as ID,
				PD.nombreProducto as Nombre,
				PD.cantidadProducto as Cantidad,
				PD.precioProducto as Precio,
				PD.estadoProducto as Estado,
				PD.idCategoria as CategoriaID
			from Productos PD
				where PD.idProducto = p_idProducto;
        end//
delimiter ;

-- _____ _____ _____ _____ _____ DATOS _____ _____ _____ _____ _____

-- DATOS CATEGORIAS
CALL sp_InsertarCategoria('Libros de Texto');
CALL sp_InsertarCategoria('Cuadernos');
CALL sp_InsertarCategoria('Escritura');
CALL sp_InsertarCategoria('Dibujo y Arte');
CALL sp_InsertarCategoria('Papelería General');
CALL sp_InsertarCategoria('Mochilas y Estuches');
CALL sp_InsertarCategoria('Educación Infantil');
CALL sp_InsertarCategoria('Matemáticas y Geometría');
CALL sp_InsertarCategoria('Tecnología Educativa');
CALL sp_InsertarCategoria('Diccionarios y Enciclopedias');
CALL sp_InsertarCategoria('Papeles Especiales');
CALL sp_InsertarCategoria('Material de Oficina');
CALL sp_InsertarCategoria('Accesorios Escolares');
CALL sp_InsertarCategoria('Lectura Juvenil');
CALL sp_InsertarCategoria('Lectura Infantil');

-- DATOS PRODUCTOS
CALL sp_InsertarProducto('Libro Matemáticas 5to grado', 50, 90.00, 'Disponible', 1);
CALL sp_InsertarProducto('Cuaderno Profesional 100 hojas', 200, 15.00, 'Disponible', 2);
CALL sp_InsertarProducto('Bolígrafo Bic Azul', 500, 2.50, 'Disponible', 3);
CALL sp_InsertarProducto('Caja de crayones Crayola', 120, 18.00, 'Disponible', 4);
CALL sp_InsertarProducto('Resma de papel carta', 80, 45.00, 'Disponible', 5);
CALL sp_InsertarProducto('Mochila Escolar Spiderman', 35, 120.00, 'Disponible', 6);
CALL sp_InsertarProducto('Libro para colorear', 60, 12.00, 'Disponible', 7);
CALL sp_InsertarProducto('Juego de geometría', 100, 25.00, 'Disponible', 8);
CALL sp_InsertarProducto('Calculadora científica Casio', 40, 195.00, 'Disponible', 9);
CALL sp_InsertarProducto('Diccionario Español-Inglés', 55, 85.00, 'Disponible', 10);
CALL sp_InsertarProducto('Cartulina brillante', 300, 5.00, 'Disponible', 11);
CALL sp_InsertarProducto('Grapadora metálica', 25, 30.00, 'Disponible', 12);
CALL sp_InsertarProducto('Tijeras escolares', 150, 8.00, 'Disponible', 13);
CALL sp_InsertarProducto('Libro Harry Potter y la Piedra Filosofal', 20, 160.00, 'Disponible', 14);
CALL sp_InsertarProducto('Cuento ilustrado La Oruga Muy Hambrienta', 40, 95.00, 'Disponible', 15);




