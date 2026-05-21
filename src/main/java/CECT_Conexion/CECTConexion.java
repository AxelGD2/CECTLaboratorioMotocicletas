/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package CECT_Conexion;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author axele
 */
public class CECTConexion {
    private Connection con;

    public CECTConexion() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.con = DriverManager.getConnection("jdbc:mysql://localhost:3306/vehiculos?useSSL=false&serverTimezone=UTC", "root", "");
            
        } catch (Exception e) {
            System.out.println("Error");
            return;
        }
        System.out.println("Conectado");
    }
    
    public Connection getCon() {
        return con;
    }
}
