package es.MiHipotecaApp.TFG.Transfers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
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
    public double getCapitalPendienteTotalActual(int numero_pago, HashMap<Integer, List<Object>> amortizaciones, List<Double> euribors){
        double capital_pendiente = precio_vivienda - cantidad_abonada;
        int plazoActual = plazo_anios * 12;
        double cuota_mensual = getCuotaMensual(primer_porcentaje_variable, capital_pendiente, plazoActual);
        double cantidad_capital;

        //todo PUEDE QUE NO COMPROBEMOS MAS ALLA DEL ULTIMO PAGO, EJ PAGO 301 SI PLAZO 300
        int aux = numero_pago > duracion_primer_porcentaje_variable ? duracion_primer_porcentaje_variable : numero_pago;
        for (int i = 1; i <= aux; i++){
            if(amortizaciones.containsKey(i)){
                if(amortizaciones.get(i).get(0).equals("total")) return 0;
                else if (amortizaciones.get(i).get(0).equals("parcial_cuota")){
                    capital_pendiente -= (Double) amortizaciones.get(i).get(1);
                    cuota_mensual = getCuotaMensual(primer_porcentaje_variable, capital_pendiente, plazoActual);
                }
                else {
                    capital_pendiente -= (Double) amortizaciones.get(i).get(1);
                    plazoActual -= (Long) amortizaciones.get(i).get(2);
                }
            }
            cantidad_capital = getCapitalAmortizadoMensual(cuota_mensual, capital_pendiente, primer_porcentaje_variable);
            capital_pendiente = capital_pendiente - cantidad_capital;

        }

        int j = aux;
        while(j < numero_pago){
            double euribor = getEuriborPasado(j + 1, euribors);
            cuota_mensual = getCuotaMensual(porcentaje_diferencial_variable + euribor, capital_pendiente, plazoActual - j);

            if(amortizaciones.containsKey(j)){
                if(amortizaciones.get(j).get(0).equals("total")) return 0;
                else if (amortizaciones.get(j).get(0).equals("parcial_cuota")){
                    capital_pendiente -= (Double) amortizaciones.get(j).get(1);
                    cuota_mensual = getCuotaMensual(porcentaje_diferencial_variable + euribor, capital_pendiente, plazoActual);
                }
                else {
                    capital_pendiente -= (Double) amortizaciones.get(j).get(1);
                    plazoActual -= (Long) amortizaciones.get(j).get(2);
                }
            }

            cantidad_capital = getCapitalAmortizadoMensual(cuota_mensual, capital_pendiente, porcentaje_diferencial_variable + euribor);
            capital_pendiente = capital_pendiente - cantidad_capital;
            j++;

        }

        return capital_pendiente;
    }


    /** Esta funcion devuelve la cantidad de intereses hasta el numero de pago pasado**/
    @Override
    public double getInteresesHastaNumPago(int numero_pago, HashMap<Integer, List<Object>> amortizaciones, List<Double> euribors){
        double intereses_totales = 0;
        double capital_pendiente = precio_vivienda - cantidad_abonada;
        double cuota_mensual = getCuotaMensual(primer_porcentaje_variable, capital_pendiente, plazo_anios * 12);
        double cantidad_capital;
        int plazoActual = plazo_anios * 12;

        int aux = numero_pago > duracion_primer_porcentaje_variable ? duracion_primer_porcentaje_variable : numero_pago;
        for (int i = 1; i <= aux; i++){
            if(amortizaciones.containsKey(i)){
                if(amortizaciones.get(i).get(0).equals("total")) return 0;
                else if (amortizaciones.get(i).get(0).equals("parcial_cuota")){
                    capital_pendiente -= (Double) amortizaciones.get(i).get(1);
                    cuota_mensual = getCuotaMensual(primer_porcentaje_variable, capital_pendiente, plazoActual);
                }
                else {
                    capital_pendiente -= (Double) amortizaciones.get(i).get(1);
                    plazoActual -= (Long) amortizaciones.get(i).get(2);
                }
            }
            intereses_totales += getInteresMensual(capital_pendiente, primer_porcentaje_variable);
            cantidad_capital = getCapitalAmortizadoMensual(cuota_mensual, capital_pendiente, primer_porcentaje_variable);
            capital_pendiente = capital_pendiente - cantidad_capital;
        }

        int j = aux;
        while(j < numero_pago){
            double euribor = getEuriborPasado(j + 1, euribors);
            cuota_mensual = getCuotaMensual(porcentaje_diferencial_variable + euribor, capital_pendiente,  plazoActual - j);
            if(amortizaciones.containsKey(j)){
                if(amortizaciones.get(j).get(0).equals("total")) return 0;
                else if (amortizaciones.get(j).get(0).equals("parcial_cuota")){
                    capital_pendiente -= (Double) amortizaciones.get(j).get(1);
                    cuota_mensual = getCuotaMensual(porcentaje_diferencial_variable + euribor, capital_pendiente, plazoActual);
                }
                else {
                    capital_pendiente -= (Double) amortizaciones.get(j).get(1);
                    plazoActual -= (Long) amortizaciones.get(j).get(2);
                }
            }

            intereses_totales += getInteresMensual(capital_pendiente, porcentaje_diferencial_variable + euribor);
            cantidad_capital = getCapitalAmortizadoMensual(cuota_mensual, capital_pendiente, porcentaje_diferencial_variable + euribor);
            capital_pendiente = capital_pendiente - cantidad_capital;
            j++;
        }

        return intereses_totales;
    }


    /** Esta funcion devuelve el capital y los intereses pendientes por pagar, simulando que el euribor se mantiene fijo
     *  durante los años restantes. (Se utiliza el euribor del mes actual) **/
    @Override
    public double getDineroRestanteActual(int numPago, HashMap<Integer, List<Object>> amortizaciones, List<Double> euribors){

        // Si estas en primer porcentaje, tienes que acabar la fase fija y luego estimar con el euribor actual
        // Si estas en la fase variable, simular lo que queda en funcion del euribor actual

        int cuotasRestantes = getPlazoActual(amortizaciones) - numPago;
        double capital_pendiente = getCapitalPendienteTotalActual(numPago, amortizaciones, euribors);
        double porcentaje_aplicado  = numPago < duracion_primer_porcentaje_variable ? primer_porcentaje_variable : getEuriborActual(euribors) + porcentaje_diferencial_variable;
        double cuota_mensual = getCuotaMensual(porcentaje_aplicado, capital_pendiente, cuotasRestantes);

        //ESTAS EN EL PRIMER PORCENTAJE
        double dinero_restante = 0;

        if(numPago < duracion_primer_porcentaje_variable){
            int cuotas_pdte_primer_porcentaje = duracion_primer_porcentaje_variable - numPago;
            dinero_restante = cuota_mensual * cuotas_pdte_primer_porcentaje;
            porcentaje_aplicado = getEuriborActual(euribors) + porcentaje_diferencial_variable;
            cuotasRestantes = cuotasRestantes - cuotas_pdte_primer_porcentaje;
        }
        // coger euribor actual
        cuota_mensual = getCuotaMensual(porcentaje_aplicado, capital_pendiente, cuotasRestantes);
        dinero_restante += cuota_mensual * cuotasRestantes;
        return dinero_restante;
    }

    /** Esta funcion devuelve si en el siguiente pago toca revision **/
    @Override
    public boolean siguienteCuotaRevision(HashMap<Integer, List<Object>> amortizaciones){

        int numCuotasPagadas = getNumeroCuotaActual(amortizaciones);
        if (numCuotasPagadas < duracion_primer_porcentaje_variable) return false;
        if (numCuotasPagadas == duracion_primer_porcentaje_variable) return true;
        if ((revision_anual  && (numCuotasPagadas % 12 == 0)) || (!revision_anual && (numCuotasPagadas % 6  == 0))) return true;
        return false;
    }

    /** Esta funcion devuelve el porcentaje que se aplica para un determinado numero de cuota**/
    @Override
    public double getPorcentajePorCuota(int numCuota, HashMap<Integer, List<Object>> amortizaciones, List<Double> euribors){
        if (numCuota <= duracion_primer_porcentaje_variable) return primer_porcentaje_variable;
        return getEuriborPasado(numCuota, euribors) + porcentaje_diferencial_variable;
    }

    /** Esta funcion devuelve la cuota, capital, intereses y capital pendiente del numero de cuota pasado **/
    @Override
    public ArrayList<Double> getFilaCuadroAmortizacionMensual(int numCuota, HashMap<Integer, List<Object>> amortizaciones, List<Double> euribors){
        ArrayList<Double> valores = new ArrayList<>();

        double porcentaje_aplicado = getPorcentajePorCuota(numCuota, amortizaciones, euribors);
        double capPdteCuota = getCapitalPendienteTotalActual(numCuota, amortizaciones, euribors);
        double capPdte = numCuota == 0 ? precio_vivienda - cantidad_abonada : getCapitalPendienteTotalActual(numCuota - 1, amortizaciones, euribors);
        double cuota = cogerCuotaActual(numCuota, amortizaciones, euribors);
        double capAmortMensual = getCapitalAmortizadoMensual(cuota, capPdte, porcentaje_aplicado);

        if(amortizaciones.containsKey(numCuota)){
            capPdte -= (Double) amortizaciones.get(numCuota).get(1);
            cuota += (Double) amortizaciones.get(numCuota).get(1);
            capAmortMensual += (Double) amortizaciones.get(numCuota).get(1);
            if(amortizaciones.get(numCuota).get(0).equals("total")){
                cuota = (Double) amortizaciones.get(numCuota).get(1);
                capAmortMensual = (Double) amortizaciones.get(numCuota).get(1);
            }
        }

        valores.add(cuota);
        valores.add(capAmortMensual);
        valores.add(getInteresMensual(capPdte, porcentaje_aplicado));
        valores.add(capPdteCuota);
        return valores;
    }

    /** Esta funcion devuelve el capital del numero de cuota pasado **/
    @Override
    public double getCapitalDeUnaCuota(int numCuota, HashMap<Integer, List<Object>> amortizaciones, List<Double> euribors){
        double porcentaje_aplicado = getPorcentajePorCuota(numCuota, amortizaciones, euribors);
        double capPdte = numCuota == 1 ? precio_vivienda - cantidad_abonada : getCapitalPendienteTotalActual(numCuota - 1, amortizaciones, euribors);

        double cuota   = getCuotaMensual(porcentaje_aplicado, capPdte , plazo_anios * 12  - numCuota + 1);
        double capitalCuota = getCapitalAmortizadoMensual(cuota, capPdte, porcentaje_aplicado);

        return capitalCuota;
    }

    //TODO esta funcion
    @Override
    public double cogerCuotaActual(int num_cuota, HashMap<Integer, List<Object>> amortizaciones, List<Double> euribors){

        // si el numero de cuota es mayor al plazo actual la cuota es 0
        if (num_cuota > getPlazoActual(amortizaciones)) return 0;

        double capital_pendiente = precio_vivienda - cantidad_abonada;
        double cuota = getCuotaMensual(primer_porcentaje_variable, capital_pendiente, plazo_anios * 12);


        // estoy en el primer porcentaje
        if(num_cuota <= duracion_primer_porcentaje_variable){
            //Ver si hay reducción de cuota
            for (int i = 1; i <= num_cuota; i++) {
                if(amortizaciones.containsKey(i)){
                    if(amortizaciones.get(i).get(0).equals("total")) return 0;
                    else if (amortizaciones.get(i).get(0).equals("parcial_cuota")){
                        capital_pendiente -= (Double) amortizaciones.get(i).get(1);
                        cuota = getCuotaMensual(primer_porcentaje_variable, capital_pendiente, getPlazoNumPago(i, amortizaciones)  - i + 1);
                        //TODO CREO QUE LO DE ARRIBA ESTARIA BIEN y LA DE ABAJO ES LO QUE TENIAMOS ANTES (REVISAR LINEA DE ARRIBA POR SI PONER getPlazoNumPago o getPlazoActual )
                        //cuota = getCuotaMensual(primer_porcentaje_variable, capital_pendiente, (plazo_anios * 12) - i + 1);
                    }
                    // Si hay reducción de plazo da igual porque la cuota es la misma
                }
                double cantidad_capital = getCapitalAmortizadoMensual(cuota, capital_pendiente, primer_porcentaje_variable);
                capital_pendiente = capital_pendiente - cantidad_capital;
            }
        }
        //caso cuota esta en la parte variable
        else{
            int revision = isRevision_anual() ? 12 : 6;
            int numCuotaRevisionAnterior = 1;
            if (num_cuota % revision == 0)  numCuotaRevisionAnterior += num_cuota - revision;
            else numCuotaRevisionAnterior += num_cuota - num_cuota % revision;

            capital_pendiente = getCapitalPendienteTotalActual(numCuotaRevisionAnterior - 1, amortizaciones, euribors);
            double porcentaje_aplicado = getEuriborPasado(num_cuota, euribors) + porcentaje_diferencial_variable;
            cuota = getCuotaMensual(porcentaje_aplicado, capital_pendiente, getPlazoNumPago(numCuotaRevisionAnterior, amortizaciones) - numCuotaRevisionAnterior + 1);

            //Comprobar si hay reduccion de cuota
            for (int i = numCuotaRevisionAnterior; i <= num_cuota; i++) {
                if(amortizaciones.containsKey(i)){
                    if(amortizaciones.get(i).get(0).equals("total")) return 0;
                    else if (amortizaciones.get(i).get(0).equals("parcial_cuota")){
                        capital_pendiente -= (Double) amortizaciones.get(i).get(1);
                        porcentaje_aplicado = getEuriborPasado(i, euribors) + porcentaje_diferencial_variable;

                        cuota = getCuotaMensual(porcentaje_aplicado, capital_pendiente, getPlazoNumPago(i, amortizaciones)  - i + 1);
                        //TODO CREO QUE LO DE ARRIBA ESTARIA BIEN y LA DE ABAJO ES LO QUE TENIAMOS ANTES (REVISAR LINEA DE ARRIBA POR SI PONER getPlazoNumPago o getPlazoActual )
                        //cuota = getCuotaMensual(porcentaje_aplicado, capital_pendiente, (plazo_anios * 12) - i + 1);
                    }
                    // Si hay reducción de plazo da igual porque la cuota es la misma
                }
                double cantidad_capital = getCapitalAmortizadoMensual(cuota, capital_pendiente, primer_porcentaje_variable);
                capital_pendiente = capital_pendiente - cantidad_capital;
            }
        }
        return cuota;
    }


    /** Esta funcion devuelve el total anual, capital anual, intereses anuales y capital pendiente del numero de anio pasado**/
    @Override
    public ArrayList<Double> getFilaCuadroAmortizacionAnual(int anio, int num_anio, HashMap<Integer, List<Object>> amortizaciones, List<Double> euribors){

        ArrayList<Double> valores = new ArrayList<>();
        Calendar inicio = Calendar.getInstance();
        inicio.setTime(fecha_inicio);
        int cuotasAnuales;
        boolean ultimoAnio = false;

        //si es el primer año de hipoteca
        if(inicio.get(Calendar.YEAR) == anio) cuotasAnuales = 12 + (getNumeroCuotaEnEnero(anio) - 1);
        else if(inicio.get(Calendar.YEAR) + (int) Math.ceil(getPlazoActual(amortizaciones) / 12) == anio) {
            cuotasAnuales = getPlazoActual(amortizaciones) - (getNumeroCuotaEnEnero(anio) - 1);
            ultimoAnio = true;
        }else cuotasAnuales = 12;


        int cuotasPrimerAnio = (getNumeroCuotaEnEnero(inicio.get(Calendar.YEAR)) + 12) - 1;

        int cuotasPagadas = num_anio > 1 ?  cuotasPrimerAnio + (num_anio - 2) * 12 + cuotasAnuales : cuotasAnuales;

        // Capital pendiente para diciembre de este año
        double capPdteUltimo = ultimoAnio ? 0 : getCapitalPendienteTotalActual(cuotasPagadas, amortizaciones, euribors);
        // Capital pendiente para diciembre del año anterior
        double capPdteAnterior = cuotasPagadas < 12 ? precio_vivienda - cantidad_abonada : getCapitalPendienteTotalActual(cuotasPagadas - cuotasAnuales, amortizaciones, euribors);

        double totalCapitalAnual = capPdteAnterior - capPdteUltimo;

        double interesesAnteriores = cuotasAnuales < 12 && !ultimoAnio ? 0 : getInteresesHastaNumPago(cuotasPagadas - cuotasAnuales, amortizaciones, euribors);
        double interesesSiguientes = getInteresesHastaNumPago(cuotasPagadas, amortizaciones, euribors);
        double totalInteresesAnio = interesesSiguientes - interesesAnteriores;

        valores.add(totalCapitalAnual + totalInteresesAnio);
        valores.add(totalCapitalAnual);
        valores.add(totalInteresesAnio);
        valores.add(capPdteUltimo);

        return valores;
    }

    /** Devuelve el porcentaje aplicado en la ultimaCuota **/
    @Override
    public double getPorcentajeUltimaCuota(HashMap<Integer, List<Object>> amortizaciones, List<Double> euribors){
        int ultimaCuota = getPlazoActual(amortizaciones);
        if(ultimaCuota <= duracion_primer_porcentaje_variable) return primer_porcentaje_variable;
        else return porcentaje_diferencial_variable + getEuriborPasado(ultimaCuota, euribors);
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
