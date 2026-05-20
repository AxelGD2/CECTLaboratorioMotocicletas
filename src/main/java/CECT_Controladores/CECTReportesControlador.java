package CECT_Controladores;

import CECT_Modelos.CECTReportesDAO;
import CECT_Vistas.CECT_FrmReportes;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class CECTReportesControlador implements ActionListener {
    
    private CECTReportesDAO dao;
    private CECT_FrmReportes vista;

    public CECTReportesControlador(CECTReportesDAO dao, CECT_FrmReportes vista) {
        this.dao = dao;
        this.vista = vista;
        
        this.vista.btnExportarTXT.addActionListener(this);
        this.vista.btnExportarCSV.addActionListener(this);
        this.vista.btnExportarJSON.addActionListener(this);
    }
    
    public void iniciar() {
        vista.txtConteoMarcas.setText(dao.obtenerConteoMarcas());
        vista.txtClasificacionMarcas.setText(dao.obtenerClasificacionMarcas());
        vista.txtColoresMarca.setText(dao.obtenerColoresMarca());
        
        vista.txtConteoMarcas.setEditable(false);
        vista.txtClasificacionMarcas.setEditable(false);
        vista.txtColoresMarca.setEditable(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
 
        if (e.getSource() == vista.btnExportarTXT) {
            String contenidoTxt = "REPORTE CONSOLIDADO DE MOTOCICLETAS\n\n" +
                                 vista.txtConteoMarcas.getText() + "\n" +
                                 vista.txtClasificacionMarcas.getText() + "\n" +
                                 vista.txtColoresMarca.getText();
            
            guardarArchivo(contenidoTxt, "reporte_motos.txt", "Archivos de Texto (.txt)");
        }
        
     
        if (e.getSource() == vista.btnExportarCSV) {
            String contenidoCsv = dao.obtenerClasificacionCSV();
            guardarArchivo(contenidoCsv, "reporte_motos.csv", "Archivos CSV (.csv)");
        }
        

        if (e.getSource() == vista.btnExportarJSON) {
            String contenidoJson = dao.obtenerClasificacionJSON();
            guardarArchivo(contenidoJson, "reporte_motos.json", "Archivos JSON (.json)");
        }
    }
    

    private void guardarArchivo(String contenido, String nombrePorDefecto, String descripcionFiltro) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Seleccione dónde guardar el reporte");
        fileChooser.setSelectedFile(new File(nombrePorDefecto));
        
        int seleccion = fileChooser.showSaveDialog(vista);
        
        if (seleccion == JFileChooser.APPROVE_OPTION) {
            File archivoDestino = fileChooser.getSelectedFile();
            
            try (FileWriter escritor = new FileWriter(archivoDestino)) {
                escritor.write(contenido);
                JOptionPane.showMessageDialog(vista, "¡Archivo exportado con éxito!\nGuardado en: " + archivoDestino.getAbsolutePath(), "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(vista, "Error al escribir el archivo: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}