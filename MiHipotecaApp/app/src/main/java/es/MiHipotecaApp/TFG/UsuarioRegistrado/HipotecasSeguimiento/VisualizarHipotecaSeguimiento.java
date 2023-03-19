package es.MiHipotecaApp.TFG.UsuarioRegistrado.HipotecasSeguimiento;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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

    private TextView nombre_hipoteca;
    private TextView tipo_hipoteca_seg;
    private TextView dinero_restante_a_pagar;
    private TextView anios_restantes_hipoteca;
    private TextView mes_actual_cuota;
    private TextView cuota_mensual_seguimiento;

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

        if(getIntent().getStringExtra("tipo_hipoteca").equals("fija")) hip = (HipotecaSegFija) getIntent().getSerializableExtra("hipoteca");
        else if (getIntent().getStringExtra("tipo_hipoteca").equals("variable")) hip = (HipotecaSegVariable) getIntent().getSerializableExtra("hipoteca");
        else hip = (HipotecaSegMixta) getIntent().getSerializableExtra("hipoteca");
        initUI();
        rellenarUI();
        eventos();
    }

    private void initUI(){
        goBarChart                = findViewById(R.id.go_bar_chart);
        goPieChart                = findViewById(R.id.go_pie_chart);
        goRadarChart              = findViewById(R.id.go_radar_chart);
        dinero_restante_a_pagar   = findViewById(R.id.label_cantidad_pendiente_seguimiento_hip);
        nombre_hipoteca           = findViewById(R.id.nombre_seguimiento_hipoteca);
        tipo_hipoteca_seg         = findViewById(R.id.tipo_hipoteca_seguimiento);
        anios_restantes_hipoteca  = findViewById(R.id.label_anios_restantes_seguimiento_hip);
        mes_actual_cuota          = findViewById(R.id.nombre_mes_actual_seguimiento_hip);
        cuota_mensual_seguimiento = findViewById(R.id.cuota_mensual_seguimiento);

        //BOTONES
        btn_aportado_vs_financiar_valor      = findViewById(R.id.btn_valor_aportado_vs_a_financiar);
        btn_aportado_vs_financiar_porcentaje = findViewById(R.id.btn_porcentaje_aportado_vs_a_financiar);
        aportado_vs_a_financiar              = findViewById(R.id.pie_chart_aportado_vs_a_financiar);

        btn_gastos_totales_valor             = findViewById(R.id.btn_valor_gastos_totales);
        btn_gastos_totales_porcentaje        = findViewById(R.id.btn_porcentaje_gastos_totales);
        gastos_totales                       = findViewById(R.id.pie_chart_gastos_totales);
    }

    private void rellenarUI(){
        DecimalFormat formato = new DecimalFormat("#.##"); // Establecer el formato a dos decimales
        String numeroFormateado = formato.format(hip.getDineroRestanteActual(pasarDateANumPago(hip.getFecha_inicio())))  + "€"; // Formatear el número
        dinero_restante_a_pagar.setText(numeroFormateado);


        nombre_hipoteca.setText(hip.getNombre());


        tipo_hipoteca_seg.setText(hip.getTipo_hipoteca().substring(0, 1).toUpperCase() + hip.getTipo_hipoteca().substring(1));


        /*Calendar fechaActual = Calendar.getInstance();
        Calendar fechaInicioCalendar = Calendar.getInstance();
        fechaInicioCalendar.setTime(hip.getFecha_inicio());
        fechaInicioCalendar.add(Calendar.YEAR, hip.getPlazo_anios());
        long diferenciaMillis = fechaInicioCalendar.getTimeInMillis() - fechaActual.getTimeInMillis();
        Calendar diferenciaCalendar = Calendar.getInstance();
        diferenciaCalendar.setTimeInMillis(diferenciaMillis);
        anios_restantes_hipoteca.setText(Integer.toString(diferenciaCalendar.get(Calendar.YEAR) - 1970)  + " años");*/
        anios_restantes_hipoteca.setText(hip.getAniosRestantes() + " años");


        /*String nombreMesActual = fechaActual.getDisplayName(Calendar.MONTH, Calendar.LONG, new Locale("es", "ES"));
        mes_actual_cuota.setText(nombreMesActual.substring(0, 1).toUpperCase() + nombreMesActual.substring(1));*/
        mes_actual_cuota.setText(hip.getNombreMesActual());

        if(hip.getTipo_hipoteca().equals("fija")) {
            String cuotaFormateada = formato.format(hip.getCuotaMensual(hip.getPorcentaje_fijo(), hip.getPrecio_vivienda() - hip.getCantidad_abonada())) + "€"; // Formatear el número
            cuota_mensual_seguimiento.setText(cuotaFormateada);
        } else if(hip.getTipo_hipoteca().equals("variable")) {
            //...
        } else {
            //...
        }
    }
    private void eventos(){
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

        int aux = pasarDateANumPago(hip.getFecha_inicio());
        list.add(new PieEntry((float) hip.getDineroAportadoActual(aux), "DINERO APORTADO"));
        list.add(new PieEntry((float) hip.getDineroRestanteActual(pasarDateANumPago(hip.getFecha_inicio())), "DINERO RESTANTE"));


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
        double auxAportado = hip.getDineroAportadoActual(pasarDateANumPago(hip.getFecha_inicio()));
        double auxRestante = hip.getDineroRestanteActual(pasarDateANumPago(hip.getFecha_inicio()));
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
        double aux = hip.getCuotaMensual(hip.getPorcentaje_fijo(), hip.getPrecio_vivienda() - hip.getCantidad_abonada());
        list.add(new PieEntry((float) ((aux * hip.getPlazo_anios() * 12) - hip.getInteresesTotales()), "CAPITAL TOTAL"));
        list.add(new PieEntry((float) hip.getInteresesTotales(), "INTERESES TOTALES"));
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
        double total = hip.getGastosTotalesHipoteca();
        list.add(new PieEntry((float) pasarEnteroAPorcentaje((hip.getCuotaMensual(hip.getPorcentaje_fijo(), hip.getPrecio_vivienda() - hip.getCantidad_abonada()) * hip.getPlazo_anios() * 12) - hip.getInteresesTotales(), total), "CAPITAL TOTAL"));
        list.add(new PieEntry((float) pasarEnteroAPorcentaje(hip.getInteresesTotales(), total), "INTERESES TOTALES"));
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

    public int pasarDateANumPago(Date fecha_inicio){
        Calendar calendar = Calendar.getInstance(); // Obtenemos un objeto Calendar con la fecha actual
        long diffInMillis = calendar.getTimeInMillis() - fecha_inicio.getTime(); // Calculamos la diferencia en milisegundos entre la fecha actual y la fecha de inicio
        int diffInMonths = (int) (diffInMillis / (30L * 24L * 60L * 60L * 1000L)); // Convertimos la diferencia en milisegundos a meses

        return diffInMonths;
    }

}

