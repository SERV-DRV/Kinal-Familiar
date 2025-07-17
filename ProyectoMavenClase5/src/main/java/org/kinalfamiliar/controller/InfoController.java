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
public class InfoController implements Initializable {

    @FXML
    private Label labelNombre;
    @FXML
    private Label labelApellido;
    @FXML
    private Label labelEmail;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarDatosUsuario();
    }

    private Main principal;

    public Main getPrincipal() {
        return principal;
    }

    public void setPrincipal(Main principal) {
        this.principal = principal;
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

    public void manejarBotonMenu(ActionEvent evento) {
        principal.cambiarEscena("MenuPrincipalView.fxml", 1280, 720);
    }

    public void manejarBotonContacto(ActionEvent evento) {
        principal.cambiarEscena("ContactoView.fxml", 1280, 720);
    }

    public void manejarBotonSalir(ActionEvent evento) {
        Platform.exit();
    }
}
