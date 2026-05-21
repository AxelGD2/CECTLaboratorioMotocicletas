package CECT_Controladores;

import CECT_Modelos.CECTMarca;
import CECT_Modelos.CECTVehiculo;
import CECT_Modelos.CECTMaestroDetalleDAO;
import CECT_Vistas.CECT_FrmMaestroDetalle; 
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import javax.swing.table.DefaultTableModel;

public class CECTMaestroDetalleControlador implements MouseListener {

    private CECTMaestroDetalleDAO dao;
    private CECT_FrmMaestroDetalle vista;

    DefaultTableModel modeloMaestro = new DefaultTableModel();
    DefaultTableModel modeloDetalle = new DefaultTableModel();

    public CECTMaestroDetalleControlador(CECTMaestroDetalleDAO dao, CECT_FrmMaestroDetalle vista) {
        this.dao = dao;
        this.vista = vista;

        this.vista.MarcasTable.addMouseListener(this);
    }

    public void iniciar() {
        cargarTablaMaestra();
        limpiarTablaDetalle(); 
    }

    private void cargarTablaMaestra() {
        List<CECTMarca> lista = dao.listarMarcas();
        modeloMaestro = (DefaultTableModel) vista.MarcasTable.getModel();
        modeloMaestro.setRowCount(0);

     
        Object[] objeto = new Object[4];
        for (int i = 0; i < lista.size(); i++) {
            objeto[0] = lista.get(i).getIdmarca();
            objeto[1] = lista.get(i).getNombre_marca();
            objeto[2] = lista.get(i).getPais_origen();
            objeto[3] = lista.get(i).getCantidad(); 
            modeloMaestro.addRow(objeto);
        }
        vista.MarcasTable.setModel(modeloMaestro);
    }

    private void cargarTablaDetalle(int idMarca) {
 
        List<CECTVehiculo> lista = dao.listarVehiculosPorMarca(idMarca);
        

        modeloDetalle = (DefaultTableModel) vista.MotosTable.getModel();
        modeloDetalle.setRowCount(0);

        Object[] objeto = new Object[6];
        for (int i = 0; i < lista.size(); i++) {
            objeto[0] = lista.get(i).getIdVehiculo(); 
            objeto[1] = lista.get(i).getModelo();
            objeto[2] = lista.get(i).getAnio(); 
            objeto[3] = lista.get(i).getColor();
            objeto[4] = lista.get(i).getMarca(); 
            objeto[5] = lista.get(i).getImagen();
            modeloDetalle.addRow(objeto);
        }
        vista.MotosTable.setModel(modeloDetalle);
    }

    private void limpiarTablaDetalle() {
        modeloDetalle = (DefaultTableModel) vista.MotosTable.getModel();
        modeloDetalle.setRowCount(0);
    }

    @Override
    public void mouseClicked(MouseEvent e) {

        if (e.getSource() == vista.MarcasTable) {
            int filaSeleccionada = vista.MarcasTable.getSelectedRow();
            if (filaSeleccionada >= 0) {

                int idMarca = Integer.parseInt(vista.MarcasTable.getValueAt(filaSeleccionada, 0).toString());

                cargarTablaDetalle(idMarca);
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}