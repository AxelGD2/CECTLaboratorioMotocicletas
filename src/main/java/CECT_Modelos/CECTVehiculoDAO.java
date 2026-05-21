package CECT_Modelos;

import CECT_Conexion.CECTConexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CECTVehiculoDAO {

    CECTConexion con = new CECTConexion();
    private PreparedStatement ps;
    private ResultSet rs;

    public boolean registrarVehiculo(CECTVehiculo vehiculo) {
        String sql = "INSERT INTO Vehiculos (modelo, anio, color, idmarca, imagen) VALUES(?,?,?,?,?)";
        Connection conexion = con.getCon();
        try {
            ps = conexion.prepareStatement(sql);
            ps.setString(1, vehiculo.getModelo());
            ps.setInt(2, vehiculo.getAnio());
            ps.setString(3, vehiculo.getColor());
            ps.setInt(4, vehiculo.getIdMarca());
            ps.setString(5, vehiculo.getImagen());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al registrar vehículo: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizarVehiculo(CECTVehiculo vehiculo) {
        String sql = "UPDATE Vehiculos SET modelo=?, anio=?, color=?, idmarca=?, imagen=? WHERE idvehiculo=?";
        Connection conexion = con.getCon();
        try {
            ps = conexion.prepareStatement(sql);
            ps.setString(1, vehiculo.getModelo());
            ps.setInt(2, vehiculo.getAnio());
            ps.setString(3, vehiculo.getColor());
            ps.setInt(4, vehiculo.getIdMarca());
            ps.setString(5, vehiculo.getImagen());
            ps.setInt(6, vehiculo.getIdVehiculo());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al actualizar vehículo: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminarVehiculo(int id) {
        String sql = "DELETE FROM Vehiculos WHERE idvehiculo = ?";
        Connection conexion = con.getCon();
        try {
            ps = conexion.prepareStatement(sql);
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al eliminar vehículo: " + e.getMessage());
            return false;
        }
    }

    public ArrayList<String> obtenerPaisesConVehiculos() {
        ArrayList<String> paises = new ArrayList<>();
        paises.add("Todos");
        String sql = "SELECT DISTINCT pais_origen FROM Marcas WHERE pais_origen IS NOT NULL AND pais_origen != '' ORDER BY pais_origen";
        Connection conexion = con.getCon();
        try {
            ps = conexion.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                paises.add(rs.getString("pais_origen"));
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener países: " + e.getMessage());
        }
        return paises;
    }

    public ArrayList<CECTVehiculo> listarVehiculosPorPais(String pais) {
        ArrayList<CECTVehiculo> vehiculos = new ArrayList<>();
        String sql = "SELECT v.idvehiculo, v.modelo, v.anio, v.color, ma.nombre_marca, ma.pais_origen, v.imagen "
                + "FROM Vehiculos v INNER JOIN Marcas ma ON v.idmarca = ma.idmarca ";
        if (!pais.equals("Todos")) sql += "WHERE ma.pais_origen = ? ";
        Connection conexion = con.getCon();
        try {
            ps = conexion.prepareStatement(sql);
            if (!pais.equals("Todos")) ps.setString(1, pais);
            rs = ps.executeQuery();
            while (rs.next()) {
                CECTVehiculo vehiculo = new CECTVehiculo();
                vehiculo.setIdVehiculo(rs.getInt("idvehiculo"));
                vehiculo.setModelo(rs.getString("modelo"));
                vehiculo.setAnio(rs.getInt("anio"));
                vehiculo.setColor(rs.getString("color"));
                vehiculo.setMarca(rs.getString("nombre_marca"));
                vehiculo.setPais(rs.getString("pais_origen"));
                vehiculo.setImagen(rs.getString("imagen"));
                vehiculos.add(vehiculo);
            }
        } catch (SQLException e) {
            System.out.println("Error al listar vehículos por país: " + e.getMessage());
        }
        return vehiculos;
    }

    public ArrayList<CECTVehiculo> listarVehiculos() {
        ArrayList<CECTVehiculo> vehiculos = new ArrayList<>();
        String sql = "SELECT v.idvehiculo, v.modelo, v.anio, v.color, ma.nombre_marca, ma.pais_origen, v.imagen "
                + "FROM Vehiculos v INNER JOIN Marcas ma ON v.idmarca = ma.idmarca";
        Connection conexion = con.getCon();
        try {
            ps = conexion.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                CECTVehiculo vehiculo = new CECTVehiculo();
                vehiculo.setIdVehiculo(rs.getInt("idvehiculo"));
                vehiculo.setModelo(rs.getString("modelo"));
                vehiculo.setAnio(rs.getInt("anio"));
                vehiculo.setColor(rs.getString("color"));
                vehiculo.setMarca(rs.getString("nombre_marca"));
                vehiculo.setPais(rs.getString("pais_origen"));
                vehiculo.setImagen(rs.getString("imagen"));
                vehiculos.add(vehiculo);
            }
        } catch (SQLException e) {
            System.out.println("Error al listar vehículos: " + e.getMessage());
        }
        return vehiculos;
    }

    public String obtenerImagenPorId(int id) {
        String sql = "SELECT imagen FROM Vehiculos WHERE idvehiculo = ?";
        Connection conexion = con.getCon();
        try {
            ps = conexion.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                String img = rs.getString("imagen");
                return img != null ? img : "";
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener imagen: " + e.getMessage());
        }
        return "";
    }
}