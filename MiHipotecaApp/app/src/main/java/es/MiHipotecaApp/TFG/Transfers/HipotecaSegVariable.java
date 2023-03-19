package es.MiHipotecaApp.TFG.Transfers;

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

    public int getDuracion_primer_porcentaje_variable() {
        return duracion_primer_porcentaje_variable;
    }

    public double getPrimer_porcentaje_variable() {
        return primer_porcentaje_variable;
    }

    public double getPorcentaje_diferencial_variable() {
        return porcentaje_diferencial_variable;
    }

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
