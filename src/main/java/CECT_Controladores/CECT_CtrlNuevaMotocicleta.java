/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package CECT_Controladores;

import CECT_Modelos.CECTMarca;
import CECT_Modelos.CECTMotocicleta;
import CECT_Modelos.CECTMotocicletaDAO;
import CECT_Vistas.CECT_FrmNuevaMotocicleta;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author axele
 */
public class CECT_CtrlNuevaMotocicleta implements ActionListener{
    private CECTMotocicleta motocicleta;
    private CECT_FrmNuevaMotocicleta form;
    private CECTMotocicletaDAO dao;
    
    public CECT_CtrlNuevaMotocicleta(CECTMotocicleta motocicleta, CECT_FrmNuevaMotocicleta form, CECTMotocicletaDAO dao){
        this.motocicleta = motocicleta;
        this.form = form;
        this.dao = dao;
        
        this.form.btnGuardarMoto.addActionListener(this);
        this.form.btnActualizarMoto.addActionListener(this);
        this.form.btnEliminarMoto.addActionListener(this);
    }
    
    public void limpiarCampos() {
        form.txtModeloMoto.setText("");
        form.spinCilindrajeMoto.setValue(0);
        form.txtColorMoto.setText("");
        form.comboMarcaMoto.setSelectedIndex(0);
        form.txtImagenMoto.setText("");
        if (form.comboMarcaMoto.getItemCount() > 0) {
            form.comboMarcaMoto.setSelectedIndex(0); 
        }
    }
    
    public void cargarTabla() {
        DefaultTableModel modeloTabla = (DefaultTableModel) form.tableMotocicletas.getModel();
        modeloTabla.setRowCount(0);
        
        ArrayList<CECTMotocicleta> listaMotocicletas= dao.listarMotocicletas();
        
        Object[] fila = new Object[4];
        
        for (CECTMotocicleta m : listaMotocicletas) {
            fila[0] = m.getIdMotocicleta();
            fila[1] = m.getModelo();
            fila[2] = m.getCilindraje();
            fila[3] = m.getColor();
            fila[4] = m.getMarca();
            fila[5] = m.getImagen();           
            
            modeloTabla.addRow(fila);
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == form.btnGuardarMoto) {
            
            try {
                String modelo = form.txtModeloMoto.getText();
                int cilindraje = (int)form.spinCilindrajeMoto.getValue();
                String color = form.txtColorMoto.getText();
                String marca = form.comboMarcaMoto.getSelectedItem().toString();
                String imagen = form.txtImagenMoto.getText();

                if (modelo.isEmpty() || color.isEmpty() || imagen.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "El modelo, color e imgen son obligatorios.");
                    return;
                }

                if (form.comboMarcaMoto.getSelectedItem() == null) {
                    JOptionPane.showMessageDialog(null, "Debe seleccionar una marca.");
                    return;
                }

              
                CECTMarca marcaSeleccionada = (CECTMarca) form.comboMarcaMoto.getSelectedItem();

                motocicleta.setModelo(modelo); 
                motocicleta.setCilindraje(cilindraje);                    
                motocicleta.setColor(color);
                motocicleta.setMarca(marca);
                motocicleta.setImagen(imagen);
                
                if (dao.registrarMotocicleta(motocicleta)) {
                    JOptionPane.showMessageDialog(null, "¡Motocicleta registrada exitosamente!");
                    limpiarCampos();
                    cargarTabla();
                } else {
                    JOptionPane.showMessageDialog(null, "Error al registrar la motocicleta.");
                }
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Ocurrió un error inesperado. Verifica los datos.");
                System.out.println("Error en el controlador de Motocicleta: " + ex.getMessage());
            }
        }
        
        if (e.getSource() == form.btnEliminarMoto) {
            
            int filaSeleccionada = form.tableMotocicletas.getSelectedRow();
            
            if (filaSeleccionada == -1) {
                JOptionPane.showMessageDialog(null, "Por favor, seleccione una motocicleta de la tabla para eliminar.");
                return;
            }
            
            try {
                
                int idMotocicleta = Integer.parseInt(form.tableMotocicletas.getValueAt(filaSeleccionada, 0).toString());
                
                int confirmarEliminacion = JOptionPane.showConfirmDialog(null,
                        "¿Está seguro que desea eliminar esta motocicleta?",
                        "Confirmar eliminación",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE);
                
                if (confirmarEliminacion == JOptionPane.YES_OPTION) {
                    
                    if (dao.eliminarMotocicleta(idMotocicleta)) {
                        JOptionPane.showMessageDialog(null, "Motocicleta eliminada correctamente.");
                        cargarTabla(); 
                    } else {
                        JOptionPane.showMessageDialog(null, "Error al eliminar la motocicleta.");
                    }
                }                
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error al intentar eliminar.");
                System.out.println("Error: " + ex.getMessage());
            }
        }
        
        if (e.getSource() == form.btnActualizarMoto) {
            int filaSeleccionada = form.tableMotocicletas.getSelectedRow();
            
            if (filaSeleccionada == -1) {
                JOptionPane.showMessageDialog(null, "Por favor, seleccione una motocicleta de la tabla para actualizar.");
                return;
            }
            
            try {
                int idMotocicleta = Integer.parseInt(form.tableMotocicletas.getValueAt(filaSeleccionada, 0).toString());
                
                String modelo = form.txtModeloMoto.getText();
                int cilindraje = (int)form.spinCilindrajeMoto.getValue();
                String color = form.txtColorMoto.getText();
                String marca = form.comboMarcaMoto.getSelectedItem().toString();
                String imagen = form.txtImagenMoto.getText();

                if (modelo.isEmpty() || color.isEmpty() || imagen.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "El modelo, color e imagen son obligatorios.");
                    return;
                }

                if (form.comboMarcaMoto.getSelectedItem() == null) {
                    JOptionPane.showMessageDialog(null, "Debe seleccionar una marca.");
                    return;
                }

                motocicleta.setIdMotocicleta(idMotocicleta);
                motocicleta.setModelo(modelo); 
                motocicleta.setCilindraje(cilindraje);                    
                motocicleta.setColor(color);
                motocicleta.setMarca(marca);
                motocicleta.setImagen(imagen);
                
                if (dao.actualizarMotocicleta(motocicleta)) {
                    JOptionPane.showMessageDialog(null, "¡Motocicleta actualizada exitosamente!");
                    limpiarCampos();
                    cargarTabla();
                } else {
                    JOptionPane.showMessageDialog(null, "Error al actualizar la motocicleta.");
                }
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Ocurrió un error inesperado. Verifica los datos.");
                System.out.println("Error en el controlador al actualizar: " + ex.getMessage());
            }
        }
    }
}
