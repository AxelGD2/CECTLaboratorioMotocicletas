package CECT_Modelos;

import CECT_Conexion.CECTConexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CECTMaestroDetalleDAO {

    CECTConexion con = new CECTConexion();
    Connection cx;
    PreparedStatement ps;
    ResultSet rs;

    // Método para cargar la tabla Maestra (Marcas)
    public List<CECTMarca> listarMarcas() {
        List<CECTMarca> lista = new ArrayList<>();
        String sql = "SELECT * FROM Marcas";
        try {
            cx = con.getCon();
            ps = cx.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                CECTMarca m = new CECTMarca();
                m.setIdmarca(rs.getInt("idmarca"));
                m.setNombre_marca(rs.getString("nombre_marca"));
                m.setPais_origen(rs.getString("pais_origen"));
                lista.add(m);
            }
        } catch (Exception e) {
            System.err.println("Error al listar marcas (Maestro): " + e.getMessage());
        }
        return lista;
    }


    public List<CECTMotocicleta> listarMotosPorMarca(int idMarcaSeleccionada) {
        List<CECTMotocicleta> lista = new ArrayList<>();
        String sql = "SELECT mo.*, ma.nombre_marca FROM Motocicletas mo "
                + "INNER JOIN Marcas ma ON mo.idmarca = ma.idmarca "
                + "WHERE mo.idmarca = ?";
        try {
            cx = con.getCon();
            ps = cx.prepareStatement(sql);
            ps.setInt(1, idMarcaSeleccionada);
            rs = ps.executeQuery();
            while (rs.next()) {
                CECTMotocicleta moto = new CECTMotocicleta();
                moto.setIdMotocicleta(rs.getInt("idmotocicleta"));
                moto.setModelo(rs.getString("modelo"));
                moto.setCilindraje(rs.getInt("cilindraje"));
                moto.setColor(rs.getString("color"));
                moto.setIdMarca(rs.getInt("idmarca"));
                moto.setImagen(rs.getString("imagen"));
                moto.setMarca(rs.getString("nombre_marca")); // Guardamos el nombre
                lista.add(moto);
            }
        } catch (Exception e) {
            System.err.println("Error al listar motos (Detalle): " + e.getMessage());
        }
        return lista;
    }
}
