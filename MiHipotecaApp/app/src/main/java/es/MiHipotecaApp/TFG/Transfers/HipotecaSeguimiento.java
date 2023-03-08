package es.MiHipotecaApp.TFG.Transfers;

import java.io.Serializable;

public class HipotecaSeguimiento implements Serializable {

    private String nombre;
    private String comunidad_autonoma;
    private String tipo_vivienda;
    private String antiguedad_vivienda;
    private float precio_vivienda;
    private float cantidad_abonada;
    private int plazo_anios;
    private int anio_hipoteca_actual;
    private String tipo_hipoteca;

    //Hipoteca fija
    private float porcentaje_fijo;
    //Hipoteca variable
    private int duracion_primer_porcentaje_variable;
    private float primer_porcentaje_variable;
    private float porcentaje_diferencial_variable;

    //Hipoteca mixta
    private int anios_fija_mixta;
    private float porcentaje_fijo_mixta;
    private float getPorcentaje_diferencial_mixta;
    //Hipoteca variable y mixta
    // Si es false, la revision es cada 6 meses
    private boolean revision_anual;

    //Gastos
    private float totalGastos;

    private float totalVinculacionesAnual;

    private String idUsuario;


    public HipotecaSeguimiento(String nombre) {
        this.nombre = nombre;
    }

    public HipotecaSeguimiento(String nombre, String comunidad_autonoma, String tipo_vivienda, String antiguedad_vivienda, float precio_vivienda, float cantidad_abonada, int plazo_anios, int anio_hipoteca_actual, float totalGastos, float totalVinculacionesAnual) {
        this.nombre = nombre;
        this.comunidad_autonoma = comunidad_autonoma;
        this.tipo_vivienda = tipo_vivienda;
        this.antiguedad_vivienda = antiguedad_vivienda;
        this.precio_vivienda = precio_vivienda;
        this.cantidad_abonada = cantidad_abonada;
        this.plazo_anios = plazo_anios;
        this.anio_hipoteca_actual = anio_hipoteca_actual;
        this.totalGastos = totalGastos;
        this.totalVinculacionesAnual = totalVinculacionesAnual;
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

    public float getPrecio_vivienda() {
        return precio_vivienda;
    }

    public float getCantidad_abonada() {
        return cantidad_abonada;
    }

    public int getPlazo_anios() {
        return plazo_anios;
    }

    public int getAnio_hipoteca_actual() {
        return anio_hipoteca_actual;
    }

    public String getTipo_hipoteca() {
        return tipo_hipoteca;
    }

    public float getPorcentaje_fijo() {
        return porcentaje_fijo;
    }

    public int getDuracion_primer_porcentaje_variable() {
        return duracion_primer_porcentaje_variable;
    }

    public float getPrimer_porcentaje_variable() {
        return primer_porcentaje_variable;
    }

    public float getPorcentaje_diferencial_variable() {
        return porcentaje_diferencial_variable;
    }

    public int getAnios_fija_mixta() {
        return anios_fija_mixta;
    }

    public float getPorcentaje_fijo_mixta() {
        return porcentaje_fijo_mixta;
    }

    public float getGetPorcentaje_diferencial_mixta() {
        return getPorcentaje_diferencial_mixta;
    }

    public boolean isRevision_anual() {
        return revision_anual;
    }

    public float getTotalGastos() {
        return totalGastos;
    }

    public float getTotalVinculacionesAnual() {
        return totalVinculacionesAnual;
    }

    public String getIdUsuario() {
        return idUsuario;
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

    public void setPrecio_vivienda(float precio_vivienda) {
        this.precio_vivienda = precio_vivienda;
    }

    public void setCantidad_abonada(float cantidad_abonada) {
        this.cantidad_abonada = cantidad_abonada;
    }

    public void setPlazo_anios(int plazo_anios) {
        this.plazo_anios = plazo_anios;
    }

    public void setAnio_hipoteca_actual(int anio_hipoteca_actual) {
        this.anio_hipoteca_actual = anio_hipoteca_actual;
    }

    public void setTipo_hipoteca(String tipo_hipoteca) {
        this.tipo_hipoteca = tipo_hipoteca;
    }

    public void setPorcentaje_fijo(float porcentaje_fijo) {
        this.porcentaje_fijo = porcentaje_fijo;
    }

    public void setDuracion_primer_porcentaje_variable(int duracion_primer_porcentaje_variable) {
        this.duracion_primer_porcentaje_variable = duracion_primer_porcentaje_variable;
    }

    public void setPrimer_porcentaje_variable(float primer_porcentaje_variable) {
        this.primer_porcentaje_variable = primer_porcentaje_variable;
    }

    public void setPorcentaje_diferencial_variable(float porcentaje_diferencial_variable) {
        this.porcentaje_diferencial_variable = porcentaje_diferencial_variable;
    }

    public void setAnios_fija_mixta(int anios_fija_mixta) {
        this.anios_fija_mixta = anios_fija_mixta;
    }

    public void setPorcentaje_fijo_mixta(float porcentaje_fijo_mixta) {
        this.porcentaje_fijo_mixta = porcentaje_fijo_mixta;
    }

    public void setGetPorcentaje_diferencial_mixta(float getPorcentaje_diferencial_mixta) {
        this.getPorcentaje_diferencial_mixta = getPorcentaje_diferencial_mixta;
    }

    public void setRevision_anual(boolean revision_anual) {
        this.revision_anual = revision_anual;
    }

    public void setTotalGastos(float totalGastos) {
        this.totalGastos = totalGastos;
    }

    public void setTotalVinculacionesAnual(float totalVinculacionesAnual) {
        this.totalVinculacionesAnual = totalVinculacionesAnual;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }
}
