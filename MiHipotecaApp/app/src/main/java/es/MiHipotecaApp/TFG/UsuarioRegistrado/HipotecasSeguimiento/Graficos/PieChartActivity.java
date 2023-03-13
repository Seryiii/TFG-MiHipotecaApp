package es.MiHipotecaApp.TFG.UsuarioRegistrado.HipotecasSeguimiento.Graficos;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.List;

import es.MiHipotecaApp.TFG.R;
import es.MiHipotecaApp.TFG.UsuarioRegistrado.HipotecasSeguimiento.HipotecaSeguimientoFija;

public class PieChartActivity extends AppCompatActivity {

    PieChart pieChart;
    Button btn_euros;
    Button btn_porcentaje;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pie_chart);

        pieChart = findViewById(R.id.pie_chart);
        btn_euros = findViewById(R.id.btn_euros);
        btn_porcentaje = findViewById(R.id.btn_porcentaje);

        btn_euros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HipotecaSeguimientoFija h = new HipotecaSeguimientoFija(100000, 50000, 9, 128, 25);
                h.calcularHipoteca();

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
        });

        btn_porcentaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_porcentaje.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        HipotecaSeguimientoFija h = new HipotecaSeguimientoFija(100000, 50000, 9, 128, 25);
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
        });

    }

    public float enteroPorcentaje(float n, float total){
        return (n * 100f)/total;
    }
}