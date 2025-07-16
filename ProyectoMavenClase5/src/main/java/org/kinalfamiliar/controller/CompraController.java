package org.kinalfamiliar.controller;

import java.net.URL;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
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
import org.kinalfamiliar.system.Main;

public class CompraController implements Initializable {

    @FXML
    private TableView<Carrito> tblCarrito;
    @FXML
    private TableColumn colIdCarrito, colIdUsuario, colFechaCreacion, colEstado;
    @FXML
    private TableView<DetalleCarrito> tblDetalleCarrito;
    @FXML
    private TableColumn colIdDetalleCarrito, colCantidad, colIdProducto, colIdCarrito2;
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

    private Main principal;
    private Carrito mCarrito;
    private DetalleCarrito mDetalleCarrito;
    private ObservableList<Carrito> listaCarritos;
    private ObservableList<DetalleCarrito> listaDetalleCarritos;

    private enum acciones {
        AGREGAR, EDITAR, ELIMINAR, NINGUNA
    }

    acciones accionActual = acciones.NINGUNA;

    public Main getPrincipal() {
        return principal;
    }

    public void setPrincipal(Main principal) {
        this.principal = principal;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarColumnasCarrito();
        configurarColumnasDetalleCarrito();
        cargarTableViewCarrito();
        cargarTableViewDetalleCarrito();
    }

    private void configurarColumnasCarrito() {
        colIdCarrito.setCellValueFactory(new PropertyValueFactory<Carrito, Integer>("idCarrito"));
        colIdUsuario.setCellValueFactory(new PropertyValueFactory<Carrito, Integer>("idUsuario"));
        colFechaCreacion.setCellValueFactory(new PropertyValueFactory<Carrito, LocalDateTime>("fechaCreacion"));
        colEstado.setCellValueFactory(new PropertyValueFactory<Carrito, String>("estado"));
    }

    private void configurarColumnasDetalleCarrito() {
        colIdDetalleCarrito.setCellValueFactory(new PropertyValueFactory<DetalleCarrito, Integer>("idDetalleCarrito"));
        colIdCarrito2.setCellValueFactory(new PropertyValueFactory<DetalleCarrito, Integer>("idCarrito"));
        colIdProducto.setCellValueFactory(new PropertyValueFactory<DetalleCarrito, Integer>("idProducto"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<DetalleCarrito, Integer>("cantidad"));
    }

    private void cargarFormularioCarrito() {
        Carrito carrito = tblCarrito.getSelectionModel().getSelectedItem();
        if (carrito != null) {
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

    private ArrayList<Carrito> listarCarrito() {
        ArrayList<Carrito> carrito = new ArrayList<>();
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion()
                    .prepareCall("call sp_ListarCarritos();");
            ResultSet resultado = enunciado.executeQuery();
            while (resultado.next()) {
                carrito.add(new Carrito(
                        resultado.getInt(1),
                        resultado.getString(2),
                        resultado.getTimestamp(3).toLocalDateTime(),
                        resultado.getInt(4)
                )
                );
            }

        } catch (SQLException ex) {
            System.out.println("Error al listar Carrito");
            ex.printStackTrace();
        }
        return carrito;
    }

    private ArrayList<DetalleCarrito> listarDetalleCarrito() {
        ArrayList<DetalleCarrito> detalleCarrito = new ArrayList<>();
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion()
                    .prepareCall("call sp_ListarDetalleCarritos()");
            ResultSet resultado = enunciado.executeQuery();
            while (resultado.next()) {
                detalleCarrito.add(new DetalleCarrito(
                        resultado.getInt(1),
                        resultado.getInt(2),
                        resultado.getInt(3),
                        resultado.getInt(4)
                )
                );
            }
        } catch (SQLException ex) {
            System.out.println("Error al listar DetalleCarritos");
            ex.printStackTrace();
        }
        return detalleCarrito;
    }

    private void cargarTableViewCarrito() {
        listaCarritos = FXCollections.observableArrayList(listarCarrito());
        tblCarrito.setItems(listaCarritos);
        tblCarrito.getSelectionModel().selectFirst();
        cargarFormularioCarrito();
    }

    private void cargarTableViewDetalleCarrito() {
        listaDetalleCarritos = FXCollections.observableArrayList(listarDetalleCarrito());
        tblDetalleCarrito.setItems(listaDetalleCarritos);
        tblDetalleCarrito.getSelectionModel().selectFirst();
        cargarFormularioDetalleCarrito();
    }

    private ArrayList<Usuario> listarUsuarios() {
        ArrayList<Usuario> usuario = new ArrayList<>();
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion()
                    .prepareCall("call sp_ListarUsuarios(?, ?, ?)");
            ResultSet resultado = enunciado.executeQuery();
            while (resultado.next()) {
                usuario.add(new Usuario(
                        resultado.getInt(1),
                        resultado.getString(2),
                        resultado.getString(3)
                )
                );
            }
        } catch (SQLException ex) {
            System.out.println("Error al listar usuarios en vista compras");
            ex.printStackTrace();
        }
        return usuario;
    }

    private void cargarUsuarios() {
        ObservableList<Usuario> listaUsuario = FXCollections.observableArrayList(listarUsuarios());
        cbxUsuario.setItems(listaUsuario);
    }

    private ArrayList<Producto> listarProductos() {
        ArrayList<Producto> productos = new ArrayList<>();
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion()
                    .prepareCall("call sp_ListarProductos();");
            ResultSet resultado = enunciado.executeQuery();
            while (resultado.next()) {
                productos.add(new Producto(
                        resultado.getInt(1),
                        resultado.getString(2),
                        resultado.getInt(3),
                        resultado.getDouble(4),
                        resultado.getString(5),
                        resultado.getInt(6)
                )
                );
            }
        } catch (SQLException ex) {
            System.out.println("Error al listar productos");
            ex.printStackTrace();
        }
        return productos;
    }

    private void cargarProductos() {
        ObservableList<Producto> listaCategoria = FXCollections.observableArrayList(listarProductos());
        cbxProducto.setItems(listaCategoria);
    }

    private Carrito cargarModeloCarrito() {
        int idCarrito = txtIdCarrito.getText().isEmpty() ? 0 : Integer.parseInt(txtIdCarrito.getText());

        Usuario usuarioSeleccionado = cbxUsuario.getSelectionModel().getSelectedItem();
        int idUsuario = usuarioSeleccionado != null ? usuarioSeleccionado.getIdUsuario() : 0;

        String estado = rbActivo.isSelected() ? "Activo" : "Inactivo";

        return new Carrito(idCarrito, estado, idUsuario);
    }

    private DetalleCarrito cargarModeloDetalleCarrito() {
        int idDetalleCarrito = txtIdCarrito.getText().isEmpty() ? 0 : Integer.parseInt(txtIdDetalleCarrito.getText());

        int FKidCarrito = Integer.valueOf(txtIdCarrito.getText());
        txtIdCarrito2.setText(String.valueOf(FKidCarrito));

        Producto productoSeleccionado = cbxProducto.getSelectionModel().getSelectedItem();
        int idProducto = productoSeleccionado != null ? productoSeleccionado.getIdProducto() : 0;

        return new DetalleCarrito(idDetalleCarrito, FKidCarrito, Integer.parseInt(txtACantidad.getText()), idProducto);
    }
    
    // Crud de Carritos    ------------------------------------------------------------
    
    // Insertar Carrito
    private void insertarCarrito(){
        mCarrito = cargarModeloCarrito();
        Usuario usuarioSeleccionado = cbxUsuario.getSelectionModel().getSelectedItem();
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion()
                    .prepareCall("call sp_InsertarCarrito(?, ?)");
            enunciado.setString(1, mCarrito.getEstado());
            enunciado.setInt(2, usuarioSeleccionado.getIdUsuario());
        } catch (SQLException ex) {
            System.out.println("Error al insertar carrito");
            ex.printStackTrace();
        }
    }
    
     // Actualizar/Modificar Carrito
    private void actualizarCarrito(){
        mCarrito = cargarModeloCarrito();
        Usuario usuarioSeleccionado = cbxUsuario.getSelectionModel().getSelectedItem();
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion()
                    .prepareCall("call sp_ActualizarCarrito(?, ?, ?)");
            enunciado.setInt(1, mCarrito.getIdCarrito());
            enunciado.setString(1, mCarrito.getEstado());
            enunciado.setInt(2, usuarioSeleccionado.getIdUsuario());
        } catch (SQLException ex) {
            System.out.println("Error al insertar carrito");
            ex.printStackTrace();
        }
    }
    
    // Crud de Detalle Carrito    ------------------------------------------------------------
    
    // Insertar Detalle Carrito
    private void insertarDetalleCarrito() {
        mDetalleCarrito = cargarModeloDetalleCarrito();
        Producto productoSeleccionado = cbxProducto.getSelectionModel().getSelectedItem();
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion()
                    .prepareCall("call sp_InsertarDetalleCarrito(?, ?, ?)");
            enunciado.setInt(1, mDetalleCarrito.getIdCarrito());
            enunciado.setInt(2, productoSeleccionado.getIdProducto());
            enunciado.setInt(3, mDetalleCarrito.getCantidad());
            int registroAgregado = enunciado.executeUpdate();
            if (registroAgregado > 0) {
                System.out.println("Producto al carrito agregado correctamente");
                cargarTableViewDetalleCarrito();
            }
        } catch (SQLException ex) {
            System.out.println("Error al insertar en detalleCarrito");
            ex.printStackTrace();
        }
    }
    
    // Actualizar/Modificar Detalle Carrito
    private void actualizarDetalleCarrito() {
        mDetalleCarrito = cargarModeloDetalleCarrito();
        Producto productoSeleccionado = cbxProducto.getSelectionModel().getSelectedItem();
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion()
                    .prepareCall("call sp_ActualizarDetalleCarrito(?, ?, ?, ?)");
            enunciado.setInt(1, mDetalleCarrito.getIdDetalleCarrito());
            enunciado.setInt(2, mDetalleCarrito.getIdCarrito());
            enunciado.setInt(3, productoSeleccionado.getIdProducto());
            enunciado.setInt(4, mDetalleCarrito.getCantidad());
            enunciado.executeUpdate();
            cargarTableViewDetalleCarrito();
        } catch (SQLException ex) {
            System.out.println("Error al modificar pedido en detalleCarrito");
            ex.printStackTrace();
        }
    }
    
    // Eliminar Detalle Carrito
    private void eliminarDetalleCarrito() {
        mDetalleCarrito = tblDetalleCarrito.getSelectionModel().getSelectedItem();
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion()
                    .prepareCall("call sp_EliminarDetalleCarrito(?)");
            enunciado.setInt(1, mDetalleCarrito.getIdDetalleCarrito());
            enunciado.executeUpdate();
            cargarTableViewDetalleCarrito();
        } catch (SQLException ex) {
            System.out.println("Error al eliminar en detalleCarrito");
            ex.printStackTrace();
        }
    }
    
    private void actualizarEstadoAccionCarrito(acciones estado) {
        accionActual = estado;
        boolean activo = (estado == acciones.AGREGAR || estado == acciones.EDITAR);
        
        txtIdCarrito.setDisable(activo);
        cbxUsuario.setDisable(!activo);
        
        tblCarrito.setDisable(activo);
        btnNuevoC.setText(activo ? "," : ".");
        btnEditarC.setDisable(activo);
    }
    
    private void actualizarEstadoAccionDetalleCarrito(acciones estado) {
        accionActual = estado;
        boolean activo = (estado == acciones.AGREGAR || estado == acciones.EDITAR);
        
        txtIdDetalleCarrito.setDisable(activo);
        txtACantidad.setDisable(!activo);
        cbxProducto.setDisable(!activo);
        
        tblDetalleCarrito.setDisable(activo);
        btnNuevoDc.setText(activo ? "Guardar" : "Agregar al carrito");
        btnEliminarDc.setText(activo ? "Cancelar" : "Eliminar producto");       
        btnEditarDc.setDisable(activo);
    }

    /*
    
    @FXML
    private TableView<Carrito> tblCarrito;
    @FXML
    private TableColumn colIdCarrito, colIdUsuario, colFechaCreacion, colEstado;
    @FXML
    private TableView<DetalleCarrito> tblDetalleCarrito;
    @FXML
    private TableColumn colIdDetalleCarrito, colCantidad, colIdProducto, colIdCarrito2;
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
    */
    
    @FXML
    private void agregarCarritoAction() {
        switch (accionActual) {
            case NINGUNA:
                actualizarEstadoAccionCarrito(acciones.AGREGAR);
                break;
            case AGREGAR:
                if (cbxUsuario.getValue()==null) {
                    Alert alerta = new Alert(Alert.AlertType.WARNING);
                    alerta.setTitle("Campos incompletos");
                    alerta.setHeaderText("Faltan datos");
                    alerta.setContentText("Por favor, llena todos los campos antes de guardar.");
                    alerta.showAndWait();
                    return;
                }
                insertarCarrito();
                System.out.println("Guardando los datos ingresados");
                actualizarEstadoAccionCarrito(acciones.NINGUNA);
                break;
            case EDITAR:
                if (cbxUsuario.getValue() == null) {
                    Alert alerta = new Alert(Alert.AlertType.WARNING);
                    alerta.setTitle("Campos incompletos");
                    alerta.setHeaderText("Faltan datos");
                    alerta.setContentText("Por favor, llena todos los campos antes de guardar.");
                    alerta.showAndWait();
                    return;
                }
                actualizarCarrito();
                System.out.println("Guardando edicion indicada");
                actualizarEstadoAccionCarrito(acciones.NINGUNA);
                break;
        }
    }

    @FXML
    private void editarCarritoAction() {
        if (txtIdCarrito.getText().isEmpty()) {
            Alert alerta = new Alert(Alert.AlertType.WARNING);
            alerta.setTitle("Error al editar");
            alerta.setHeaderText("No hay datos que editar");
            alerta.setContentText("No se puede editar si no hay datos.");
            alerta.showAndWait();
            return;
        }
        actualizarEstadoAccionCarrito(acciones.EDITAR);
    }

    @FXML
    private void cancelarEliminarDetalleCarrito() {
        if (accionActual == acciones.NINGUNA) {
            if (txtIdDetalleCarrito.getText().isEmpty()) {
                Alert alerta = new Alert(Alert.AlertType.WARNING);
                alerta.setTitle("Error al eliminar");
                alerta.setHeaderText("No hay datos que eliminar");
                alerta.setContentText("No se puede eliminar si no hay datos.");
                alerta.showAndWait();
                return;
            } else {
                Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
                confirmacion.setTitle("Confirmar eliminación");
                confirmacion.setHeaderText("¿Seguro que desea eliminar este cliente?");
                confirmacion.setContentText("Esta acción no se puede deshacer.");

                ButtonType btnEliminar = new ButtonType("Eliminar");
                ButtonType btnCancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);

                confirmacion.getButtonTypes().setAll(btnEliminar, btnCancelar);

                Optional<ButtonType> resultado = confirmacion.showAndWait();

                if (resultado.isPresent() && resultado.get() == btnEliminar) {
                    System.out.println("Eliminando registro");
                    eliminarDetalleCarrito();
                } else {
                    System.out.println("Eliminación cancelada");
                }

            }
        } else {
//            actualizarEstadoAccion(acciones.NINGUNA);
            cargarFormularioDetalleCarrito();
        }
    }

}
