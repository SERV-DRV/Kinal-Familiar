/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.kinalfamiliar.controller;

import java.net.URL;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.kinalfamiliar.db.Conexion;
import org.kinalfamiliar.system.Main;

/**
 * FXML Controller class
 *
 * @author ks
 */
public class RegisterController implements Initializable {

    private Main principal;

    @FXML
    private CheckBox terminosCheckBox;
    @FXML
    private TextField nombreField;
    @FXML
    private TextField apellidoField;
    @FXML
    private TextField correoField;
    @FXML
    private TextField contrasenaField;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public void setPrincipal(Main principal) {
        this.principal = principal;
    }

    public void manejarBotonLogin(ActionEvent evento) {
        principal.cambiarEscena("LoginView.fxml", 1213, 722);
    }

    public void manejarBotonRegistro(ActionEvent evento) {
        register();
    }

    private void register() {

        if (!terminosCheckBox.isSelected()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Términos y Condiciones");
            alert.setHeaderText(null);
            alert.setContentText("Para registrarte en Kinal Familiar, debes aceptar los Términos y Condiciones.");
            //((Stage) alert.getDialogPane().getScene().getWindow()).getIcons()
            // .add(new Image(getClass().getResourceAsStream(".png")));
            alert.showAndWait();
            return;
        }

        boolean comprobacion = false;
        String nombreCliente = nombreField.getText();
        String apellidoCliente = apellidoField.getText();
        String emailCliente = correoField.getText();
        String contraseñaCliente = contrasenaField.getText();

        if (nombreCliente.isEmpty() || apellidoCliente.isEmpty() || emailCliente.isEmpty() | contraseñaCliente.isEmpty()) {
            System.out.println("Algunos campos estan vacios");
            return;
        }
        if (!emailCliente.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            comprobacion = true;
            System.out.println("El correo debe tener un dominio");
        }
        if (!contraseñaCliente.matches(".*\\d.*")) {
            comprobacion = true;
            System.out.println("La contraseña debe tener al menos 1 numero");
        }
        if (comprobacion) {
            return;
        }
        try {
            Connection quintom = Conexion.getInstancia().getConexion();
            String sql = "call sp_AgregarUsuario(?, ?, ?, ?)";
            CallableStatement agregado = quintom.prepareCall(sql);
            agregado.setString(1, nombreCliente);
            agregado.setString(2, apellidoCliente);
            agregado.setString(3, emailCliente);
            agregado.setString(4, contraseñaCliente);
            agregado.executeUpdate();
            principal.cambiarEscena("LoginView.fxml", 1213, 722);
            agregado.close();
        } catch (SQLException e) {
            System.out.println("Error al agregar el usuario: " + e.getMessage());
        }
    }

}
