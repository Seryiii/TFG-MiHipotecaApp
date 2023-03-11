package es.MiHipotecaApp.TFG.UsuarioRegistrado.HipotecasSeguimiento.Graficos;

import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.List;

import es.MiHipotecaApp.TFG.R;

public class BarChartActivity extends AppCompatActivity {

    BarChart barChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_chart);

        barChart = findViewById(R.id.bar_chart);

        ArrayList<BarEntry> list = new ArrayList();

        list.add(new BarEntry(100f, 100f));
        list.add(new BarEntry(101f, 101f));
        list.add(new BarEntry(102f, 102f));
        list.add(new BarEntry(103f, 103f));

        BarDataSet barDataSet = new BarDataSet(list, "List");

        List<Integer> colors = new ArrayList<>();
        colors.add(Color.RED);
        colors.add(Color.BLUE);
        colors.add(Color.GREEN);
        colors.add(Color.YELLOW);
        barDataSet.setColors(colors);


        barDataSet.setValueTextColor(Color.BLACK);

        BarData barData = new BarData(barDataSet);

        barChart.setFitBars(true);

        barChart.setData(barData);

        Description d = new Description();
        d.setText("Bar Chart");
        barChart.setDescription(d);
        barChart.animateY(2000);



    }
}