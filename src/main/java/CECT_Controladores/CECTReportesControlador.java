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
    private boolean cargandoPaises = false;

    public CECTReportesControlador(CECTReportesDAO dao, CECT_FrmReportes vista) {
        this.dao = dao;
        this.vista = vista;

        this.vista.cmbPais.addActionListener(this);

        this.vista.btnExportarTXTCompleto.addActionListener(this);
        this.vista.btnExportarCSVCompleto.addActionListener(this);
        this.vista.btnExportarJSONCompleto.addActionListener(this);

        this.vista.btnExportarTXTConteo.addActionListener(this);
        this.vista.btnExportarCSVConteo.addActionListener(this);
        this.vista.btnExportarJSONConteo.addActionListener(this);

        this.vista.btnExportarTXTClasif.addActionListener(this);
        this.vista.btnExportarCSVClasif.addActionListener(this);
        this.vista.btnExportarJSONClasif.addActionListener(this);

        this.vista.btnExportarTXTColores.addActionListener(this);
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
        cargandoPaises = true;
        vista.cmbPais.removeAllItems();
        List<String> paises = dao.obtenerPaises();
        for (String p : paises) {
            vista.cmbPais.addItem(p);
        }
        cargandoPaises = false;
    }

    private void actualizarReportes() {
        String pais = obtenerPaisSeleccionado();
        vista.txtConteoMarcas.setText(dao.obtenerConteoMarcas(pais));
        vista.txtClasificacionMarcas.setText(dao.obtenerClasificacionMarcas(pais));
        vista.txtColoresMarca.setText(dao.obtenerColoresMarca(pais));
    }

    private String obtenerPaisSeleccionado() {
        if (vista.cmbPais.getSelectedItem() != null) {
            return vista.cmbPais.getSelectedItem().toString();
        }
        return "Todos";
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.cmbPais) {
            if (!cargandoPaises && vista.cmbPais.getSelectedItem() != null) {
                actualizarReportes();
            }
            return;
        }

        String pais = obtenerPaisSeleccionado();

        if (e.getSource() == vista.btnExportarTXTCompleto) {
            guardarArchivo(dao.obtenerReporteCompletoTXT(pais), "reporte_completo.txt");
        } else if (e.getSource() == vista.btnExportarCSVCompleto) {
            guardarArchivo(dao.obtenerReporteCompletoCSV(pais), "reporte_completo.csv");
        } else if (e.getSource() == vista.btnExportarJSONCompleto) {
            guardarArchivo(dao.obtenerReporteCompletoJSON(pais), "reporte_completo.json");

        } else if (e.getSource() == vista.btnExportarTXTConteo) {
            guardarArchivo(vista.txtConteoMarcas.getText(), "conteo_marcas.txt");
        } else if (e.getSource() == vista.btnExportarCSVConteo) {
            guardarArchivo(dao.obtenerConteoCSV(pais), "conteo_marcas.csv");
        } else if (e.getSource() == vista.btnExportarJSONConteo) {
            guardarArchivo(dao.obtenerConteoJSON(pais), "conteo_marcas.json");

        } else if (e.getSource() == vista.btnExportarTXTClasif) {
            guardarArchivo(vista.txtClasificacionMarcas.getText(), "clasificacion_marcas.txt");
        } else if (e.getSource() == vista.btnExportarCSVClasif) {
            guardarArchivo(dao.obtenerClasificacionCSV(pais), "clasificacion_marcas.csv");
        } else if (e.getSource() == vista.btnExportarJSONClasif) {
            guardarArchivo(dao.obtenerClasificacionJSON(pais), "clasificacion_marcas.json");

        } else if (e.getSource() == vista.btnExportarTXTColores) {
            guardarArchivo(vista.txtColoresMarca.getText(), "colores_marcas.txt");
        } else if (e.getSource() == vista.btnExportarCSVColores) {
            guardarArchivo(dao.obtenerColoresCSV(pais), "colores_marcas.csv");
        } else if (e.getSource() == vista.btnExportarJSONColores) {
            guardarArchivo(dao.obtenerColoresJSON(pais), "colores_marcas.json");
        }
    }

    private void guardarArchivo(String contenido, String nombrePorDefecto) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Seleccione dónde guardar el reporte");
        fileChooser.setSelectedFile(new File(nombrePorDefecto));
        int seleccion = fileChooser.showSaveDialog(vista);
        if (seleccion == JFileChooser.APPROVE_OPTION) {
            File archivo = fileChooser.getSelectedFile();
            try (FileWriter escritor = new FileWriter(archivo)) {
                escritor.write(contenido);
                JOptionPane.showMessageDialog(vista,
                        "¡Archivo exportado con éxito!\nGuardado en: " + archivo.getAbsolutePath(),
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(vista,
                        "Error al escribir el archivo: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}