package org.kinalfamiliar.reporte;

import com.mysql.jdbc.Connection;
import java.io.InputStream;
import java.util.Map;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;

public class Report {

    //EL reporte, visualizador, imprimir
    private static JasperReport jreport;
    private static JasperViewer jviewer;
    private static JasperPrint jprint;

    //crear reporte
    //3 parametros: conexion, mapa, objeto:InputStream
    public static void crearReporte(Connection conexion, Map<String, Object> map, InputStream reporteStream) {
        try {
            jreport = JasperCompileManager.compileReport(reporteStream);

            jprint = JasperFillManager.fillReport(jreport, map, conexion);
            
        } catch (Exception e) {
            System.out.println("Error al crear un reporte " + e.getMessage());
            e.printStackTrace();
        }
    }

    //mostrar el repote
    public static void mostrarReporte() {
        if (jprint != null) {
            jviewer = new JasperViewer(jprint, false);
            jviewer.setTitle("Visor de Reportes");
            jviewer.setVisible(true);
        } else {
            System.err.println("JasperPrint es nulo, no se puede mostrar el reporte. Revisa el método crearReporte.");
        }
    }
}