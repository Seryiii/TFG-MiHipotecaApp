package es.MiHipotecaApp.TFG.UsuarioRegistrado.HipotecasSeguimiento;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.anychart.APIlib;
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.charts.Pie;
import com.anychart.enums.LegendLayout;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.skydoves.balloon.ArrowOrientation;
import com.skydoves.balloon.ArrowPositionRules;
import com.skydoves.balloon.Balloon;
import com.skydoves.balloon.BalloonAnimation;
import com.skydoves.balloon.BalloonSizeSpec;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import es.MiHipotecaApp.TFG.R;
import es.MiHipotecaApp.TFG.Transfers.HipotecaSegFija;
import es.MiHipotecaApp.TFG.Transfers.HipotecaSegMixta;
import es.MiHipotecaApp.TFG.Transfers.HipotecaSegVariable;
import es.MiHipotecaApp.TFG.Transfers.HipotecaSeguimiento;

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

    private TextView titulo_grafico;
    private AnyChartView grafico;

    private TextView capital_amortizado;
    private TextView capital_pendiente;
    private TextView intereses_pagados;
    private TextView intereses_pendientes;



    private HipotecaSeguimiento hip;
    private Button btn_cuadro_amortizacion;
    private ImageButton info_dinero_restante;
    private ImageView info_cuota;
    private ImageButton before_grafico;
    private ImageButton next_grafico;

    private LinearLayout capital_layout;
    private LinearLayout capital_layout_valor;
    private LinearLayout intereses_layout;
    private LinearLayout intereses_layout_valor;
    private final String TAG = "VisHipotecaSeg";

    private int[] colorClassArray = new int[] {Color.LTGRAY, Color.BLUE, Color.CYAN, Color.DKGRAY, Color.GREEN, Color.MAGENTA, Color.RED};

    private static int numGrafico = 0;
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
        construirGraficoLineas();
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

        btn_cuadro_amortizacion              = findViewById(R.id.btn_cuadro_amortizacion);
        info_dinero_restante                 = findViewById(R.id.btn_info_dinero_por_pagar);
        info_cuota                           = findViewById(R.id.btn_info_cuota);

        //GRÁFICOS
        titulo_grafico                       = findViewById(R.id.titulo_grafico);
        grafico                              = findViewById(R.id.grafico_seguimiento);
        next_grafico                         = findViewById(R.id.next_grafico);
        before_grafico                       = findViewById(R.id.before_grafico);
        capital_amortizado                   = findViewById(R.id.capital_amortizado_seguimiento_val);
        capital_pendiente                    = findViewById(R.id.capital_pendiente_seguimiento_val);
        intereses_pagados                    = findViewById(R.id.intereses_pagados_seguimiento_val);
        intereses_pendientes                 = findViewById(R.id.intereses_pendientes_seguimiento_val);
        capital_layout                       = findViewById(R.id.capital_layout);
        capital_layout_valor                 = findViewById(R.id.capital_layout_valor);
        intereses_layout                     = findViewById(R.id.intereses_layout);
        intereses_layout_valor               = findViewById(R.id.intereses_layout_valor);



    }

    private void rellenarUI(){

        info_cuota.setVisibility(View.GONE);

        DecimalFormat formato = new DecimalFormat("#.##"); // Establecer el formato a dos decimales
        String numeroFormateado = formato.format(hip.getDineroRestanteActual(hip.getNumeroCuotaActual()))  + "€"; // Formatear el número
        dinero_restante_a_pagar.setText(numeroFormateado);
        nombre_hipoteca.setText(hip.getNombre());
        tipo_hipoteca_seg.setText(hip.getTipo_hipoteca().substring(0, 1).toUpperCase() + hip.getTipo_hipoteca().substring(1));

        ArrayList<Integer> anios_meses = hip.getAniosMesesRestantes();
        if(anios_meses.get(0) > 0) anios_restantes_hipoteca.setText(anios_meses.get(0) + " años " + anios_meses.get(1) + " meses");
        else anios_restantes_hipoteca.setText(anios_meses.get(1) + " meses");
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

        if(anios_meses.get(0) <= 0 && anios_meses.get(1) <= 0){
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

        next_grafico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(numGrafico == 0) camposGraficoAportarVsFinanciar(View.GONE);
                numGrafico++;
                if(numGrafico == 1) construirGraficoGastosTotales();
                else if(numGrafico == 2) construirGraficoLineas();
                else{
                    numGrafico = 0;
                    camposGraficoAportarVsFinanciar(View.VISIBLE);
                    construirGraficoAportadoVsAFinanciar();
                }

            }
        });

        before_grafico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(numGrafico == 0) camposGraficoAportarVsFinanciar(View.GONE);
                numGrafico--;
                if(numGrafico == -1){
                    numGrafico = 2;
                    construirGraficoLineas();
                }
                else if(numGrafico == 1) construirGraficoGastosTotales();
                else {
                    camposGraficoAportarVsFinanciar(View.VISIBLE);
                    construirGraficoAportadoVsAFinanciar();
                }


            }
        });

     }


    public void construirGraficoAportadoVsAFinanciar(){
        //grafico.clear();
        grafico.destroyDrawingCache();
        titulo_grafico.setText("Aportado vs a financiar");
        double capitalPendiente  = hip.getCapitalPendienteTotalActual(hip.getNumeroCuotaActual());
        double capitalAmortizado = (hip.getPrecio_vivienda() - hip.getCantidad_abonada()) - capitalPendiente;

        double interesesTotales    = hip.getInteresesHastaNumPago(hip.getPlazo_anios() * 12);
        double interesesPagados    = hip.getInteresesHastaNumPago(hip.getNumeroCuotaActual());
        double interesesPendientes = interesesTotales - interesesPagados;

        Pie pie = AnyChart.pie();
        List<DataEntry> data = new ArrayList<>();
        data.add(new ValueDataEntry("CAPITAL AMORTIZADO", capitalAmortizado));
        data.add(new ValueDataEntry("CAPITAL PENDIENTE", capitalPendiente));
        data.add(new ValueDataEntry("INTERESES PENDIENTES", interesesPendientes));
        data.add(new ValueDataEntry("INTERESES PAGADOS", interesesPagados));
        pie.data(data);
        pie.labels().fontSize(22);
        pie.labels().position("outside");
        pie.connectorLength(30);
        grafico.setChart(pie);
        grafico.invalidate();

        capital_amortizado.setText("" + Math.round(capitalAmortizado * 100.0) / 100.0 + "€");
        capital_pendiente.setText("" + capitalPendiente + "€");
        intereses_pagados.setText("" + interesesPagados + "€");
        intereses_pendientes.setText("" + interesesPendientes + "€");
    }

    private void camposGraficoAportarVsFinanciar(int view) {
        capital_layout.setVisibility(view);
        capital_layout_valor.setVisibility(view);
        intereses_layout.setVisibility(view);
        intereses_layout_valor.setVisibility(view);
    }



    public void construirGraficoGastosTotales(){
        //grafico.clear();
        grafico.destroyDrawingCache();
        titulo_grafico.setText("Gastos Totales");
        Pie pie = AnyChart.pie();
        List<DataEntry> data = new ArrayList<>();
        data.add(new ValueDataEntry("VINCULACIONES", hip.getTotalVinculacionesAnual()));
        data.add(new ValueDataEntry("OTROS GASTOS (GESTORÍA, COMISIONES, ...)", hip.getTotalGastos()));
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("impuestos").document(hip.getComunidad_autonoma()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        double iva_itp;
                        double ajd;
                        String impuestos = "IVA";
                        if(hip.getAntiguedad_vivienda().equals("nueva")){
                            //Calcular IVA
                            if(hip.getTipo_vivienda().equals("proteccion_oficial")) iva_itp = document.getDouble("IVA");
                            else iva_itp = document.getDouble("IVA_PO");
                            ajd = document.getDouble("AJD_OBRA_NUEVA");

                        }else{
                            //Calcular ITP
                            iva_itp = document.getDouble("ITP");
                            ajd = document.getDouble("AJD_HIPOTECA");
                            impuestos = "ITP";
                        }

                        iva_itp = hip.getPrecio_vivienda() * (iva_itp / 100);
                        ajd     = hip.getPrecio_vivienda() * (ajd / 100);

                        data.add(new ValueDataEntry(impuestos, iva_itp));
                        data.add(new ValueDataEntry("AJD", ajd));
                        pie.data(data);
                        pie.labels().fontSize(22);
                        pie.labels().position("outside");
                        pie.connectorLength(30);
                        grafico.setChart(pie);
                        grafico.invalidate();

                    } else {
                        Log.e(TAG,"El documento no existe");
                    }
                }else Log.e(TAG,"Error al obtener datos del documento");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG,"Error al traer impuestos de bd");
            }
        });

    }

    public void construirGraficoLineas(){
        //grafico.clear();
        grafico.destroyDrawingCache();
        titulo_grafico.setText("Evolucion de los intereses y el capital amortizado anual");

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
        lineChart.line(capitalAnual).name("Capital anual");
        lineChart.line(interesesAnuales).name("Intereses anuales");
        lineChart.line(cuotaAnual).name("Cuota anual");
        lineChart.line(vinculacionesAnules).name("Vinculaciones anuales");

        lineChart.legend().enabled(true);
        lineChart.legend().fontSize(14d);
        lineChart.legend().padding(10d, 10d, 10d, 10d);
        lineChart.legend().itemsLayout(LegendLayout.HORIZONTAL_EXPANDABLE);
        lineChart.legend().position("bottom");
        grafico.setChart(lineChart);

    }




}

