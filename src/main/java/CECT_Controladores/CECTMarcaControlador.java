package CECT_Controladores;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import CECT_Modelos.CECTMarca;
import CECT_Modelos.CECTMarcaDAO;
import CECT_Vistas.CECT_FrmNuevaMarca;

public class CECTMarcaControlador implements ActionListener, MouseListener {
    
    private CECTMarca marca;
    private CECTMarcaDAO dao;
    private CECT_FrmNuevaMarca vista;
    
    DefaultTableModel modeloTabla = new DefaultTableModel();
    private int idFilaSeleccionada = -1;

    public CECTMarcaControlador(CECTMarca marca, CECTMarcaDAO dao, CECT_FrmNuevaMarca vista) {
        this.marca = marca;
        this.dao = dao;
        this.vista = vista;
        
        this.vista.btnGuardarMarca.addActionListener(this);
        this.vista.btnActualizarMarca.addActionListener(this);
        this.vista.btnEliminarMarca.addActionListener(this);
        this.vista.tableMarcas.addMouseListener(this);
        this.vista.btnLimpiar.addActionListener(this); 
    }
    
    public void iniciar() {
        listarTabla();
        limpiarCampos();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
 
        if (e.getSource() == vista.btnGuardarMarca) {
            if (vista.txtNombreMarca.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "El nombre de la marca es obligatorio.");
            } else {
                marca.setNombre_marca(vista.txtNombreMarca.getText());
                marca.setPais_origen(vista.txtPaisOrigen.getText());
                
                if (dao.registrar(marca)) {
                    JOptionPane.showMessageDialog(null, "Marca registrada con éxito.");
                    limpiarCampos();
                    listarTabla();
                } else {
                    JOptionPane.showMessageDialog(null, "Error al registrar la marca.");
                }
            }
        }
        
     
        if (e.getSource() == vista.btnActualizarMarca) {
            if (idFilaSeleccionada == -1) {
                JOptionPane.showMessageDialog(null, "Seleccione una marca de la tabla para actualizar.");
            } else {
                marca.setIdmarca(idFilaSeleccionada);
                marca.setNombre_marca(vista.txtNombreMarca.getText());
                marca.setPais_origen(vista.txtPaisOrigen.getText());
                
                if (dao.actualizar(marca)) {
                    JOptionPane.showMessageDialog(null, "Marca actualizada con éxito.");
                    limpiarCampos();
                    listarTabla();
                } else {
                    JOptionPane.showMessageDialog(null, "Error al actualizar la marca.");
                }
            }
        }
        
  
        if (e.getSource() == vista.btnEliminarMarca) {
            if (idFilaSeleccionada == -1) {
                JOptionPane.showMessageDialog(null, "Seleccione una marca de la tabla para eliminar.");
            } else {
                int confirmacion = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar esta marca?", "Confirmar", JOptionPane.YES_NO_OPTION);
                if (confirmacion == JOptionPane.YES_OPTION) {
                    if (dao.eliminar(idFilaSeleccionada)) {
                        JOptionPane.showMessageDialog(null, "Marca eliminada con éxito.");
                        limpiarCampos();
                        listarTabla();
                    } else {
                        JOptionPane.showMessageDialog(null, "Error al eliminar la marca.");
                    }
                }
            }
        }
        

        if (e.getSource() == vista.btnLimpiar) {
            limpiarCampos();
        }
    }

    public void listarTabla() {
        List<CECTMarca> lista = dao.listar();
        modeloTabla = (DefaultTableModel) vista.tableMarcas.getModel();
        modeloTabla.setRowCount(0); 
        
        Object[] objeto = new Object[4];
        for (int i = 0; i < lista.size(); i++) {
            objeto[0] = lista.get(i).getIdmarca();
            objeto[1] = lista.get(i).getNombre_marca();
            objeto[2] = lista.get(i).getPais_origen();
            objeto[3] = lista.get(i).getCantidad(); 
            modeloTabla.addRow(objeto);
        }
        vista.tableMarcas.setModel(modeloTabla);
    }
    
    public void limpiarCampos() {
        vista.txtNombreMarca.setText("");
        vista.txtPaisOrigen.setText("");
        idFilaSeleccionada = -1;
        
        vista.tableMarcas.clearSelection(); 
        vista.btnGuardarMarca.setEnabled(true); 
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == vista.tableMarcas) {
            int fila = vista.tableMarcas.rowAtPoint(e.getPoint());
            if (fila > -1) {
                idFilaSeleccionada = Integer.parseInt(vista.tableMarcas.getValueAt(fila, 0).toString());
                vista.txtNombreMarca.setText(vista.tableMarcas.getValueAt(fila, 1).toString());
                
                Object pais = vista.tableMarcas.getValueAt(fila, 2);
                vista.txtPaisOrigen.setText(pais != null ? pais.toString() : "");
               
                vista.btnGuardarMarca.setEnabled(false);
            }
        }
    }

    @Override public void mousePressed(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
}