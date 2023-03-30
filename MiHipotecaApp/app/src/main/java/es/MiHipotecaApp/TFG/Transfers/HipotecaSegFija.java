package es.MiHipotecaApp.TFG.Transfers;

import android.util.Log;

import org.checkerframework.checker.units.qual.A;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
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
        for (int i = 1; i <= numero_pago; i++){
            double cantidad_capital = getCapitalAmortizadoMensual(cuota_mensual, capital_pendiente, porcentaje_fijo);
            capital_pendiente = capital_pendiente - cantidad_capital;
        }
        return Math.round(capital_pendiente * 100.0) / 100.0;
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

        return Math.round(interesesTotales * 100.0) / 100.0;
    }

    /** Cuotas mensuales + Gastos asociados + Vinculaciones **/
    @Override
    public double getGastosTotalesHipoteca(){
        return getCuotaMensual(porcentaje_fijo, precio_vivienda - cantidad_abonada, plazo_anios * 12) * plazo_anios*12 + totalVinculacionesAnual*plazo_anios + totalGastos;
    }

    /** Esta funcion devuelve la cuota, capital, intereses y capital pendiente del numero de cuota pasado **/
    @Override
    public ArrayList<Double> getFilaCuadroAmortizacionMensual(int numCuota){
        ArrayList<Double> valores = new ArrayList<>();
        double cuota   = getCuotaMensual(porcentaje_fijo, precio_vivienda - cantidad_abonada, plazo_anios * 12);
        double capPdteCuota = getCapitalPendienteTotalActual(numCuota);

        double capPdte = numCuota == 1 ? precio_vivienda - cantidad_abonada : getCapitalPendienteTotalActual(numCuota - 1);
        valores.add(cuota);
        valores.add(getCapitalAmortizadoMensual(cuota, capPdte, porcentaje_fijo));
        valores.add(getInteresMensual(capPdte, porcentaje_fijo));
        valores.add(capPdteCuota);
        return valores;
    }

    /** Esta funcion devuelve el total anual, capital anual, intereses anuales y capital pendiente del numero de anio pasado**/
    @Override
    public ArrayList<Double> getFilaCuadroAmortizacionAnual(int anio, int num_anio){
        ArrayList<Double> valores = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha_inicio);
        int cuotasAnuales;
        //si es el primer año de hipoteca
        if(calendar.get(Calendar.YEAR) == anio) cuotasAnuales = 12 + (getNumeroCuotaEnEnero(anio) - 1);
        else if(calendar.get(Calendar.YEAR) + plazo_anios == anio) cuotasAnuales = (getNumeroCuotaEnEnero(anio) - 1) - 12;
        else cuotasAnuales = 12;
        int cuotasPagadas = num_anio > 1 ? (num_anio - 1) * 12 + cuotasAnuales : cuotasAnuales;
        // Capital pendiente para diciembre de este año
        double capPdteUltimo = getCapitalPendienteTotalActual(cuotasPagadas);
        // Capital pendiente para diciembre del año anterior

        //El numero pasado por param esta mal
        double capPdteAnterior = cuotasPagadas < 12 ? precio_vivienda - cantidad_abonada : getCapitalPendienteTotalActual(cuotasPagadas - cuotasAnuales);
        double cuotaMensual = getCuotaMensual(porcentaje_fijo, precio_vivienda - cantidad_abonada, plazo_anios * 12);

        valores.add(Math.round(cuotaMensual * cuotasAnuales * 100.0) / 100.0);
        valores.add(Math.round((capPdteAnterior - capPdteUltimo) * 100.0) / 100.0);
        valores.add(getInteresesPlazo(capPdteAnterior, cuotasAnuales, porcentaje_fijo, cuotaMensual));
        valores.add(capPdteUltimo);

        return valores;
    }


    /** Esta funcion devuelve el porcentaje que se aplica para un determinado numero de cuota**/
    @Override
    public double getPorcentajePorCuota(int numCuota){ return porcentaje_fijo; }

    /** Getters y Setters*/
    @Override
    public double getPorcentaje_fijo() {
        return porcentaje_fijo;
    }


    public void setPorcentaje_fijo(double porcentaje_fijo) {
        this.porcentaje_fijo = porcentaje_fijo;
    }
}
