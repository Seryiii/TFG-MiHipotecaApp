package es.MiHipotecaApp.TFG.UsuarioRegistrado.HipotecasSeguimiento;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.List;

import es.MiHipotecaApp.TFG.R;
import es.MiHipotecaApp.TFG.Transfers.HipotecaSegFija;
import es.MiHipotecaApp.TFG.Transfers.HipotecaSegMixta;
import es.MiHipotecaApp.TFG.Transfers.HipotecaSegVariable;
import es.MiHipotecaApp.TFG.Transfers.HipotecaSeguimiento;
import es.MiHipotecaApp.TFG.UsuarioRegistrado.HipotecasSeguimiento.Graficos.BarChartActivity;
import es.MiHipotecaApp.TFG.UsuarioRegistrado.HipotecasSeguimiento.Graficos.GraficosHipotecaFija;
import es.MiHipotecaApp.TFG.UsuarioRegistrado.HipotecasSeguimiento.Graficos.RadarChartActivity;

public class VisualizarHipotecaSeguimiento extends AppCompatActivity {

    private Button goBarChart;
    private Button goPieChart;
    private Button goRadarChart;

    private PieChart aportado_vs_a_financiar;
    private PieChart gastos_totales;
    private LineChart intereses_vs_capital_mensual;

    private HipotecaSeguimiento hip;
    private Button btn_aportado_vs_financiar_valor;
    private Button btn_aportado_vs_financiar_porcentaje;
    private Button btn_gastos_totales_valor;

    private Button btn_gastos_totales_porcentaje;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_hipoteca_seguimiento);

        if(getIntent().getStringExtra("tipo").equals("fija")) hip = (HipotecaSegFija) getIntent().getSerializableExtra("hipoteca");
        else if (getIntent().getStringExtra("tipo").equals("variable")) hip = (HipotecaSegVariable) getIntent().getSerializableExtra("hipoteca");
        else hip = (HipotecaSegMixta) getIntent().getSerializableExtra("hipoteca");

        goBarChart = findViewById(R.id.go_bar_chart);
        goPieChart = findViewById(R.id.go_pie_chart);
        goRadarChart = findViewById(R.id.go_radar_chart);

        btn_aportado_vs_financiar_valor = findViewById(R.id.btn_valor_aportado_vs_a_financiar);
        btn_aportado_vs_financiar_porcentaje = findViewById(R.id.btn_porcentaje_aportado_vs_a_financiar);
        aportado_vs_a_financiar = findViewById(R.id.pie_chart_aportado_vs_a_financiar);

        btn_gastos_totales_valor = findViewById(R.id.btn_valor_gastos_totales);
        btn_gastos_totales_porcentaje = findViewById(R.id.btn_porcentaje_gastos_totales);
        gastos_totales = findViewById(R.id.pie_chart_gastos_totales);

        pieChartAportadoVsFinanciarValor(); //Se visualiza por defecto el grafico de valor
        btn_aportado_vs_financiar_valor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pieChartAportadoVsFinanciarValor();
            }
        });
        btn_aportado_vs_financiar_porcentaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pieChartAportadoVsFinanciarPorcentaje();
            }
        });

        pieChartGastosTotalesValor(); //Se visualiza por defecto el grafico de valor
        btn_gastos_totales_valor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pieChartGastosTotalesValor();
            }
        });
        btn_gastos_totales_porcentaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pieChartGastosTotalesPorcentaje();
            }
        });

        goBarChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(VisualizarHipotecaSeguimiento.this, BarChartActivity.class);
                startActivity(i);
            }
        });

        goPieChart.setOnClickListener(v -> startActivity(new Intent(this, GraficosHipotecaFija.class)));

        goRadarChart.setOnClickListener(v -> startActivity(new Intent(this, RadarChartActivity.class)));
    }

    public void pieChartAportadoVsFinanciarValor(){

        ArrayList<PieEntry> list = new ArrayList();

        list.add(new PieEntry((float) hip.obtenerDineroAportadoActual(hip.getAnio_hipoteca_actual()*12 + hip.getMes_hipoteca_actual()), "DINERO APORTADO"));
        list.add(new PieEntry((float) hip.obtenerDineroRestanteActual(hip.getAnio_hipoteca_actual()*12 + hip.getMes_hipoteca_actual()), "DINERO RESTANTE"));


        PieDataSet pieDataSet = new PieDataSet(list, "List");

        List<Integer> colors = new ArrayList<>();
        colors.add(Color.RED);
        colors.add(Color.BLUE);
        colors.add(Color.GREEN);
        colors.add(Color.YELLOW);
        pieDataSet.setColors(colors);

        pieDataSet.setValueTextColor(Color.BLACK);
        pieDataSet.setValueLineColor(Color.BLACK);
        pieDataSet.setValueTextSize(25);

        PieData pieData = new PieData(pieDataSet);

        aportado_vs_a_financiar.setData(pieData);
        Description d = new Description();
        d.setText("Pie Chart");
        aportado_vs_a_financiar.setDescription(d);
        aportado_vs_a_financiar.animateY(2000);

    }

    public void pieChartAportadoVsFinanciarPorcentaje(){

        ArrayList<PieEntry> list = new ArrayList();
        double auxAportado = hip.obtenerDineroAportadoActual(hip.getAnio_hipoteca_actual()*12 + hip.getMes_hipoteca_actual());
        double auxRestante = hip.obtenerDineroRestanteActual(hip.getAnio_hipoteca_actual()*12 + hip.getMes_hipoteca_actual());
        double auxTotal = auxAportado + auxRestante;
        auxAportado = pasarEnteroAPorcentaje(auxAportado, auxTotal);
        auxRestante = pasarEnteroAPorcentaje(auxRestante, auxTotal);
        list.add(new PieEntry((float) auxAportado, "DINERO APORTADO"));
        list.add(new PieEntry((float) auxRestante, "DINERO RESTANTE"));


        PieDataSet pieDataSet = new PieDataSet(list, "List");

        List<Integer> colors = new ArrayList<>();
        colors.add(Color.RED);
        colors.add(Color.BLUE);
        colors.add(Color.GREEN);
        colors.add(Color.YELLOW);
        pieDataSet.setColors(colors);

        pieDataSet.setValueTextColor(Color.BLACK);
        pieDataSet.setValueLineColor(Color.BLACK);
        pieDataSet.setValueTextSize(25);

        PieData pieData = new PieData(pieDataSet);

        aportado_vs_a_financiar.setData(pieData);
        Description d = new Description();
        d.setText("Pie Chart");
        aportado_vs_a_financiar.setDescription(d);
        aportado_vs_a_financiar.animateY(2000);

    }

    public void pieChartGastosTotalesValor(){
        ArrayList<PieEntry> list = new ArrayList();

        list.add(new PieEntry((float) ((hip.getCuotaMensual(hip.getPorcentaje_fijo(), hip.getPrecio_vivienda() - hip.getCantidad_abonada()) * hip.getPlazo_anios() * 12) - hip.obtenerInteresesTotales()), "CAPITAL TOTAL"));
        list.add(new PieEntry((float) hip.obtenerInteresesTotales(), "INTERESES TOTALES"));
        list.add(new PieEntry((float) hip.getTotalGastos(), "OTROS GASTOS"));
        list.add(new PieEntry((float) hip.getTotalVinculacionesAnual() * hip.getPlazo_anios(), "VINCULACIONES"));


        PieDataSet pieDataSet = new PieDataSet(list, "List");

        List<Integer> colors = new ArrayList<>();
        colors.add(Color.RED);
        colors.add(Color.BLUE);
        colors.add(Color.GREEN);
        colors.add(Color.YELLOW);
        pieDataSet.setColors(colors);

        pieDataSet.setValueTextColor(Color.BLACK);
        pieDataSet.setValueLineColor(Color.BLACK);
        pieDataSet.setValueTextSize(25);

        PieData pieData = new PieData(pieDataSet);

        gastos_totales.setData(pieData);
        Description d = new Description();
        d.setText("Pie Chart");
        gastos_totales.setDescription(d);
        gastos_totales.animateY(2000);
    }

    public void pieChartGastosTotalesPorcentaje(){
        ArrayList<PieEntry> list = new ArrayList();
        double total = hip.obtenerGastosTotales();
        list.add(new PieEntry((float) pasarEnteroAPorcentaje((hip.getCuotaMensual(hip.getPorcentaje_fijo(), hip.getPrecio_vivienda() - hip.getCantidad_abonada()) * hip.getPlazo_anios() * 12) - hip.obtenerInteresesTotales(), total), "CAPITAL TOTAL"));
        list.add(new PieEntry((float) pasarEnteroAPorcentaje(hip.obtenerInteresesTotales(), total), "INTERESES TOTALES"));
        list.add(new PieEntry((float) pasarEnteroAPorcentaje(hip.getTotalGastos(), total),"OTROS GASTOS"));
        list.add(new PieEntry((float) pasarEnteroAPorcentaje(hip.getTotalVinculacionesAnual() * hip.getPlazo_anios(), total), "VINCULACIONES"));


        PieDataSet pieDataSet = new PieDataSet(list, "List");

        List<Integer> colors = new ArrayList<>();
        colors.add(Color.RED);
        colors.add(Color.BLUE);
        colors.add(Color.GREEN);
        colors.add(Color.YELLOW);
        pieDataSet.setColors(colors);

        pieDataSet.setValueTextColor(Color.BLACK);
        pieDataSet.setValueLineColor(Color.BLACK);
        pieDataSet.setValueTextSize(25);

        PieData pieData = new PieData(pieDataSet);

        gastos_totales.setData(pieData);
        Description d = new Description();
        d.setText("Pie Chart");
        gastos_totales.setDescription(d);
        gastos_totales.animateY(2000);
    }

    public double pasarEnteroAPorcentaje(double entero, double total){
        return (entero * 100) / total;
    }

}

