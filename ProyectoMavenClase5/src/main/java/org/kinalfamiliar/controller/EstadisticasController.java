/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.kinalfamiliar.controller;

import java.net.URL;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.kinalfamiliar.db.Conexion;
import org.kinalfamiliar.system.Main;

/**
 * FXML Controller class
 *
 * @author enriq
 */
public class EstadisticasController implements Initializable {

    @FXML
    private TextField txtClientes, txtCarritos;
    @FXML
    private Label labelNombre;
    @FXML
    private Label labelApellido;
    @FXML
    private Label labelEmail;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        mostrarEstadisticas();
        cargarDatosUsuario();
    }

    private Main principal;

    public Main getPrincipal() {
        return principal;
    }

    public void setPrincipal(Main principal) {
        this.principal = principal;
    }

    private String obtenerConteo(String procedimiento) {
        String total = null;
        try {
            Connection conexion = Conexion.getInstancia().getConexion();
            CallableStatement cs = conexion.prepareCall("call " + procedimiento + "();");
            ResultSet rs = cs.executeQuery();
            if (rs.next()) {
                total = rs.getString("RESULTADO");
            }
        } catch (Exception e) {
            System.out.println("Error al obtener estadísticas de " + procedimiento);
            e.printStackTrace();
        }
        return total;
    }

    public void mostrarEstadisticas() {
        txtClientes.setText(String.valueOf(obtenerConteo("sp_contarClientes")));
        txtCarritos.setText(String.valueOf(obtenerConteo("sp_contarCarritos")));
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
        principal.cambiarEscena("LoginView.fxml", 1213, 722);
    }

    public void manejarBotonCarrito(ActionEvent evento) {
        principal.cambiarEscena("CompraView.fxml", 1280, 720);
    }

    public void manejarBotonInventario(ActionEvent evento) {
        principal.cambiarEscena("InventarioView.fxml", 1280, 720);
    }

    public void manejarBotonSalir(ActionEvent evento) {
        Platform.exit();
    }
}
