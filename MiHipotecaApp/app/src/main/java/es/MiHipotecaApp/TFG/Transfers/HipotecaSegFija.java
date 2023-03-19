package es.MiHipotecaApp.TFG.Transfers;

import java.io.Serializable;
import java.util.Date;

public class HipotecaSegFija extends HipotecaSeguimiento implements Serializable {

    //Hipoteca fija
    private double porcentaje_fijo;

    public HipotecaSegFija(String nombre, String comunidad_autonoma, String tipo_vivienda, String antiguedad_vivienda, double precio_vivienda, double cantidad_abonada, int plazo_anios, Date fecha_inicio, String tipo_hipoteca, double totalGastos, double totalVinculacionesAnual, String banco_asociado, double porcentaje_fijo) {
        super(nombre, comunidad_autonoma, tipo_vivienda, antiguedad_vivienda, precio_vivienda, cantidad_abonada, plazo_anios, fecha_inicio, tipo_hipoteca, totalGastos, totalVinculacionesAnual, banco_asociado);
        this.porcentaje_fijo = porcentaje_fijo;
        //obtenerCuotaMensual();
    }

    /** Esta funcion devuelve el capital y los intereses pagados hasta el momento actual**/
    @Override
    public double getDineroAportadoActual(int numPago){
        return getCuotaMensual(porcentaje_fijo, precio_vivienda - cantidad_abonada) * numPago;
    }

    /** Esta funcion devuelve el capital y los intereses pendientes por pagar **/
    @Override
    public double getDineroRestanteActual(int numPago){
        return getCuotaMensual(porcentaje_fijo, precio_vivienda - cantidad_abonada)*(plazo_anios*12 - numPago);
    }


    /** Revisar esta funcion, creo que esta mal*/
    @Override
    public double getInteresesTotales() {
        double interesesTotales = 0;
        double capPendiente = precio_vivienda - cantidad_abonada;

        for (int i = 0; i < plazo_anios * 12; i++) {
            interesesTotales += getInteresMensual(capPendiente, porcentaje_fijo);
            capPendiente -= (getCuotaMensual(getPorcentaje_fijo(), precio_vivienda - cantidad_abonada) - getInteresMensual(capPendiente, getPorcentaje_fijo()));
        }

        return interesesTotales;
    }

    /** Cuotas mensuales + Gastos asociados + Vinculaciones **/
    @Override
    public double getGastosTotalesHipoteca(){
        return getCuotaMensual(porcentaje_fijo, precio_vivienda - cantidad_abonada)*plazo_anios*12 + totalVinculacionesAnual*plazo_anios + totalGastos;
    }

    public double getPorcentaje_fijo() {
        return porcentaje_fijo;
    }


    public void setPorcentaje_fijo(double porcentaje_fijo) {
        this.porcentaje_fijo = porcentaje_fijo;
    }
}
