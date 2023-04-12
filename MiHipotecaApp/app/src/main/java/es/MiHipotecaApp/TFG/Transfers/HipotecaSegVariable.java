package es.MiHipotecaApp.TFG.Transfers;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class HipotecaSegVariable extends HipotecaSeguimiento implements Serializable {

    //Hipoteca variable
    private int duracion_primer_porcentaje_variable;
    private double primer_porcentaje_variable;
    private double porcentaje_diferencial_variable;
    private boolean revision_anual;

    public HipotecaSegVariable(String nombre, String comunidad_autonoma, String tipo_vivienda, String antiguedad_vivienda, double precio_vivienda, double cantidad_abonada, int plazo_anios, Date fecha_inicio, String tipo_hipoteca, double totalGastos, List<Double> arrayVinculacionesAnual, String banco_asociado, int duracion_primer_porcentaje_variable, double primer_porcentaje_variable, double porcentaje_diferencial_variable, boolean revision_anual) {
        super(nombre, comunidad_autonoma, tipo_vivienda, antiguedad_vivienda, precio_vivienda, cantidad_abonada, plazo_anios, fecha_inicio, tipo_hipoteca, totalGastos, arrayVinculacionesAnual, banco_asociado);
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
                j++;
            }
        }

        return Math.round(capital_pendiente * 100.0) / 100.0;
    }

    /** Esta funcion devuelve la cantidad de intereses hasta el numero de pago pasado**/
    @Override
    public double getInteresesHastaNumPago(int numero_pago){
        double intereses_totales = 0;
        double capital_pendiente = precio_vivienda - cantidad_abonada;
        double cuota_mensual = getCuotaMensual(primer_porcentaje_variable, capital_pendiente, plazo_anios * 12);
        double cantidad_capital;
        int aux = numero_pago > duracion_primer_porcentaje_variable ? duracion_primer_porcentaje_variable : numero_pago;
        for (int i = 1; i <= aux; i++){
            intereses_totales += getInteresMensual(capital_pendiente, primer_porcentaje_variable);
            cantidad_capital = getCapitalAmortizadoMensual(cuota_mensual, capital_pendiente, primer_porcentaje_variable);
            capital_pendiente = capital_pendiente - cantidad_capital;
        }

        int j = aux;
        int revision = 6;
        while(j < numero_pago){
            if(isRevision_anual()) revision = 12;
            double euribor = getEuriborPasado(j);
            cuota_mensual = getCuotaMensual(porcentaje_diferencial_variable + euribor, capital_pendiente, (plazo_anios * 12) - j);
            for(int h = 0; h < revision && j < numero_pago; h++){
                intereses_totales += getInteresMensual(capital_pendiente, porcentaje_diferencial_variable + euribor);
                cantidad_capital = getCapitalAmortizadoMensual(cuota_mensual, capital_pendiente, porcentaje_diferencial_variable + euribor);
                capital_pendiente = capital_pendiente - cantidad_capital;
                j++;
            }
        }

        return Math.round(intereses_totales * 100.0) / 100.0;
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

    /** Esta funcion devuelve si en el siguiente pago toca revision **/
    @Override
    public boolean siguienteCuotaRevision(){

        int numCuotasPagadas = getNumeroCuotaActual();
        if (numCuotasPagadas < duracion_primer_porcentaje_variable) return false;
        if (numCuotasPagadas == duracion_primer_porcentaje_variable) return true;
        if ((revision_anual  && (numCuotasPagadas % 12 == 0)) || (!revision_anual && (numCuotasPagadas % 6  == 0))) return true;
        return false;
    }

    /** Esta funcion devuelve el porcentaje que se aplica para un determinado numero de cuota**/
    @Override
    public double getPorcentajePorCuota(int numCuota){
        if (numCuota <= duracion_primer_porcentaje_variable) return primer_porcentaje_variable;
        return getEuriborPasado(numCuota) + porcentaje_diferencial_variable;
    }

    /** Esta funcion devuelve la cuota, capital, intereses y capital pendiente del numero de cuota pasado **/
    @Override
    public ArrayList<Double> getFilaCuadroAmortizacionMensual(int numCuota){
        ArrayList<Double> valores = new ArrayList<>();
        double porcentaje_aplicado = getPorcentajePorCuota(numCuota);

        double capPdte = numCuota == 1 ? precio_vivienda - cantidad_abonada : getCapitalPendienteTotalActual(numCuota - 1);

        double cuota = getCuotaMensual(porcentaje_aplicado, capPdte , plazo_anios * 12  - numCuota + 1);
        valores.add(cuota);
        valores.add(getCapitalAmortizadoMensual(cuota, capPdte, porcentaje_aplicado));
        valores.add(getInteresMensual(capPdte, porcentaje_aplicado));
        double capitalPdte = getCapitalPendienteTotalActual(numCuota);
        valores.add(capitalPdte);
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
        double capPdteAnterior = cuotasPagadas < 12 ? precio_vivienda - cantidad_abonada : getCapitalPendienteTotalActual(cuotasPagadas - cuotasAnuales);

        double interesesTotalesUltimo = getInteresesHastaNumPago(cuotasPagadas);
        // Capital pendiente para diciembre del año anterior
        double interesesTotalesAnterior = cuotasPagadas < 12 ? 0 : getInteresesHastaNumPago(cuotasPagadas - cuotasAnuales);

        double capitalTotal     = Math.round((capPdteAnterior - capPdteUltimo) * 100.0) / 100.0;
        double interesesTotales = Math.round((interesesTotalesUltimo - interesesTotalesAnterior) * 100.0) / 100.0;
        valores.add(Math.round((capitalTotal + interesesTotales) * 100.0) / 100.0);
        valores.add(capitalTotal);
        valores.add(interesesTotales);
        valores.add(capPdteUltimo);

        return valores;
    }

    /** Esta funcion devuelve cuantos meses quedan para la siguiente revision **/
    @Override
    public int mesesParaSiguienteRevision(int numPago){
        if (revision_anual) return numPago % 12;
        else                return numPago % 6;
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
