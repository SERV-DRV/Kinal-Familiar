package org.kinalfamiliar.system;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.json.JSONObject;
import org.kinalfamiliar.controller.LoginController;

public class Main extends Application {

    public Stage cambioEscena;
    private static String URL_VIEW = "/view/";
    private static String URL_IMAGE = "/images/";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage escenario) throws Exception {
        this.cambioEscena = escenario;
        FXMLLoader cargador = new FXMLLoader(getClass().getResource(URL_VIEW + "LoginView.fxml"));
        Parent raiz = cargador.load();

        LoginController controlador = cargador.getController();
        controlador.setPrincipal(this);

        Scene escena = new Scene(raiz);
        escenario.setScene(escena);
        escenario.setResizable(false);
        escenario.show();
    }

    public void cambiarEscena(String fxml, double ancho, double alto) {
        try {
            FXMLLoader cargadorFXML = new FXMLLoader(getClass().getResource(URL_VIEW + fxml));
            Parent archivoFXML = cargadorFXML.load();

            Scene escena = new Scene(archivoFXML, ancho, alto);
            cambioEscena.setScene(escena);

            Object controller = cargadorFXML.getController();

            try {
                Method metodo = controller.getClass().getMethod("setPrincipal", Main.class);
                metodo.invoke(controller, this);
            } catch (NoSuchMethodException e) {
                System.out.println("El controlador no tiene el método setPrincipal.");
            } catch (IllegalAccessException | InvocationTargetException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (IOException ex) {
            System.out.println("Ocurrió un error al cambiar la escena: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

}
