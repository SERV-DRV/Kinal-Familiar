package org.kinalfamiliar.reporte;

import com.mysql.jdbc.Connection;
import java.io.InputStream;
import java.util.Map;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

public class Report {

    //EL reporte, visualizador, imprimir
    private static JasperReport jreport;
    private static JasperViewer jviewer;
    private static JasperPrint jprint;

    //crear reporte
    //3 parametros: conexion, mapa, objeto:InputStream
    public static void crearReporte(Connection conexion, Map<String, Object> map, InputStream reporte) {
        try {
            jreport = (JasperReport) JRLoader.loadObject(reporte);
            jprint = JasperFillManager.fillReport(jreport, map , conexion);
        } catch (Exception e) {
            System.out.println("Error al crear un reporte "+e.getMessage());
            e.printStackTrace();
        }
    }
    
    //mostrar el repote
    public static void mostrarReporte (){
        jviewer = new JasperViewer(jprint,false);
        jviewer.setVisible(true);
    }
}