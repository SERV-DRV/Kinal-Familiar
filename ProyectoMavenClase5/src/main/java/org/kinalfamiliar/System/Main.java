/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package org.kinalfamiliar.System;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.json.JSONObject;

/**
 *
 * @author informatica
 */
public class Main extends Application{

    public static void main(String[] args) {
        launch(args);
//        JSONObject persona = new JSONObject();
//            persona.put("nombre:","Santiago");
//            persona.put("apellido:","De Rosa");
//            persona.put("edad:", 17 );
//            persona.put("valido:",true);
//            
//            System.out.println("Contenido de JSON:");
//            System.out.println(persona.toString(1));
    }

    @Override
    public void start(Stage escenario) throws Exception {
       FXMLLoader cargador = new FXMLLoader(getClass().getResource("/view/InicioView.fxml"));
       Parent raiz = cargador.load();
       Scene escena = new Scene(raiz);
       escenario.setScene(escena);
       escenario.show();
    }
}