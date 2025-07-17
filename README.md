\# Proyecto: Kinal Familiar



\## Descripción General



\*\*Kinal Familiar\*\* es una aplicación de escritorio desarrollada en JavaFX que funciona como un sistema de gestión de inventario y compras. Permite a los usuarios registrarse, iniciar sesión, administrar productos en un inventario y gestionar carritos de compra. La aplicación se conecta a una base de datos MySQL para la persistencia de los datos y utiliza JasperReports para la generación de reportes en PDF.



\## Características Principales



\*   \*\*Gestión de Usuarios\*\*: Sistema completo de registro e inicio de sesión para los usuarios.

\*   \*\*Sesión de Usuario\*\*: Mantiene la sesión del usuario activa en las diferentes vistas, mostrando su información y personalizando la experiencia.

\*   \*\*Módulo de Inventario\*\*: Permite realizar operaciones CRUD (Crear, Leer, Actualizar, Eliminar) sobre los productos. Incluye:

&nbsp;   \*   Visualización de productos en una tabla.

&nbsp;   \*   Formulario para agregar y editar productos.

&nbsp;   \*   Funcionalidad de búsqueda de productos.

&nbsp;   \*   Generación de un reporte de inventario en PDF.

\*   \*\*Módulo de Carrito de Compras\*\*:

&nbsp;   \*   Creación y gestión de múltiples carritos de compra.

&nbsp;   \*   Adición, edición y eliminación de productos dentro de un carrito.

&nbsp;   \*   Cálculo de cantidades.

&nbsp;   \*   Generación de un reporte en PDF del carrito del usuario actual.

\*   \*\*Navegación Intuitiva\*\*: Interfaz de múltiples vistas con una navegación fluida entre los diferentes módulos de la aplicación.

\*   \*\*Base de Datos\*\*: Toda la lógica de negocio y la manipulación de datos se realiza a través de procedimientos almacenados en una base de datos MySQL, lo que garantiza seguridad e integridad.



\## Tecnologías Utilizadas



\*   \*\*Lenguaje\*\*: Java 21

\*   \*\*Framework de UI\*\*: JavaFX (OpenJFX)

\*   \*\*Gestor de Proyectos\*\*: Apache Maven

\*   \*\*Base de Datos\*\*: MySQL

\*   \*\*Generación de Reportes\*\*: JasperReports

\*   \*\*Librerías Adicionales\*\*: `org.json`



\## Estructura y Funcionamiento del Proyecto



El proyecto sigue una arquitectura similar al patrón Modelo-Vista-Controlador (MVC), donde:



\*   \*\*Model\*\*: (`org.kinalfamiliar.model`) Contiene las clases que representan las entidades de la base de datos como `Producto`, `Carrito`, `Usuario`, `Categoria`, etc.

\*   \*\*View\*\*: (`/resources/org/kinalfamiliar/view/`) Son los archivos FXML que definen la estructura y el diseño de la interfaz gráfica de usuario.

\*   \*\*Controller\*\*: (`org.kinalfamiliar.controller`) Contiene las clases que manejan la lógica de la aplicación y la interacción entre las vistas (FXML) y los modelos de datos.



\### Clases Clave



1\.  \*\*`Main.java` (`org.kinalfamiliar.system`)\*\*

&nbsp;   \*   Es el punto de entrada de la aplicación.

&nbsp;   \*   Inicializa la aplicación JavaFX y carga la primera vista (`LoginView.fxml`).

&nbsp;   \*   Contiene el método `cambiarEscena()`, que es fundamental para la navegación, permitiendo cambiar la vista que se muestra en la ventana principal.



2\.  \*\*`LoginController.java` y `RegisterController.java`\*\*

&nbsp;   \*   Gestionan las vistas de inicio de sesión y registro.

&nbsp;   \*   Se comunican con la base de datos a través de procedimientos almacenados (`sp\_inicioSesion`, `sp\_AgregarUsuario`) para autenticar o registrar usuarios.

&nbsp;   \*   Tras un inicio de sesión exitoso, guardan los datos del usuario en la clase singleton `UsuarioAutenticado`.



3\.  \*\*`UsuarioAutenticado.java`\*\*

&nbsp;   \*   Es una clase que sigue el patrón Singleton. Su propósito es mantener los datos del usuario que ha iniciado sesión (nombre, apellido, correo) de forma global para que puedan ser accedidos desde cualquier controlador. Esto permite personalizar la interfaz y usar los datos del usuario en otras operaciones, como la generación de reportes.



4\.  \*\*`InventarioController.java`\*\*

&nbsp;   \*   Controla toda la lógica de la vista de inventario (`InventarioView.fxml`).

&nbsp;   \*   Implementa los métodos para listar, agregar, editar y eliminar productos, interactuando siempre con la base de datos mediante los procedimientos almacenados correspondientes.

&nbsp;   \*   Gestiona el estado de la interfaz para alternar entre el modo de visualización y el modo de edición/creación.



5\.  \*\*`CompraController.java`\*\*

&nbsp;   \*   Es el controlador más complejo. Gestiona la vista de compras (`CompraView.fxml`).

&nbsp;   \*   Maneja dos tablas: una para los carritos de compra y otra para los detalles (productos) del carrito seleccionado.

&nbsp;   \*   Permite la creación de nuevos carritos y la gestión completa de los productos que contienen.



6\.  \*\*`Conexion.java` (`org.kinalfamiliar.db`)\*\*

&nbsp;   \*   Gestiona la conexión a la base de datos MySQL utilizando el patrón Singleton para asegurar que solo exista una instancia de la conexión en toda la aplicación.



7\.  \*\*`Report.java` (`org.kinalfamiliar.reporte`)\*\*

&nbsp;   \*   Clase de utilidad para generar reportes. Contiene el método `generarReporte()` que toma un archivo de plantilla `.jrxml`, un nombre para el reporte de salida y parámetros opcionales (como el ID de usuario para el reporte del carrito) para compilar y mostrar un reporte en PDF.



\## Requisitos Previos



\*   \*\*JDK 21\*\* o superior.

\*   \*\*Apache Maven\*\* 3.6 o superior.

\*    \*\*NetBeans IDE 21\*\*

\*   \*\*Servidor de Base de Datos MySQL\*\* con la base de datos y los procedimientos almacenados del proyecto ya creados.



\## Instalación y Ejecución



1\.  \*\*Clonar el repositorio\*\* (o tener la carpeta del proyecto).



2\.  \*\*Configurar la Base de Datos\*\*:

&nbsp;   \*   Asegúrate de que tu servidor MySQL esté en funcionamiento.

&nbsp;   \*   Crea la base de datos y ejecuta los scripts SQL \*\*kinalFamiliarDB.sql\*\* y luego \*\*SP\_kinalFamiliar.sql\*\* para garantizar un correcto funcionamiento del programa.



3\.  \*\*Actualizar Credenciales de Conexión\*\*:

&nbsp;   \*   Abre el archivo `src/main/java/org/kinalfamiliar/db/Conexion.java`.

&nbsp;   \*   Modifica las credenciales (URL del servidor, nombre de usuario, contraseña) para que coincidan con la configuración de tu base de datos local.



4\.  \*\*Compilar y Ejecutar con Maven\*\*:

&nbsp;   \*   Abre una terminal en la raíz del proyecto.

&nbsp;   \*   Ejecuta tu aplicación presionando ejecutar en \*\*NetBeans IDE\*\*



5\.  La aplicación se iniciará y mostrará la ventana de inicio de sesión.

&nbsp;   \* Existen credenciales de ejemplo que puedes usar:  ```Nombre: Juan | Contraseña: password123```

&nbsp;   \* De quererse así, se puede dirigir a la pantalla de registro para poder crear una cuenta propia (En el inicio de sesión únicamente se utiliza el nombre y la contraseña)

