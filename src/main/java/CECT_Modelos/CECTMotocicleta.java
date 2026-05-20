/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package CECT_Modelos;

/**
 *
 * @author axele
 */
public class CECTMotocicleta {
    private int idMotocicleta;
    private String modelo;
    private int cilindraje;
    private String color;
    private int idMarca;
    private String imagen;
    private String marca;

    public CECTMotocicleta() {
    }

    public CECTMotocicleta(String modelo, int cilindraje, String color, int idMarca, String imagen, String marca) {
        this.modelo = modelo;
        this.cilindraje = cilindraje;
        this.color = color;
        this.idMarca = idMarca;
        this.imagen = imagen;
        this.marca = marca;
    }

    public CECTMotocicleta(int idMotocicleta, String modelo, int cilindraje, String color, int idMarca, String imagen, String marca) {
        this.idMotocicleta = idMotocicleta;
        this.modelo = modelo;
        this.cilindraje = cilindraje;
        this.color = color;
        this.idMarca = idMarca;
        this.imagen = imagen;
        this.marca = marca;
    }

    public int getIdMotocicleta() {
        return idMotocicleta;
    }

    public void setIdMotocicleta(int idMotocicleta) {
        this.idMotocicleta = idMotocicleta;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public int getCilindraje() {
        return cilindraje;
    }

    public void setCilindraje(int cilindraje) {
        this.cilindraje = cilindraje;
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
