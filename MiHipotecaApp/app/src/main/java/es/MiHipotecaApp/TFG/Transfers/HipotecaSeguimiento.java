package es.MiHipotecaApp.TFG.Transfers;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

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
    protected double totalVinculacionesAnual;
    protected String idUsuario;

    public HipotecaSeguimiento(String nombre) {
        this.nombre = nombre;
    }
    public HipotecaSeguimiento(String nombre, String comunidad_autonoma, String tipo_vivienda, String antiguedad_vivienda, double precio_vivienda, double cantidad_abonada, int plazo_anios, Date fecha_inicio, String tipo_hipoteca, double totalGastos, double totalVinculacionesAnual, String banco_asociado) {
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
        this.totalVinculacionesAnual = totalVinculacionesAnual;
        this.banco_asociado = banco_asociado;
    }

    //FUNCIONES

    /** Esta funcion devuelve la cuota mensual de una hipoteca en funcion del porcentaje aplicado
     *  y de la cantidad pendiente del prestamo **/
    public double getCuotaMensual(double porcentaje_aplicado, double cantidad_pendiente){
        double aux = Math.pow((1 + (porcentaje_aplicado / 100) / 12), plazo_anios * 12);
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

    /** Funcion que devuelve el interes pagado de un determinado numero de pago **/
    /*public double calcularInteresMensualConNumeroMes(int numPago){
        double capitalPendiente = super.obtenerCuotaMensual(porcentaje_fijo, precio_vivienda - cantidad_abonada)  * numPago; //MAAAAAL
        return capitalPendiente * (porcentaje_fijo / 100) / 12;
    }*/

    /** Esta funcion devuelve los intereses en un plazo pasandole el capital pendiente, el numero de cuotas, y el
     *  porcentaje aplicado**/
    public double getInteresesPlazo(double capital_pendiente, int numeroCuotas, double porcentaje_aplicado){
        double interesesTotales = 0;

        for(int i = 0; i < numeroCuotas; i++){
            interesesTotales += getInteresMensual(capital_pendiente, porcentaje_aplicado);
            capital_pendiente -= (getCuotaMensual(porcentaje_aplicado, capital_pendiente) - getInteresMensual(capital_pendiente, porcentaje_aplicado));
        }
        return interesesTotales;
    }

    public int getAniosRestantes(){
        Calendar inicio = Calendar.getInstance();
        inicio.setTime(fecha_inicio);
        // Dia actual
        Calendar actual = Calendar.getInstance();
        // Calcula la diferencia de años
        int diferencia = actual.get(Calendar.YEAR) - inicio.get(Calendar.YEAR);
        // Si la diferencia de años es 0, no hay que restar nada
        // Si el año del dia de la fecha que yo paso es mayor que la actual, resto uno
        if (diferencia != 0 && (inicio.get(Calendar.DAY_OF_YEAR) > actual.get(Calendar.DAY_OF_YEAR))) diferencia--;

        return plazo_anios - diferencia;
    }


    public String getNombreMesActual(){
        Calendar fechaActual = Calendar.getInstance();
        String nombreMesActual = fechaActual.getDisplayName(Calendar.MONTH, Calendar.LONG, new Locale("es", "ES"));
        return nombreMesActual.substring(0, 1).toUpperCase() + nombreMesActual.substring(1);
    }

    /** Devuelve el numero de cuota por el que va actualmente la hipoteca [1 - plazo_anios * 12 ] **/
    public int getNumeroCuotaActual(){
        Calendar inicio = Calendar.getInstance();
        inicio.setTime(fecha_inicio);
        // Dia actual
        Calendar actual = Calendar.getInstance();
        int difA = actual.get(Calendar.YEAR) - inicio.get(Calendar.YEAR);
        int numeroPagoActual = difA * 12 + actual.get(Calendar.MONTH) - inicio.get(Calendar.MONTH);
        // Se le suma 1 porque se asume que el primer pago es el 1 y no el 0
        return numeroPagoActual + 1;
    }


    //FUNCIONES SOBREESCRITAS
    public double getCapitalPendienteTotalActual(int numero_pago){
        return 0;
    }

    //GETTERS Y SETTERS SOBREESCRITOS
    public double getPorcentaje_fijo() {
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

    public double getTotalVinculacionesAnual() {
        return totalVinculacionesAnual;
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

    public void setTotalVinculacionesAnual(double totalVinculacionesAnual) {
        this.totalVinculacionesAnual = totalVinculacionesAnual;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public void setBanco_asociado(String banco_asociado) {
        this.banco_asociado = banco_asociado;
    }

    public double getDineroAportadoActual(int i) { return 0;}

    public double getDineroRestanteActual(int i) { return 0;}

    public double getInteresesTotales() { return 0;}

    public double getGastosTotalesHipoteca() { return 0;}
}
