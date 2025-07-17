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
    private Label labelNombre, labelApellido, labelEmail;
    @FXML
    private Button btnCancelar;

    private Main principal;
    private ObservableList<Carrito> listaCarritos;
    private ObservableList<DetalleCarrito> listaDetalleCarritos;
    private ObservableList<DetalleCarrito> listaCompletaDetalles;

    private enum acciones {
        EDITAR_CARRITO, AGREGAR_DETALLE, EDITAR_DETALLE, NINGUNA
    }
    private acciones accionActual = acciones.NINGUNA;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarColumnas();
        configurarBotonesCantidad();
        configurarListeners();
        cargarDatosUsuario();
        cargarProductos();
        cargarUsuarios();
        cargarVistaParaUsuarioAutenticado();
    }

    private void cargarVistaParaUsuarioAutenticado() {
        int idUsuario = obtenerIdUsuarioPorCorreo(UsuarioAutenticado.getInstancia().getCorreoUsuario());
        if (idUsuario == 0) {
            mostrarAlerta("Error Crítico", "No se pudo verificar la identidad del usuario.");
            return;
        }

        cargarCarritosDelUsuario(idUsuario);

        try (CallableStatement cs = Conexion.getInstancia().getConexion().prepareCall("{call sp_ObtenerCarritoActivoPorUsuario(?, ?)}")) {
            cs.setInt(1, idUsuario);
            cs.registerOutParameter(2, java.sql.Types.INTEGER);
            cs.execute();
            int idCarritoActivo = cs.getInt(2);

            if (!cs.wasNull()) {
                seleccionarCarritoEnTabla(idCarritoActivo);
            } else {
                limpiarFormularioCarritoYDetalle();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            mostrarAlerta("Error de Base de Datos", "No se pudo verificar la existencia de un carrito activo.");
        }
        actualizarEstadoInterfaz();
    }

    @FXML
    private void agregarCarritoAction() {
        for (Carrito c : tblCarrito.getItems()) {
            if ("Activo".equalsIgnoreCase(c.getEstado())) {
                mostrarAlerta("Acción no Válida", "Ya tienes un carrito activo. Finaliza tu compra o desactívalo antes de crear uno nuevo.");
                return;
            }
        }
        int idUsuario = obtenerIdUsuarioPorCorreo(UsuarioAutenticado.getInstancia().getCorreoUsuario());
        if (idUsuario != 0) {
            ejecutarCRUD("call sp_AgregarCarrito(?)", idUsuario);
            cargarVistaParaUsuarioAutenticado();
        }
    }

    @FXML
    private void cancelarAccion(ActionEvent event) {
        accionActual = acciones.NINGUNA;
        actualizarEstadoInterfaz();
        cargarFormularioCarrito();
        cargarFormularioDetalleCarrito();
    }

    @FXML
    private void editarCarritoAction() {
        if (accionActual == acciones.EDITAR_CARRITO) {
            Carrito carritoEditado = cargarModeloCarrito();
            if (carritoEditado != null) {
                ejecutarCRUD("call sp_EditarCarrito(?, ?, ?)",
                        carritoEditado.getIdCarrito(),
                        carritoEditado.getEstado(),
                        carritoEditado.getIdUsuario());
            }
            accionActual = acciones.NINGUNA;
            cargarVistaParaUsuarioAutenticado();
        } else {
            if (tblCarrito.getSelectionModel().getSelectedItem() == null) {
                mostrarAlerta("Acción no válida", "Debe seleccionar un carrito para editar.");
                return;
            }
            accionActual = acciones.EDITAR_CARRITO;
            actualizarEstadoInterfaz();
        }
    }

    @FXML
    private void agregarDetalleCarritoAction() {
        if (tblCarrito.getSelectionModel().getSelectedItem() == null && accionActual == acciones.NINGUNA) {
            mostrarAlerta("Acción no válida", "Debe tener un carrito activo seleccionado.");
            return;
        }
        if (accionActual == acciones.AGREGAR_DETALLE || accionActual == acciones.EDITAR_DETALLE) {
            DetalleCarrito detalle = cargarModeloDetalleCarrito();
            if (detalle != null) {
                if (accionActual == acciones.AGREGAR_DETALLE) {
                    ejecutarCRUD("call sp_AgregarDetalleCarrito(?, ?, ?)", detalle.getIdCarrito(), detalle.getIdProducto(), detalle.getCantidad());
                } else {
                    ejecutarCRUD("call sp_EditarDetalleCarrito(?, ?, ?, ?)", detalle.getIdDetalleCarrito(), detalle.getIdCarrito(), detalle.getIdProducto(), detalle.getCantidad());
                }

                refrescarDetallesYTabla();
                accionActual = acciones.NINGUNA;
                filtrarYcargarTableViewDetalle(detalle.getIdCarrito());
                actualizarEstadoInterfaz();
            }
        } else {
            accionActual = acciones.AGREGAR_DETALLE;
            limpiarFormularioDetalle();
            actualizarEstadoInterfaz();
        }
    }

    private void refrescarDetallesYTabla() {
        this.listaCompletaDetalles = listarTodosLosDetalles();

        Carrito carritoSeleccionado = tblCarrito.getSelectionModel().getSelectedItem();
        if (carritoSeleccionado != null) {
            filtrarYcargarTableViewDetalle(carritoSeleccionado.getIdCarrito());
        }
    }

    @FXML
    private void editarDetalleCarritoAction() {
        if (tblDetalleCarrito.getSelectionModel().getSelectedItem() == null) {
            mostrarAlerta("Acción no válida", "Debe seleccionar un producto del carrito para editar.");
            return;
        }
        accionActual = acciones.EDITAR_DETALLE;
        actualizarEstadoInterfaz();
    }

    @FXML
    private void cancelarEliminarDetalleCarritoAction() {
        if (accionActual == acciones.AGREGAR_DETALLE || accionActual == acciones.EDITAR_DETALLE) {
            accionActual = acciones.NINGUNA;
            actualizarEstadoInterfaz();
            cargarFormularioDetalleCarrito();
        } else {
            DetalleCarrito detalleSeleccionado = tblDetalleCarrito.getSelectionModel().getSelectedItem();
            if (detalleSeleccionado == null) {
                mostrarAlerta("Acción no válida", "Debe seleccionar un producto para eliminar.");
                return;
            }
            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION, "¿Seguro que desea eliminar este producto del carrito?", ButtonType.OK, ButtonType.CANCEL);
            confirmacion.setTitle("Confirmar eliminación");

            Optional<ButtonType> resultado = confirmacion.showAndWait();
            if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
                ejecutarCRUD("call sp_EliminarDetalleCarrito(?)", detalleSeleccionado.getIdDetalleCarrito());
                refrescarDetallesYTabla();
            }
        }
    }

    private void actualizarEstadoInterfaz() {
        Carrito carritoSeleccionado = tblCarrito.getSelectionModel().getSelectedItem();
        boolean hayCarritoSeleccionado = carritoSeleccionado != null;
        boolean esCarritoActivo = hayCarritoSeleccionado && "Activo".equalsIgnoreCase(carritoSeleccionado.getEstado());
        boolean modoEdicionCarrito = (accionActual == acciones.EDITAR_CARRITO);
        boolean modoEdicionDetalle = (accionActual == acciones.AGREGAR_DETALLE || accionActual == acciones.EDITAR_DETALLE);
        boolean existeActivo = tblCarrito.getItems().stream().anyMatch(c -> "Activo".equalsIgnoreCase(c.getEstado()));

        // --- Botón Cancelar ---
        btnCancelar.setDisable(accionActual == acciones.NINGUNA);

        // --- Controles del Carrito ---
        btnNuevoC.setDisable(existeActivo || modoEdicionDetalle || modoEdicionCarrito);
        btnEditarC.setDisable(!hayCarritoSeleccionado || modoEdicionDetalle); // Se puede editar aunque haya otro activo
        rbActivo.setDisable(!modoEdicionCarrito);
        rbInactivo.setDisable(!modoEdicionCarrito);

        // --- Controles del Detalle del Carrito ---
        btnNuevoDc.setDisable(!esCarritoActivo || modoEdicionCarrito);
        btnNuevoDc.setText((modoEdicionDetalle && accionActual == acciones.AGREGAR_DETALLE) ? "Guardar" : "Agregar");
        btnEditarDc.setText((modoEdicionDetalle && accionActual == acciones.EDITAR_DETALLE) ? "Guardar" : "Editar");
        btnEditarDc.setDisable(!esCarritoActivo || tblDetalleCarrito.getSelectionModel().getSelectedItem() == null || modoEdicionCarrito);
        btnEliminarDc.setDisable(!esCarritoActivo || tblDetalleCarrito.getSelectionModel().getSelectedItem() == null || modoEdicionDetalle || modoEdicionCarrito);

        cbxProducto.setDisable(!modoEdicionDetalle);
        txtACantidad.setDisable(!modoEdicionDetalle);
        btnMas.setDisable(!modoEdicionDetalle);
        btnMenos.setDisable(!modoEdicionDetalle);

        // Bloquear tablas durante cualquier edición
        tblCarrito.setDisable(modoEdicionCarrito || modoEdicionDetalle);
        tblDetalleCarrito.setDisable(modoEdicionCarrito || modoEdicionDetalle);
    }

    private void configurarListeners() {
        tblCarrito.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                cargarFormularioCarrito();
                filtrarYcargarTableViewDetalle(newSelection.getIdCarrito());
            } else {
                limpiarFormularioCarritoYDetalle();
            }
            actualizarEstadoInterfaz();
        });
        tblDetalleCarrito.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                cargarFormularioDetalleCarrito();
            }
            actualizarEstadoInterfaz();
        });
    }

    private void cargarCarritosDelUsuario(int idUsuario) {
        ArrayList<Carrito> carritosDelUsuario = new ArrayList<>();
        String sql = "call sp_ListarCarritos()";
        try (CallableStatement cs = Conexion.getInstancia().getConexion().prepareCall(sql); ResultSet rs = cs.executeQuery()) {
            while (rs.next()) {
                if (rs.getInt("idUsuario") == idUsuario) {
                    carritosDelUsuario.add(new Carrito(
                            rs.getInt("idCarrito"), rs.getString("estado"),
                            rs.getTimestamp("fechaCreacion").toLocalDateTime(), rs.getInt("idUsuario")));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        listaCarritos = FXCollections.observableArrayList(carritosDelUsuario);
        tblCarrito.setItems(listaCarritos);
        listaCompletaDetalles = listarTodosLosDetalles();
    }

    private int obtenerIdUsuarioPorCorreo(String correo) {
        if (correo == null || correo.trim().isEmpty()) {
            return 0;
        }
        String sql = "{call sp_ObtenerIdUsuarioPorCorreo(?, ?)}";
        try (CallableStatement cs = Conexion.getInstancia().getConexion().prepareCall(sql)) {
            cs.setString(1, correo);
            cs.registerOutParameter(2, java.sql.Types.INTEGER);
            cs.execute();
            int idUsuario = cs.getInt(2);
            return cs.wasNull() ? 0 : idUsuario;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return 0;
        }
    }

    private void filtrarYcargarTableViewDetalle(int idCarrito) {
        listaDetalleCarritos = listaCompletaDetalles.stream()
                .filter(d -> d.getIdCarrito() == idCarrito)
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
        tblDetalleCarrito.setItems(listaDetalleCarritos);
        if (!listaDetalleCarritos.isEmpty()) {
            tblDetalleCarrito.getSelectionModel().selectFirst();
        } else {
            limpiarFormularioDetalle();
        }
    }

    private void ejecutarCRUD(String nombreProcedimiento, Object... parametros) {
        try (CallableStatement cs = Conexion.getInstancia().getConexion().prepareCall(nombreProcedimiento)) {
            for (int i = 0; i < parametros.length; i++) {
                cs.setObject(i + 1, parametros[i]);
            }
            cs.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();
            mostrarAlerta("Error de Base de Datos", "No se pudo completar la operación.");
        }
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

    private void configurarBotonesCantidad() {
        btnMas.setOnAction(e -> cambiarCantidad(1));
        btnMenos.setOnAction(e -> cambiarCantidad(-1));
    }

    private void cambiarCantidad(int delta) {
        try {
            int nuevaCantidad = Integer.parseInt(txtACantidad.getText()) + delta;
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
            cbxUsuario.getItems().stream()
                    .filter(u -> u.getIdUsuario() == carrito.getIdUsuario())
                    .findFirst().ifPresent(cbxUsuario::setValue);
            if (carrito.getEstado().equalsIgnoreCase("Activo")) {
                rbActivo.setSelected(true);
            } else {
                rbInactivo.setSelected(true);
            }
        }
    }

    private void cargarFormularioDetalleCarrito() {
        DetalleCarrito detalle = tblDetalleCarrito.getSelectionModel().getSelectedItem();
        if (detalle != null) {
            txtIdDetalleCarrito.setText(String.valueOf(detalle.getIdDetalleCarrito()));
            txtIdCarrito2.setText(String.valueOf(detalle.getIdCarrito()));
            txtACantidad.setText(String.valueOf(detalle.getCantidad()));
            cbxProducto.getItems().stream()
                    .filter(p -> p.getIdProducto() == detalle.getIdProducto())
                    .findFirst().ifPresent(cbxProducto::setValue);
        }
    }

    private void limpiarFormularioDetalle() {
        txtIdDetalleCarrito.clear();
        txtIdCarrito2.clear();
        cbxProducto.getSelectionModel().clearSelection();
        txtACantidad.setText("1");
    }

    private void limpiarFormularioCarritoYDetalle() {
        tblCarrito.getSelectionModel().clearSelection();
        txtIdCarrito.clear();
        cbxUsuario.getSelectionModel().clearSelection();
        rbActivo.setSelected(false);
        rbInactivo.setSelected(false);
        tblDetalleCarrito.getItems().clear();
        limpiarFormularioDetalle();
    }

    private void seleccionarCarritoEnTabla(int idCarrito) {
        tblCarrito.getItems().stream()
                .filter(c -> c.getIdCarrito() == idCarrito)
                .findFirst()
                .ifPresent(tblCarrito.getSelectionModel()::select);
    }

    private Carrito cargarModeloCarrito() {
        if (txtIdCarrito.getText().isEmpty()) {
            return null;
        }
        Usuario usuario = cbxUsuario.getSelectionModel().getSelectedItem();
        if (usuario == null) {
            return null;
        }
        return new Carrito(Integer.parseInt(txtIdCarrito.getText()),
                rbActivo.isSelected() ? "Activo" : "Inactivo", usuario.getIdUsuario());
    }

    private DetalleCarrito cargarModeloDetalleCarrito() {
        String idCarritoStr = txtIdCarrito.getText();
        String cantidadStr = txtACantidad.getText();
        Producto producto = cbxProducto.getSelectionModel().getSelectedItem();
        if (idCarritoStr.isEmpty() || cantidadStr.isEmpty() || producto == null) {
            mostrarAlerta("Campos incompletos", "Por favor, llene todos los campos del detalle.");
            return null;
        }
        try {
            int idDetalle = txtIdDetalleCarrito.getText().isEmpty() ? 0 : Integer.parseInt(txtIdDetalleCarrito.getText());
            return new DetalleCarrito(idDetalle, Integer.parseInt(idCarritoStr),
                    producto.getIdProducto(), Integer.parseInt(cantidadStr));
        } catch (NumberFormatException e) {
            mostrarAlerta("Error de Formato", "La cantidad debe ser un valor numérico válido.");
            return null;
        }
    }

    private void cargarDatosUsuario() {
        UsuarioAutenticado usuario = UsuarioAutenticado.getInstancia();
        labelNombre.setText(usuario.getNombreUsuario());
        labelApellido.setText(usuario.getApellidoUsuario());
        labelEmail.setText(usuario.getCorreoUsuario());
    }

    private void cargarUsuarios() {
        cbxUsuario.setItems(listarUsuarios());
    }

    private ObservableList<Usuario> listarUsuarios() {
        ArrayList<Usuario> usuarios = new ArrayList<>();
        String sql = "call sp_ListarUsuarios()";
        try (CallableStatement cs = Conexion.getInstancia().getConexion().prepareCall(sql); ResultSet rs = cs.executeQuery()) {
            while (rs.next()) {
                usuarios.add(new Usuario(rs.getInt("idUsuario"), rs.getString("nombreUsuario"),
                        rs.getString("apellidoUsuario"), rs.getString("correoUsuario")));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return FXCollections.observableArrayList(usuarios);
    }

    private void cargarProductos() {
        cbxProducto.setItems(listarProductos());
    }

    private ObservableList<Producto> listarProductos() {
        ArrayList<Producto> productos = new ArrayList<>();
        String sql = "call sp_ListarProductos();";
        try (CallableStatement cs = Conexion.getInstancia().getConexion().prepareCall(sql); ResultSet rs = cs.executeQuery()) {
            while (rs.next()) {
                productos.add(new Producto(rs.getInt("idProducto"), rs.getString("nombreProducto"),
                        rs.getInt("cantidadProducto"), rs.getDouble("precioProducto"),
                        rs.getString("estadoProducto"), rs.getInt("idCategoria")));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return FXCollections.observableArrayList(productos);
    }

    private ObservableList<DetalleCarrito> listarTodosLosDetalles() {
        ArrayList<DetalleCarrito> detalles = new ArrayList<>();
        String sql = "call sp_ListarDetalleCarritos()";
        try (CallableStatement cs = Conexion.getInstancia().getConexion().prepareCall(sql); ResultSet rs = cs.executeQuery()) {
            while (rs.next()) {
                detalles.add(new DetalleCarrito(rs.getInt("idDetalleCarrito"), rs.getInt("idCarrito"),
                        rs.getInt("idProducto"), rs.getInt("cantidad")));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return FXCollections.observableArrayList(detalles);
    }

    private void mostrarAlerta(String titulo, String contenido) {
        Alert alerta = new Alert(Alert.AlertType.WARNING);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(contenido);
        alerta.showAndWait();
    }

    public Main getPrincipal() {
        return principal;
    }

    public void setPrincipal(Main principal) {
        this.principal = principal;
    }

    public void cerrarSesion(ActionEvent evento) {
        UsuarioAutenticado.getInstancia().cerrarSesion();
        principal.cambiarEscena("LoginView.fxml", 1280, 720);
    }

    public void manejarBotonInventario(ActionEvent e) {
        principal.cambiarEscena("InventarioView.fxml", 1280, 720);
    }

    public void manejarBotonEstadistica(ActionEvent e) {
        principal.cambiarEscena("EstadisticasView.fxml", 1280, 720);
    }

    public void manejarBotonMenu(ActionEvent e) {
        principal.cambiarEscena("MenuPrincipalView.fxml", 1280, 720);
    }

    public void manejarBotonContacto(ActionEvent e) {
        principal.cambiarEscena("ContactoView.fxml", 1280, 720);
    }

    public void manejarBotonSalir(ActionEvent e) {
        Platform.exit();
    }

    @FXML
    private void imprimirReporte(ActionEvent event) {
        Integer idUsuario = obtenerIdUsuarioPorCorreo(labelEmail.getText());
        if (idUsuario != 0) {
            new Report().generarReporte("Carrito.jrxml", "ReporteCarrito", idUsuario);
        } else {
            mostrarAlerta("Error de Reporte", "No se pudo generar el reporte porque no se encontró el usuario.");
        }
    }
}
