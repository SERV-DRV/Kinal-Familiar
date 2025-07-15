/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.kinalfamiliar.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import org.kinalfamiliar.system.Main;
import org.kinalfamiliar.db.Conexion;
import org.kinalfamiliar.model.Categoria;
import org.kinalfamiliar.model.Producto;
import org.kinalfamiliar.reporte.*;

/**
 * FXML Controller class
 *
 * @author informatica
 */
public class InventarioController implements Initializable {

    @FXML
    private Button btnNuevo, btnEditar, btnEliminar;
    @FXML
    private TableView<Producto> tblProductos;
    @FXML
    private TableColumn colId, colNombre, colCantidad, colPrecio, colEstado, colIdCategoria;
    @FXML
    private TextField txtIdProducto, txtNombre, txtPrecio, txtBuscar;
    @FXML
    private Spinner<Integer> spCantidad;
    @FXML
    private ComboBox<Categoria> cbxCategoria;
    @FXML
    private RadioButton rbDisponible, rbIndisponible;
    @FXML
    private ToggleGroup grupoEstado;

    private Main principal;
    private Producto mProducto;
    private ObservableList<Producto> listaProductos;

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
        // TODO
        configurarColumnas();
        configurarSpinner();
        cargarCategorias();
        cargarTableView();
        tblProductos.setOnMouseClicked(eh -> cargarFormulario());
        txtBuscar.textProperty().addListener((obs, oldText, newText) -> {
            buscarProducto();
        });
    }

    private void configurarColumnas() {
        colId.setCellValueFactory(new PropertyValueFactory<Producto, Integer>("idProducto"));
        colNombre.setCellValueFactory(new PropertyValueFactory<Producto, String>("nombre"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<Producto, Integer>("cantidad"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<Producto, Double>("precio"));
        colEstado.setCellValueFactory(new PropertyValueFactory<Producto, String>("estado"));
        colIdCategoria.setCellValueFactory(new PropertyValueFactory<Producto, Integer>("idCategoria"));
    }

    private void configurarSpinner() {
        // tipo Double
        SpinnerValueFactory<Integer> valoresEnteros
                = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 500, 1);
        spCantidad.setValueFactory(valoresEnteros);
    }

    private void cargarFormulario() {
        Producto producto = tblProductos.getSelectionModel().getSelectedItem();
        if (producto != null) {
            txtIdProducto.setText(String.valueOf(producto.getIdProducto()));
            txtNombre.setText(producto.getNombre());
            spCantidad.getValueFactory().setValue(producto.getCantidad());
            txtPrecio.setText(String.valueOf(producto.getPrecio()));
            for (Categoria c : cbxCategoria.getItems()) {
                if (c.getIdCategoria() == producto.getIdCategoria()) {
                    cbxCategoria.setValue(c);
                    break;
                }
            }
            if (producto.getEstado().equalsIgnoreCase("Disponible")) {
                rbDisponible.setSelected(true);
            } else {
                rbIndisponible.setSelected(true);
            }
        }
    }

    private void limpiarCampos() {
        txtIdProducto.clear();
        txtNombre.clear();
        configurarSpinner();
        txtPrecio.clear();
        rbDisponible.setSelected(false);
        rbIndisponible.setSelected(false);
        cbxCategoria.getSelectionModel().clearSelection();
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

    private void cargarTableView() {
        listaProductos = FXCollections.observableArrayList(listarProductos());
        tblProductos.setItems(listaProductos);
        tblProductos.getSelectionModel().selectFirst();
        cargarFormulario();
    }

    private Producto cargarModelo() {
        int idProducto = txtIdProducto.getText().isEmpty() ? 0 : Integer.parseInt(txtIdProducto.getText());

        Categoria categoriaSeleccionada = cbxCategoria.getSelectionModel().getSelectedItem();
        int idCategoria = categoriaSeleccionada != null ? categoriaSeleccionada.getIdCategoria() : 0;

        String estado = rbDisponible.isSelected() ? "Disponible" : "Indisponible";

        return new Producto(idProducto, txtNombre.getText(), spCantidad.getValue(),
                Double.parseDouble(txtPrecio.getText()), estado, idCategoria);
    }

    private ArrayList<Categoria> listarCategorias() {
        ArrayList<Categoria> categorias = new ArrayList<>();
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion()
                    .prepareCall("call sp_ListarCategorias()");
            ResultSet resultado = enunciado.executeQuery();
            while (resultado.next()) {
                categorias.add(new Categoria(
                        resultado.getInt(1),
                        resultado.getString(2)
                )
                );
            }
        } catch (SQLException ex) {
            System.out.println("Error al listar categorias en productos");
            ex.printStackTrace();
        }
        return categorias;
    }

    private void cargarCategorias() {
        ObservableList<Categoria> listaCategoria = FXCollections.observableArrayList(listarCategorias());
        cbxCategoria.setItems(listaCategoria);
    }

    private void insertarProducto() {
        mProducto = cargarModelo();
        Categoria categoriaSeleccionada = cbxCategoria.getSelectionModel().getSelectedItem();
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion()
                    .prepareCall("call sp_InsertarProducto(?, ?, ?, ?, ?)");
            enunciado.setString(1, mProducto.getNombre());
            enunciado.setInt(2, mProducto.getCantidad());
            enunciado.setDouble(3, mProducto.getPrecio());
            enunciado.setString(4, mProducto.getEstado());
            enunciado.setInt(5, categoriaSeleccionada.getIdCategoria());
            int registroAgregado = enunciado.executeUpdate();
            if (registroAgregado > 0) {
                System.out.println("Producto agregado correctamente");
                cargarTableView();
            }
        } catch (SQLException ex) {
            System.out.println("Error al insertar producto");
            ex.printStackTrace();
        }
    }

    private void actualizarProducto() {
        mProducto = cargarModelo();
        Categoria categoriaSeleccionada = cbxCategoria.getSelectionModel().getSelectedItem();
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion()
                    .prepareCall("call sp_ActualizarProducto(?, ?, ?, ?, ?, ?)");
            enunciado.setInt(1, mProducto.getIdProducto());
            enunciado.setString(2, mProducto.getNombre());
            enunciado.setInt(3, mProducto.getCantidad());
            enunciado.setDouble(4, mProducto.getPrecio());
            enunciado.setString(5, mProducto.getEstado());
            enunciado.setInt(6, categoriaSeleccionada.getIdCategoria());
            enunciado.executeUpdate();
            cargarTableView();
        } catch (SQLException ex) {
            System.out.println("Error al editar producto");
            ex.printStackTrace();
        }
    }

    private void eliminarProducto() {
        mProducto = tblProductos.getSelectionModel().getSelectedItem();
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion()
                    .prepareCall("call sp_EliminarProducto(?);");
            enunciado.setInt(1, mProducto.getIdProducto());
            enunciado.executeUpdate();
            cargarTableView();
        } catch (SQLException ex) {
            System.out.println("Error al eliminar producto");
            ex.printStackTrace();
        }
    }

    private void actualizarEstadoAccion(acciones estado) {
        accionActual = estado;
        boolean activo = (estado == acciones.AGREGAR || estado == acciones.EDITAR);

        txtIdProducto.setDisable(activo);
        txtNombre.setDisable(!activo);
        spCantidad.setDisable(!activo);
        txtPrecio.setDisable(!activo);
        rbDisponible.setDisable(!activo);
        rbIndisponible.setDisable(!activo);
        cbxCategoria.setDisable(!activo);

        tblProductos.setDisable(activo);
        txtBuscar.setDisable(activo);
        btnNuevo.setText(activo ? "Guardar" : "Nuevo");
        btnEliminar.setText(activo ? "Cancelar" : "Eliminar");
        if (btnNuevo.getText().contains("Nuevo")) {
            btnNuevo.getStyleClass().remove("botonGuardar");
            btnNuevo.getStyleClass().add("botonNuevo");
        } else if (btnNuevo.getText().contains("Guardar")) {
            btnNuevo.getStyleClass().remove("botonNuevo");
            btnNuevo.getStyleClass().add("botonGuardar");
        }
        btnEditar.setDisable(activo);
    }

    @FXML
    private void agregarAction() {
        switch (accionActual) {
            case NINGUNA:
                actualizarEstadoAccion(acciones.AGREGAR);
                limpiarCampos();
                break;
            case AGREGAR:
                if (txtNombre.getText().isEmpty() || spCantidad.getValue() == null || (!rbDisponible.isSelected()
                        && !rbIndisponible.isSelected()) || txtPrecio.getText().isEmpty()
                        || cbxCategoria.getValue() == null) {
                    Alert alerta = new Alert(Alert.AlertType.WARNING);
                    alerta.setTitle("Campos incompletos");
                    alerta.setHeaderText("Faltan datos");
                    alerta.setContentText("Por favor, llena todos los campos antes de guardar.");
                    alerta.showAndWait();
                    return;
                }
                try {
                    Double.parseDouble(txtPrecio.getText());
                } catch (NumberFormatException e) {
                    Alert alerta = new Alert(Alert.AlertType.WARNING);
                    alerta.setTitle("Campos incorrectos");
                    alerta.setHeaderText("Tipo de dato incorrecto");
                    alerta.setContentText("Por favor, intenta con un valor numerico.");
                    alerta.showAndWait();
                    return;
                }
                insertarProducto();
                System.out.println("Guardando los datos ingresados");
                actualizarEstadoAccion(acciones.NINGUNA);
                break;
            case EDITAR:
                if (txtNombre.getText().isEmpty() || spCantidad.getValue() == null || (!rbDisponible.isSelected()
                        && !rbIndisponible.isSelected()) || txtPrecio.getText().isEmpty()
                        || cbxCategoria.getValue() == null) {
                    Alert alerta = new Alert(Alert.AlertType.WARNING);
                    alerta.setTitle("Campos incompletos");
                    alerta.setHeaderText("Faltan datos");
                    alerta.setContentText("Por favor, llena todos los campos antes de guardar.");
                    alerta.showAndWait();
                    return;
                }
                try {
                    Double.parseDouble(txtPrecio.getText());
                } catch (NumberFormatException e) {
                    Alert alerta = new Alert(Alert.AlertType.WARNING);
                    alerta.setTitle("Campos incorrectos");
                    alerta.setHeaderText("Tipo de dato incorrecto");
                    alerta.setContentText("Por favor, intenta con un valor numerico.");
                    alerta.showAndWait();
                    return;
                }
                actualizarProducto();
                System.out.println("Guardando edicion indicada");
                actualizarEstadoAccion(acciones.NINGUNA);
                break;
        }
    }

    @FXML
    private void editarAction() {
        if (txtIdProducto.getText().isEmpty()) {
            Alert alerta = new Alert(Alert.AlertType.WARNING);
            alerta.setTitle("Error al editar");
            alerta.setHeaderText("No hay datos que editar");
            alerta.setContentText("No se puede editar si no hay datos.");
            alerta.showAndWait();
            return;
        }
        actualizarEstadoAccion(acciones.EDITAR);
    }

    @FXML
    private void cancelarEliminar() {
        if (accionActual == acciones.NINGUNA) {
            if (txtIdProducto.getText().isEmpty()) {
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
                    eliminarProducto();
                } else {
                    System.out.println("Eliminación cancelada");
                }

            }
        } else {
            actualizarEstadoAccion(acciones.NINGUNA);
            cargarFormulario();
        }
    }

    @FXML
    private void buscarProducto() {
        String filtro = txtBuscar.getText().toLowerCase();
        ObservableList<Producto> items = tblProductos.getItems();

        for (int i = 0; i < items.size(); i++) {
            Producto producto = items.get(i);
            if (producto.getNombre().toLowerCase().contains(filtro)) {
                tblProductos.getSelectionModel().select(i);
                tblProductos.scrollTo(i);
                break;
            }
        }
    }

    @FXML
    private void btnAtrasAction() {
        int indice = tblProductos.getSelectionModel().getSelectedIndex();
        if (indice > 0) {
            tblProductos.getSelectionModel().select(indice - 1);
            cargarFormulario();
        }
    }

    @FXML
    private void btnAdelanteAction() {
        int indice = tblProductos.getSelectionModel().getSelectedIndex();
        if (indice < listaProductos.size() - 1) {
            tblProductos.getSelectionModel().select(indice + 1);
            cargarFormulario();
        }
    }

    public void manejarBotonCarrito(ActionEvent evento) {
        principal.cambiarEscena("CompraView.fxml", 1213, 722);
    }

    @FXML
    private void imprimirReporte(ActionEvent event) {
        Report reporte = new Report();
        reporte.imprimirReporteInventario(event);
    }
}
