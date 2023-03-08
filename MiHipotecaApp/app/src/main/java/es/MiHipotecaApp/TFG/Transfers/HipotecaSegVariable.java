package es.MiHipotecaApp.TFG.Transfers;

import java.io.Serializable;

public class HipotecaSegVariable extends HipotecaSeguimiento implements Serializable {

    //Hipoteca variable
    private int duracion_primer_porcentaje_variable;
    private float primer_porcentaje_variable;
    private float porcentaje_diferencial_variable;
    private boolean revision_anual;

    public HipotecaSegVariable(String nombre, String comunidad_autonoma, String tipo_vivienda, String antiguedad_vivienda, float precio_vivienda, float cantidad_abonada, int plazo_anios, int anio_hipoteca_actual, float totalGastos, float totalVinculacionesAnual, String banco_asociado, int duracion_primer_porcentaje_variable, float primer_porcentaje_variable, float porcentaje_diferencial_variable, boolean revision_anual) {
        super(nombre, comunidad_autonoma, tipo_vivienda, antiguedad_vivienda, precio_vivienda, cantidad_abonada, plazo_anios, anio_hipoteca_actual, totalGastos, totalVinculacionesAnual, banco_asociado);
        this.duracion_primer_porcentaje_variable = duracion_primer_porcentaje_variable;
        this.primer_porcentaje_variable = primer_porcentaje_variable;
        this.porcentaje_diferencial_variable = porcentaje_diferencial_variable;
        this.revision_anual = revision_anual;
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

    public boolean isRevision_anual() {
        return revision_anual;
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

    public void setRevision_anual(boolean revision_anual) {
        this.revision_anual = revision_anual;
    }
}
