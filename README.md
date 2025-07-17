# Proyecto: Kinal Familiar

## Descripción General

**Kinal Familiar** es una aplicación de escritorio desarrollada en JavaFX que funciona como un sistema de gestión de inventario y compras. Permite a los usuarios registrarse, iniciar sesión, administrar productos en un inventario y gestionar carritos de compra. La aplicación se conecta a una base de datos MySQL para la persistencia de los datos y utiliza JasperReports para la generación de reportes en PDF.

## Características Principales

- **Gestión de Usuarios**: Sistema completo de registro e inicio de sesión para los usuarios.
- **Sesión de Usuario**: Mantiene la sesión del usuario activa en las diferentes vistas, mostrando su información y personalizando la experiencia.
- **Módulo de Inventario**: Permite realizar operaciones CRUD (Crear, Leer, Actualizar, Eliminar) sobre los productos. Incluye:
  - Visualización de productos en una tabla.
  - Formulario para agregar y editar productos.
  - Funcionalidad de búsqueda de productos.
  - Generación de un reporte de inventario en PDF.
- **Módulo de Carrito de Compras**:
  - Creación y gestión de múltiples carritos de compra.
  - Adición, edición y eliminación de productos dentro de un carrito.
  - Cálculo de cantidades.
  - Generación de un reporte en PDF del carrito del usuario actual.
- **Navegación Intuitiva**: Interfaz de múltiples vistas con una navegación fluida entre los diferentes módulos de la aplicación.
- **Base de Datos**: Toda la lógica de negocio y la manipulación de datos se realiza a través de procedimientos almacenados en una base de datos MySQL, lo que garantiza seguridad e integridad.

## Tecnologías Utilizadas

- **Lenguaje**: Java 21
- **Framework de UI**: JavaFX (OpenJFX)
- **Gestor de Proyectos**: Apache Maven
- **Base de Datos**: MySQL
- **Generación de Reportes**: JasperReports
- **Librerías Adicionales**: `org.json`

## Estructura y Funcionamiento del Proyecto

El proyecto sigue una arquitectura similar al patrón Modelo-Vista-Controlador (MVC), donde:

- **Model** (`org.kinalfamiliar.model`): Contiene las clases que representan las entidades de la base de datos como `Producto`, `Carrito`, `Usuario`, `Categoria`, etc.
- **View** (`/resources/org/kinalfamiliar/view/`): Son los archivos FXML que definen la estructura y el diseño de la interfaz gráfica de usuario.
- **Controller** (`org.kinalfamiliar.controller`): Contiene las clases que manejan la lógica de la aplicación y la interacción entre las vistas (FXML) y los modelos de datos.

### Clases Clave

1. **`Main.java` (`org.kinalfamiliar.system`)**
   - Es el punto de entrada de la aplicación.
   - Inicializa la aplicación JavaFX y carga la primera vista (`LoginView.fxml`).
   - Contiene el método `cambiarEscena()`, que es fundamental para la navegación.

2. **`LoginController.java` y `RegisterController.java`**
   - Gestionan las vistas de inicio de sesión y registro.
   - Se comunican con la base de datos mediante procedimientos almacenados (`sp_inicioSesion`, `sp_AgregarUsuario`).
   - Tras un inicio de sesión exitoso, guardan los datos del usuario en la clase singleton `UsuarioAutenticado`.

3. **`UsuarioAutenticado.java`**
   - Clase Singleton que mantiene los datos del usuario autenticado de forma global.
   - Permite personalizar la interfaz y utilizar los datos del usuario en otras operaciones.

4. **`InventarioController.java`**
   - Controla la vista de inventario (`InventarioView.fxml`).
   - Implementa los métodos para listar, agregar, editar y eliminar productos.
   - Gestiona el estado de la interfaz para alternar entre modo visualización y edición.

5. **`CompraController.java`**
   - Controlador de la vista de compras (`CompraView.fxml`).
   - Maneja las tablas de carritos y detalles del carrito seleccionado.
   - Permite crear nuevos carritos y administrar sus productos.

6. **`Conexion.java` (`org.kinalfamiliar.db`)**
   - Gestiona la conexión a la base de datos MySQL.
   - Usa el patrón Singleton para garantizar una sola instancia activa.

7. **`Report.java` (`org.kinalfamiliar.reporte`)**
   - Clase de utilidad para generar reportes.
   - El método `generarReporte()` permite compilar y mostrar reportes en PDF usando archivos `.jrxml`.

## Requisitos Previos

- **JDK 21** o superior
- **Apache Maven** 3.6 o superior
- **NetBeans IDE 21**
- **Servidor de Base de Datos MySQL** con la base de datos y procedimientos creados previamente

## Instalación y Ejecución

1. **Clonar el repositorio** o descargar la carpeta del proyecto.

2. **Configurar la Base de Datos**:
   - Asegurarse de que el servidor MySQL esté en funcionamiento.
   - Crear la base de datos y ejecutar los scripts SQL `kinalFamiliarDB.sql` y luego `SP_kinalFamiliar.sql`.

3. **Actualizar Credenciales de Conexión**:
   - Abrir `src/main/java/org/kinalfamiliar/db/Conexion.java`.
   - Modificar las credenciales para que coincidan con tu entorno local.

4. **Compilar y Ejecutar con Maven**:
   - Abrir una terminal en la raíz del proyecto.
   - Ejecutar la aplicación desde **NetBeans IDE**.

5. Al iniciarse, se mostrará la ventana de inicio de sesión:
   - Puedes usar las credenciales de ejemplo:
     ```
     Nombre: Juan
     Contraseña: password123
     ```
   - También puedes crear una cuenta propia desde la pantalla de registro.
