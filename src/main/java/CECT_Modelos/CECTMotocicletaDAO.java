/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package CECT_Modelos;

import CECT_Conexion.CECTConexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author axele
 */
public class CECTMotocicletaDAO {
    CECTConexion con = new CECTConexion();
    private PreparedStatement ps;
    private ResultSet rs;

    public boolean registrarMotocicleta(CECTMotocicleta motocicleta) {
        String sql = "INSERT INTO motocicletas (modelo,cilindraje,color,idmarca,imagen) VALUES(?,?,?,?,?);";

        Connection conexion = con.getCon();

        try {
            ps = conexion.prepareStatement(sql);
            ps.setString(1, motocicleta.getModelo());
            ps.setInt(2, motocicleta.getCilindraje());
            ps.setString(3, motocicleta.getColor());
            ps.setInt(4, motocicleta.getIdMarca());
            ps.setString(5, motocicleta.getImagen());

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.out.println("Error al registrar motocicleta: " + e.getMessage());
            return false;
        }
    }
    
    public ArrayList<CECTMotocicleta> listarMotocicletas(){
        ArrayList<CECTMotocicleta> motocicletas = new ArrayList<>();
        String sql="SELECT mo.idmotocicleta, mo.modelo, mo.cilindraje, mo.color, ma.nombre_marca, mo.imagen FROM motocicletas mo INNER JOIN marcas ma ON mo.idmarca = ma.idmarca";
        Connection conexion = con.getCon();
        
        try{
            ps=conexion.prepareStatement(sql);
            rs=ps.executeQuery();
            
            while(rs.next()){
                CECTMotocicleta motocicleta =new CECTMotocicleta();
                motocicleta.setIdMotocicleta(rs.getInt("idmotocicleta"));
                motocicleta.setModelo(rs.getString("modelo"));
                motocicleta.setCilindraje(rs.getInt("cilindraje"));
                motocicleta.setColor(rs.getString("color"));
                motocicleta.setMarca(rs.getString("nombre_marca"));
                motocicleta.setImagen(rs.getString("imagen"));
                
                motocicletas.add(motocicleta);
            }
        }catch(SQLException e){
            System.out.println("Error al listar motocicletas: " + e.getMessage());
        }
        
        return  motocicletas;
    }
    
    public boolean eliminarMotocicleta(int id){
        String sql="DELETE FROM motocicletas WHERE idmotocicleta = ?";
        
        Connection conexion = con.getCon();
        
        try{
            ps=conexion.prepareStatement(sql);
            ps.setInt(1, id);
            int filasAfectadas=ps.executeUpdate();
         
            if (filasAfectadas > 0) {
                return true;
            } else {
                return false;
            }
            
        }catch(SQLException e){
            System.out.println("Error al eliminar motocicleta: " + e.getMessage());
        }
        return false;
    }
    
    public boolean actualizarMotocicleta(CECTMotocicleta motocicleta){
        String sql="UPDATE motocicletas SET modelo=?,cilindraje=?,color=?,idmarca=?,imagen=? WHERE idmotocicleta=?";
        
        Connection conexion = con.getCon();
        
        try{
            ps=conexion.prepareStatement(sql);
            ps.setString(1, motocicleta.getModelo());
            ps.setInt(2,motocicleta.getCilindraje());
            ps.setString(3,motocicleta.getColor());
            ps.setInt(4,motocicleta.getIdMarca());
            ps.setString(5,motocicleta.getImagen());
            int filasAfectadas=ps.executeUpdate();
         
            if (filasAfectadas > 0) {
                return true;
            } else {
                return false;
            }
            
        }catch(SQLException e){
            System.out.println("Error al actualizar motocicleta: " + e.getMessage());
        }
        return false;
    }
}
