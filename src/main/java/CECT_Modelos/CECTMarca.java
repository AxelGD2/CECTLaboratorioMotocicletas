package CECT_Modelos;

public class CECTMarca {
    private int idmarca;
    private String nombre_marca;
    private String pais_origen;
    private int cantidad; 


    public CECTMarca() {
    }


    public CECTMarca(String nombre_marca, String pais_origen) {
        this.nombre_marca = nombre_marca;
        this.pais_origen = pais_origen;
    }


    public CECTMarca(int idmarca, String nombre_marca, String pais_origen) {
        this.idmarca = idmarca;
        this.nombre_marca = nombre_marca;
        this.pais_origen = pais_origen;
    }

    public CECTMarca(int idmarca, String nombre_marca, String pais_origen, int cantidad) {
        this.idmarca = idmarca;
        this.nombre_marca = nombre_marca;
        this.pais_origen = pais_origen;
        this.cantidad = cantidad;
    }

    public int getIdmarca() {
        return idmarca;
    }

    public void setIdmarca(int idmarca) {
        this.idmarca = idmarca;
    }

    public String getNombre_marca() {
        return nombre_marca;
    }

    public void setNombre_marca(String nombre_marca) {
        this.nombre_marca = nombre_marca;
    }

    public String getPais_origen() {
        return pais_origen;
    }

    public void setPais_origen(String pais_origen) {
        this.pais_origen = pais_origen;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
    
    @Override
    public String toString(){
        return nombre_marca;
    }
}