package org.kinalfamiliar.reporte;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import javafx.event.ActionEvent;
import javafx.stage.FileChooser;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import org.kinalfamiliar.db.Conexion;

public class Report {

    public void imprimirReporteInventario(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar Reporte");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos PDF (*.pdf)", "*.pdf"));

        String reportName = "ReporteInventario";
        String date = LocalDate.now().toString();
        String initialFileName = reportName + "_" + date + ".pdf";
        fileChooser.setInitialFileName(initialFileName);

        File file = fileChooser.showSaveDialog(null);

        if (file != null) {
            try {
                Connection conexion = Conexion.getInstancia().getConexion();

                if (conexion == null) {
                    System.err.println("Error Crítico: La conexión a la base de datos es nula.");
                    return;
                }

                Map<String, Object> parametros = new HashMap<>();

                URL imageUrl = getClass().getResource("/jasper/");
                if (imageUrl != null) {
                    File imageDir = new File(imageUrl.toURI());
                    parametros.put("IMAGES_DIR", imageDir.getAbsolutePath() + File.separator);
                }

                InputStream reporteStream = getClass().getResourceAsStream("/jasper/Inventario.jrxml");
                if (reporteStream == null) {
                    System.err.println("Error: No se pudo encontrar el archivo de reporte '/jasper/Inventario.jrxml'.");
                    return;
                }

                JasperDesign jasperDesign = JRXmlLoader.load(reporteStream);
                JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, conexion);

                OutputStream outputStream = new FileOutputStream(file);
                JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);

                outputStream.close();

                System.out.println("Reporte guardado exitosamente en: " + file.getAbsolutePath());

                if (Desktop.isDesktopSupported()) {
                    try {
                        Desktop.getDesktop().open(file);
                    } catch (IOException e) {
                        System.err.println("Error al intentar abrir el archivo PDF automáticamente.");
                        e.printStackTrace();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("El usuario canceló la operación de guardado.");
        }
    }
}