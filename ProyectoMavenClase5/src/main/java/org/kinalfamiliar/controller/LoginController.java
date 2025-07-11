package org.kinalfamiliar.controller;

import java.net.URL;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import org.kinalfamiliar.db.Conexion;
import org.kinalfamiliar.system.Main;

public class LoginController implements Initializable {

    private Main principal;

    @FXML
    private TextField usernameField;
    @FXML
    private TextField passwordField;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public void setPrincipal(Main principal) {
        this.principal = principal;
    }

    public void manejarBotonLogin(ActionEvent evento) {
        //login();
        principal.cambiarEscena("InventarioView.fxml", 1280, 720);
    }

    private void login() {
        String emailCliente = usernameField.getText();
        String contraseñaCliente = passwordField.getText();

        if (emailCliente.isEmpty() || contraseñaCliente.isEmpty()) {
            System.out.println("El area está vacia");
        } else {
            try {
                Connection quintom = Conexion.getInstancia().getConexion();
                String sql = "call sp_inicioSesion(?, ?)";
                CallableStatement login = quintom.prepareCall(sql);
                login.setString(1, emailCliente);
                login.setString(2, contraseñaCliente);

                boolean esValido = login.execute();
                if (esValido) {
                    var resultado = login.getResultSet();
                    if (resultado.next()) {
                        int valido = resultado.getInt("inicioValido");
                        if (valido == 1) {
                            principal.cambiarEscena("InventarioView.fxml", 1280, 720);
                        } else {
                            System.out.println("Algo fue incorrecto");
                        }
                    }
                }
                login.close();
            } catch (SQLException e) {
                System.out.println("Error al verificar el empleado: " + e.getMessage());
            }
        }
    }

}
