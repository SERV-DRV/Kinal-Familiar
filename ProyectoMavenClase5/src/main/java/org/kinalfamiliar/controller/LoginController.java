package org.kinalfamiliar.controller;

import java.net.URL;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
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
        if (login()) {
            principal.cambiarEscena("InventarioView.fxml", 1280, 720);
        }
    }

    public void manejarBotonRegister(ActionEvent evento) {
        principal.cambiarEscena("RegistroView.fxml", 1213, 722);
    }

    private boolean login() {
        String emailCliente = usernameField.getText();
        String contraseñaCliente = passwordField.getText();
        boolean accesoConcedido = false;

        if (emailCliente.isEmpty() || contraseñaCliente.isEmpty()) {
            System.out.println("Por favor, ingrese su correo y contraseña.");
            return false;
        }

        try {
            Connection quintom = Conexion.getInstancia().getConexion();
            String sqlLogin = "call sp_inicioSesion(?, ?)";
            CallableStatement loginStatement = quintom.prepareCall(sqlLogin);
            loginStatement.setString(1, emailCliente);
            loginStatement.setString(2, contraseñaCliente);

            if (loginStatement.execute()) {
                ResultSet resultadoLogin = loginStatement.getResultSet();
                if (resultadoLogin.next()) {
                    int valido = resultadoLogin.getInt("inicioValido");
                    if (valido == 1) {
                        System.out.println("Inicio de sesión exitoso.");
                        obtenerYGuardarDatosEmpleado(emailCliente);
                        accesoConcedido = true;
                    } else {
                        System.out.println("Correo o contraseña incorrectos.");
                    }
                }
            }
            loginStatement.close();
        } catch (SQLException e) {
            System.out.println("Error en el proceso de login: " + e.getMessage());
        }
        return accesoConcedido;
    }

    private void obtenerYGuardarDatosEmpleado(String email) {
        try {
            Connection quintom = Conexion.getInstancia().getConexion();
            String sqlDatos = "call sp_ObtenerDatosEmpleado(?)";
            CallableStatement datosStatement = quintom.prepareCall(sqlDatos);
            datosStatement.setString(1, email);

            if (datosStatement.execute()) {
                ResultSet resultadoDatos = datosStatement.getResultSet();
                if (resultadoDatos.next()) {
                    UsuarioAutenticado.getInstancia().setNombreUsuario(resultadoDatos.getString("nombreUsuario"));
                    UsuarioAutenticado.getInstancia().setApellidoUsuario(resultadoDatos.getString("apellidoUsuario"));
                    UsuarioAutenticado.getInstancia().setCorreoUsuario(resultadoDatos.getString("correoUsuario"));

                    System.out.println("Datos del usuario guardados: " + UsuarioAutenticado.getInstancia().getNombreUsuario());
                }
            }
            datosStatement.close();
        } catch (SQLException e) {
            System.out.println("Error al obtener los datos del empleado: " + e.getMessage());
        }
    }
}
