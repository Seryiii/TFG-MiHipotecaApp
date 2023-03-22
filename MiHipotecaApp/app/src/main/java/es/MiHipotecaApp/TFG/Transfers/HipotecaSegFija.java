package es.MiHipotecaApp.TFG.Transfers;

import android.util.Log;

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
        return getCuotaMensual(porcentaje_fijo, precio_vivienda - cantidad_abonada, plazo_anios * 12) * numPago;
    }

    /** Esta funcion devuelve el capital y los intereses pendientes por pagar **/
    @Override
    public double getDineroRestanteActual(int numPago){
        return getCuotaMensual(porcentaje_fijo, precio_vivienda - cantidad_abonada, plazo_anios * 12)*(plazo_anios*12 - numPago);
    }

    /** Esta funcion devuelve el capital pendiente total por amortizar**/
    @Override
    public double getCapitalPendienteTotalActual(int numero_pago){
        double capital_pendiente = precio_vivienda - cantidad_abonada;
        double cuota_mensual = getCuotaMensual(porcentaje_fijo, capital_pendiente, plazo_anios * 12);
        for (int i = 1; i < numero_pago; i++){
            double cantidad_capital = getCapitalAmortizadoMensual(cuota_mensual, capital_pendiente, porcentaje_fijo);
            capital_pendiente = capital_pendiente - cantidad_capital;
            Log.i("Pago " + i + ": ", "CANT PDTE: " + capital_pendiente + "     CAPITAL MENSUAL: " + cantidad_capital);
        }
        return capital_pendiente;
    }

    /** Revisar esta funcion, creo que esta mal*/
    @Override
    public double getInteresesTotales() {
        double interesesTotales = 0;
        double capPendiente = precio_vivienda - cantidad_abonada;

        for (int i = 0; i < plazo_anios * 12; i++) {
            interesesTotales += getInteresMensual(capPendiente, porcentaje_fijo);
            capPendiente -= (getCuotaMensual(getPorcentaje_fijo(), precio_vivienda - cantidad_abonada, plazo_anios * 12) - getInteresMensual(capPendiente, getPorcentaje_fijo()));
        }

        return interesesTotales;
    }

    /** Cuotas mensuales + Gastos asociados + Vinculaciones **/
    @Override
    public double getGastosTotalesHipoteca(){
        return getCuotaMensual(porcentaje_fijo, precio_vivienda - cantidad_abonada, plazo_anios * 12) * plazo_anios*12 + totalVinculacionesAnual*plazo_anios + totalGastos;
    }


    /** Getters y Setters*/
    @Override
    public double getPorcentaje_fijo() {
        return porcentaje_fijo;
    }


    public void setPorcentaje_fijo(double porcentaje_fijo) {
        this.porcentaje_fijo = porcentaje_fijo;
    }
}
