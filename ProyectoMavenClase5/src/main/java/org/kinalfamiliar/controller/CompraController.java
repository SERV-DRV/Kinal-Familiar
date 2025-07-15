/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.kinalfamiliar.controller;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import org.kinalfamiliar.model.Producto;
import org.kinalfamiliar.system.Main;

/**
 * FXML Controller class
 *
 * @author enriq
 */
public class CompraController implements Initializable {

    @FXML
    private TableView<Carrito> tblCarrito;
    @FXML
    private TableView<DetalleCarrito> tblDetalleCarrito;
    @FXML
    private TableColumn colIdCarrito, colIdCliente, colFechaCreacion, colEstado;
    @FXML
    private TableColumn colIdDetalleCarrito, colCantidad, colIdProducto, colIdCarrito2;
    @FXML
    private ComboBox<Usuario> cbxUsuario;
    @FXML
    private ComboBox<Producto> cbxProducto;
    @FXML
    private Button btnMas, btnMenos;
    @FXML
    private Button btnNuevo, btnEliminar, btnEditar;
    @FXML
    private TextArea txtACantidad;

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

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    private void configurarColumnasCarrito() {
        colIdCarrito.setCellValueFactory(new PropertyValueFactory<Carrito, Integer>("idCarrito"));
        colIdCliente.setCellValueFactory(new PropertyValueFactory<Carrito, Integer>("idCliente"));
        colFechaCreacion.setCellValueFactory(new PropertyValueFactory<Carrito, LocalDateTime>("fechaCreacion"));
        colEstado.setCellValueFactory(new PropertyValueFactory<Carrito, String>("estado"));
    }
    
    private void cargarFormularioCarrito() {
        Carrito carrito = tblCarrito.getSelectionModel().getSelectedItem();
        if(carrito != null){
            for(Usuario u: cbxUsuario.getItems()){
                if(u.idUsuario == carrito.getIdUsuario()){
                    cbxUsuario.setValue(u);
                    break;
                }
            }
        }
    }

}
