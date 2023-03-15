package es.MiHipotecaApp.TFG.Transfers;

import java.io.Serializable;

public class HipotecaSegFija extends HipotecaSeguimiento implements Serializable {

    //Hipoteca fija
    private double porcentaje_fijo;

    public HipotecaSegFija(String nombre, String comunidad_autonoma, String tipo_vivienda, String antiguedad_vivienda, double precio_vivienda, double cantidad_abonada, int plazo_anios, int anio_hipoteca_actual, int mes_hipoteca_actual, double totalGastos, double totalVinculacionesAnual, String banco_asociado, double porcentaje_fijo) {
        super(nombre, comunidad_autonoma, tipo_vivienda, antiguedad_vivienda, precio_vivienda, cantidad_abonada, plazo_anios, anio_hipoteca_actual, mes_hipoteca_actual, totalGastos, totalVinculacionesAnual, banco_asociado);
        this.porcentaje_fijo = porcentaje_fijo;
        obtenerCuotaMensual();
    }

    private double cuotaMensual;

    public double getPorcentaje_fijo() {
        return porcentaje_fijo;
    }

    public double getCuotaMensual() {
        return cuotaMensual;
    }

    public void setPorcentaje_fijo(double porcentaje_fijo) {
        this.porcentaje_fijo = porcentaje_fijo;
    }

    public void obtenerCuotaMensual(){
        double aux = Math.pow((1 + (porcentaje_fijo / 100) / 12), plazo_anios * 12);
        cuotaMensual = ((precio_vivienda - cantidad_abonada) * ((porcentaje_fijo / 100) / 12))/(1 -(1 / aux));
    }

    //Funcion que devuelve el interes pagado en funcion del capital que quede por pagar
    public double calcularInteresMensualConCapitalPendiente(double capitalPendiente){
        return capitalPendiente * (porcentaje_fijo / 100) / 12;
    }

    //Funcion que devuelve el interes pagado de un determinado numero de pago
    public double calcularInteresMensualConNumeroMes(int numPago){
        double capitalPendiente = cuotaMensual * numPago; //MAAAAAL
        return capitalPendiente * (porcentaje_fijo / 100) / 12;
    }

    //Llamar a esta funcion para actualizar el capital pendiente --> capPendiente -= h.obtenerobtenerCapitalAmortizadoMensual(capPendiente)
    public double obtenerCapitalAmortizadoMensual(double capitalPendiente){
        return cuotaMensual - calcularInteresMensualConCapitalPendiente(capitalPendiente);
    }

    public double obtenerInteresesTotales(){
        double interesesTotales = 0;
        double capPendiente = precio_vivienda - cantidad_abonada;

        for(int i = 0; i < plazo_anios * 12; i++){
            capPendiente -= (cuotaMensual - calcularInteresMensualConCapitalPendiente(capPendiente));
            interesesTotales += calcularInteresMensualConCapitalPendiente(capPendiente);
        }

        return interesesTotales;
    }

    public double obtenerDineroAportadoActual(int numPago){
        return cuotaMensual*numPago;
    }
    public double obtenerDineroRestanteActual(int numPago){
        return cuotaMensual*(plazo_anios*12 - numPago);
    }

    public double obtenerGastosTotales(){
        return cuotaMensual*plazo_anios*12 + totalVinculacionesAnual*plazo_anios + totalGastos;
    }
}
