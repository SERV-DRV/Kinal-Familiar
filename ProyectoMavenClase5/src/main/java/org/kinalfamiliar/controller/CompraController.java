package org.kinalfamiliar.controller;

import java.net.URL;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
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
    private Button btnMas, btnMenos;
    @FXML
    private Button btnNuevo, btnEliminar, btnEditar;
    @FXML
    private TextArea txtACantidad;

    private Main principal;
    private Producto mProducto;
    private ObservableList<Producto> listaProductos;
    private ObservableList<Usuario> listaUsuarios;
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
            txtACantidad.setText(String.valueOf(detalleCarrito.getCantidad()));
        }
    }
    
    private  ArrayList<Carrito> listarCarrito(){
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
    
    private void cargarTableView(){
        listaCarritos = FXCollections.observableArrayList(listarCarrito());
        tblCarrito.setItems(listaCarritos);
        tblCarrito.getSelectionModel().selectFirst();
        cargarFormularioCarrito();
    }
    
    private ArrayList<Usuario> listarUsuarios(){
        ArrayList<Usuario> usuario = new ArrayList<>();
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion()
                    .prepareCall("call sp_ListarDetalleCarritos");
            ResultSet resultado = enunciado.executeQuery();
            while(resultado.next()){
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
    }
    
}
