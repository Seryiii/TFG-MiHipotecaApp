package es.MiHipotecaApp.TFG.Transfers;

import java.io.Serializable;
import java.util.Date;

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
        return ((cantidad_pendiente) * ((porcentaje_aplicado / 100) / 12))/(1 -(1 / aux));
    }

    /** Funcion que devuelve el interes pagado en funcion del capital que quede por pagar
     *  y un porcentaje aplicado **/
    public double getInteresMensual(double capitalPendiente, double porcentaje_aplicado){
        return capitalPendiente * (porcentaje_aplicado / 100) / 12;
    }

    /** Funcion que devuelve el capital amortizado mensual en funcion del capital pendiente y un
     *  porcentaje aplicado **/
    public double getCapitalAmortizadoMensual(double capitalPendiente, double porcentaje_aplicado){
        return getCuotaMensual(porcentaje_aplicado, capitalPendiente) - getInteresMensual(capitalPendiente, porcentaje_aplicado);
    }

    /** Funcion que devuelve el interes pagado de un determinado numero de pago **/
    /*public double calcularInteresMensualConNumeroMes(int numPago){
        double capitalPendiente = super.obtenerCuotaMensual(porcentaje_fijo, precio_vivienda - cantidad_abonada)  * numPago; //MAAAAAL
        return capitalPendiente * (porcentaje_fijo / 100) / 12;
    }*/

    /** Esta funcion devuelve los intereses en un plazo pasandole el capital pendiente, el numero de cuotas, y el
     *  porcentaje aplicado**/
    public double obtenerInteresesPlazo(double capital_pendiente, int numeroCuotas, double porcentaje_aplicado){
        double interesesTotales = 0;

        for(int i = 0; i < numeroCuotas; i++){
            interesesTotales += getInteresMensual(capital_pendiente, porcentaje_aplicado);
            capital_pendiente -= (getCuotaMensual(porcentaje_aplicado, capital_pendiente) - getInteresMensual(capital_pendiente, porcentaje_aplicado));
        }
        return interesesTotales;
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
