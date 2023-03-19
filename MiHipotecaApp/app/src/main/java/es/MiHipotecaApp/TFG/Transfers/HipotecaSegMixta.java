package es.MiHipotecaApp.TFG.Transfers;

import java.io.Serializable;
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
