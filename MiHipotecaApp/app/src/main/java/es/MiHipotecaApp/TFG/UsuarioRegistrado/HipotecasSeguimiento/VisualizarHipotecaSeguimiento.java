package es.MiHipotecaApp.TFG.UsuarioRegistrado.HipotecasSeguimiento;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.TooltipCompat;
import androidx.core.content.ContextCompat;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.skydoves.balloon.ArrowOrientation;
import com.skydoves.balloon.ArrowPositionRules;
import com.skydoves.balloon.Balloon;
import com.skydoves.balloon.BalloonAnimation;
import com.skydoves.balloon.BalloonSizeSpec;

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

    private TextView nombre_hipoteca;
    private TextView tipo_hipoteca_seg;
    private TextView dinero_restante_a_pagar;
    private TextView anios_restantes_hipoteca;
    private TextView mes_actual_cuota;

    private TextView capital_cuota_mensual;
    private TextView intereses_cuota_mensual;
    private TextView numero_cuota_actual;
    private TextView cuota_mensual_seguimiento;

    private PieChart aportado_vs_a_financiar;
    private PieChart gastos_totales;

    private LineChart intereses_vs_capital_mensual;

    private HipotecaSeguimiento hip;
    private Button btn_aportado_vs_financiar_valor;
    private Button btn_aportado_vs_financiar_porcentaje;
    private Button btn_gastos_totales_valor;
    private Button btn_cuadro_amortizacion;

    private Button btn_gastos_totales_porcentaje;

    private ImageButton info_dinero_restante;
    private ImageView info_cuota;

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
        dinero_restante_a_pagar   = findViewById(R.id.label_cantidad_pendiente_seguimiento_hip);
        nombre_hipoteca           = findViewById(R.id.nombre_seguimiento_hipoteca);
        tipo_hipoteca_seg         = findViewById(R.id.tipo_hipoteca_seguimiento);
        anios_restantes_hipoteca  = findViewById(R.id.label_anios_restantes_seguimiento_hip);
        mes_actual_cuota          = findViewById(R.id.nombre_mes_actual_seguimiento_hip);
        cuota_mensual_seguimiento = findViewById(R.id.cuota_mensual_seguimiento);
        capital_cuota_mensual     = findViewById(R.id.capital_cuota_mensual_hip);
        intereses_cuota_mensual   = findViewById(R.id.intereses_cuota_mensual_hip);
        numero_cuota_actual       = findViewById(R.id.numero_cuota_actual_hip);

        //BOTONES
        btn_aportado_vs_financiar_valor      = findViewById(R.id.btn_valor_aportado_vs_a_financiar);
        btn_aportado_vs_financiar_porcentaje = findViewById(R.id.btn_porcentaje_aportado_vs_a_financiar);
        aportado_vs_a_financiar              = findViewById(R.id.pie_chart_aportado_vs_a_financiar);

        btn_gastos_totales_valor             = findViewById(R.id.btn_valor_gastos_totales);
        btn_gastos_totales_porcentaje        = findViewById(R.id.btn_porcentaje_gastos_totales);
        gastos_totales                       = findViewById(R.id.pie_chart_gastos_totales);
        btn_cuadro_amortizacion              = findViewById(R.id.btn_cuadro_amortizacion);
        info_dinero_restante                 = findViewById(R.id.btn_info_dinero_por_pagar);
        info_cuota                           = findViewById(R.id.btn_info_cuota);

        //GRÁFICOS
        aportado_vs_a_financiar              = findViewById(R.id.pie_chart_aportado_vs_a_financiar);

    }

    private void rellenarUI(){

        info_cuota.setVisibility(View.GONE);

        DecimalFormat formato = new DecimalFormat("#.##"); // Establecer el formato a dos decimales
        String numeroFormateado = formato.format(hip.getDineroRestanteActual(hip.getNumeroCuotaActual()))  + "€"; // Formatear el número
        dinero_restante_a_pagar.setText(numeroFormateado);
        nombre_hipoteca.setText(hip.getNombre());
        tipo_hipoteca_seg.setText(hip.getTipo_hipoteca().substring(0, 1).toUpperCase() + hip.getTipo_hipoteca().substring(1));
        anios_restantes_hipoteca.setText(hip.getAniosRestantes() + " años");
        mes_actual_cuota.setText(hip.getNombreMesActual());
        numero_cuota_actual.setText("Cuotas pagadas: " + hip.getNumeroCuotaActual() + " / " + hip.getPlazo_anios() * 12);

        double porcentaje_aplicado;
        double cantidad_pendiente;
        int numero_cuotas_restantes;

        if(hip.getTipo_hipoteca().equals("fija")) {
            porcentaje_aplicado  = hip.getPorcentaje_fijo();
            cantidad_pendiente   = hip.getPrecio_vivienda() - hip.getCantidad_abonada() ;
            numero_cuotas_restantes = hip.getPlazo_anios() * 12 ;
            info_dinero_restante.setVisibility(View.GONE);

        } else if(hip.getTipo_hipoteca().equals("variable")) {
            //Si cumple la condicion, esta aplicando el primer porcentaje fijado, en otro caso el diferencial + euribor
            porcentaje_aplicado  = hip.getNumeroCuotaActual() <= hip.getDuracion_primer_porcentaje_variable() ? hip.getPrimer_porcentaje_variable() : hip.getEuriborActual() + hip.getPorcentaje_diferencial_variable();
            cantidad_pendiente   = hip.getNumeroCuotaActual() <= hip.getDuracion_primer_porcentaje_variable() ? hip.getPrecio_vivienda() - hip.getCantidad_abonada() : hip.getCapitalPendienteTotalActual(hip.getNumeroCuotaActual());
            numero_cuotas_restantes = hip.getNumeroCuotaActual() <= hip.getDuracion_primer_porcentaje_variable() ? hip.getPlazo_anios() * 12 : hip.getPlazo_anios() * 12 - hip.getNumeroCuotaActual();
        } else {
            //Si cumple la condicion, esta en la fase fija, en otro en la variable
            porcentaje_aplicado  = hip.getNumeroCuotaActual() <= hip.getAnios_fija_mixta() * 12 ? hip.getPorcentaje_fijo_mixta() : hip.getEuriborActual() + hip.getPorcentaje_diferencial_mixta();
            cantidad_pendiente   = hip.getNumeroCuotaActual() <= hip.getAnios_fija_mixta() * 12 ? hip.getPrecio_vivienda() - hip.getCantidad_abonada() : hip.getCapitalPendienteTotalActual(hip.getNumeroCuotaActual());
            numero_cuotas_restantes = hip.getNumeroCuotaActual() <= hip.getAnios_fija_mixta() * 12 ? hip.getPlazo_anios() * 12 : hip.getPlazo_anios() * 12 - hip.getNumeroCuotaActual();
        }

        if (hip.siguienteCuotaRevision()) info_cuota.setVisibility(View.VISIBLE);

        if(hip.getAniosRestantes() <= 0){
            numero_cuotas_restantes = 0;
            info_cuota.setVisibility(View.GONE);
        }
        double cuota_mensual = hip.getCuotaMensual(porcentaje_aplicado, cantidad_pendiente, numero_cuotas_restantes);
        String cuotaFormateada = formato.format(cuota_mensual) + "€"; // Formatear el número
        cuota_mensual_seguimiento.setText(cuotaFormateada);

        double capitalPendiente = hip.getCapitalPendienteTotalActual(hip.getNumeroCuotaActual());
        String capitalFormateado = formato.format(hip.getCapitalAmortizadoMensual(cuota_mensual, capitalPendiente, porcentaje_aplicado)) + "€";
        capital_cuota_mensual.setText(capitalFormateado);

        String interesesFormateado = formato.format(hip.getInteresMensual(capitalPendiente, porcentaje_aplicado)) + "€";
        intereses_cuota_mensual.setText(interesesFormateado);
    }
    private void eventos(){

        btn_cuadro_amortizacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(VisualizarHipotecaSeguimiento.this, Cuadro_amortizacion.class);
                i.putExtra("hipoteca", hip);
                i.putExtra("tipo_hipoteca", hip.getTipo_hipoteca());
                startActivity(i);
            }
        });

        info_dinero_restante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Balloon balloon = new Balloon.Builder(getApplicationContext())
                        .setArrowSize(10)
                        .setArrowOrientation(ArrowOrientation.TOP)
                        .setArrowPositionRules(ArrowPositionRules.ALIGN_ANCHOR)
                        .setArrowPosition(0.5f)
                        .setWidth(BalloonSizeSpec.WRAP)
                        .setHeight(100)
                        .setTextSize(15f)
                        .setCornerRadius(4f)
                        .setAlpha(0.9f)
                        .setText("El dinero restante está estimado con el Euribor actual manteniéndolo fijo el resto de años. Puede tener alguna modificación")
                        .setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.black))
                        .setTextIsHtml(true)
                        .setIconDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.info))
                        .setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white))
                        .setBalloonAnimation(BalloonAnimation.FADE)
                        .build();

                balloon.showAlignTop(info_dinero_restante);
            }
        });

        info_cuota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Balloon balloon = new Balloon.Builder(getApplicationContext())
                        .setArrowSize(10)
                        .setArrowOrientation(ArrowOrientation.TOP)
                        .setArrowPositionRules(ArrowPositionRules.ALIGN_ANCHOR)
                        .setArrowPosition(0.5f)
                        .setWidth(BalloonSizeSpec.WRAP)
                        .setHeight(100)
                        .setTextSize(15f)
                        .setCornerRadius(4f)
                        .setAlpha(0.9f)
                        .setText("La cuota mostrada está en función del euribor aplicado actualmente. Los datos van a variar ya que la siguiente cuota hay que actualizarla con el nuevo Euribor")
                        .setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.black))
                        .setTextIsHtml(true)
                        .setIconDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.info))
                        .setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white))
                        .setBalloonAnimation(BalloonAnimation.FADE)
                        .build();

                balloon.showAlignTop(info_cuota);
            }
        });

        //pieChartAportadoVsFinanciarValor(); //Se visualiza por defecto el grafico de valor
        /*btn_aportado_vs_financiar_valor.setOnClickListener(new View.OnClickListener() {
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

        //pieChartGastosTotalesValor(); //Se visualiza por defecto el grafico de valor
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

        goRadarChart.setOnClickListener(v -> startActivity(new Intent(this, RadarChartActivity.class)));*/
    }
/*
    public void pieChartAportadoVsFinanciarValor(){

        ArrayList<PieEntry> list = new ArrayList();

        int aux = hip.getNumeroCuotaActual();
        list.add(new PieEntry((float) hip.getDineroAportadoActual(aux), "DINERO APORTADO"));
        list.add(new PieEntry((float) hip.getDineroRestanteActual(hip.getNumeroCuotaActual()), "DINERO RESTANTE"));


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
        double auxAportado = hip.getDineroAportadoActual(hip.getNumeroCuotaActual());
        double auxRestante = hip.getDineroRestanteActual(hip.getNumeroCuotaActual());
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

    public void pieChartGastosTotalesValor(double cuota_mensual){
        ArrayList<PieEntry> list = new ArrayList();
        list.add(new PieEntry((float) ((cuota_mensual * hip.getPlazo_anios() * 12) - hip.getInteresesTotales()), "CAPITAL TOTAL"));
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
*/
}

