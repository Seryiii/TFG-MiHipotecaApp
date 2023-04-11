package es.MiHipotecaApp.TFG.UsuarioRegistrado.HipotecasSeguimiento.Graficos;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.enums.LegendLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import es.MiHipotecaApp.TFG.R;
import es.MiHipotecaApp.TFG.Transfers.HipotecaSegFija;
import es.MiHipotecaApp.TFG.Transfers.HipotecaSegMixta;
import es.MiHipotecaApp.TFG.Transfers.HipotecaSegVariable;
import es.MiHipotecaApp.TFG.Transfers.HipotecaSeguimiento;

public class grafico_intereses_capital extends AppCompatActivity {

    private AnyChartView grafico;
    private HipotecaSeguimiento hip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grafico_intereses_capital);
        initUI();
        construirGraficoLineas();
    }

    private void initUI(){
        grafico = findViewById(R.id.grafico_intereses_capital);
        if(getIntent().getStringExtra("tipo_hipoteca").equals("fija")) hip = (HipotecaSegFija) getIntent().getSerializableExtra("hipoteca");
        else if (getIntent().getStringExtra("tipo_hipoteca").equals("variable")) hip = (HipotecaSegVariable) getIntent().getSerializableExtra("hipoteca");
        else hip = (HipotecaSegMixta) getIntent().getSerializableExtra("hipoteca");
    }

    public void construirGraficoLineas(){

        List<DataEntry> capitalAnual = new ArrayList<>();
        List<DataEntry> interesesAnuales = new ArrayList<>();
        List<DataEntry> cuotaAnual = new ArrayList<>();
        List<DataEntry> vinculacionesAnules = new ArrayList<>();

        Calendar anio = Calendar.getInstance();
        anio.setTime(hip.getFecha_inicio());


        for(int i = 1; i <= hip.getPlazo_anios(); i++){
            ArrayList<Double> valores = hip.getFilaCuadroAmortizacionAnual(anio.get(Calendar.YEAR) + i - 1, i);

            capitalAnual.add(new ValueDataEntry(anio.get(Calendar.YEAR) + i, valores.get(1)));
            interesesAnuales.add(new ValueDataEntry(anio.get(Calendar.YEAR) + i, valores.get(2)));
            cuotaAnual.add(new ValueDataEntry(anio.get(Calendar.YEAR) + i, valores.get(0)));
            vinculacionesAnules.add(new ValueDataEntry(anio.get(Calendar.YEAR) + i, hip.getTotalVinculacionesAnual()));

        }

        Cartesian lineChart = AnyChart.line();
        lineChart.line(capitalAnual).name("Capital anual").stroke("3 #00ff00");
        lineChart.line(interesesAnuales).name("Intereses anuales").stroke("3 #ff0000");
        lineChart.line(cuotaAnual).name("Cuota anual").stroke("2 #0000ff");
        lineChart.line(vinculacionesAnules).name("Vinculaciones anuales").stroke("2 #ffc107");

        lineChart.legend().enabled(true);
        lineChart.legend().fontSize(14d);
        lineChart.legend().padding(10d, 10d, 10d, 10d);
        lineChart.legend().itemsLayout(LegendLayout.HORIZONTAL_EXPANDABLE);
        lineChart.legend().position("bottom");
        grafico.setChart(lineChart);
        grafico.invalidate();
    }
}