module org.kinalfamiliar.proyectomavenclase5 {
    requires java.sql;
    requires java.logging;
    requires javafx.controls;
    requires javafx.fxml;
    requires org.json;
    requires mysql.connector.java;
    requires java.base;

    opens org.kinalfamiliar.system to javafx.fxml;
    opens org.kinalfamiliar.controller to javafx.fxml;
    opens org.kinalfamiliar.model to javafx.base;
    
    exports org.kinalfamiliar.system;
        
    requires jasperreports;
}