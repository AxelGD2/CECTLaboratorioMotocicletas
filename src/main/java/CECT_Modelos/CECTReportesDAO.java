package CECT_Modelos; // Ajusta a tu paquete

import CECT_Conexion.CECTConexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CECTReportesDAO {

    CECTConexion con = new CECTConexion();
    Connection cx;
    PreparedStatement ps;
    ResultSet rs;

    public String obtenerConteoMarcas() {
        StringBuilder reporte = new StringBuilder();
        // El LEFT JOIN asegura que salgan las marcas, incluso si la cuenta de idmotocicleta es nula (0)
        String sql = "SELECT ma.nombre_marca, COUNT(mo.idmotocicleta) AS cantidad "
                + "FROM Marcas ma LEFT JOIN Motocicletas mo ON ma.idmarca = mo.idmarca "
                + "GROUP BY ma.idmarca, ma.nombre_marca "
                + "ORDER BY cantidad DESC";
        try {
            cx = con.getCon();
            ps = cx.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                reporte.append("Marca: ").append(rs.getString("nombre_marca"))
                        .append(" | Total: ").append(rs.getInt("cantidad")).append(" motos\n");
            }
        } catch (Exception e) {
            System.err.println("Error en reporte conteo (2.3): " + e.getMessage());
        }
        return reporte.toString();
    }

    public String obtenerClasificacionMarcas() {
        StringBuilder reporte = new StringBuilder();
        String sql = "SELECT ma.nombre_marca, "
                + "COUNT(mo.idmotocicleta) AS cantidad, "
                + "IFNULL(ROUND(AVG(mo.cilindraje), 2), 0) AS prom_cilindraje, "
                + "CASE "
                + "   WHEN COUNT(mo.idmotocicleta) = 0 THEN 'SIN STOCK' "
                + "   WHEN COUNT(mo.idmotocicleta) BETWEEN 1 AND 2 THEN 'MEDIA' "
                + "   ELSE 'ALTA DEMANDA' "
                + "END AS clasificacion "
                + "FROM Marcas ma LEFT JOIN Motocicletas mo ON ma.idmarca = mo.idmarca "
                + "GROUP BY ma.idmarca, ma.nombre_marca "
                + "ORDER BY cantidad DESC";

        try {
            cx = con.getCon();
            ps = cx.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                reporte.append("- ").append(rs.getString("nombre_marca")).append("\n")
                        .append("  Estado: ").append(rs.getString("clasificacion")).append("\n")
                        .append("  Cilindraje Promedio: ").append(rs.getDouble("prom_cilindraje")).append(" cc\n\n");
            }
        } catch (Exception e) {
            System.err.println("Error en reporte clasificacion (2.4): " + e.getMessage());
        }
        return reporte.toString();
    }

    public String obtenerColoresMarca() {
        StringBuilder reporte = new StringBuilder();
        String sql = "SELECT mo.color, "
                + "COUNT(mo.idmotocicleta) AS cantidad, "
                + "GROUP_CONCAT(DISTINCT ma.nombre_marca SEPARATOR ', ') AS marcas_que_lo_usan "
                + "FROM Motocicletas mo INNER JOIN Marcas ma ON mo.idmarca = ma.idmarca "
                + "GROUP BY mo.color "
                + "ORDER BY cantidad DESC";

        try {
            cx = con.getCon();
            ps = cx.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                reporte.append("Color: ").append(rs.getString("color").toUpperCase()).append("\n")
                        .append("  > Total motos: ").append(rs.getInt("cantidad")).append("\n")
                        .append("  > Marcas: [").append(rs.getString("marcas_que_lo_usan")).append("]\n\n");
            }
        } catch (Exception e) {
            System.err.println("Error en reporte colores (2.5): " + e.getMessage());
        }
        return reporte.toString();
    }

    public String obtenerClasificacionCSV() {
        StringBuilder csv = new StringBuilder();
        csv.append("Nombre Marca;Cantidad Motos;Cilindraje Promedio;Clasificacion\n");

        String sql = "SELECT ma.nombre_marca, COUNT(mo.idmotocicleta) AS cantidad, "
                + "IFNULL(ROUND(AVG(mo.cilindraje), 2), 0) AS prom_cilindraje, "
                + "CASE "
                + "   WHEN COUNT(mo.idmotocicleta) = 0 THEN 'SIN STOCK' "
                + "   WHEN COUNT(mo.idmotocicleta) BETWEEN 1 AND 2 THEN 'MEDIA' "
                + "   ELSE 'ALTA DEMANDA' "
                + "END AS clasificacion "
                + "FROM Marcas ma LEFT JOIN Motocicletas mo ON ma.idmarca = mo.idmarca "
                + "GROUP BY ma.idmarca, ma.nombre_marca";
        try {
            cx = con.getCon();
            ps = cx.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                csv.append(rs.getString("nombre_marca")).append(";")
                        .append(rs.getInt("cantidad")).append(";")
                        .append(rs.getDouble("prom_cilindraje")).append(";")
                        .append(rs.getString("clasificacion")).append("\n");
            }
        } catch (Exception e) {
            System.err.println("Error al generar CSV: " + e.getMessage());
        }
        return csv.toString();
    }

    public String obtenerClasificacionJSON() {
        StringBuilder json = new StringBuilder();
        json.append("[\n");

        String sql = "SELECT ma.nombre_marca, COUNT(mo.idmotocicleta) AS cantidad, "
                + "IFNULL(ROUND(AVG(mo.cilindraje), 2), 0) AS prom_cilindraje, "
                + "CASE "
                + "   WHEN COUNT(mo.idmotocicleta) = 0 THEN 'SIN STOCK' "
                + "   WHEN COUNT(mo.idmotocicleta) BETWEEN 1 AND 2 THEN 'MEDIA' "
                + "   ELSE 'ALTA DEMANDA' "
                + "END AS clasificacion "
                + "FROM Marcas ma LEFT JOIN Motocicletas mo ON ma.idmarca = mo.idmarca "
                + "GROUP BY ma.idmarca, ma.nombre_marca";
        try {
            cx = con.getCon();
            ps = cx.prepareStatement(sql);
            rs = ps.executeQuery();
            boolean primero = true;

            while (rs.next()) {
                if (!primero) {
                    json.append(",\n");
                }
                json.append("  {\n")
                        .append("    \"marca\": \"").append(rs.getString("nombre_marca")).append("\",\n")
                        .append("    \"cantidad_motos\": ").append(rs.getInt("cantidad")).append(",\n")
                        .append("    \"cilindraje_promedio\": ").append(rs.getDouble("prom_cilindraje")).append(",\n")
                        .append("    \"clasificacion\": \"").append(rs.getString("clasificacion")).append("\"\n")
                        .append("  }");
                primero = false;
            }
        } catch (Exception e) {
            System.err.println("Error al generar JSON: " + e.getMessage());
        }
        json.append("\n]");
        return json.toString();
    }
}
