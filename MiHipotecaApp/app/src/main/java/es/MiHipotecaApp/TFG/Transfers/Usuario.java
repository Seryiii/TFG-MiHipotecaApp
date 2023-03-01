package es.MiHipotecaApp.TFG.Transfers;

import java.io.Serializable;

public class Usuario implements Serializable {

    private int id;
    private String correo;
    private String password;
    private String nombre;
    private int premium;
    private int avatar;

    public Usuario() {

    }

    public Usuario(int id, String correo, String password, String nombre, int premium, int avatar) {
        this.id = id;
        this.correo = correo;
        this.password = password;
        this.nombre = nombre;
        this.premium = premium;
        this.avatar = avatar;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int isPremium() {
        return premium;
    }

    public void setPremium(int premium) {
        this.premium = premium;
    }

    public int getAvatar() {
        return avatar;
    }

    public void setAvatar(int avatar) {
        this.avatar = avatar;
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


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
