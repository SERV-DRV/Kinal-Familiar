-- ________________________________ CRUD ________________________________ --

-- ________________________ Usuarios ________________________ --

-- Insertar Usuario
delimiter //
	create procedure sp_insertarUsuario(
		in p_nombreUsuario varchar(64),
		in p_apellidoUsuario varchar(64),
		in p_correoUsuario varchar(128),
		in p_contrasenaUsuario varchar(255)
		)
		begin
			insert into Usuarios(nombreUsuario, apellidoUsuario, correoUsuario, contrasenaUsuario)
				values(p_nombreUsuario, p_apellidoUsuario, p_correoUsuario, p_contrasenaUsuario);
		end//
delimiter ;

-- Listar Usuarios
delimiter //
	create procedure sp_ListarUsuarios()
		begin
			select 
				U.idUsuario as ID,
				U.nombreUsuario as Nombre,
				U.apellidoUsuario as Apellido
			from Usuarios U;
		end//
delimiter ;

-- Actualizar Usuario
delimiter //
	create procedure sp_ActualizarUsuario(
		in p_idUsuario int,
		in p_nombreUsuario varchar(64),
		in p_apellidoUsuario varchar(64),
		in p_correoUsuario varchar(128),
		in p_contrasenaUsuario varchar(255),
		in p_estadoUsuario enum('Activo', 'Inactivo')
		)
		begin
			update Usuarios U
				set 
					U.nombreUsuario = p_nombreUsuario,
					U.apellidoUsuario = p_apellidoUsuario,
					U.correoUsuario = p_correoUsuario,
					U.contrasenaUsuario = p_contrasenaUsuario,
					U.estadoUsuario = p_estadoUsuario
			where U.idUsuario = p_idUsuario;
		end//
delimiter ;

-- Eliminar Usuario
delimiter //
	create procedure sp_EliminarUsuario(in p_idUsuario int)
		begin
			delete from Usuarios
				where idUsuario = p_idUsuario;
		end//
delimiter ;

-- Buscar Usuario
delimiter //
	create procedure sp_BuscarUsuario(in p_idUsuario int)
		begin
			select 
				U.idUsuario as ID,
				U.nombreUsuario as Nombre,
				U.apellidoUsuario as Apellido,
				U.correoUsuario as Correo,
				U.estadoUsuario as Estado,
				U.fechaRegistro as Fecha
			from Usuarios U
				where U.idUsuario = p_idUsuario;
		end//
delimiter ;

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

-- ________________________ Carritos ________________________ -- 

-- Insertar Carrito
delimiter //
	create procedure sp_InsertarCarrito(
		in p_estado enum('Activo', 'Inactivo'),
		in p_idUsuario int
		)
		begin
			insert into Carritos(estado, idUsuario)
				values(p_estado, p_idUsuario);
		end//
delimiter ;

-- Listar Carritos
delimiter //
	create procedure sp_ListarCarritos()
		begin
			select 
				CA.idCarrito as ID,
				CA.estado as Estado,
				CA.fechaCreacion as Fecha,
				CA.idUsuario as UsuarioID
			from Carritos CA;
		end//
delimiter ;

-- Actualizar Carrito
delimiter //
	create procedure sp_ActualizarCarrito(
		in p_idCarrito int,
		in p_estado enum('Activo', 'Inactivo'),
		in p_idUsuario int
		)
		begin
			update Carritos CA
				set 
					CA.estado = p_estado,
					CA.idUsuario = p_idUsuario
			where CA.idCarrito = p_idCarrito;
		end//
delimiter ;

-- Eliminar Carrito
delimiter //
	create procedure sp_EliminarCarrito(in p_idCarrito int)
		begin
			delete from Carritos
				where idCarrito = p_idCarrito;
		end//
delimiter ;

-- Buscar Carrito
delimiter //
	create procedure sp_BuscarCarrito(in p_idCarrito int)
		begin
			select 
				CA.idCarrito as ID,
				CA.estado as Estado,
				CA.fechaCreacion as Fecha,
				CA.idUsuario as UsuarioID
			from Carritos CA
				where CA.idCarrito = p_idCarrito;
		end//
delimiter ;

-- ________________________ Detalles Carritos ________________________ -- 

-- Insertar DetalleCarrito
delimiter //
	create procedure sp_InsertarDetalleCarrito(
		in p_idCarrito int,
		in p_idProducto int,
		in p_cantidad int
		)
		begin
			insert into DetalleCarritos(idCarrito, idProducto, cantidad)
				values(p_idCarrito, p_idProducto, p_cantidad);
		end//
delimiter ;

-- Listar DetalleCarritos
delimiter //
	create procedure sp_ListarDetalleCarritos()
		begin
			select 
				DC.idDetalleCarrito as ID,
				DC.idCarrito as CarritoID,
				DC.idProducto as ProductoID,
				DC.cantidad as Cantidad
			from DetalleCarritos DC;
		end//
delimiter ;

-- Actualizar DetalleCarrito
delimiter //
	create procedure sp_ActualizarDetalleCarrito(
		in p_idDetalleCarrito int,
		in p_idCarrito int,
		in p_idProducto int,
		in p_cantidad int
		)
		begin
			update DetalleCarritos DC
				set 
					DC.idCarrito = p_idCarrito,
					DC.idProducto = p_idProducto,
					DC.cantidad = p_cantidad
			where DC.idDetalleCarrito = p_idDetalleCarrito;
		end//
delimiter ;

-- Eliminar DetalleCarrito
delimiter //
create procedure sp_EliminarDetalleCarrito(in p_idDetalleCarrito int)
	begin
		delete from DetalleCarritos
			where idDetalleCarrito = p_idDetalleCarrito;
	end//
delimiter ;

-- Buscar DetalleCarrito
delimiter //
create procedure sp_BuscarDetalleCarrito(in p_idDetalleCarrito int)
	begin
		select 
			DC.idDetalleCarrito as ID,
			DC.idCarrito as CarritoID,
			DC.idProducto as ProductoID,
			DC.cantidad as Cantidad
		from DetalleCarritos DC
			where DC.idDetalleCarrito = p_idDetalleCarrito;
	end//
delimiter ;

-- _____ _____ _____ _____ _____ Login _____ _____ _____ _____ _____

delimiter //
create procedure sp_inicioSesion(
    in p_nombreUsuario varchar(100),
    in p_contrasenaUsuario varchar(100)
)
begin
    if exists (
        select 1 from Usuarios
        where nombreUsuario = p_nombreUsuario
        and contrasenaUsuario = p_contrasenaUsuario
    ) then
        select 1 as inicioValido;
    else
        select 0 as inicioInvalido;
    end if;
end//
delimiter ;

-- _____ _____ _____ _____ _____ DATOS _____ _____ _____ _____ _____

INSERT INTO Usuarios (nombreUsuario, apellidoUsuario, correoUsuario, contrasenaUsuario) VALUES
	( 'Juan','Pérez','juan.perez@example.com','contrasena123');

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

use kinalfamiliardb;
select * from Usuarios;