package es.TFG.MiHipotecaApp.Transfers;

import java.io.Serializable;

public class Usuario implements Serializable {

    private int id;
    private String correo;
    private String contra;
    private String nombre;

    public Usuario() {

    }
    public Usuario(int id, String correo, String contra, String nombre) {
        this.id = id;
        this.correo = correo;
        this.contra = contra;
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContra() {
        return contra;
    }

    public void setContra(String contra) {
        this.contra = contra;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
