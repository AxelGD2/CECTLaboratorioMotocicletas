package CECT_Controladores;

import CECT_Modelos.CECTReportesDAO;
import CECT_Vistas.CECT_FrmReportes;

public class CECTReportesControlador {
    
    private CECTReportesDAO dao;
    private CECT_FrmReportes vista;

    public CECTReportesControlador(CECTReportesDAO dao, CECT_FrmReportes vista) {
        this.dao = dao;
        this.vista = vista;
    }
    
    public void iniciar() {
        // Llenar cada JTextPane con su respectivo reporte al abrir el formulario
        vista.txtConteoMarcas.setText(dao.obtenerConteoMarcas());
        vista.txtClasificacionMarcas.setText(dao.obtenerClasificacionMarcas());
        vista.txtColoresMarca.setText(dao.obtenerColoresMarca());
        
        // Opcional: Hacer que los JTextPane sean de solo lectura para que el usuario no modifique el reporte
        vista.txtConteoMarcas.setEditable(false);
        vista.txtClasificacionMarcas.setEditable(false);
        vista.txtColoresMarca.setEditable(false);
    }
}