package es.MiHipotecaApp.TFG.Transfers;

import android.util.Log;

import org.checkerframework.checker.units.qual.A;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class HipotecaSegFija extends HipotecaSeguimiento implements Serializable {

    //Hipoteca fija
    private double porcentaje_fijo;

    public HipotecaSegFija(String nombre, String comunidad_autonoma, String tipo_vivienda, String antiguedad_vivienda, double precio_vivienda, double cantidad_abonada, int plazo_anios, Date fecha_inicio, String tipo_hipoteca, double totalGastos, List<Double> arrayVinculacionesAnual, String banco_asociado, double porcentaje_fijo) {
        super(nombre, comunidad_autonoma, tipo_vivienda, antiguedad_vivienda, precio_vivienda, cantidad_abonada, plazo_anios, fecha_inicio, tipo_hipoteca, totalGastos, arrayVinculacionesAnual, banco_asociado);
        this.porcentaje_fijo = porcentaje_fijo;
    }

    /** Esta funcion devuelve el capital y los intereses pendientes por pagar **/
    @Override
    public double getDineroRestanteActual(int numPago, HashMap<Integer, List<Object>> amortizaciones){

        int cuotasRestantes = getPlazoActual(amortizaciones) - numPago;
        double capPendiente = getCapitalPendienteTotalActual(numPago, amortizaciones);
        return getCuotaMensual(porcentaje_fijo, capPendiente, getPlazoActual(amortizaciones), amortizaciones) * cuotasRestantes;
    }

    /** Esta funcion devuelve el capital pendiente total por amortizar**/
    @Override
    public double getCapitalPendienteTotalActual(int numero_pago, HashMap<Integer, List<Object>> amortizaciones){
        double capital_pendiente = precio_vivienda - cantidad_abonada;
        int plazoActual = plazo_anios * 12;
        double cuota_mensual = getCuotaMensual(porcentaje_fijo, capital_pendiente, plazoActual, amortizaciones);

        for (int i = 1; i <= numero_pago; i++){
            if(amortizaciones.containsKey(i)){
                if(amortizaciones.get(i).get(0).equals("total")) return 0;
                else if (amortizaciones.get(i).get(0).equals("parcial_cuota")){
                    capital_pendiente -= (Double) amortizaciones.get(i).get(1);
                    cuota_mensual = getCuotaMensual(porcentaje_fijo, capital_pendiente, plazoActual, amortizaciones);
                }
                else {
                    capital_pendiente -= (Double) amortizaciones.get(i).get(1);
                    plazoActual -= (Long) amortizaciones.get(i).get(2);
                }
            }
            double cantidad_capital = getCapitalAmortizadoMensual(cuota_mensual, capital_pendiente, porcentaje_fijo);
            capital_pendiente = capital_pendiente - cantidad_capital;
        }

        return Math.round(capital_pendiente * 100.0) / 100.0;
    }

    /** Devuelve los intereses hasta el numero de pago pasado por parametro **/
    @Override
    public double getInteresesHastaNumPago(int num_pago, HashMap<Integer, List<Object>> amortizaciones) {
        double interesesTotales = 0;
        double capPendiente = precio_vivienda - cantidad_abonada;
        int plazoActual = plazo_anios * 12;
        double cuota_mensual = getCuotaMensual(getPorcentaje_fijo(), capPendiente, plazoActual, amortizaciones);

        for (int i = 1; i <= num_pago; i++) {
            if(amortizaciones.containsKey(i)){
                if(amortizaciones.get(i).get(0).equals("total")) return 0;
                else if (amortizaciones.get(i).get(0).equals("parcial_cuota")){
                    capPendiente -= (Double) amortizaciones.get(i).get(1);
                    cuota_mensual = getCuotaMensual(porcentaje_fijo, capPendiente, plazoActual, amortizaciones);
                }
                else {
                    capPendiente -= (Double) amortizaciones.get(i).get(1);
                    plazoActual -= (Long) amortizaciones.get(i).get(2);
                }
            }
            interesesTotales += getInteresMensual(capPendiente, porcentaje_fijo);
            capPendiente -= (cuota_mensual - getInteresMensual(capPendiente, getPorcentaje_fijo()));
        }

        return Math.round(interesesTotales * 100.0) / 100.0;
    }

    /** Esta funcion devuelve la cuota, capital, intereses y capital pendiente del numero de cuota pasado **/
    @Override
    public ArrayList<Double> getFilaCuadroAmortizacionMensual(int numCuota, HashMap<Integer, List<Object>> amortizaciones){
        ArrayList<Double> valores = new ArrayList<>();
        double capPdteCuota = getCapitalPendienteTotalActual(numCuota, amortizaciones);
        double cuota        = getCuotaMensual(porcentaje_fijo, capPdteCuota, getPlazoActual(amortizaciones) - numCuota, amortizaciones);
        //double capPdte      = numCuota <= 1 ? precio_vivienda - cantidad_abonada : getCapitalPendienteTotalActual(numCuota, amortizaciones);

        double capPdte = getCapitalPendienteTotalActual(numCuota - 1, amortizaciones);
        valores.add(cuota);
        valores.add(getCapitalAmortizadoMensual(cuota, capPdte, porcentaje_fijo));
        valores.add(getInteresMensual(capPdte, porcentaje_fijo));
        valores.add(capPdteCuota);
        return valores;
    }

    /** Esta funcion devuelve el capital del numero de cuota pasado **/
    @Override
    public double getCapitalDeUnaCuota(int numCuota, HashMap<Integer, List<Object>> amortizaciones){
        double capPdteCuota = getCapitalPendienteTotalActual(numCuota, amortizaciones);
        double cuota        = getCuotaMensual(porcentaje_fijo, capPdteCuota, getPlazoActual(amortizaciones) - numCuota, amortizaciones);
        //double capPdte      = numCuota == 1 ? precio_vivienda - cantidad_abonada : getCapitalPendienteTotalActual(numCuota - 1, amortizaciones);
        double capPdte = getCapitalPendienteTotalActual(numCuota - 1, amortizaciones);
        double capitalCuota = getCapitalAmortizadoMensual(cuota, capPdte, porcentaje_fijo);
        return capitalCuota;
    }

    /** Esta funcion devuelve el total anual, capital anual, intereses anuales y capital pendiente del numero de anio pasado**/
    //TODO FALTA REVISAR BIEN ESTA FUNCION, ES JODIDA MUY JODIDA
    @Override
    public ArrayList<Double> getFilaCuadroAmortizacionAnual(int anio, int num_anio, HashMap<Integer, List<Object>> amortizaciones){

        // TOTAL_ANUAL, CAPITAL_ANUAL, INTERESES_ANUALES, CAPITAL PDTE
        ArrayList<Double> valores = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha_inicio);
        int cuotasAnuales;

        //si es el primer año de hipoteca
        if(calendar.get(Calendar.YEAR) == anio) cuotasAnuales = 12 + (getNumeroCuotaEnEnero(anio) - 1);
        else if(calendar.get(Calendar.YEAR) + (int) Math.ceil(getPlazoActual(amortizaciones) / 12) == anio) cuotasAnuales = (getNumeroCuotaEnEnero(anio) - 1) - 12;
        else cuotasAnuales = 12;

        Calendar inicio = Calendar.getInstance();
        inicio.setTime(fecha_inicio);
        int cuotasPrimerAnio = (getNumeroCuotaEnEnero(inicio.get(Calendar.YEAR)) + 12) - 1;

        int cuotasPagadas = num_anio > 1 ? cuotasPrimerAnio + (num_anio - 2) * 12 + cuotasAnuales : cuotasAnuales;

        // Capital pendiente para diciembre de este año
        double capPdteUltimo = getCapitalPendienteTotalActual(cuotasPagadas, amortizaciones);
        // Capital pendiente para diciembre del año anterior
        double capPdteAnterior = cuotasPagadas < 12 ? precio_vivienda - cantidad_abonada : getCapitalPendienteTotalActual(cuotasPagadas - cuotasAnuales, amortizaciones);
        double totalCapitalAnual = capPdteAnterior - capPdteUltimo;

        double interesesAnteriores = cuotasAnuales < 12 ? 0 : getInteresesHastaNumPago(cuotasPagadas - cuotasAnuales, amortizaciones);
        double interesesSiguientes = getInteresesHastaNumPago(cuotasPagadas, amortizaciones);
        double totalInteresesAnio = interesesSiguientes - interesesAnteriores;

        valores.add(Math.round((totalCapitalAnual + totalInteresesAnio) * 100.0) / 100.0);
        valores.add(Math.round(totalCapitalAnual * 100.0) / 100.0);
        valores.add(totalInteresesAnio);
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
