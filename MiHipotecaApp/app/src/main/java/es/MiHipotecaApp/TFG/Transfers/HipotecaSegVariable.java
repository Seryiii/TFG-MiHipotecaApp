package es.MiHipotecaApp.TFG.Transfers;

import android.util.Log;

import java.io.Serializable;
import java.util.Date;

public class HipotecaSegVariable extends HipotecaSeguimiento implements Serializable {

    //Hipoteca variable
    private int duracion_primer_porcentaje_variable;
    private double primer_porcentaje_variable;
    private double porcentaje_diferencial_variable;
    private boolean revision_anual;

    public HipotecaSegVariable(String nombre, String comunidad_autonoma, String tipo_vivienda, String antiguedad_vivienda, double precio_vivienda, double cantidad_abonada, int plazo_anios, Date fecha_inicio, String tipo_hipoteca, double totalGastos, double totalVinculacionesAnual, String banco_asociado, int duracion_primer_porcentaje_variable, double primer_porcentaje_variable, double porcentaje_diferencial_variable, boolean revision_anual) {
        super(nombre, comunidad_autonoma, tipo_vivienda, antiguedad_vivienda, precio_vivienda, cantidad_abonada, plazo_anios, fecha_inicio, tipo_hipoteca, totalGastos, totalVinculacionesAnual, banco_asociado);
        this.duracion_primer_porcentaje_variable = duracion_primer_porcentaje_variable;
        this.primer_porcentaje_variable = primer_porcentaje_variable;
        this.porcentaje_diferencial_variable = porcentaje_diferencial_variable;
        this.revision_anual = revision_anual;
    }


    /** Esta funcion devuelve el capital pendiente total por amortizar**/
    @Override
    public double getCapitalPendienteTotalActual(int numero_pago){
        double capital_pendiente = precio_vivienda - cantidad_abonada;
        double cuota_mensual = getCuotaMensual(primer_porcentaje_variable, capital_pendiente, plazo_anios * 12);
        double cantidad_capital;
        int aux = numero_pago > duracion_primer_porcentaje_variable ? duracion_primer_porcentaje_variable : numero_pago;
        for (int i = 1; i <= aux; i++){
            cantidad_capital = getCapitalAmortizadoMensual(cuota_mensual, capital_pendiente, primer_porcentaje_variable);
            capital_pendiente = capital_pendiente - cantidad_capital;
            Log.i("Pago " + i + ": ", "CANT PDTE: " + capital_pendiente + "     CAPITAL MENSUAL: " + cantidad_capital + "     CUOTA MENSUAL: " + cuota_mensual);
        }

        int j = aux;
        int revision = 6;
        while(j < numero_pago){
            if(isRevision_anual()) revision = 12;
            double euribor = getEuriborPasado(j);
            cuota_mensual = getCuotaMensual(porcentaje_diferencial_variable + euribor, capital_pendiente, (plazo_anios * 12) - j);
            for(int h = 0; h < revision && j < numero_pago; h++){
                cantidad_capital = getCapitalAmortizadoMensual(cuota_mensual, capital_pendiente, porcentaje_diferencial_variable + euribor);
                capital_pendiente = capital_pendiente - cantidad_capital;
                Log.i("Pago " + Integer.toString(j + 1) + ": ", "CANT PDTE: " + capital_pendiente + "     CAPITAL MENSUAL: " + cantidad_capital + "     CUOTA MENSUAL: " + cuota_mensual);
                j++;
            }
        }

        return capital_pendiente;
    }

    /** Esta funcion devuelve el capital y los intereses pendientes por pagar, simulando que el euribor se mantiene fijo
     *  durante los años restantes. (Se utiliza el euribor del mes actual) **/
    @Override
    public double getDineroRestanteActual(int numPago){

        // Si estas en primer porcentaje, tienes que acabar la fase fija y luego estimar con el euribor actual
        // Si estas en la fase variable, simular lo que queda en funcion del euribor actual

        double capital_pendiente = getCapitalPendienteTotalActual(numPago);
        double porcentaje_aplicado  = numPago <= duracion_primer_porcentaje_variable ? primer_porcentaje_variable : getEuriborActual() + porcentaje_diferencial_variable;
        double cuota_mensual = getCuotaMensual(porcentaje_aplicado, capital_pendiente, plazo_anios * 12 - getNumeroCuotaActual());
        if(numPago <= duracion_primer_porcentaje_variable){
            int aux = duracion_primer_porcentaje_variable - numPago;
            // se inicializa el dinero restante con lo que queda por pagar del primer porcentaje
            double dinero_restante = cuota_mensual * aux;
            double capital_pendiente_restante = getCapitalPendienteTotalActual(duracion_primer_porcentaje_variable);
            // Sumar lo que queda con el euribor actual + diferencial
            cuota_mensual = getCuotaMensual(getEuriborActual() + porcentaje_diferencial_variable, capital_pendiente_restante, plazo_anios * 12 - duracion_primer_porcentaje_variable);
            dinero_restante = dinero_restante + (cuota_mensual * (plazo_anios * 12 - duracion_primer_porcentaje_variable));
            return dinero_restante;
        }
        else return cuota_mensual * (plazo_anios * 12 - numPago);
    }


    /** Getters y Setters*/
    @Override
    public int getDuracion_primer_porcentaje_variable() {
        return duracion_primer_porcentaje_variable;
    }

    @Override
    public double getPrimer_porcentaje_variable() {
        return primer_porcentaje_variable;
    }

    @Override
    public double getPorcentaje_diferencial_variable() {
        return porcentaje_diferencial_variable;
    }

    @Override
    public boolean isRevision_anual() {
        return revision_anual;
    }

    public void setDuracion_primer_porcentaje_variable(int duracion_primer_porcentaje_variable) {
        this.duracion_primer_porcentaje_variable = duracion_primer_porcentaje_variable;
    }

    public void setPrimer_porcentaje_variable(double primer_porcentaje_variable) {
        this.primer_porcentaje_variable = primer_porcentaje_variable;
    }

    public void setPorcentaje_diferencial_variable(double porcentaje_diferencial_variable) {
        this.porcentaje_diferencial_variable = porcentaje_diferencial_variable;
    }

    public void setRevision_anual(boolean revision_anual) {
        this.revision_anual = revision_anual;
    }
}
