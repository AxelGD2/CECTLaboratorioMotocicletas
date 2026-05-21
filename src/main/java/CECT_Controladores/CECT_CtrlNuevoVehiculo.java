package CECT_Controladores;

import CECT_Modelos.CECTMarca;
import CECT_Modelos.CECTVehiculo;
import CECT_Modelos.CECTVehiculoDAO;
import CECT_Vistas.CECT_FrmNuevoVehiculo;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

public class CECT_CtrlNuevoVehiculo implements ActionListener, MouseListener {

    private CECTVehiculo vehiculo;
    private CECT_FrmNuevoVehiculo form;
    private CECTVehiculoDAO dao;
    private String rutaImagenActual = "";
    private boolean cargandoPaises = false;

    public CECT_CtrlNuevoVehiculo(CECTVehiculo vehiculo, CECT_FrmNuevoVehiculo form, CECTVehiculoDAO dao) {
        this.vehiculo = vehiculo;
        this.form = form;
        this.dao = dao;

        this.form.btnGuardarVehiculo.addActionListener(this);
        this.form.btnActualizarVehiculo.addActionListener(this);
        this.form.btnEliminarVehiculo.addActionListener(this);
        this.form.btnBuscarImagen.addActionListener(this);
        this.form.btnLimpiar.addActionListener(this);
        this.form.btnReporte.addActionListener(this);
        this.form.btnVer.addActionListener(this);
        this.form.cmbPais.addActionListener(this);
        this.form.tableVehiculos.addMouseListener(this);
    }

    public void iniciar() {
        cargarPaisesFiltro();
        cargarTabla("Todos");
        limpiarCampos();
    }

    private void cargarPaisesFiltro() {
        cargandoPaises = true;
        form.cmbPais.removeAllItems();
        ArrayList<String> paises = dao.obtenerPaisesConVehiculos();
        for (String p : paises) {
            form.cmbPais.addItem(p);
        }
        cargandoPaises = false;
    }

    public void cargarTabla(String paisFiltro) {
        DefaultTableModel modeloTabla = (DefaultTableModel) form.tableVehiculos.getModel();
        modeloTabla.setRowCount(0);
        ArrayList<CECTVehiculo> listaVehiculos = dao.listarVehiculosPorPais(paisFiltro);
        for (CECTVehiculo v : listaVehiculos) {
            modeloTabla.addRow(new Object[]{
                v.getIdVehiculo(),
                v.getModelo(),
                v.getAnio(),
                v.getColor(),
                v.getMarca(),
                v.getPais(),
                false
            });
        }
    }

    public void limpiarCampos() {
        form.txtModeloVehiculo.setText("");
        form.spinAnioVehiculo.setValue(0);
        form.txtColorVehiculo.setText("");
        rutaImagenActual = "";
        form.lblImagen.setIcon(null);
        form.lblImagen.setText("Sin Imagen");
        if (form.comboMarcaVehiculo.getItemCount() > 0) {
            form.comboMarcaVehiculo.setSelectedIndex(0);
        }
        form.tableVehiculos.clearSelection();
        form.btnGuardarVehiculo.setEnabled(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == form.cmbPais) {
            if (!cargandoPaises && form.cmbPais.getSelectedItem() != null) {
                cargarTabla(form.cmbPais.getSelectedItem().toString());
            }
            return;
        }

        if (e.getSource() == form.btnBuscarImagen) {
            JFileChooser jfc = new JFileChooser();
            jfc.setFileFilter(new FileNameExtensionFilter("JPG, PNG & GIF", "jpg", "png", "gif"));
            if (jfc.showOpenDialog(form) == JFileChooser.APPROVE_OPTION) {
                rutaImagenActual = jfc.getSelectedFile().getAbsolutePath();
                ImageIcon iconoOriginal = new ImageIcon(rutaImagenActual);
                Image imagenEscalada = iconoOriginal.getImage().getScaledInstance(
                        form.lblImagen.getWidth(), form.lblImagen.getHeight(), Image.SCALE_SMOOTH);
                form.lblImagen.setText("");
                form.lblImagen.setIcon(new ImageIcon(imagenEscalada));
            }
        }

        if (e.getSource() == form.btnLimpiar) {
            limpiarCampos();
        }

        if (e.getSource() == form.btnReporte) {
            exportarTablaACSV();
        }

        if (e.getSource() == form.btnVer) {
            verDetallesModal();
        }

        if (e.getSource() == form.btnGuardarVehiculo) {
            try {
                if (validarCamposVacios()) return;
                CECTMarca marcaSeleccionada = (CECTMarca) form.comboMarcaVehiculo.getSelectedItem();
                vehiculo.setModelo(form.txtModeloVehiculo.getText());
                vehiculo.setAnio((int) form.spinAnioVehiculo.getValue());
                vehiculo.setColor(form.txtColorVehiculo.getText());
                vehiculo.setImagen(rutaImagenActual);
                vehiculo.setIdMarca(marcaSeleccionada.getIdmarca());
                if (dao.registrarVehiculo(vehiculo)) {
                    JOptionPane.showMessageDialog(null, "¡Vehículo registrado exitosamente!");
                    String paisActual = form.cmbPais.getSelectedItem() != null ? form.cmbPais.getSelectedItem().toString() : "Todos";
                    cargarTabla(paisActual);
                    limpiarCampos();
                } else {
                    JOptionPane.showMessageDialog(null, "Error al registrar el vehículo.");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error al guardar: " + ex.getMessage());
            }
        }

        if (e.getSource() == form.btnActualizarVehiculo) {
            int filaSeleccionada = form.tableVehiculos.getSelectedRow();
            if (filaSeleccionada == -1) {
                JOptionPane.showMessageDialog(null, "Seleccione un vehículo para actualizar.");
                return;
            }
            try {
                if (validarCamposVacios()) return;
                int idVehiculo = Integer.parseInt(form.tableVehiculos.getValueAt(filaSeleccionada, 0).toString());
                CECTMarca marcaSeleccionada = (CECTMarca) form.comboMarcaVehiculo.getSelectedItem();
                vehiculo.setIdVehiculo(idVehiculo);
                vehiculo.setModelo(form.txtModeloVehiculo.getText());
                vehiculo.setAnio((int) form.spinAnioVehiculo.getValue());
                vehiculo.setColor(form.txtColorVehiculo.getText());
                vehiculo.setImagen(rutaImagenActual);
                vehiculo.setIdMarca(marcaSeleccionada.getIdmarca());
                if (dao.actualizarVehiculo(vehiculo)) {
                    JOptionPane.showMessageDialog(null, "¡Vehículo actualizado exitosamente!");
                    String paisActual = form.cmbPais.getSelectedItem() != null ? form.cmbPais.getSelectedItem().toString() : "Todos";
                    cargarTabla(paisActual);
                    limpiarCampos();
                } else {
                    JOptionPane.showMessageDialog(null, "Error al actualizar el vehículo.");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error al actualizar: " + ex.getMessage());
            }
        }

        if (e.getSource() == form.btnEliminarVehiculo) {
            eliminarSeleccionados();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == form.tableVehiculos) {
            int fila = form.tableVehiculos.getSelectedRow();
            if (fila > -1) {
                form.btnGuardarVehiculo.setEnabled(false);
                form.txtModeloVehiculo.setText(form.tableVehiculos.getValueAt(fila, 1).toString());
                form.spinAnioVehiculo.setValue(Integer.parseInt(form.tableVehiculos.getValueAt(fila, 2).toString()));
                form.txtColorVehiculo.setText(form.tableVehiculos.getValueAt(fila, 3).toString());

                String marcaTabla = form.tableVehiculos.getValueAt(fila, 4).toString();
                for (int i = 0; i < form.comboMarcaVehiculo.getItemCount(); i++) {
                    if (form.comboMarcaVehiculo.getItemAt(i).toString().equals(marcaTabla)) {
                        form.comboMarcaVehiculo.setSelectedIndex(i);
                        break;
                    }
                }

                int idVehiculo = Integer.parseInt(form.tableVehiculos.getValueAt(fila, 0).toString());
                rutaImagenActual = dao.obtenerImagenPorId(idVehiculo);

                if (!rutaImagenActual.isEmpty()) {
                    File fileImg = new File(rutaImagenActual);
                    if (fileImg.exists()) {
                        ImageIcon iconoOriginal = new ImageIcon(rutaImagenActual);
                        Image imagenEscalada = iconoOriginal.getImage().getScaledInstance(
                                form.lblImagen.getWidth(), form.lblImagen.getHeight(), Image.SCALE_SMOOTH);
                        form.lblImagen.setText("");
                        form.lblImagen.setIcon(new ImageIcon(imagenEscalada));
                    } else {
                        form.lblImagen.setIcon(null);
                        form.lblImagen.setText("Ruta no encontrada");
                    }
                } else {
                    form.lblImagen.setIcon(null);
                    form.lblImagen.setText("Sin Imagen");
                }
            }
        }
    }

    private void eliminarSeleccionados() {
        DefaultTableModel modelo = (DefaultTableModel) form.tableVehiculos.getModel();
        ArrayList<Integer> idsAEliminar = new ArrayList<>();

        for (int i = 0; i < modelo.getRowCount(); i++) {
            Boolean seleccionado = (Boolean) modelo.getValueAt(i, 6);
            if (seleccionado != null && seleccionado) {
                idsAEliminar.add(Integer.parseInt(modelo.getValueAt(i, 0).toString()));
            }
        }

        if (idsAEliminar.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Marque al menos un vehículo con el checkbox para eliminar.");
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(null,
                "¿Eliminar " + idsAEliminar.size() + " vehículo(s) seleccionado(s)?",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            int eliminados = 0;
            for (int id : idsAEliminar) {
                if (dao.eliminarVehiculo(id)) eliminados++;
            }
            JOptionPane.showMessageDialog(null, eliminados + " vehículo(s) eliminado(s) correctamente.");
            String paisActual = form.cmbPais.getSelectedItem() != null ? form.cmbPais.getSelectedItem().toString() : "Todos";
            cargarTabla(paisActual);
            limpiarCampos();
        }
    }

    private boolean validarCamposVacios() {
        if (form.txtModeloVehiculo.getText().isEmpty() || form.txtColorVehiculo.getText().isEmpty() || rutaImagenActual.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Modelo, Color y Foto son obligatorios.");
            return true;
        }
        if (form.comboMarcaVehiculo.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(null, "Seleccione una marca.");
            return true;
        }
        return false;
    }

    private void exportarTablaACSV() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar Reporte CSV");
        fileChooser.setSelectedFile(new File("reporte_vehiculos.csv"));
        if (fileChooser.showSaveDialog(form) == JFileChooser.APPROVE_OPTION) {
            File archivo = fileChooser.getSelectedFile();
            try (FileWriter fw = new FileWriter(archivo)) {
                fw.write("ID;Modelo;Anio;Color;Marca;Pais\n");
                for (int i = 0; i < form.tableVehiculos.getRowCount(); i++) {
                    for (int j = 0; j < 6; j++) {
                        fw.write(form.tableVehiculos.getValueAt(i, j).toString() + ";");
                    }
                    fw.write("\n");
                }
                JOptionPane.showMessageDialog(null, "¡Exportado con éxito!");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Error al exportar: " + ex.getMessage());
            }
        }
    }

    private void verDetallesModal() {
        int fila = form.tableVehiculos.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(null, "Seleccione un vehículo para ver detalles.");
            return;
        }

        String id     = form.tableVehiculos.getValueAt(fila, 0).toString();
        String modelo = form.tableVehiculos.getValueAt(fila, 1).toString();
        String anio   = form.tableVehiculos.getValueAt(fila, 2).toString();
        String color  = form.tableVehiculos.getValueAt(fila, 3).toString();
        String marca  = form.tableVehiculos.getValueAt(fila, 4).toString();
        String pais   = form.tableVehiculos.getValueAt(fila, 5).toString();

        String rutaImagen = dao.obtenerImagenPorId(Integer.parseInt(id));

        Object previewImagen = "Sin imagen disponible";
        if (!rutaImagen.isEmpty()) {
            File f = new File(rutaImagen);
            if (f.exists()) {
                ImageIcon iconoOriginal = new ImageIcon(rutaImagen);
                Image imgEscalada = iconoOriginal.getImage().getScaledInstance(250, 250, Image.SCALE_SMOOTH);
                previewImagen = new ImageIcon(imgEscalada);
            } else {
                previewImagen = "Imagen no encontrada en la ruta.";
            }
        }

        Object[] contenidoModal = {
            "ID: " + id,
            "Marca: " + marca,
            "País: " + pais,
            "Modelo: " + modelo,
            "Año: " + anio,
            "Color: " + color,
            "\nFotografía del Vehículo:",
            previewImagen
        };

        JOptionPane.showMessageDialog(form, contenidoModal, "Vista de Vehículo", JOptionPane.PLAIN_MESSAGE);
    }

    @Override public void mousePressed(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
}