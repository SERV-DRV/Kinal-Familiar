package org.kinalfamiliar.controller;

import java.net.URL;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import org.kinalfamiliar.db.Conexion;
import org.kinalfamiliar.model.Carrito;
import org.kinalfamiliar.model.DetalleCarrito;
import org.kinalfamiliar.model.Producto;
import org.kinalfamiliar.model.Usuario;
import org.kinalfamiliar.reporte.Report;
import org.kinalfamiliar.system.Main;

public class CompraController implements Initializable {

    @FXML
    private TableView<Carrito> tblCarrito;
    @FXML
    private TableColumn<Carrito, Integer> colIdCarrito;
    @FXML
    private TableColumn<Carrito, Integer> colIdUsuario;
    @FXML
    private TableColumn<Carrito, LocalDateTime> colFechaCreacion;
    @FXML
    private TableColumn<Carrito, String> colEstado;
    @FXML
    private TableView<DetalleCarrito> tblDetalleCarrito;
    @FXML
    private TableColumn<DetalleCarrito, Integer> colIdDetalleCarrito;
    @FXML
    private TableColumn<DetalleCarrito, Integer> colCantidad;
    @FXML
    private TableColumn<DetalleCarrito, Integer> colIdProducto;
    @FXML
    private TableColumn<DetalleCarrito, Integer> colIdCarrito2;
    @FXML
    private ComboBox<Usuario> cbxUsuario;
    @FXML
    private ComboBox<Producto> cbxProducto;
    @FXML
    private Button btnNuevoDc, btnEliminarDc, btnEditarDc, btnNuevoC, btnEditarC, btnMas, btnMenos;
    @FXML
    private TextArea txtACantidad;
    @FXML
    private TextField txtIdCarrito, txtIdCarrito2, txtIdDetalleCarrito;
    @FXML
    private RadioButton rbActivo, rbInactivo;
    @FXML
    private ToggleGroup grupoEstado;
    @FXML
    private Label labelNombre;
    @FXML
    private Label labelApellido;
    @FXML
    private Label labelEmail;

    private Main principal;
    private ObservableList<Carrito> listaCarritos;
    private ObservableList<DetalleCarrito> listaDetalleCarritos;
    private ObservableList<DetalleCarrito> listaCompletaDetalles;

    private enum acciones {
        AGREGAR_CARRITO, EDITAR_CARRITO, AGREGAR_DETALLE, EDITAR_DETALLE, NINGUNA
    }

    private acciones accionActual = acciones.NINGUNA;

    public Main getPrincipal() {
        return principal;
    }

    public void setPrincipal(Main principal) {
        this.principal = principal;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarColumnas();
        cargarDatosIniciales();
        configurarListeners();
        configurarBotonesCantidad();
        cargarDatosUsuario();
        cargarUsuarios();
        limpiarDetallesDelUsuario();
        seleccionarUsuarioAutenticadoEnComboBox();

    }

    public void cargarDatosUsuario() {
        UsuarioAutenticado usuario = UsuarioAutenticado.getInstancia();
        labelNombre.setText(usuario.getNombreUsuario());
        labelApellido.setText(usuario.getApellidoUsuario());
        labelEmail.setText(usuario.getCorreoUsuario());
    }

    public void cerrarSesion(ActionEvent evento) {
        UsuarioAutenticado usuario = UsuarioAutenticado.getInstancia();
        usuario.cerrarSesion();
        principal.cambiarEscena("LoginView.fxml", 1280, 720);
    }

    private void configurarColumnas() {
        colIdCarrito.setCellValueFactory(new PropertyValueFactory<>("idCarrito"));
        colIdUsuario.setCellValueFactory(new PropertyValueFactory<>("idUsuario"));
        colFechaCreacion.setCellValueFactory(new PropertyValueFactory<>("fechaCreacion"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        colIdDetalleCarrito.setCellValueFactory(new PropertyValueFactory<>("idDetalleCarrito"));
        colIdCarrito2.setCellValueFactory(new PropertyValueFactory<>("idCarrito"));
        colIdProducto.setCellValueFactory(new PropertyValueFactory<>("idProducto"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
    }

    private void cargarDatosIniciales() {
        cargarUsuarios();
        cargarProductos();
        cargarTableViewCarrito();
        listaCompletaDetalles = FXCollections.observableArrayList(listarTodosLosDetalles());
    }

    private void configurarListeners() {
        tblCarrito.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                cargarFormularioCarrito();
                filtrarYcargarTableViewDetalle(newSelection.getIdCarrito());
            }
        });
        tblDetalleCarrito.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                cargarFormularioDetalleCarrito();
            }
        });
    }

    private void configurarBotonesCantidad() {
        btnMas.setOnAction(e -> cambiarCantidad(1));
        btnMenos.setOnAction(e -> cambiarCantidad(-1));
    }

    private void cambiarCantidad(int delta) {
        try {
            int cantidadActual = Integer.parseInt(txtACantidad.getText());
            int nuevaCantidad = cantidadActual + delta;
            if (nuevaCantidad > 0) {
                txtACantidad.setText(String.valueOf(nuevaCantidad));
            }
        } catch (NumberFormatException e) {
            txtACantidad.setText("1");
        }
    }

    private void cargarFormularioCarrito() {
        Carrito carrito = tblCarrito.getSelectionModel().getSelectedItem();
        if (carrito != null) {
            txtIdCarrito.setText(String.valueOf(carrito.getIdCarrito()));
            for (Usuario u : cbxUsuario.getItems()) {
                if (u.getIdUsuario() == carrito.getIdUsuario()) {
                    cbxUsuario.setValue(u);
                    break;
                }
            }
            if (carrito.getEstado().equalsIgnoreCase("Activo")) {
                rbActivo.setSelected(true);
            } else {
                rbInactivo.setSelected(true);
            }
        }
    }

    private void cargarFormularioDetalleCarrito() {
        DetalleCarrito detalleCarrito = tblDetalleCarrito.getSelectionModel().getSelectedItem();
        if (detalleCarrito != null) {
            txtIdDetalleCarrito.setText(String.valueOf(detalleCarrito.getIdDetalleCarrito()));
            txtIdCarrito2.setText(String.valueOf(detalleCarrito.getIdCarrito()));
            txtACantidad.setText(String.valueOf(detalleCarrito.getCantidad()));
            for (Producto p : cbxProducto.getItems()) {
                if (p.getIdProducto() == detalleCarrito.getIdProducto()) {
                    cbxProducto.setValue(p);
                    break;
                }
            }
        }
    }

    private void limpiarFormularioDetalle() {
        txtIdDetalleCarrito.clear();
        txtIdCarrito2.clear();
        cbxProducto.getSelectionModel().clearSelection();
        txtACantidad.setText("1");
    }


private ArrayList<Carrito> listarCarritosPorUsuario() {
    ArrayList<Carrito> carritos = new ArrayList<>();
    try {
        UsuarioAutenticado usuario = UsuarioAutenticado.getInstancia();
        String correo = usuario.getCorreoUsuario();

        CallableStatement stmt = Conexion.getInstancia().getConexion()
            .prepareCall("{call sp_ObtenerIdUsuarioPorCorreo(?, ?)}");
        stmt.setString(1, correo);
        stmt.registerOutParameter(2, java.sql.Types.INTEGER);
        stmt.execute();

        int idUsuario = stmt.getInt(2);
        if (!stmt.wasNull()) {
            CallableStatement carritoStmt = Conexion.getInstancia().getConexion()
                .prepareCall("CALL sp_ListarCarritos()");
            ResultSet rs = carritoStmt.executeQuery();
            while (rs.next()) {
                if (rs.getInt("idUsuario") == idUsuario) {
                    carritos.add(new Carrito(
                        rs.getInt("idCarrito"),
                        rs.getString("estado"),
                        rs.getTimestamp("fechaCreacion").toLocalDateTime(),
                        rs.getInt("idUsuario")
                    ));
                }
            }
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
    }
    return carritos;
}


private void limpiarDetallesDelUsuario() {
    UsuarioAutenticado usuario = UsuarioAutenticado.getInstancia();
    String correo = usuario.getCorreoUsuario();

    try {
        CallableStatement stmt = Conexion.getInstancia().getConexion()
            .prepareCall("{call sp_ObtenerIdUsuarioPorCorreo(?, ?)}");
        stmt.setString(1, correo);
        stmt.registerOutParameter(2, java.sql.Types.INTEGER);
        stmt.execute();

        int idUsuario = stmt.getInt(2);
        if (!stmt.wasNull()) {
            // Obtener carritos del usuario
            for (Carrito carrito : listarCarritosPorUsuario()) {
                CallableStatement deleteStmt = Conexion.getInstancia().getConexion()
                    .prepareCall("CALL sp_EliminarDetalleCarritoPorCarrito(?)");
                deleteStmt.setInt(1, carrito.getIdCarrito());
                deleteStmt.execute();
            }
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
    }

    tblDetalleCarrito.getItems().clear();
}



    private ArrayList<DetalleCarrito> listarTodosLosDetalles() {
        ArrayList<DetalleCarrito> detalleCarritos = new ArrayList<>();
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion().prepareCall("call sp_ListarDetalleCarritos()");
            ResultSet resultado = enunciado.executeQuery();
            while (resultado.next()) {
                detalleCarritos.add(new DetalleCarrito(
                        resultado.getInt("idDetalleCarrito"),
                        resultado.getInt("idCarrito"),
                        resultado.getInt("idProducto"),
                        resultado.getInt("cantidad")
                ));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return detalleCarritos;
    }

    private void cargarTableViewCarrito() {
        listaCarritos = FXCollections.observableArrayList(listarCarritosPorUsuario());
        tblCarrito.setItems(listaCarritos);
        if (!listaCarritos.isEmpty()) {
            tblCarrito.getSelectionModel().selectFirst();
        }
    }

    private void filtrarYcargarTableViewDetalle(int idCarrito) {
        ArrayList<DetalleCarrito> detallesFiltrados = listaCompletaDetalles.stream()
                .filter(d -> d.getIdCarrito() == idCarrito)
                .collect(Collectors.toCollection(ArrayList::new));

        listaDetalleCarritos = FXCollections.observableArrayList(detallesFiltrados);
        tblDetalleCarrito.setItems(listaDetalleCarritos);

        if (!listaDetalleCarritos.isEmpty()) {
            tblDetalleCarrito.getSelectionModel().selectFirst();
        } else {
            limpiarFormularioDetalle();
        }
    }

    private ArrayList<Usuario> listarUsuarios() {
        ArrayList<Usuario> usuarios = new ArrayList<>();
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion().prepareCall("call sp_ListarUsuarios()");
            ResultSet resultado = enunciado.executeQuery();
            while (resultado.next()) {
                usuarios.add(new Usuario(
                        resultado.getInt("idUsuario"),
                        resultado.getString("nombreUsuario"),
                        resultado.getString("apellidoUsuario"),
                        resultado.getString("correoUsuario")
                ));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return usuarios;
    }

    private void cargarUsuarios() {
        ObservableList<Usuario> listaUsuario = FXCollections.observableArrayList(listarUsuarios());
        cbxUsuario.setItems(listaUsuario);
    }

    private ArrayList<Producto> listarProductos() {
        ArrayList<Producto> productos = new ArrayList<>();
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion().prepareCall("call sp_ListarProductos();");
            ResultSet resultado = enunciado.executeQuery();
            while (resultado.next()) {
                productos.add(new Producto(
                        resultado.getInt("idProducto"),
                        resultado.getString("nombreProducto"),
                        resultado.getInt("cantidadProducto"),
                        resultado.getDouble("precioProducto"),
                        resultado.getString("estadoProducto"),
                        resultado.getInt("idCategoria")
                ));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return productos;
    }

    private void cargarProductos() {
        ObservableList<Producto> listaProductos = FXCollections.observableArrayList(listarProductos());
        cbxProducto.setItems(listaProductos);
    }

    private void seleccionarUsuarioAutenticadoEnComboBox() {
        UsuarioAutenticado usuarioAutenticado = UsuarioAutenticado.getInstancia();
        String correo = usuarioAutenticado.getCorreoUsuario();

        for (Usuario usuario : cbxUsuario.getItems()) {
            if (usuario.getCorreoUsuario().equalsIgnoreCase(correo)) {
                cbxUsuario.setValue(usuario);
                cbxUsuario.setDisable(true);
                break;
            }
        }
    }

    private Carrito cargarModeloCarrito() {
        int idCarrito = txtIdCarrito.getText().isEmpty() ? 0 : Integer.parseInt(txtIdCarrito.getText());
        Usuario usuarioSeleccionado = cbxUsuario.getSelectionModel().getSelectedItem();
        int idUsuario = (usuarioSeleccionado != null) ? usuarioSeleccionado.getIdUsuario() : 0;
        String estado = rbActivo.isSelected() ? "Activo" : "Inactivo";
        return new Carrito(idCarrito, estado, idUsuario);
    }

    private DetalleCarrito cargarModeloDetalleCarrito() {
        int idDetalleCarrito = txtIdDetalleCarrito.getText().isEmpty() ? 0 : Integer.parseInt(txtIdDetalleCarrito.getText());
        int FKidCarrito = Integer.parseInt(txtIdCarrito.getText());
        Producto productoSeleccionado = cbxProducto.getSelectionModel().getSelectedItem();
        int idProducto = (productoSeleccionado != null) ? productoSeleccionado.getIdProducto() : 0;
        int cantidad = Integer.parseInt(txtACantidad.getText());
        return new DetalleCarrito(idDetalleCarrito, FKidCarrito, idProducto, cantidad);
    }

    private void ejecutarCRUD(String nombreProcedimiento, Object... parametros) {
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion().prepareCall(nombreProcedimiento);
            for (int i = 0; i < parametros.length; i++) {
                enunciado.setObject(i + 1, parametros[i]);
            }
            enunciado.execute();
            refrescarDatos();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void refrescarDatos() {
        cargarTableViewCarrito();
        listaCompletaDetalles = FXCollections.observableArrayList(listarTodosLosDetalles());
        Carrito carritoSeleccionado = tblCarrito.getSelectionModel().getSelectedItem();
        if (carritoSeleccionado != null) {
            filtrarYcargarTableViewDetalle(carritoSeleccionado.getIdCarrito());
        }
    }

    private void actualizarEstadoAcciones() {
        boolean modoEdicion = (accionActual != acciones.NINGUNA);
        boolean carritoEditable = (accionActual == acciones.AGREGAR_CARRITO || accionActual == acciones.EDITAR_CARRITO);
        boolean detalleEditable = (accionActual == acciones.AGREGAR_DETALLE || accionActual == acciones.EDITAR_DETALLE);

        cbxUsuario.setDisable(!carritoEditable);
        rbActivo.setDisable(!carritoEditable);
        rbInactivo.setDisable(!carritoEditable);

        cbxProducto.setDisable(!detalleEditable);
        txtACantidad.setDisable(!detalleEditable);
        btnMas.setDisable(!detalleEditable);
        btnMenos.setDisable(!detalleEditable);

        tblCarrito.setDisable(modoEdicion);
        tblDetalleCarrito.setDisable(modoEdicion);

        btnNuevoC.setText(carritoEditable ? "Guardar" : "Nuevo");
        btnEditarC.setDisable(modoEdicion);

        btnNuevoDc.setText(detalleEditable ? "Guardar" : "Agregar");
        btnEliminarDc.setText(detalleEditable ? "Cancelar" : "Eliminar");
        btnEditarDc.setDisable(modoEdicion);
    }

    private void manejarAccionGuardar() {
        switch (accionActual) {
            case AGREGAR_CARRITO:
                if (cbxUsuario.getValue() == null) {
                    mostrarAlerta("Campos incompletos", "Por favor, seleccione un usuario.");
                    return;
                }
                ejecutarCRUD("call sp_AgregarCarrito(?)", cargarModeloCarrito().getIdUsuario());
                break;
            case EDITAR_CARRITO:
                if (cbxUsuario.getValue() == null) {
                    mostrarAlerta("Campos incompletos", "Por favor, seleccione un usuario.");
                    return;
                }
                ejecutarCRUD("call sp_EditarCarrito(?, ?, ?)", cargarModeloCarrito().getIdCarrito(), cargarModeloCarrito().getEstado(), cargarModeloCarrito().getIdUsuario());
                break;
            case AGREGAR_DETALLE:
                if (cbxProducto.getValue() == null || txtACantidad.getText().isEmpty()) {
                    mostrarAlerta("Campos incompletos", "Por favor, llene todos los campos del detalle.");
                    return;
                }
                ejecutarCRUD("call sp_AgregarDetalleCarrito(?, ?, ?)", cargarModeloDetalleCarrito().getIdCarrito(), cargarModeloDetalleCarrito().getIdProducto(), cargarModeloDetalleCarrito().getCantidad());
                break;
            case EDITAR_DETALLE:
                if (cbxProducto.getValue() == null || txtACantidad.getText().isEmpty()) {
                    mostrarAlerta("Campos incompletos", "Por favor, llene todos los campos del detalle.");
                    return;
                }
                ejecutarCRUD("call sp_EditarDetalleCarrito(?, ?, ?, ?)", cargarModeloDetalleCarrito().getIdDetalleCarrito(), cargarModeloDetalleCarrito().getIdCarrito(), cargarModeloDetalleCarrito().getIdProducto(), cargarModeloDetalleCarrito().getCantidad());
                break;
        }
        accionActual = acciones.NINGUNA;
        actualizarEstadoAcciones();
    }

    @FXML
    private void agregarCarritoAction() {
        if (accionActual == acciones.NINGUNA) {
            accionActual = acciones.AGREGAR_CARRITO;
            actualizarEstadoAcciones();
        } else {
            manejarAccionGuardar();
        }
    }

    @FXML
    private void editarCarritoAction() {
        if (tblCarrito.getSelectionModel().getSelectedItem() == null) {
            mostrarAlerta("Acción no válida", "Debe seleccionar un carrito para editar.");
            return;
        }
        accionActual = acciones.EDITAR_CARRITO;
        actualizarEstadoAcciones();
    }

    @FXML
    private void agregarDetalleCarritoAction() {
        if (tblCarrito.getSelectionModel().getSelectedItem() == null) {
            mostrarAlerta("Acción no válida", "Debe seleccionar un carrito para agregarle productos.");
            return;
        }

        if (accionActual == acciones.NINGUNA) {
            accionActual = acciones.AGREGAR_DETALLE;
            limpiarFormularioDetalle();
            actualizarEstadoAcciones();
        } else {
            manejarAccionGuardar();
        }
    }

    @FXML
    private void editarDetalleCarritoAction() {
        if (tblDetalleCarrito.getSelectionModel().getSelectedItem() == null) {
            mostrarAlerta("Acción no válida", "Debe seleccionar un producto del carrito para editar.");
            return;
        }
        accionActual = acciones.EDITAR_DETALLE;
        actualizarEstadoAcciones();
    }

    @FXML
    private void cancelarEliminarDetalleCarritoAction() {
        if (accionActual == acciones.NINGUNA) {
            if (tblDetalleCarrito.getSelectionModel().getSelectedItem() == null) {
                mostrarAlerta("Acción no válida", "Debe seleccionar un producto para eliminar.");
                return;
            }
            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION, "¿Seguro que desea eliminar este producto del carrito?", ButtonType.OK, ButtonType.CANCEL);
            confirmacion.setTitle("Confirmar eliminación");
            Optional<ButtonType> resultado = confirmacion.showAndWait();
            if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
                ejecutarCRUD("call sp_EliminarDetalleCarrito(?)", tblDetalleCarrito.getSelectionModel().getSelectedItem().getIdDetalleCarrito());
            }
        } else {
            accionActual = acciones.NINGUNA;
            actualizarEstadoAcciones();
            cargarFormularioDetalleCarrito();
        }
    }

    private void mostrarAlerta(String titulo, String contenido) {
        Alert alerta = new Alert(Alert.AlertType.WARNING);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(contenido);
        alerta.showAndWait();
    }

    public void manejarBotonInventario(ActionEvent evento) {
        principal.cambiarEscena("InventarioView.fxml", 1280, 720);
    }

    public void manejarBotonSalir(ActionEvent evento) {
        Platform.exit();
    }

    @FXML
    private void imprimirReporte(ActionEvent event) {
        String correoUsuario = labelEmail.getText();
        Integer idUsuario = null;

        if (correoUsuario != null && !correoUsuario.isEmpty()) {
            try {
                CallableStatement enunciado = Conexion.getInstancia().getConexion().prepareCall("{call sp_ObtenerIdUsuarioPorCorreo(?, ?)}");
                enunciado.setString(1, correoUsuario);
                enunciado.registerOutParameter(2, java.sql.Types.INTEGER);
                enunciado.execute();

                int retrievedId = enunciado.getInt(2);
                if (!enunciado.wasNull()) {
                    idUsuario = retrievedId;
                }

                enunciado.close();

            } catch (SQLException ex) {
                ex.printStackTrace();
                mostrarAlerta("Error de Base de Datos", "No se pudo obtener el ID del usuario para el reporte.");
            }
        }

        Report reporte = new Report();
        if (idUsuario != null) {
            reporte.generarReporte("Carrito.jrxml", "ReporteCarrito", idUsuario);
        } else {
            reporte.generarReporte("Carrito.jrxml", "ReporteCarrito");
        }
    }
}
