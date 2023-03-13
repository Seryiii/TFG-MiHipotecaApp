package es.MiHipotecaApp.TFG.Transfers;

import java.io.Serializable;

public class HipotecaSegFija extends HipotecaSeguimiento implements Serializable {

    //Hipoteca fija
    private double porcentaje_fijo;

    public HipotecaSegFija(String nombre, String comunidad_autonoma, String tipo_vivienda, String antiguedad_vivienda, double precio_vivienda, double cantidad_abonada, int plazo_anios, int anio_hipoteca_actual, int mes_hipoteca_actual, double totalGastos, double totalVinculacionesAnual, String banco_asociado, double porcentaje_fijo) {
        super(nombre, comunidad_autonoma, tipo_vivienda, antiguedad_vivienda, precio_vivienda, cantidad_abonada, plazo_anios, anio_hipoteca_actual, mes_hipoteca_actual, totalGastos, totalVinculacionesAnual, banco_asociado);
        this.porcentaje_fijo = porcentaje_fijo;
    }

    public double getPorcentaje_fijo() {
        return porcentaje_fijo;
    }

    public void setPorcentaje_fijo(double porcentaje_fijo) {
        this.porcentaje_fijo = porcentaje_fijo;
    }

}
