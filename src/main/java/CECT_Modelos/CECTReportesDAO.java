package CECT_Modelos;

import CECT_Conexion.CECTConexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CECTReportesDAO {

    CECTConexion con = new CECTConexion();
    Connection cx;
    PreparedStatement ps;
    ResultSet rs;

    public List<String> obtenerPaises() {
        List<String> paises = new ArrayList<>();
        paises.add("Todos");
        String sql = "SELECT DISTINCT pais_origen FROM Marcas WHERE pais_origen IS NOT NULL AND pais_origen != '' ORDER BY pais_origen";
        try {
            cx = con.getCon();
            ps = cx.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                paises.add(rs.getString("pais_origen"));
            }
        } catch (Exception e) {
            System.err.println("Error al cargar países: " + e.getMessage());
        }
        return paises;
    }

    public String obtenerConteoMarcas(String pais) {
        StringBuilder reporte = new StringBuilder();
        String sql = "SELECT ma.nombre_marca, COUNT(v.idvehiculo) AS cantidad "
                + "FROM Marcas ma LEFT JOIN Vehiculos v ON ma.idmarca = v.idmarca ";
        if (!pais.equals("Todos")) sql += "WHERE ma.pais_origen = ? ";
        sql += "GROUP BY ma.idmarca, ma.nombre_marca ORDER BY cantidad DESC";
        try {
            cx = con.getCon();
            ps = cx.prepareStatement(sql);
            if (!pais.equals("Todos")) ps.setString(1, pais);
            rs = ps.executeQuery();
            while (rs.next()) {
                reporte.append("Marca: ").append(rs.getString("nombre_marca"))
                        .append(" | Total: ").append(rs.getInt("cantidad")).append(" vehículos\n");
            }
        } catch (Exception e) {
            System.err.println("Error en conteo: " + e.getMessage());
        }
        return reporte.toString();
    }

    public String obtenerClasificacionMarcas(String pais) {
        StringBuilder reporte = new StringBuilder();
        String sql = "SELECT ma.nombre_marca, COUNT(v.idvehiculo) AS cantidad, "
                + "IFNULL(ROUND(AVG(v.anio), 0), 0) AS prom_anio, "
                + "CASE "
                + "   WHEN COUNT(v.idvehiculo) = 0 THEN 'SIN STOCK' "
                + "   WHEN COUNT(v.idvehiculo) BETWEEN 1 AND 2 THEN 'MEDIA' "
                + "   ELSE 'ALTA DEMANDA' "
                + "END AS clasificacion "
                + "FROM Marcas ma LEFT JOIN Vehiculos v ON ma.idmarca = v.idmarca ";
        if (!pais.equals("Todos")) sql += "WHERE ma.pais_origen = ? ";
        sql += "GROUP BY ma.idmarca, ma.nombre_marca ORDER BY cantidad DESC";
        try {
            cx = con.getCon();
            ps = cx.prepareStatement(sql);
            if (!pais.equals("Todos")) ps.setString(1, pais);
            rs = ps.executeQuery();
            while (rs.next()) {
                reporte.append("- ").append(rs.getString("nombre_marca")).append("\n")
                        .append("  Estado: ").append(rs.getString("clasificacion")).append("\n")
                        .append("  Año Promedio: ").append(rs.getInt("prom_anio")).append("\n\n");
            }
        } catch (Exception e) {
            System.err.println("Error en clasificación: " + e.getMessage());
        }
        return reporte.toString();
    }

    public String obtenerColoresMarca(String pais) {
        StringBuilder reporte = new StringBuilder();
        String sql = "SELECT v.color, COUNT(v.idvehiculo) AS cantidad, "
                + "GROUP_CONCAT(DISTINCT ma.nombre_marca SEPARATOR ', ') AS marcas_que_lo_usan "
                + "FROM Vehiculos v INNER JOIN Marcas ma ON v.idmarca = ma.idmarca ";
        if (!pais.equals("Todos")) sql += "WHERE ma.pais_origen = ? ";
        sql += "GROUP BY v.color ORDER BY cantidad DESC";
        try {
            cx = con.getCon();
            ps = cx.prepareStatement(sql);
            if (!pais.equals("Todos")) ps.setString(1, pais);
            rs = ps.executeQuery();
            while (rs.next()) {
                reporte.append("Color: ").append(rs.getString("color").toUpperCase()).append("\n")
                        .append("  > Total vehículos: ").append(rs.getInt("cantidad")).append("\n")
                        .append("  > Marcas: [").append(rs.getString("marcas_que_lo_usan")).append("]\n\n");
            }
        } catch (Exception e) {
            System.err.println("Error en colores: " + e.getMessage());
        }
        return reporte.toString();
    }

    public String obtenerConteoCSV(String pais) {
        StringBuilder csv = new StringBuilder();
        csv.append("Nombre Marca;Cantidad Vehiculos\n");
        String sql = "SELECT ma.nombre_marca, COUNT(v.idvehiculo) AS cantidad "
                + "FROM Marcas ma LEFT JOIN Vehiculos v ON ma.idmarca = v.idmarca ";
        if (!pais.equals("Todos")) sql += "WHERE ma.pais_origen = ? ";
        sql += "GROUP BY ma.idmarca, ma.nombre_marca ORDER BY cantidad DESC";
        try {
            cx = con.getCon();
            ps = cx.prepareStatement(sql);
            if (!pais.equals("Todos")) ps.setString(1, pais);
            rs = ps.executeQuery();
            while (rs.next()) {
                csv.append(rs.getString("nombre_marca")).append(";")
                        .append(rs.getInt("cantidad")).append("\n");
            }
        } catch (Exception e) {
            System.err.println("Error CSV conteo: " + e.getMessage());
        }
        return csv.toString();
    }

    public String obtenerClasificacionCSV(String pais) {
        StringBuilder csv = new StringBuilder();
        csv.append("Nombre Marca;Cantidad Vehiculos;Año Promedio;Clasificacion\n");
        String sql = "SELECT ma.nombre_marca, COUNT(v.idvehiculo) AS cantidad, "
                + "IFNULL(ROUND(AVG(v.anio), 0), 0) AS prom_anio, "
                + "CASE "
                + "   WHEN COUNT(v.idvehiculo) = 0 THEN 'SIN STOCK' "
                + "   WHEN COUNT(v.idvehiculo) BETWEEN 1 AND 2 THEN 'MEDIA' "
                + "   ELSE 'ALTA DEMANDA' "
                + "END AS clasificacion "
                + "FROM Marcas ma LEFT JOIN Vehiculos v ON ma.idmarca = v.idmarca ";
        if (!pais.equals("Todos")) sql += "WHERE ma.pais_origen = ? ";
        sql += "GROUP BY ma.idmarca, ma.nombre_marca";
        try {
            cx = con.getCon();
            ps = cx.prepareStatement(sql);
            if (!pais.equals("Todos")) ps.setString(1, pais);
            rs = ps.executeQuery();
            while (rs.next()) {
                csv.append(rs.getString("nombre_marca")).append(";")
                        .append(rs.getInt("cantidad")).append(";")
                        .append(rs.getInt("prom_anio")).append(";")
                        .append(rs.getString("clasificacion")).append("\n");
            }
        } catch (Exception e) {
            System.err.println("Error CSV clasificación: " + e.getMessage());
        }
        return csv.toString();
    }

    public String obtenerColoresCSV(String pais) {
        StringBuilder csv = new StringBuilder();
        csv.append("Color;Cantidad Vehiculos;Marcas que lo usan\n");
        String sql = "SELECT v.color, COUNT(v.idvehiculo) AS cantidad, "
                + "GROUP_CONCAT(DISTINCT ma.nombre_marca SEPARATOR ', ') AS marcas_que_lo_usan "
                + "FROM Vehiculos v INNER JOIN Marcas ma ON v.idmarca = ma.idmarca ";
        if (!pais.equals("Todos")) sql += "WHERE ma.pais_origen = ? ";
        sql += "GROUP BY v.color ORDER BY cantidad DESC";
        try {
            cx = con.getCon();
            ps = cx.prepareStatement(sql);
            if (!pais.equals("Todos")) ps.setString(1, pais);
            rs = ps.executeQuery();
            while (rs.next()) {
                csv.append(rs.getString("color").toUpperCase()).append(";")
                        .append(rs.getInt("cantidad")).append(";")
                        .append(rs.getString("marcas_que_lo_usan")).append("\n");
            }
        } catch (Exception e) {
            System.err.println("Error CSV colores: " + e.getMessage());
        }
        return csv.toString();
    }

    public String obtenerConteoJSON(String pais) {
        StringBuilder json = new StringBuilder();
        json.append("[\n");
        String sql = "SELECT ma.nombre_marca, COUNT(v.idvehiculo) AS cantidad "
                + "FROM Marcas ma LEFT JOIN Vehiculos v ON ma.idmarca = v.idmarca ";
        if (!pais.equals("Todos")) sql += "WHERE ma.pais_origen = ? ";
        sql += "GROUP BY ma.idmarca, ma.nombre_marca ORDER BY cantidad DESC";
        try {
            cx = con.getCon();
            ps = cx.prepareStatement(sql);
            if (!pais.equals("Todos")) ps.setString(1, pais);
            rs = ps.executeQuery();
            boolean primero = true;
            while (rs.next()) {
                if (!primero) json.append(",\n");
                json.append("  {\n")
                        .append("    \"marca\": \"").append(rs.getString("nombre_marca")).append("\",\n")
                        .append("    \"cantidad\": ").append(rs.getInt("cantidad")).append("\n")
                        .append("  }");
                primero = false;
            }
        } catch (Exception e) {
            System.err.println("Error JSON conteo: " + e.getMessage());
        }
        json.append("\n]");
        return json.toString();
    }

    public String obtenerClasificacionJSON(String pais) {
        StringBuilder json = new StringBuilder();
        json.append("[\n");
        String sql = "SELECT ma.nombre_marca, COUNT(v.idvehiculo) AS cantidad, "
                + "IFNULL(ROUND(AVG(v.anio), 0), 0) AS prom_anio, "
                + "CASE "
                + "   WHEN COUNT(v.idvehiculo) = 0 THEN 'SIN STOCK' "
                + "   WHEN COUNT(v.idvehiculo) BETWEEN 1 AND 2 THEN 'MEDIA' "
                + "   ELSE 'ALTA DEMANDA' "
                + "END AS clasificacion "
                + "FROM Marcas ma LEFT JOIN Vehiculos v ON ma.idmarca = v.idmarca ";
        if (!pais.equals("Todos")) sql += "WHERE ma.pais_origen = ? ";
        sql += "GROUP BY ma.idmarca, ma.nombre_marca";
        try {
            cx = con.getCon();
            ps = cx.prepareStatement(sql);
            if (!pais.equals("Todos")) ps.setString(1, pais);
            rs = ps.executeQuery();
            boolean primero = true;
            while (rs.next()) {
                if (!primero) json.append(",\n");
                json.append("  {\n")
                        .append("    \"marca\": \"").append(rs.getString("nombre_marca")).append("\",\n")
                        .append("    \"cantidad_vehiculos\": ").append(rs.getInt("cantidad")).append(",\n")
                        .append("    \"anio_promedio\": ").append(rs.getInt("prom_anio")).append(",\n")
                        .append("    \"clasificacion\": \"").append(rs.getString("clasificacion")).append("\"\n")
                        .append("  }");
                primero = false;
            }
        } catch (Exception e) {
            System.err.println("Error JSON clasificación: " + e.getMessage());
        }
        json.append("\n]");
        return json.toString();
    }

    public String obtenerColoresJSON(String pais) {
        StringBuilder json = new StringBuilder();
        json.append("[\n");
        String sql = "SELECT v.color, COUNT(v.idvehiculo) AS cantidad, "
                + "GROUP_CONCAT(DISTINCT ma.nombre_marca SEPARATOR ', ') AS marcas_que_lo_usan "
                + "FROM Vehiculos v INNER JOIN Marcas ma ON v.idmarca = ma.idmarca ";
        if (!pais.equals("Todos")) sql += "WHERE ma.pais_origen = ? ";
        sql += "GROUP BY v.color ORDER BY cantidad DESC";
        try {
            cx = con.getCon();
            ps = cx.prepareStatement(sql);
            if (!pais.equals("Todos")) ps.setString(1, pais);
            rs = ps.executeQuery();
            boolean primero = true;
            while (rs.next()) {
                if (!primero) json.append(",\n");
                json.append("  {\n")
                        .append("    \"color\": \"").append(rs.getString("color").toUpperCase()).append("\",\n")
                        .append("    \"cantidad\": ").append(rs.getInt("cantidad")).append(",\n")
                        .append("    \"marcas\": \"").append(rs.getString("marcas_que_lo_usan")).append("\"\n")
                        .append("  }");
                primero = false;
            }
        } catch (Exception e) {
            System.err.println("Error JSON colores: " + e.getMessage());
        }
        json.append("\n]");
        return json.toString();
    }

    public String obtenerReporteCompletoTXT(String pais) {
        return "===== REPORTE CONSOLIDADO (" + pais.toUpperCase() + ") =====\n\n"
                + "--- CONTEO POR MARCAS ---\n" + obtenerConteoMarcas(pais) + "\n"
                + "--- CLASIFICACIÓN DE MARCAS ---\n" + obtenerClasificacionMarcas(pais) + "\n"
                + "--- COLORES POR MARCA ---\n" + obtenerColoresMarca(pais);
    }

    public String obtenerReporteCompletoCSV(String pais) {
        return "=== CONTEO ===\n" + obtenerConteoCSV(pais)
                + "\n=== CLASIFICACION ===\n" + obtenerClasificacionCSV(pais)
                + "\n=== COLORES ===\n" + obtenerColoresCSV(pais);
    }

    public String obtenerReporteCompletoJSON(String pais) {
        return "{\n"
                + "  \"conteo\": " + obtenerConteoJSON(pais) + ",\n"
                + "  \"clasificacion\": " + obtenerClasificacionJSON(pais) + ",\n"
                + "  \"colores\": " + obtenerColoresJSON(pais) + "\n"
                + "}";
    }
}