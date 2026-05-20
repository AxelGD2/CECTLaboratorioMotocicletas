package CECT_Modelos;

import CECT_Conexion.CECTConexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CECTMarcaDAO {
    CECTConexion con = new CECTConexion();
    Connection cx;
    PreparedStatement ps;
    ResultSet rs;

    public boolean registrar(CECTMarca marca) {
        String sql = "INSERT INTO Marcas (nombre_marca, pais_origen) VALUES (?, ?)";
        try {
            cx = con.getCon(); // Llama a tu método de conexión
            ps = cx.prepareStatement(sql);
            ps.setString(1, marca.getNombre_marca());
            ps.setString(2, marca.getPais_origen());
            ps.execute();
            return true;
        } catch (SQLException e) {
            System.err.println("Error al registrar marca: " + e.toString());
            return false;
        }
    }

    public List<CECTMarca> listar() {
        List<CECTMarca> listaMarcas = new ArrayList<>();
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
                listaMarcas.add(m);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar marcas: " + e.toString());
        }
        return listaMarcas;
    }

    public boolean actualizar(CECTMarca marca) {
        String sql = "UPDATE Marcas SET nombre_marca=?, pais_origen=? WHERE idmarca=?";
        try {
            cx = con.getCon();
            ps = cx.prepareStatement(sql);
            ps.setString(1, marca.getNombre_marca());
            ps.setString(2, marca.getPais_origen());
            ps.setInt(3, marca.getIdmarca());
            ps.execute();
            return true;
        } catch (SQLException e) {
            System.err.println("Error al actualizar marca: " + e.toString());
            return false;
        }
    }

    public boolean eliminar(int id) {
        String sql = "DELETE FROM Marcas WHERE idmarca=?";
        try {
            cx = con.getCon();
            ps = cx.prepareStatement(sql);
            ps.setInt(1, id);
            ps.execute();
            return true;
        } catch (SQLException e) {
            System.err.println("Error al eliminar marca: " + e.toString());
            return false;
        }
    }
}