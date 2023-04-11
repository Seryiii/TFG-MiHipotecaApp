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
import es.MiHipotecaApp.TFG.UsuarioRegistrado.HipotecasSeguimiento.Graficos.grafico_gastos_totales;
import es.MiHipotecaApp.TFG.UsuarioRegistrado.HipotecasSeguimiento.Graficos.grafico_intereses_capital;

public class VisualizarHipotecaSeguimiento extends AppCompatActivity {

    private TextView nombre_hipoteca;
    private TextView tipo_hipoteca_seg;
    private ImageView logo_banco_seg;
    private TextView comunidad_autonoma_seg;
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
    private LinearLayout capital_layout;
    private LinearLayout capital_layout_valor;
    private LinearLayout intereses_layout;
    private LinearLayout intereses_layout_valor;
    private LinearLayout btn_grafico_gastos_totales;
    private LinearLayout btn_grafico_intereses_capital;

    //private int[] colorClassArray = new int[] {Color.LTGRAY, Color.BLUE, Color.CYAN, Color.DKGRAY, Color.GREEN, Color.MAGENTA, Color.RED};

    //private static int numGrafico = 0;
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
        construirGraficoAportadoVsAFinanciar();
    }

    private void initUI(){
        dinero_restante_a_pagar   = findViewById(R.id.label_cantidad_pendiente_seguimiento_hip);
        nombre_hipoteca           = findViewById(R.id.nombre_seguimiento_hipoteca);
        tipo_hipoteca_seg         = findViewById(R.id.tipo_hipoteca_seguimiento);
        logo_banco_seg            = findViewById(R.id.logo_banco_seg);
        comunidad_autonoma_seg    = findViewById(R.id.comunidad_autonoma_seg);
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
        capital_amortizado                   = findViewById(R.id.capital_amortizado_seguimiento_val);
        capital_pendiente                    = findViewById(R.id.capital_pendiente_seguimiento_val);
        intereses_pagados                    = findViewById(R.id.intereses_pagados_seguimiento_val);
        intereses_pendientes                 = findViewById(R.id.intereses_pendientes_seguimiento_val);
        capital_layout                       = findViewById(R.id.capital_layout);
        capital_layout_valor                 = findViewById(R.id.capital_layout_valor);
        intereses_layout                     = findViewById(R.id.intereses_layout);
        intereses_layout_valor               = findViewById(R.id.intereses_layout_valor);

        btn_grafico_gastos_totales           = findViewById(R.id.btn_grafico_gastos_totales);
        btn_grafico_intereses_capital        = findViewById(R.id.btn_grafico_intereses_capital);

    }

    private void rellenarUI(){

        info_cuota.setVisibility(View.GONE);

        DecimalFormat formato = new DecimalFormat("#.##"); // Establecer el formato a dos decimales
        String numeroFormateado = formato.format(hip.getDineroRestanteActual(hip.getNumeroCuotaActual()))  + "€"; // Formatear el número
        dinero_restante_a_pagar.setText(numeroFormateado);
        nombre_hipoteca.setText(hip.getNombre());
        tipo_hipoteca_seg.setText(hip.getTipo_hipoteca().substring(0, 1).toUpperCase() + hip.getTipo_hipoteca().substring(1));
        ponerLogoBanco();
        comunidad_autonoma_seg.setText(hip.getComunidad_autonoma());
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

    private void ponerLogoBanco(){
        switch (hip.getBanco_asociado()){
            case "ING":
                logo_banco_seg.setImageResource(R.drawable.logo_ing);
                break;
            case "SANTANDER":
                logo_banco_seg.setImageResource(R.drawable.logo_santander);
                break;
            case "BBVA":
                logo_banco_seg.setImageResource(R.drawable.logo_bbva);
                break;
            case "CAIXABANK":
                logo_banco_seg.setImageResource(R.drawable.logo_caixabank);
                break;
            case "BANKINTER":
                logo_banco_seg.setImageResource(R.drawable.logo_bankinter);
                break;
            case "EVO BANCO":
                logo_banco_seg.setImageResource(R.drawable.logo_evo_banco);
                break;
            case "SABADELL":
                logo_banco_seg.setImageResource(R.drawable.logo_sabadell);
                break;
            case "UNICAJA":
                logo_banco_seg.setImageResource(R.drawable.logo_unicaja);
                break;
            case "DEUTSCHE BANK":
                logo_banco_seg.setImageResource(R.drawable.logo_deutsche_bank);
                break;
            case "OPEN BANK":
                logo_banco_seg.setImageResource(R.drawable.logo_open_bank);
                break;
            case "KUTXA BANK":
                logo_banco_seg.setImageResource(R.drawable.logo_kutxa_bank);
                break;
            case "IBERCAJA":
                logo_banco_seg.setImageResource(R.drawable.logo_ibercaja);
                break;
            case "ABANCA":
                logo_banco_seg.setImageResource(R.drawable.logo_abanca);
                break;
        }
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

        btn_grafico_gastos_totales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(VisualizarHipotecaSeguimiento.this, grafico_gastos_totales.class);
                i.putExtra("tipo_hipoteca", hip.getTipo_hipoteca());
                i.putExtra("hipoteca", hip);
                startActivity(i);
            }
        });

        btn_grafico_intereses_capital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(VisualizarHipotecaSeguimiento.this, grafico_intereses_capital.class);
                i.putExtra("tipo_hipoteca", hip.getTipo_hipoteca());
                i.putExtra("hipoteca", hip);
                startActivity(i);
            }
        });

        /*
        next_grafico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(numGrafico == 0) camposGraficoAportarVsFinanciar(View.GONE);
                numGrafico++;
                if(numGrafico == 1) construirGraficoLineas();
                else if(numGrafico == 2) construirGraficoLineas();
                else{
                    numGrafico = 0;
                    camposGraficoAportarVsFinanciar(View.VISIBLE);
                    construirGraficoAportadoVsAFinanciar();
                }

            }
        });*/

        /*

        before_grafico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(numGrafico == 0) camposGraficoAportarVsFinanciar(View.GONE);
                numGrafico--;
                if(numGrafico == -1){
                    numGrafico = 2;
                    construirGraficoLineas();
                }
                else if(numGrafico == 1) construirGraficoLineas();
                else {
                    camposGraficoAportarVsFinanciar(View.VISIBLE);
                    construirGraficoAportadoVsAFinanciar();
                }
            }
        });*/

     }


    public void construirGraficoAportadoVsAFinanciar(){

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
        pie.labels().fontSize(18);
        pie.labels().position("outside");
        pie.connectorLength(20);
        //pie.width(400);
        grafico.setChart(pie);
        grafico.invalidate();

        capital_amortizado.setText("" + Math.round(capitalAmortizado * 100.0) / 100.0 + "€");
        capital_pendiente.setText("" + capitalPendiente + "€");
        intereses_pagados.setText("" + interesesPagados + "€");
        intereses_pendientes.setText("" + interesesPendientes + "€");
    }

    /*
    private void camposGraficoAportarVsFinanciar(int view) {
        capital_layout.setVisibility(view);
        capital_layout_valor.setVisibility(view);
        intereses_layout.setVisibility(view);
        intereses_layout_valor.setVisibility(view);
    }*/
}

