-- ##################################################################################################################################
-- ############################################################ USUARIOS ############################################################
-- ##################################################################################################################################

-- ------------------------------------------------- Agregar Usuario -------------------------------------------------
DELIMITER $$
CREATE PROCEDURE sp_AgregarUsuario(
    IN p_nombreUsuario VARCHAR(64),
    IN p_apellidoUsuario VARCHAR(64),
    IN p_correoUsuario VARCHAR(128),
    IN p_contrasenaUsuario VARCHAR(255)
)
BEGIN
    INSERT INTO Usuarios(nombreUsuario, apellidoUsuario, correoUsuario, contrasenaUsuario)
    VALUES (p_nombreUsuario, p_apellidoUsuario, p_correoUsuario, p_contrasenaUsuario);
END$$
DELIMITER ;

-- ------------------------------------------------- Listar Usuarios -------------------------------------------------
DELIMITER $$
CREATE PROCEDURE sp_ListarUsuarios()
BEGIN
    SELECT * FROM Usuarios;
END$$
DELIMITER ;

-- ------------------------------------------------- Buscar Usuario -------------------------------------------------
DELIMITER $$
CREATE PROCEDURE sp_BuscarUsuario(IN p_idUsuario INT)
BEGIN
    SELECT * FROM Usuarios WHERE idUsuario = p_idUsuario;
END$$
DELIMITER ;

-- ------------------------------------------------- Editar Usuario -------------------------------------------------
DELIMITER $$
CREATE PROCEDURE sp_EditarUsuario(
    IN p_idUsuario INT,
    IN p_nombreUsuario VARCHAR(64),
    IN p_apellidoUsuario VARCHAR(64),
    IN p_correoUsuario VARCHAR(128),
    IN p_contrasenaUsuario VARCHAR(255),
    IN p_estadoUsuario ENUM('Activo', 'Inactivo')
)
BEGIN
    UPDATE Usuarios
    SET
        nombreUsuario = p_nombreUsuario,
        apellidoUsuario = p_apellidoUsuario,
        correoUsuario = p_correoUsuario,
        contrasenaUsuario = p_contrasenaUsuario,
        estadoUsuario = p_estadoUsuario
    WHERE idUsuario = p_idUsuario;
END$$
DELIMITER ;

-- ------------------------------------------------- Eliminar Usuario -------------------------------------------------
DELIMITER $$
CREATE PROCEDURE sp_EliminarUsuario(IN p_idUsuario INT)
BEGIN
    DELETE FROM Usuarios WHERE idUsuario = p_idUsuario;
END$$
DELIMITER ;

-- ##################################################################################################################################
-- ########################################################### CATEGORIAS ###########################################################
-- ##################################################################################################################################

-- ------------------------------------------------- Agregar Categoria -------------------------------------------------
DELIMITER $$
CREATE PROCEDURE sp_AgregarCategoria(IN p_nombreCategoria VARCHAR(64))
BEGIN
    INSERT INTO Categorias(nombreCategoria) VALUES (p_nombreCategoria);
END$$
DELIMITER ;

-- ------------------------------------------------- Listar Categorias -------------------------------------------------
DELIMITER $$
CREATE PROCEDURE sp_ListarCategorias()
BEGIN
    SELECT * FROM Categorias;
END$$
DELIMITER ;

-- ------------------------------------------------- Buscar Categoria -------------------------------------------------
DELIMITER $$
CREATE PROCEDURE sp_BuscarCategoria(IN p_idCategoria INT)
BEGIN
    SELECT * FROM Categorias WHERE idCategoria = p_idCategoria;
END$$
DELIMITER ;

-- ------------------------------------------------- Editar Categoria -------------------------------------------------
DELIMITER $$
CREATE PROCEDURE sp_EditarCategoria(
    IN p_idCategoria INT,
    IN p_nombreCategoria VARCHAR(64)
)
BEGIN
    UPDATE Categorias
    SET
        nombreCategoria = p_nombreCategoria
    WHERE idCategoria = p_idCategoria;
END$$
DELIMITER ;

-- ------------------------------------------------- Eliminar Categoria -------------------------------------------------
DELIMITER $$
CREATE PROCEDURE sp_EliminarCategoria(IN p_idCategoria INT)
BEGIN
    DELETE FROM Categorias WHERE idCategoria = p_idCategoria;
END$$
DELIMITER ;

-- ##################################################################################################################################
-- ########################################################### PRODUCTOS ############################################################
-- ##################################################################################################################################

-- ------------------------------------------------- Agregar Producto -------------------------------------------------
DELIMITER $$
CREATE PROCEDURE sp_AgregarProducto(
    IN p_nombreProducto VARCHAR(128),
    IN p_cantidadProducto INT,
    IN p_precioProducto DECIMAL(10, 2),
    IN p_estadoProducto ENUM("Disponible", "Indisponible"),
    IN p_idCategoria INT
)
BEGIN
    INSERT INTO Productos(nombreProducto, cantidadProducto, precioProducto, estadoProducto, idCategoria)
    VALUES (p_nombreProducto, p_cantidadProducto, p_precioProducto, p_estadoProducto, p_idCategoria);
END$$
DELIMITER ;

-- ------------------------------------------------- Listar Productos -------------------------------------------------
DELIMITER $$
CREATE PROCEDURE sp_ListarProductos()
BEGIN
    SELECT * FROM Productos;
END$$
DELIMITER ;

-- ------------------------------------------------- Buscar Producto -------------------------------------------------
DELIMITER $$
CREATE PROCEDURE sp_BuscarProducto(IN p_idProducto INT)
BEGIN
    SELECT * FROM Productos WHERE idProducto = p_idProducto;
END$$
DELIMITER ;

-- ------------------------------------------------- Editar Producto -------------------------------------------------
DELIMITER $$
CREATE PROCEDURE sp_EditarProducto(
    IN p_idProducto INT,
    IN p_nombreProducto VARCHAR(128),
    IN p_cantidadProducto INT,
    IN p_precioProducto DECIMAL(10, 2),
    IN p_estadoProducto ENUM("Disponible", "Indisponible"),
    IN p_idCategoria INT
)
BEGIN
    UPDATE Productos
    SET
        nombreProducto = p_nombreProducto,
        cantidadProducto = p_cantidadProducto,
        precioProducto = p_precioProducto,
        estadoProducto = p_estadoProducto,
        idCategoria = p_idCategoria
    WHERE idProducto = p_idProducto;
END$$
DELIMITER ;

-- ------------------------------------------------- Eliminar Producto -------------------------------------------------
DELIMITER $$
CREATE PROCEDURE sp_EliminarProducto(IN p_idProducto INT)
BEGIN
    DELETE FROM Productos WHERE idProducto = p_idProducto;
END$$
DELIMITER ;

-- ##################################################################################################################################
-- ############################################################ CARRITOS ############################################################
-- ##################################################################################################################################

-- ------------------------------------------------- Agregar Carrito -------------------------------------------------
DELIMITER $$
CREATE PROCEDURE sp_AgregarCarrito(
    IN p_idUsuario INT
)
BEGIN
    INSERT INTO Carritos(idUsuario) VALUES (p_idUsuario);
END$$
DELIMITER ;

-- ------------------------------------------------- Listar Carritos -------------------------------------------------
DELIMITER $$
CREATE PROCEDURE sp_ListarCarritos()
BEGIN
    SELECT * FROM Carritos;
END$$
DELIMITER ;

-- ------------------------------------------------- Buscar Carrito -------------------------------------------------
DELIMITER $$
CREATE PROCEDURE sp_BuscarCarrito(IN p_idCarrito INT)
BEGIN
    SELECT * FROM Carritos WHERE idCarrito = p_idCarrito;
END$$
DELIMITER ;

-- ------------------------------------------------- Editar Carrito -------------------------------------------------
DELIMITER $$
CREATE PROCEDURE sp_EditarCarrito(
    IN p_idCarrito INT,
    IN p_estado ENUM('Activo', 'Inactivo'),
    IN p_idUsuario INT
)
BEGIN
    UPDATE Carritos
    SET
        estado = p_estado,
        idUsuario = p_idUsuario
    WHERE idCarrito = p_idCarrito;
END$$
DELIMITER ;

-- ------------------------------------------------- Eliminar Carrito -------------------------------------------------
DELIMITER $$
CREATE PROCEDURE sp_EliminarCarrito(IN p_idCarrito INT)
BEGIN
    DELETE FROM Carritos WHERE idCarrito = p_idCarrito;
END$$
DELIMITER ;

-- ##################################################################################################################################
-- ######################################################## DETALLE CARRITOS ########################################################
-- ##################################################################################################################################

-- ------------------------------------------------- Agregar DetalleCarrito -------------------------------------------------
DELIMITER $$
CREATE PROCEDURE sp_AgregarDetalleCarrito(
    IN p_idCarrito INT,
    IN p_idProducto INT,
    IN p_cantidad INT
)
BEGIN
    INSERT INTO DetalleCarritos(idCarrito, idProducto, cantidad)
    VALUES (p_idCarrito, p_idProducto, p_cantidad);
END$$
DELIMITER ;

-- ------------------------------------------------- Listar DetalleCarritos -------------------------------------------------
DELIMITER $$
CREATE PROCEDURE sp_ListarDetalleCarritos()
BEGIN
    SELECT * FROM DetalleCarritos;
END$$
DELIMITER ;

-- ------------------------------------------------- Buscar DetalleCarrito -------------------------------------------------
DELIMITER $$
CREATE PROCEDURE sp_BuscarDetalleCarrito(IN p_idDetalleCarrito INT)
BEGIN
    SELECT * FROM DetalleCarritos WHERE idDetalleCarrito = p_idDetalleCarrito;
END$$
DELIMITER ;

-- ------------------------------------------------- Editar DetalleCarrito -------------------------------------------------
DELIMITER $$
CREATE PROCEDURE sp_EditarDetalleCarrito(
    IN p_idDetalleCarrito INT,
    IN p_idCarrito INT,
    IN p_idProducto INT,
    IN p_cantidad INT
)
BEGIN
    UPDATE DetalleCarritos
    SET
        idCarrito = p_idCarrito,
        idProducto = p_idProducto,
        cantidad = p_cantidad
    WHERE idDetalleCarrito = p_idDetalleCarrito;
END$$
DELIMITER ;

-- ------------------------------------------------- Eliminar DetalleCarrito -------------------------------------------------
DELIMITER $$
CREATE PROCEDURE sp_EliminarDetalleCarrito(IN p_idDetalleCarrito INT)
BEGIN
    DELETE FROM DetalleCarritos WHERE idDetalleCarrito = p_idDetalleCarrito;
END$$
DELIMITER ;

-- ##################################################################################################################################
-- ############################################################ FACTURAS ############################################################
-- ##################################################################################################################################

-- ------------------------------------------------- Agregar Factura -------------------------------------------------
DELIMITER $$
CREATE PROCEDURE sp_AgregarFactura(
    IN p_estadoFactura ENUM("Activa", "Cancelada"),
    IN p_idCarrito INT,
    IN p_idUsuario INT
)
BEGIN
    INSERT INTO Facturas(estadoFactura, idCarrito, idUsuario)
    VALUES (p_estadoFactura, p_idCarrito, p_idUsuario);
END$$
DELIMITER ;

-- ------------------------------------------------- Listar Facturas -------------------------------------------------
DELIMITER $$
CREATE PROCEDURE sp_ListarFacturas()
BEGIN
    SELECT * FROM Facturas;
END$$
DELIMITER ;

-- ------------------------------------------------- Buscar Factura -------------------------------------------------
DELIMITER $$
CREATE PROCEDURE sp_BuscarFactura(IN p_idFactura INT)
BEGIN
    SELECT * FROM Facturas WHERE idFactura = p_idFactura;
END$$
DELIMITER ;

-- ------------------------------------------------- Editar Factura -------------------------------------------------
DELIMITER $$
CREATE PROCEDURE sp_EditarFactura(
    IN p_idFactura INT,
    IN p_estadoFactura ENUM("Activa", "Cancelada"),
    IN p_idCarrito INT,
    IN p_idUsuario INT
)
BEGIN
    UPDATE Facturas
    SET
        estadoFactura = p_estadoFactura,
        idCarrito = p_idCarrito,
        idUsuario = p_idUsuario
    WHERE idFactura = p_idFactura;
END$$
DELIMITER ;

-- ------------------------------------------------- Eliminar Factura -------------------------------------------------
DELIMITER $$
CREATE PROCEDURE sp_EliminarFactura(IN p_idFactura INT)
BEGIN
    DELETE FROM Facturas WHERE idFactura = p_idFactura;
END$$
DELIMITER ;

-- ##################################################################################################################################
-- ####################################################### DETALLE FACTURAS #########################################################
-- ##################################################################################################################################

-- ------------------------------------------------- Agregar DetalleFactura -------------------------------------------------
DELIMITER $$
CREATE PROCEDURE sp_AgregarDetalleFactura(
    IN p_cantidadPedida INT,
    IN p_precioUnitario DECIMAL(10, 2),
    IN p_idFactura INT,
    IN p_idProducto INT
)
BEGIN
    INSERT INTO DetalleFacturas(cantidadPedida, precioUnitario, idFactura, idProducto)
    VALUES (p_cantidadPedida, p_precioUnitario, p_idFactura, p_idProducto);
END$$
DELIMITER ;

-- ------------------------------------------------- Listar DetalleFacturas -------------------------------------------------
DELIMITER $$
CREATE PROCEDURE sp_ListarDetalleFacturas()
BEGIN
    SELECT * FROM DetalleFacturas;
END$$
DELIMITER ;

-- ------------------------------------------------- Buscar DetalleFactura -------------------------------------------------
DELIMITER $$
CREATE PROCEDURE sp_BuscarDetalleFactura(IN p_idDetalleFactura INT)
BEGIN
    SELECT * FROM DetalleFacturas WHERE idDetalleFactura = p_idDetalleFactura;
END$$
DELIMITER ;

-- ------------------------------------------------- Editar DetalleFactura -------------------------------------------------
DELIMITER $$
CREATE PROCEDURE sp_EditarDetalleFactura(
    IN p_idDetalleFactura INT,
    IN p_cantidadPedida INT,
    IN p_precioUnitario DECIMAL(10, 2),
    IN p_idFactura INT,
    IN p_idProducto INT
)
BEGIN
    UPDATE DetalleFacturas
    SET
        cantidadPedida = p_cantidadPedida,
        precioUnitario = p_precioUnitario,
        idFactura = p_idFactura,
        idProducto = p_idProducto
    WHERE idDetalleFactura = p_idDetalleFactura;
END$$
DELIMITER ;

-- ------------------------------------------------- Eliminar DetalleFactura -------------------------------------------------
DELIMITER $$
CREATE PROCEDURE sp_EliminarDetalleFactura(IN p_idDetalleFactura INT)
BEGIN
    DELETE FROM DetalleFacturas WHERE idDetalleFactura = p_idDetalleFactura;
END$$
DELIMITER ;


DELIMITER $$
	create procedure sp_contarClientes()
		begin
			select concat(count(*), ' CLIENTES ACTIVOS') as RESULTADO from Usuarios;
		end $$
DELIMITER ;
DELIMITER $$
	create procedure sp_contarCarritos()
		begin
			select concat(count(*), ' CARRITOS ACTIVOS') as RESULTADO from Carritos;
		end $$
DELIMITER ;

USE KinalFamiliarDB;

-- Insertar datos en Usuarios
CALL sp_AgregarUsuario('Juan', 'Pérez', 'juan.perez@example.com', 'password123');
CALL sp_AgregarUsuario('María', 'García', 'maria.garcia@example.com', 'password123');
CALL sp_AgregarUsuario('Carlos', 'López', 'carlos.lopez@example.com', 'password123');
CALL sp_AgregarUsuario('Ana', 'Martínez', 'ana.martinez@example.com', 'password123');
CALL sp_AgregarUsuario('Luis', 'Hernández', 'luis.hernandez@example.com', 'password123');
CALL sp_AgregarUsuario('Elena', 'González', 'elena.gonzalez@example.com', 'password123');
CALL sp_AgregarUsuario('Javier', 'Rodríguez', 'javier.rodriguez@example.com', 'password123');
CALL sp_AgregarUsuario('Sofia', 'Sánchez', 'sofia.sanchez@example.com', 'password123');
CALL sp_AgregarUsuario('Miguel', 'Ramírez', 'miguel.ramirez@example.com', 'password123');
CALL sp_AgregarUsuario('Laura', 'Torres', 'laura.torres@example.com', 'password123');

-- Insertar datos en Categorias
CALL sp_AgregarCategoria('Electrónicos');
CALL sp_AgregarCategoria('Ropa');
CALL sp_AgregarCategoria('Hogar');
CALL sp_AgregarCategoria('Juguetes');
CALL sp_AgregarCategoria('Alimentos');
CALL sp_AgregarCategoria('Deportes');
CALL sp_AgregarCategoria('Libros');
CALL sp_AgregarCategoria('Salud y Belleza');
CALL sp_AgregarCategoria('Automotriz');
CALL sp_AgregarCategoria('Mascotas');

-- Insertar datos en Productos
CALL sp_AgregarProducto('Laptop Gamer', 15, 1200.50, 'Disponible', 1);
CALL sp_AgregarProducto('Camiseta de Algodón', 100, 25.00, 'Disponible', 2);
CALL sp_AgregarProducto('Sofá de Tres Plazas', 10, 450.75, 'Disponible', 3);
CALL sp_AgregarProducto('Lego Star Wars', 50, 89.99, 'Disponible', 4);
CALL sp_AgregarProducto('Arroz Blanco 1kg', 200, 1.50, 'Disponible', 5);
CALL sp_AgregarProducto('Balón de Fútbol', 80, 19.95, 'Disponible', 6);
CALL sp_AgregarProducto('Cien Años de Soledad', 120, 22.00, 'Disponible', 7);
CALL sp_AgregarProducto('Shampoo Anticaspa', 90, 8.50, 'Disponible', 8);
CALL sp_AgregarProducto('Aceite para Motor 5W-30', 40, 35.20, 'Disponible', 9);
CALL sp_AgregarProducto('Comida para Perro 15kg', 60, 55.00, 'Disponible', 10);

-- Insertar datos en Carritos
CALL sp_AgregarCarrito(1);
CALL sp_AgregarCarrito(2);
CALL sp_AgregarCarrito(3);
CALL sp_AgregarCarrito(4);
CALL sp_AgregarCarrito(5);
CALL sp_AgregarCarrito(6);
CALL sp_AgregarCarrito(7);
CALL sp_AgregarCarrito(8);
CALL sp_AgregarCarrito(9);
CALL sp_AgregarCarrito(10);

-- Insertar datos en DetalleCarritos
CALL sp_AgregarDetalleCarrito(1, 1, 1);
CALL sp_AgregarDetalleCarrito(1, 5, 3);
CALL sp_AgregarDetalleCarrito(2, 2, 2);
CALL sp_AgregarDetalleCarrito(3, 3, 1);
CALL sp_AgregarDetalleCarrito(4, 4, 1);
CALL sp_AgregarDetalleCarrito(5, 6, 1);
CALL sp_AgregarDetalleCarrito(6, 7, 5);
CALL sp_AgregarDetalleCarrito(7, 8, 2);
CALL sp_AgregarDetalleCarrito(8, 9, 1);
CALL sp_AgregarDetalleCarrito(9, 10, 1);

-- Insertar datos en Facturas
CALL sp_AgregarFactura('Activa', 1, 1);
CALL sp_AgregarFactura('Activa', 2, 2);
CALL sp_AgregarFactura('Activa', 3, 3);
CALL sp_AgregarFactura('Activa', 4, 4);
CALL sp_AgregarFactura('Activa', 5, 5);
CALL sp_AgregarFactura('Cancelada', 6, 6);
CALL sp_AgregarFactura('Activa', 7, 7);
CALL sp_AgregarFactura('Activa', 8, 8);
CALL sp_AgregarFactura('Activa', 9, 9);
CALL sp_AgregarFactura('Activa', 10, 10);

-- Insertar datos en DetalleFacturas
CALL sp_AgregarDetalleFactura(1, 1200.50, 1, 1);
CALL sp_AgregarDetalleFactura(3, 1.50, 1, 5);
CALL sp_AgregarDetalleFactura(2, 25.00, 2, 2);
CALL sp_AgregarDetalleFactura(1, 450.75, 3, 3);
CALL sp_AgregarDetalleFactura(1, 89.99, 4, 4);
CALL sp_AgregarDetalleFactura(1, 19.95, 5, 6);
CALL sp_AgregarDetalleFactura(5, 22.00, 7, 7);
CALL sp_AgregarDetalleFactura(2, 8.50, 8, 8);
CALL sp_AgregarDetalleFactura(1, 35.20, 9, 9);
CALL sp_AgregarDetalleFactura(1, 55.00, 10, 10);