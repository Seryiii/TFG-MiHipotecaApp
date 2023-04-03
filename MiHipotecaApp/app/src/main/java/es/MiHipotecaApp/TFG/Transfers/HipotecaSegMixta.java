package es.MiHipotecaApp.TFG.Transfers;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class HipotecaSegMixta extends HipotecaSeguimiento implements Serializable {

    //Hipoteca mixta
    private int anios_fija_mixta;
    private double porcentaje_fijo_mixta;
    private double porcentaje_diferencial_mixta;
    private boolean revision_anual;

    public HipotecaSegMixta(String nombre, String comunidad_autonoma, String tipo_vivienda, String antiguedad_vivienda, double precio_vivienda, double cantidad_abonada, int plazo_anios, Date fecha_inicio, String tipo_hipoteca, double totalGastos, double totalVinculacionesAnual, String banco_asociado, int anios_fija_mixta, double porcentaje_fijo_mixta, double porcentaje_diferencial_mixta, boolean revision_anual) {
        super(nombre, comunidad_autonoma, tipo_vivienda, antiguedad_vivienda, precio_vivienda, cantidad_abonada, plazo_anios, fecha_inicio, tipo_hipoteca, totalGastos, totalVinculacionesAnual, banco_asociado);
        this.anios_fija_mixta = anios_fija_mixta;
        this.porcentaje_fijo_mixta = porcentaje_fijo_mixta;
        this.porcentaje_diferencial_mixta = porcentaje_diferencial_mixta;
        this.revision_anual = revision_anual;
    }

    /** Esta funcion devuelve el capital pendiente total por amortizar**/
    @Override
    public double getCapitalPendienteTotalActual(int numero_pago){
        double capital_pendiente = precio_vivienda - cantidad_abonada;
        double cuota_mensual = getCuotaMensual(porcentaje_fijo_mixta, capital_pendiente, plazo_anios * 12);
        double cantidad_capital;
        int aux = numero_pago > anios_fija_mixta * 12 ? anios_fija_mixta * 12 : numero_pago;
        for (int i = 1; i <= aux; i++){
            cantidad_capital = getCapitalAmortizadoMensual(cuota_mensual, capital_pendiente, porcentaje_fijo_mixta);
            capital_pendiente = capital_pendiente - cantidad_capital;
            Log.i("Pago " + i + ": ", "CANT PDTE: " + capital_pendiente + "     CAPITAL MENSUAL: " + cantidad_capital + "     CUOTA MENSUAL: " + cuota_mensual);
        }

        int j = aux;
        int revision = 6;
        while(j < numero_pago){
            if(isRevision_anual()) revision = 12;
            double euribor = getEuriborPasado(j);
            cuota_mensual = getCuotaMensual(porcentaje_diferencial_mixta + euribor, capital_pendiente, (plazo_anios * 12) - j);
            for(int h = 0; h < revision && j < numero_pago; h++){
                cantidad_capital = getCapitalAmortizadoMensual(cuota_mensual, capital_pendiente, porcentaje_diferencial_mixta + euribor);
                capital_pendiente = capital_pendiente - cantidad_capital;
                Log.i("Pago " + Integer.toString(j + 1) + ": ", "CANT PDTE: " + capital_pendiente + "     CAPITAL MENSUAL: " + cantidad_capital + "     CUOTA MENSUAL: " + cuota_mensual);
                j++;
            }
        }
        return Math.round(capital_pendiente * 100.0) / 100.0;
    }

    /** Esta funcion devuelve el capital pendiente total por amortizar**/
    @Override
    public double getInteresesTotales(int numero_pago){
        double intereses_totales = 0;
        double capital_pendiente = precio_vivienda - cantidad_abonada;
        double cuota_mensual = getCuotaMensual(porcentaje_fijo_mixta, capital_pendiente, plazo_anios * 12);
        double cantidad_capital;
        int aux = numero_pago > anios_fija_mixta * 12 ? anios_fija_mixta * 12 : numero_pago;
        for (int i = 1; i <= aux; i++){
            intereses_totales += getInteresMensual(capital_pendiente, porcentaje_fijo_mixta);
            cantidad_capital = getCapitalAmortizadoMensual(cuota_mensual, capital_pendiente, porcentaje_fijo_mixta);
            capital_pendiente = capital_pendiente - cantidad_capital;
        }

        int j = aux;
        int revision = 6;
        while(j < numero_pago){
            if(isRevision_anual()) revision = 12;
            double euribor = getEuriborPasado(j);
            cuota_mensual = getCuotaMensual(porcentaje_diferencial_mixta + euribor, capital_pendiente, (plazo_anios * 12) - j);
            for(int h = 0; h < revision && j < numero_pago; h++){
                intereses_totales += getInteresMensual(capital_pendiente, porcentaje_diferencial_mixta + euribor);
                cantidad_capital = getCapitalAmortizadoMensual(cuota_mensual, capital_pendiente, porcentaje_diferencial_mixta + euribor);
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

        // Si estas en la fase fija, tienes que acabar la fase fija y luego estimar con el euribor actual
        // Si estas en la fase variable, simular lo que queda en funcion del euribor actual

        double capital_pendiente = getCapitalPendienteTotalActual(numPago);
        double porcentaje_aplicado  = numPago <= (anios_fija_mixta * 12) ? porcentaje_fijo_mixta : getEuriborActual() + porcentaje_diferencial_mixta;
        double cuota_mensual = getCuotaMensual(porcentaje_aplicado, capital_pendiente, plazo_anios * 12 - getNumeroCuotaActual());
        if(numPago <= anios_fija_mixta * 12){
            int aux = (anios_fija_mixta * 12) - numPago;
            // se inicializa el dinero restante con lo que queda por pagar del primer porcentaje
            double dinero_restante = cuota_mensual * aux;
            double capital_pendiente_restante = getCapitalPendienteTotalActual(anios_fija_mixta * 12);
            // Sumar lo que queda con el euribor actual + diferencial
            cuota_mensual = getCuotaMensual(getEuriborActual() + porcentaje_diferencial_mixta, capital_pendiente_restante, (plazo_anios * 12) - (anios_fija_mixta * 12));
            dinero_restante = dinero_restante + (cuota_mensual * ((plazo_anios * 12) -(anios_fija_mixta * 12)));
            return dinero_restante;
        }
        else return cuota_mensual * (plazo_anios * 12 - numPago);
    }

    /** Esta funcion devuelve si en el siguiente pago toca revision **/
    @Override
    public boolean siguienteCuotaRevision(){

        int numCuotasPagadas = getNumeroCuotaActual();
        if (numCuotasPagadas < anios_fija_mixta * 12) return false;
        if (numCuotasPagadas == anios_fija_mixta * 12) return true;
        if ((revision_anual  && (numCuotasPagadas % 12 == 0)) || (!revision_anual && (numCuotasPagadas % 6  == 0))) return true;
        return false;
    }

    /** Esta funcion devuelve el porcentaje que se aplica para un determinado numero de cuota**/
    @Override
    public double getPorcentajePorCuota(int numCuota){
        if (numCuota <= anios_fija_mixta * 12) return porcentaje_fijo_mixta;
        return getEuriborPasado(numCuota) + porcentaje_diferencial_mixta;
    }

    /** Esta funcion devuelve la cuota, capital, intereses y capital pendiente del numero de cuota pasado **/
    @Override
    public ArrayList<Double> getFilaCuadroAmortizacionMensual(int numCuota){
        ArrayList<Double> valores = new ArrayList<>();
        double porcentaje_aplicado = getPorcentajePorCuota(numCuota);

        double capPdte = numCuota == 1 ? precio_vivienda - cantidad_abonada : getCapitalPendienteTotalActual(numCuota - 1);

        double capitalPdte = getCapitalPendienteTotalActual(numCuota);
        double cuota = getCuotaMensual(porcentaje_aplicado, capPdte , plazo_anios * 12  - numCuota + 1);
        valores.add(cuota);
        valores.add(getCapitalAmortizadoMensual(cuota, capPdte, porcentaje_aplicado));
        valores.add(getInteresMensual(capPdte, porcentaje_aplicado));
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

        double interesesTotalesUltimo = getInteresesTotales(cuotasPagadas);
        // Capital pendiente para diciembre del año anterior
        double interesesTotalesAnterior = cuotasPagadas < 12 ? 0 : getInteresesTotales(cuotasPagadas - cuotasAnuales);

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

    public int getAnios_fija_mixta() {
        return anios_fija_mixta;
    }

    public double getPorcentaje_fijo_mixta() {
        return porcentaje_fijo_mixta;
    }

    public double getPorcentaje_diferencial_mixta() {
        return porcentaje_diferencial_mixta;
    }

    public boolean isRevision_anual() {
        return revision_anual;
    }

    public void setAnios_fija_mixta(int anios_fija_mixta) {
        this.anios_fija_mixta = anios_fija_mixta;
    }

    public void setPorcentaje_fijo_mixta(double porcentaje_fijo_mixta) {
        this.porcentaje_fijo_mixta = porcentaje_fijo_mixta;
    }

    public void setPorcentaje_diferencial_mixta(double porcentaje_diferencial_mixta) {
        this.porcentaje_diferencial_mixta = porcentaje_diferencial_mixta;
    }

    public void setRevision_anual(boolean revision_anual) {
        this.revision_anual = revision_anual;
    }
}
