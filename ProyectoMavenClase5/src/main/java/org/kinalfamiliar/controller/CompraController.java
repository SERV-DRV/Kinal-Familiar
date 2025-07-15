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
import org.kinalfamiliar.model.Carrito;
import org.kinalfamiliar.model.Categoria;
import org.kinalfamiliar.model.DetalleCarrito;
import org.kinalfamiliar.model.Producto;
import org.kinalfamiliar.model.Usuario;
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
        colIdUsuario.setCellValueFactory(new PropertyValueFactory<Carrito, Integer>("idCliente"));
        colFechaCreacion.setCellValueFactory(new PropertyValueFactory<Carrito, LocalDateTime>("fechaCreacion"));
        colEstado.setCellValueFactory(new PropertyValueFactory<Carrito, String>("estado"));
    }
    
    private void configurarColumnasDetalleCarrito(){
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
        }
    }

    private void cargarFormularioDetalleCarrito() {
        DetalleCarrito detalleCarrito = tblDetalleCarrito.getSelectionModel().getSelectedItem();
        if (detalleCarrito != null) {
            for (Producto p : cbxProducto.getItems()) {
                if (p.getIdProducto() == detalleCarrito.getIdProducto()) {
                    cbxProducto.setValue(p);
                    break;
                }
            }
        }
    }
    
    

}
