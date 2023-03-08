package es.MiHipotecaApp.TFG.Transfers;

import java.io.Serializable;

public class HipotecaSegMixta extends HipotecaSeguimiento implements Serializable {

    //Hipoteca mixta
    private int anios_fija_mixta;
    private float porcentaje_fijo_mixta;
    private float getPorcentaje_diferencial_mixta;
    private boolean revision_anual;

    public HipotecaSegMixta(String nombre, String comunidad_autonoma, String tipo_vivienda, String antiguedad_vivienda, float precio_vivienda, float cantidad_abonada, int plazo_anios, int anio_hipoteca_actual, float totalGastos, float totalVinculacionesAnual, String banco_asociado, int anios_fija_mixta, float porcentaje_fijo_mixta, float getPorcentaje_diferencial_mixta, boolean revision_anual) {
        super(nombre, comunidad_autonoma, tipo_vivienda, antiguedad_vivienda, precio_vivienda, cantidad_abonada, plazo_anios, anio_hipoteca_actual, totalGastos, totalVinculacionesAnual, banco_asociado);
        this.anios_fija_mixta = anios_fija_mixta;
        this.porcentaje_fijo_mixta = porcentaje_fijo_mixta;
        this.getPorcentaje_diferencial_mixta = getPorcentaje_diferencial_mixta;
        this.revision_anual = revision_anual;
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
}
