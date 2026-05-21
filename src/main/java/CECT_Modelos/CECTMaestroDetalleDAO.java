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

    public List<CECTMarca> listarMarcas() {
        List<CECTMarca> lista = new ArrayList<>();
        String sql = "SELECT m.idmarca, m.nombre_marca, m.pais_origen, COUNT(v.idvehiculo) AS cantidad "
                   + "FROM Marcas m LEFT JOIN Vehiculos v ON m.idmarca = v.idmarca "
                   + "GROUP BY m.idmarca, m.nombre_marca, m.pais_origen";
        try {
            cx = con.getCon();
            ps = cx.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                CECTMarca m = new CECTMarca();
                m.setIdmarca(rs.getInt("idmarca"));
                m.setNombre_marca(rs.getString("nombre_marca"));
                m.setPais_origen(rs.getString("pais_origen"));
                m.setCantidad(rs.getInt("cantidad")); // ¡Guardamos la cantidad calculada!
                lista.add(m);
            }
        } catch (Exception e) {
            System.err.println("Error al listar marcas (Maestro): " + e.getMessage());
        }
        return lista;
    }


    public List<CECTVehiculo> listarVehiculosPorMarca(int idMarcaSeleccionada) {
        List<CECTVehiculo> lista = new ArrayList<>();

        String sql = "SELECT v.*, ma.nombre_marca FROM Vehiculos v "
                + "INNER JOIN Marcas ma ON v.idmarca = ma.idmarca "
                + "WHERE v.idmarca = ?";
        try {
            cx = con.getCon();
            ps = cx.prepareStatement(sql);
            ps.setInt(1, idMarcaSeleccionada);
            rs = ps.executeQuery();
            while (rs.next()) {
                CECTVehiculo vehiculo = new CECTVehiculo();
                
                vehiculo.setIdVehiculo(rs.getInt("idvehiculo"));
                vehiculo.setModelo(rs.getString("modelo"));
                vehiculo.setAnio(rs.getInt("anio")); 
                vehiculo.setColor(rs.getString("color"));
                vehiculo.setIdMarca(rs.getInt("idmarca"));
                vehiculo.setImagen(rs.getString("imagen"));
                vehiculo.setMarca(rs.getString("nombre_marca")); 
                
                lista.add(vehiculo);
            }
        } catch (Exception e) {
            System.err.println("Error al listar vehículos (Detalle): " + e.getMessage());
        }
        return lista;
    }
}