package es.MiHipotecaApp.TFG.UsuarioRegistrado.HipotecasSeguimiento.Graficos;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.List;

import es.MiHipotecaApp.TFG.R;
import es.MiHipotecaApp.TFG.UsuarioRegistrado.HipotecasSeguimiento.HipotecaPruebaSeguimientoFija;

public class GraficosHipotecaFija extends AppCompatActivity {

    PieChart pieChart;
    LineChart lineChart;
    Button btn_euros;
    Button btn_porcentaje;
    HipotecaPruebaSeguimientoFija h;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pie_chart);

        pieChart = findViewById(R.id.pie_chart);
        lineChart = findViewById(R.id.line_chart);

        btn_euros = findViewById(R.id.btn_euros);
        btn_porcentaje = findViewById(R.id.btn_porcentaje);

        h = new HipotecaPruebaSeguimientoFija(100000, 50000, 9, 128, 25);
        h.calcularHipoteca();
        btn_euros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pieChartEuro();
                lineChartEuro();
            }
        });

        btn_porcentaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pieCharPorcentaje();
                lineChartPorcentaje();
            }
        });

    }

    // Funcion que obtiene el porcentaje que es n sobre el total
    public float enteroPorcentaje(float n, float total){
        return (n * 100f)/total;
    }

    //Funcion que muestra un grafico circular en funcion del total de gastos de la la hipoteca
    public void pieChartEuro(){

        ArrayList<PieEntry> list = new ArrayList();

        list.add(new PieEntry(h.getTotalIntereses(), "Intereses Totales"));
        list.add(new PieEntry(h.getTotalAmortizado(), "Amortizado Total"));
        list.add(new PieEntry(h.getTotalVinculaciones(), "Vinculaciones Total"));


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

        pieChart.setData(pieData);
        Description d = new Description();
        d.setText("Pie Chart");
        pieChart.setDescription(d);
        pieChart.animateY(2000);
    }

    //Funcion que muestra un grafico de lineas en funcion de los intereses a lo largo del tiempo
    public void lineChartEuro(){
        ArrayList<Entry> list = new ArrayList();

        ArrayList<Float> array = h.getInteresesPorAnio();
        int i = 25;
        for(int j = 0; j < array.size(); j++){
            list.add(new Entry(i*j , array.get(j)));
        }

        LineDataSet lineDataSet = new LineDataSet(list, "List");

        List<Integer> colors = new ArrayList<>();
        colors.add(Color.RED);
        colors.add(Color.BLUE);
        colors.add(Color.GREEN);
        colors.add(Color.YELLOW);
        lineDataSet.setColors(colors);

        lineDataSet.setValueTextColor(Color.BLACK);
        lineDataSet.setValueTextSize(7);

        LineData lineData = new LineData(lineDataSet);

        lineChart.setData(lineData);
        Description d = new Description();
        d.setText("Line Chart");
        lineChart.setDescription(d);
        lineChart.animateY(2000);
    }

    //Funcion que muestra un grafico circular en funcion del porcentaje de pagos total de la hipoteca
    public void pieCharPorcentaje(){

        btn_porcentaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HipotecaPruebaSeguimientoFija h = new HipotecaPruebaSeguimientoFija(100000, 50000, 9, 128, 25);
                h.calcularHipoteca();

                ArrayList<PieEntry> list = new ArrayList();
                float total = h.getTotalIntereses() + h.getTotalAmortizado() + h.getTotalVinculaciones();
                list.add(new PieEntry(enteroPorcentaje(h.getTotalIntereses(), total), "Intereses Totales"));
                list.add(new PieEntry(enteroPorcentaje(h.getTotalAmortizado(), total), "Amortizado Total"));
                list.add(new PieEntry(enteroPorcentaje(h.getTotalVinculaciones(), total), "Vinculaciones Total"));


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

                pieChart.setData(pieData);
                Description d = new Description();
                d.setText("Pie Chart");
                pieChart.setDescription(d);
                pieChart.animateY(2000);
            }
        });
    }

    //Funcion que muestra un grafico de lineas en funcion del porcentaje de intereses a lo largo del tiempo
    public void lineChartPorcentaje(){
        ArrayList<Entry> list = new ArrayList();

        ArrayList<Float> array = h.getInteresesPorAnio();
        float interesesTotales = h.getTotalIntereses();
        int i = 25;
        for(int j = 0; j < array.size(); j++){
            list.add(new Entry(i*j , enteroPorcentaje(array.get(j), interesesTotales)));
        }

        LineDataSet lineDataSet = new LineDataSet(list, "List");

        List<Integer> colors = new ArrayList<>();
        colors.add(Color.RED);
        colors.add(Color.BLUE);
        colors.add(Color.GREEN);
        colors.add(Color.YELLOW);
        lineDataSet.setColors(colors);

        lineDataSet.setValueTextColor(Color.BLACK);
        lineDataSet.setValueTextSize(7);

        LineData lineData = new LineData(lineDataSet);

        lineChart.setData(lineData);
        Description d = new Description();
        d.setText("Line Chart");
        lineChart.setDescription(d);
        lineChart.animateY(2000);
    }
}