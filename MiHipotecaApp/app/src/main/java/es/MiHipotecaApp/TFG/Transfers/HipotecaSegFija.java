package es.MiHipotecaApp.TFG.Transfers;

import java.io.Serializable;

public class HipotecaSegFija extends HipotecaSeguimiento implements Serializable {

    //Hipoteca fija
    private float porcentaje_fijo;

    public HipotecaSegFija(String nombre, String comunidad_autonoma, String tipo_vivienda, String antiguedad_vivienda, float precio_vivienda, float cantidad_abonada, int plazo_anios, int anio_hipoteca_actual, float totalGastos, float totalVinculacionesAnual, String banco_asociado, float porcentaje_fijo) {
        super(nombre, comunidad_autonoma, tipo_vivienda, antiguedad_vivienda, precio_vivienda, cantidad_abonada, plazo_anios, anio_hipoteca_actual, totalGastos, totalVinculacionesAnual, banco_asociado);
        this.porcentaje_fijo = porcentaje_fijo;
    }

    public float getPorcentaje_fijo() {
        return porcentaje_fijo;
    }

    public void setPorcentaje_fijo(float porcentaje_fijo) {
        this.porcentaje_fijo = porcentaje_fijo;
    }

}
