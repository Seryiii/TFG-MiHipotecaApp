package es.MiHipotecaApp.TFG.Transfers;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class HipotecaSeguimiento implements Serializable {

    protected String nombre;
    protected String comunidad_autonoma;
    protected String tipo_vivienda;
    protected String antiguedad_vivienda;
    protected double precio_vivienda;
    protected double cantidad_abonada;
    protected int plazo_anios;
    protected Date fecha_inicio;
    protected String tipo_hipoteca;
    protected String banco_asociado;

    //Gastos
    protected double totalGastos;
    protected List<Double> arrayVinculacionesAnual;
    protected String idUsuario;



    public HipotecaSeguimiento(String nombre) {
        this.nombre = nombre;
    }
    public HipotecaSeguimiento(String nombre, String comunidad_autonoma, String tipo_vivienda, String antiguedad_vivienda, double precio_vivienda, double cantidad_abonada, int plazo_anios, Date fecha_inicio, String tipo_hipoteca, double totalGastos, List<Double> arrayVinculacionesAnual, String banco_asociado) {
        this.nombre = nombre;
        this.comunidad_autonoma = comunidad_autonoma;
        this.tipo_vivienda = tipo_vivienda;
        this.antiguedad_vivienda = antiguedad_vivienda;
        this.precio_vivienda = precio_vivienda;
        this.cantidad_abonada = cantidad_abonada;
        this.plazo_anios = plazo_anios;
        this.fecha_inicio = fecha_inicio;
        this.tipo_hipoteca = tipo_hipoteca;
        this.totalGastos = totalGastos;
        this.arrayVinculacionesAnual = arrayVinculacionesAnual;
        this.banco_asociado = banco_asociado;
    }

    //FUNCIONES

    /** Esta funcion devuelve el plazo total actual de la hipoteca en meses/numero de cuotas en funcion de las amortizaciones
     *  anticapadas realizadas (afectarían las parciales reduciendo el plazo solo) **/
    public int getPlazoActual(HashMap<Integer, List<Object>> amortizaciones){
        int plazoTotalActual = plazo_anios * 12;
        int cuotaActual = getNumeroCuotaActual(amortizaciones);

        for (Map.Entry<Integer, List<Object>> entry: amortizaciones.entrySet()) {
            if(entry.getKey() < cuotaActual && entry.getValue().get(0).equals("parcial_plazo")){
                plazoTotalActual -= (Integer) entry.getValue().get(2); //Campo con los meses reducidos
            }
        }

        return plazoTotalActual;
    }

    /** Esta funcion devuelve la cuota mensual de una hipoteca en funcion del porcentaje aplicado
     *  y de la cantidad pendiente del prestamo **/
    public double getCuotaMensual(double porcentaje_aplicado, double cantidad_pendiente, int num_cuotas_restantes){
        if (num_cuotas_restantes <= 0) return 0;
        double aux = Math.pow((1 + (porcentaje_aplicado / 100) / 12), num_cuotas_restantes);
        double cuotaMensual = ((cantidad_pendiente) * ((porcentaje_aplicado / 100) / 12))/(1 -(1 / aux));
        return Math.round(cuotaMensual * 100.0) / 100.0;
    }

    /** Funcion que devuelve el interes pagado en funcion del capital que quede por pagar
     *  y un porcentaje aplicado **/
    public double getInteresMensual(double capitalPendiente, double porcentaje_aplicado){
        double interesesMensual = (capitalPendiente * (porcentaje_aplicado / 100) / 12);
        return Math.round(interesesMensual * 100.0) / 100.0;
    }

    /** Funcion que devuelve el capital amortizado mensual en funcion del capital pendiente y un
     *  porcentaje aplicado **/
    public double getCapitalAmortizadoMensual(double cuota_mensual, double capitalPendiente, double porcentaje_aplicado){
        double capitalAmortizadoMensual = cuota_mensual - getInteresMensual(capitalPendiente, porcentaje_aplicado);
        return Math.round(capitalAmortizadoMensual * 100.0) / 100.0;
    }

    /** Esta funcion devuelve los años y meses que quedan de hipoteca**/
    public ArrayList<Integer> getAniosMesesRestantes(HashMap<Integer, List<Object>> amortizaciones){
        ArrayList<Integer> anios_meses = new ArrayList<>();

        int cuotasRestantes = getPlazoActual(amortizaciones) - getNumeroCuotaActual(amortizaciones);
        int anios = cuotasRestantes / 12;
        int meses = cuotasRestantes % 12;
        anios_meses.add(anios);
        anios_meses.add(meses);

        return anios_meses;
    }

    public String getNombreMesActual(){
        Calendar fechaActual = Calendar.getInstance();
        Calendar inicio = Calendar.getInstance();

        inicio.setTime(fecha_inicio);
        //Comprobar si ya se ha pagado
        if (fechaActual.get(Calendar.DAY_OF_MONTH) > inicio.get(Calendar.DAY_OF_MONTH)) fechaActual.add(Calendar.MONTH, 1);
        String nombreMesActual = fechaActual.getDisplayName(Calendar.MONTH, Calendar.LONG, new Locale("es", "ES"));

        return nombreMesActual.substring(0, 1).toUpperCase() + nombreMesActual.substring(1);
    }

    /** Devuelve el numero de cuota por el que va actualmente la hipoteca [1 - plazo_anios * 12 ] **/
    public int getNumeroCuotaActual(HashMap<Integer, List<Object>> amortizaciones){
        Calendar inicio = Calendar.getInstance();
        inicio.setTime(fecha_inicio);
        Calendar actual = Calendar.getInstance(); // Dia actual

        // En caso de que todavia no haya empezado el seguimiento de la hipoteca
        if(actual.compareTo(inicio) < 0) return 0;
        int difA = actual.get(Calendar.YEAR) - inicio.get(Calendar.YEAR);
        int numeroPagoActual = difA * 12 + actual.get(Calendar.MONTH) - inicio.get(Calendar.MONTH);

        // Si el dia es el mismo que el de pago, devuelve como si ya ha pagado esa cuota
        if(actual.get(Calendar.DAY_OF_MONTH) >= inicio.get(Calendar.DAY_OF_MONTH)) numeroPagoActual = numeroPagoActual + 1; //Se le sumaria 1 debido a que ya ha pasado el dia de pago del mes correspondiente
        //else return numeroPagoActual + 1;
        // Fin de hipoteca
        if (numeroPagoActual >= getPlazoActual(amortizaciones)) numeroPagoActual = getPlazoActual(amortizaciones);

        return numeroPagoActual;
    }

    /** Funcion que devuelve el numero de cuota de la hipoteca en enero del anio pasado por parametros **/
    public int getNumeroCuotaEnEnero(int anio){
        Calendar inicio = Calendar.getInstance();
        inicio.setTime(fecha_inicio);
        // 1 de Enero del anio dado por parametros
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, anio);
        cal.set(Calendar.MONTH, 0);
        cal.set(Calendar.DAY_OF_MONTH, 1);

        // En caso de que todavia no haya empezado el seguimiento de la hipoteca
        int difA = cal.get(Calendar.YEAR) - inicio.get(Calendar.YEAR);
        int numeroPagoActual = difA * 12 + cal.get(Calendar.MONTH) - inicio.get(Calendar.MONTH);

        // Si el dia es el mismo que el de pago, devuelve como si ya ha pagado esa cuota
        if(cal.get(Calendar.DAY_OF_MONTH) >= inicio.get(Calendar.DAY_OF_MONTH)) numeroPagoActual = numeroPagoActual + 1; //Se le sumaria 1 debido a que ya ha pasado el dia de pago del mes correspondiente

        return numeroPagoActual;
    }

    /** Devuelve la cantidad que el usuario deberá de amortizar en funcion de los meses pasados **/
    public double getAmortizarAlReducirMeses(int meses_reducir, HashMap<Integer, List<Object>> amortizaciones){
        double cantAmortizar = 0;
        int plazoReducido = getPlazoActual(amortizaciones) - meses_reducir;

        for(int i = plazoReducido; i < plazoReducido + meses_reducir; i++){
            cantAmortizar += getCapitalDeUnaCuota(i + 1,amortizaciones);
        }

        return Math.round(cantAmortizar * 100.0) / 100.0;
    }

    //FUNCIONES SOBREESCRITAS
    public double getCapitalPendienteTotalActual(int numero_pago, HashMap<Integer, List<Object>> amortizaciones){
        return 0;
    }

    public double getInteresesHastaNumPago(int numero_pago, HashMap<Integer, List<Object>> amortizaciones){ return 0; }
    public boolean siguienteCuotaRevision(HashMap<Integer, List<Object>> amortizaciones){ return false; }

    public ArrayList<Double> getFilaCuadroAmortizacionMensual(int numCuota, HashMap<Integer, List<Object>> amortizaciones){ return null; }
    public double getCapitalDeUnaCuota(int numCuota, HashMap<Integer, List<Object>> amortizaciones){ return 0;}
    public ArrayList<Double> getFilaCuadroAmortizacionAnual(int anio, int num_anio, HashMap<Integer, List<Object>> amortizaciones){ return null; }

    public double getPorcentajePorCuota(int numCuota){ return 0; }

    //GETTERS Y SETTERS SOBREESCRITOS
    public double getPorcentaje_fijo() {
        return 0;
    }

    public int getDuracion_primer_porcentaje_variable(){ return 0; }

    public double getPrimer_porcentaje_variable(){ return 0; }

    public double getPorcentaje_diferencial_variable(){ return 0; }

    public boolean isRevision_anual() { return false; }

    public int getAnios_fija_mixta() {
        return 0;
    }

    public double getPorcentaje_fijo_mixta() {
        return 0f;
    }

    public double getPorcentaje_diferencial_mixta() {
        return 0;
    }

    //GETTERS
    public String getNombre() {
        return nombre;
    }

    public String getComunidad_autonoma() {
        return comunidad_autonoma;
    }

    public String getTipo_vivienda() {
        return tipo_vivienda;
    }

    public String getAntiguedad_vivienda() {
        return antiguedad_vivienda;
    }

    public double getPrecio_vivienda() {
        return precio_vivienda;
    }

    public double getCantidad_abonada() {
        return cantidad_abonada;
    }

    public int getPlazo_anios() {
        return plazo_anios;
    }

    public Date getFecha_inicio() { return fecha_inicio; }

    public String getTipo_hipoteca() {
        return tipo_hipoteca;
    }

    public double getTotalGastos() {
        return totalGastos;
    }

    public List<Double> getArrayVinculacionesAnual(){ return arrayVinculacionesAnual; }

    public Double getPosArrayVinculacionesAnual(int i) {
        return arrayVinculacionesAnual.get(i);
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public String getBanco_asociado() {
        return banco_asociado;
    }

    //SETTERS
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setComunidad_autonoma(String comunidad_autonoma) {
        this.comunidad_autonoma = comunidad_autonoma;
    }

    public void setTipo_vivienda(String tipo_vivienda) {
        this.tipo_vivienda = tipo_vivienda;
    }

    public void setAntiguedad_vivienda(String antiguedad_vivienda) {
        this.antiguedad_vivienda = antiguedad_vivienda;
    }

    public void setPrecio_vivienda(double precio_vivienda) {
        this.precio_vivienda = precio_vivienda;
    }

    public void setCantidad_abonada(double cantidad_abonada) {
        this.cantidad_abonada = cantidad_abonada;
    }

    public void setPlazo_anios(int plazo_anios) {
        this.plazo_anios = plazo_anios;
    }

    public void setFecha_inicio(Date fecha_inicio) {
        this.fecha_inicio = fecha_inicio;
    }

    public void setTipo_hipoteca(String tipo_hipoteca) {
        this.tipo_hipoteca = tipo_hipoteca;
    }

    public void setTotalGastos(double totalGastos) {
        this.totalGastos = totalGastos;
    }

    public void setPosArrayVinculacionesAnual(int i, double vinculaciones) {
        this.arrayVinculacionesAnual.set(i, vinculaciones);
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public void setBanco_asociado(String banco_asociado) {
        this.banco_asociado = banco_asociado;
    }

    public double getDineroRestanteActual(int i, HashMap<Integer, List<Object>> amortizaciones) { return 0;}


    /** FUNCIONES DE PRUEBA PARA HACER MAS FUNCIONALIDADES, CAMBIAR POR WEB SCRAPING O SIMILARES*/
    public double getEuriborActual(){ return 3.018; }

    //Calcula el euribor que hubo correspondiente a un numero de pago

    public double getEuriborPasado(int numPago){

        //Si el numero de pago no se ha hecho devolver euribor actual
        return 3.018;
    }
}
