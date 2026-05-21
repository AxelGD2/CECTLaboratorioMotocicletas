package CECT_Modelos;

public class CECTVehiculo {
    private int idVehiculo;
    private String modelo;
    private int anio;
    private String color;
    private int idMarca;
    private String imagen;
    private String pais;
    private String marca;


    public CECTVehiculo() {
    }


    public CECTVehiculo(String modelo, int anio, String color, int idMarca, String imagen, String marca) {
        this.modelo = modelo;
        this.anio = anio;
        this.color = color;
        this.idMarca = idMarca;
        this.imagen = imagen;
        this.marca = marca;
    }


    public CECTVehiculo(int idVehiculo, String modelo, int anio, String color, int idMarca, String imagen, String marca) {
        this.idVehiculo = idVehiculo;
        this.modelo = modelo;
        this.anio = anio;
        this.color = color;
        this.idMarca = idMarca;
        this.imagen = imagen;
        this.marca = marca;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }
    
    

    public int getIdVehiculo() {
        return idVehiculo;
    }

    public void setIdVehiculo(int idVehiculo) {
        this.idVehiculo = idVehiculo;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getIdMarca() {
        return idMarca;
    }

    public void setIdMarca(int idMarca) {
        this.idMarca = idMarca;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }
    
    @Override
    public String toString(){
        return marca; 
    }
}