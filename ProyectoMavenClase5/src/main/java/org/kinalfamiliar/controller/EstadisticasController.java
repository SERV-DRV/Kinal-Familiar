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
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
    private TextField txtClientes,txtCarritos;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
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
}
