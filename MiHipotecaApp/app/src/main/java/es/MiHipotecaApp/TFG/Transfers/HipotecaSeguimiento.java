package es.MiHipotecaApp.TFG.Transfers;

import java.io.Serializable;

public class HipotecaSeguimiento implements Serializable {

    private String nombre;

    public HipotecaSeguimiento(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
