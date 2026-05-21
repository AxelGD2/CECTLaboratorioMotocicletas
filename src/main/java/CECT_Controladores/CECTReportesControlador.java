package CECT_Controladores;

import CECT_Modelos.CECTReportesDAO;
import CECT_Vistas.CECT_FrmReportes;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class CECTReportesControlador implements ActionListener {
    
    private CECTReportesDAO dao;
    private CECT_FrmReportes vista;

    public CECTReportesControlador(CECTReportesDAO dao, CECT_FrmReportes vista) {
        this.dao = dao;
        this.vista = vista;
        
        this.vista.cmbPais.addActionListener(this);
        
        this.vista.btnExportarTXTCompleto.addActionListener(this);
        this.vista.btnExportarCSVCompleto.addActionListener(this);
        this.vista.btnExportarJSONCompleto.addActionListener(this);
        

        this.vista.btnExportarTXTConteo.addActionListener(this);
        this.vista.btnExportarTXTClasif.addActionListener(this);
        this.vista.btnExportarTXTColores.addActionListener(this);
        
      
        this.vista.btnExportarCSVClasif.addActionListener(this);
        this.vista.btnExportarJSONClasif.addActionListener(this);
        

         this.vista.btnExportarCSVConteo.addActionListener(this);
         this.vista.btnExportarJSONConteo.addActionListener(this);
        this.vista.btnExportarCSVColores.addActionListener(this);
        this.vista.btnExportarJSONColores.addActionListener(this);
    }
    
    public void iniciar() {
        cargarPaises();
        actualizarReportes();
        
        vista.txtConteoMarcas.setEditable(false);
        vista.txtClasificacionMarcas.setEditable(false);
        vista.txtColoresMarca.setEditable(false);
    }
    

    private void cargarPaises() {
        vista.cmbPais.removeAllItems();
        List<String> paises = dao.obtenerPaises();
        for (String p : paises) {
            vista.cmbPais.addItem(p);
        }
    }
    
  
    private void actualizarReportes() {
        String paisSeleccionado = "Todos";
        if (vista.cmbPais.getSelectedItem() != null) {
            paisSeleccionado = vista.cmbPais.getSelectedItem().toString();
        }
        
        vista.txtConteoMarcas.setText(dao.obtenerConteoMarcas(paisSeleccionado));
        vista.txtClasificacionMarcas.setText(dao.obtenerClasificacionMarcas(paisSeleccionado));
        vista.txtColoresMarca.setText(dao.obtenerColoresMarca(paisSeleccionado));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        

        if (e.getSource() == vista.cmbPais) {
            actualizarReportes(); 
            return; 
        }
        
      
        String paisSeleccionado = "Todos";
        if (vista.cmbPais.getSelectedItem() != null) {
            paisSeleccionado = vista.cmbPais.getSelectedItem().toString();
        }


        if (e.getSource() == vista.btnExportarTXTCompleto) {
            String contenidoTxt = "REPORTE CONSOLIDADO (" + paisSeleccionado.toUpperCase() + ")\n\n" +
                                 vista.txtConteoMarcas.getText() + "\n" +
                                 vista.txtClasificacionMarcas.getText() + "\n" +
                                 vista.txtColoresMarca.getText();
            guardarArchivo(contenidoTxt, "reporte_completo.txt");
        }
        if (e.getSource() == vista.btnExportarCSVCompleto) {
            guardarArchivo(dao.obtenerClasificacionCSV(paisSeleccionado), "reporte_completo.csv");
        }
        if (e.getSource() == vista.btnExportarJSONCompleto) {
            guardarArchivo(dao.obtenerClasificacionJSON(paisSeleccionado), "reporte_completo.json");
        }

  
        if (e.getSource() == vista.btnExportarTXTConteo) {
            guardarArchivo(vista.txtConteoMarcas.getText(), "conteo_marcas.txt");
        }
        if (e.getSource() == vista.btnExportarTXTClasif) {
            guardarArchivo(vista.txtClasificacionMarcas.getText(), "clasificacion_marcas.txt");
        }
        if (e.getSource() == vista.btnExportarTXTColores) {
            guardarArchivo(vista.txtColoresMarca.getText(), "colores_marcas.txt");
        }
        
    
        if (e.getSource() == vista.btnExportarCSVClasif) {
            guardarArchivo(dao.obtenerClasificacionCSV(paisSeleccionado), "clasificacion_marcas.csv");
        }
        if (e.getSource() == vista.btnExportarJSONClasif) {
            guardarArchivo(dao.obtenerClasificacionJSON(paisSeleccionado), "clasificacion_marcas.json");
        }
    }
    

    private void guardarArchivo(String contenido, String nombrePorDefecto) {
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