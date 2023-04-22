package es.MiHipotecaApp.TFG.UsuarioRegistrado.HipotecasSeguimiento;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.skydoves.balloon.ArrowOrientation;
import com.skydoves.balloon.ArrowPositionRules;
import com.skydoves.balloon.Balloon;
import com.skydoves.balloon.BalloonAnimation;
import com.skydoves.balloon.BalloonSizeSpec;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.MiHipotecaApp.TFG.R;
import es.MiHipotecaApp.TFG.Transfers.HipotecaSegFija;
import es.MiHipotecaApp.TFG.Transfers.HipotecaSegMixta;
import es.MiHipotecaApp.TFG.Transfers.HipotecaSegVariable;
import es.MiHipotecaApp.TFG.Transfers.HipotecaSeguimiento;
import es.MiHipotecaApp.TFG.UsuarioRegistrado.CustomDialogoPremium;
import es.MiHipotecaApp.TFG.UsuarioRegistrado.HipotecasSeguimiento.Graficos.grafico_gastos_totales;
import es.MiHipotecaApp.TFG.UsuarioRegistrado.HipotecasSeguimiento.Graficos.grafico_intereses_capital;

public class VisualizarHipotecaSeguimiento extends AppCompatActivity implements NuevaVinculacionAnualFragment.NuevoAnioHipotecaListener {
    private final String TAG = "VIS_HIP_ACTIVITY";

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
    private HashMap<Integer, List<Object>> amortizaciones_anticipadas;
    private Button btn_cuadro_amortizacion;
    private Button btn_amortizar_antes;

    private ImageButton info_dinero_restante;
    private ImageView info_cuota;
    private LinearLayout capital_layout;
    private LinearLayout capital_layout_valor;
    private LinearLayout intereses_layout;
    private LinearLayout intereses_layout_valor;
    private LinearLayout layout_cuota_seguimiento;
    private LinearLayout layout_amortizacion_anticipada;
    private LinearLayout layout_capital_intereses1;
    private LinearLayout layout_capital_intereses2;
    private TextView amortizacion_anticipada_valor;
    private ImageButton btn_grafico_gastos_totales;
    private ImageButton btn_grafico_intereses_capital;

    private String cuotaFormateada;
    private double porcentaje_aplicado;
    private int numero_cuotas_restantes;
    private double cantidad_pendiente;

    private int numero_cuotas_pagadas;

    private String[] comunidades = new String[]{"Andalucía", "Aragón", "Asturias", "Baleares", "Canarias", "Cantabria", "Castilla La Mancha", "Castilla León", "Cataluña", "Ceuta", "Comunidad de Madrid", "Comunidad Valenciana", "Extremadura", "Galicia", "La Rioja", "Melilla", "Murcia", "Navarra", "País Vasco"};
    private String[] comunidades_base_datos = new String[]{"Andalucía", "Aragón", "Asturias", "Baleares", "Canarias", "Cantabria", "Castilla_La_Mancha", "Castilla_León", "Cataluña", "Ceuta", "Madrid", "Comunidad_Valenciana", "Extremadura", "Galicia", "La_Rioja", "Melilla", "Murcia", "Navarra", "País_Vasco"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_hipoteca_seguimiento);
        if(getIntent().getStringExtra("tipo_hipoteca").equals("fija")) hip = (HipotecaSegFija) getIntent().getSerializableExtra("hipoteca");
        else if (getIntent().getStringExtra("tipo_hipoteca").equals("variable")) hip = (HipotecaSegVariable) getIntent().getSerializableExtra("hipoteca");
        else hip = (HipotecaSegMixta) getIntent().getSerializableExtra("hipoteca");
        initUI();
        cogerAmortizaciones();
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
        layout_amortizacion_anticipada = findViewById(R.id.layout_amortizacion_anticipada);
        amortizacion_anticipada_valor = findViewById(R.id.amortizacion_anticipada_valor);
        layout_cuota_seguimiento = findViewById(R.id.layout_cuota_seguimiento);

        btn_cuadro_amortizacion              = findViewById(R.id.btn_cuadro_amortizacion);
        btn_amortizar_antes                  = findViewById(R.id.btn_amortizar_antes);
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

    private void cogerAmortizaciones(){

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        CollectionReference amortizacionesRef = db.collection("amortizaciones_anticipadas");

        Query query = amortizacionesRef.whereEqualTo("nombre_hipoteca", hip.getNombre()).whereEqualTo("idUsuario", firebaseAuth.getCurrentUser().getUid());
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    QuerySnapshot querySnapshot = task.getResult();
                    if(!querySnapshot.isEmpty()){
                        DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);
                        Map<String, Object> data = documentSnapshot.getData();

                        Map<String, Object> amortizaciones = (Map<String, Object>) data.get("amortizaciones_anticipadas");

                        amortizaciones_anticipadas = new HashMap<>();
                        for (String clave : amortizaciones.keySet()) {
                            Integer claveInt = Integer.parseInt(clave);
                            List<Object> lista = (List<Object>) amortizaciones.get(clave);
                            amortizaciones_anticipadas.put(claveInt, lista);
                        }
                        hip.getEuriborPasado(1, amortizaciones_anticipadas);
                        rellenarUI();
                        eventos();
                        construirGraficoAportadoVsAFinanciar();
                    }
                }else Log.e(TAG, "Error al cargar amortizaciones anticipadas", task.getException());

            }
        });


    }

    private void rellenarUI(){

        numero_cuotas_pagadas = hip.getNumeroCuotaActual(amortizaciones_anticipadas);
        info_cuota.setVisibility(View.GONE);

        DecimalFormat formato = new DecimalFormat("#.##"); // Establecer el formato a dos decimales
        String numeroFormateado = formato.format(hip.getDineroRestanteActual(numero_cuotas_pagadas, amortizaciones_anticipadas))  + "€"; // Formatear el número
        dinero_restante_a_pagar.setText(numeroFormateado);
        nombre_hipoteca.setText(hip.getNombre());
        tipo_hipoteca_seg.setText(hip.getTipo_hipoteca().substring(0, 1).toUpperCase() + hip.getTipo_hipoteca().substring(1));
        ponerLogoBanco();
        int i = 0;
        while(i < comunidades_base_datos.length){
            if(hip.getComunidad_autonoma().equals(comunidades_base_datos[i])) break;
            i++;
        }
        comunidad_autonoma_seg.setText(comunidades[i]);
        ArrayList<Integer> anios_meses = hip.getAniosMesesRestantes(amortizaciones_anticipadas);
        if(anios_meses.get(0) > 0) anios_restantes_hipoteca.setText(anios_meses.get(0) + " años " + anios_meses.get(1) + " meses");
        else anios_restantes_hipoteca.setText(anios_meses.get(1) + " meses");
        mes_actual_cuota.setText(hip.getNombreMesActual(amortizaciones_anticipadas));
        int plazoTotalActual = hip.getPlazoActual(amortizaciones_anticipadas);



        cantidad_pendiente = hip.getCapitalPendienteTotalActual(numero_cuotas_pagadas,amortizaciones_anticipadas);
        numero_cuotas_restantes = hip.getPlazoActual(amortizaciones_anticipadas) - numero_cuotas_pagadas;

        if(hip.getTipo_hipoteca().equals("fija")) {
            porcentaje_aplicado  = hip.getPorcentaje_fijo();
            info_dinero_restante.setVisibility(View.GONE);
        } else if(hip.getTipo_hipoteca().equals("variable")) {
            //Si cumple la condicion, esta aplicando el primer porcentaje fijado, en otro caso el diferencial + euribor
            porcentaje_aplicado  = hip.getNumeroCuotaActual(amortizaciones_anticipadas) <= hip.getDuracion_primer_porcentaje_variable() ? hip.getPrimer_porcentaje_variable() : hip.getEuriborActual() + hip.getPorcentaje_diferencial_variable();
        } else {
            //Si cumple la condicion, esta en la fase fija, en otro en la variable
            porcentaje_aplicado  = hip.getNumeroCuotaActual(amortizaciones_anticipadas) <= hip.getAnios_fija_mixta() * 12 ? hip.getPorcentaje_fijo_mixta() : hip.getEuriborActual() + hip.getPorcentaje_diferencial_mixta();
        }

        if (hip.siguienteCuotaRevision(amortizaciones_anticipadas)) info_cuota.setVisibility(View.VISIBLE);

        if(anios_meses.get(0) <= 0 && anios_meses.get(1) <= 0){
            numero_cuotas_restantes = 0;
            info_cuota.setVisibility(View.GONE);
        }
        //la cuota se calcula mal
        double cuota_mensual = hip.getCuotaMensual(porcentaje_aplicado, cantidad_pendiente, numero_cuotas_restantes);
        //SI HAY AMORTIZACION EN LA SIGUIENTE CUOTA
        if(amortizaciones_anticipadas.containsKey(numero_cuotas_pagadas + 1)){
            double amortizacion_ant = (Double) amortizaciones_anticipadas.get(numero_cuotas_pagadas + 1).get(1);
            layout_amortizacion_anticipada.setVisibility(View.VISIBLE);
            if(amortizaciones_anticipadas.get(numero_cuotas_pagadas + 1).get(0).equals("total")) {
                plazoTotalActual = numero_cuotas_pagadas + 1;
                layout_cuota_seguimiento.setVisibility(View.GONE);
                layout_capital_intereses1.setVisibility(View.GONE);
                layout_capital_intereses2.setVisibility(View.GONE);
            }else if(amortizaciones_anticipadas.get(numero_cuotas_pagadas + 1).get(0).equals("parcial_plazo")){
                plazoTotalActual -= (Long) amortizaciones_anticipadas.get(numero_cuotas_pagadas + 1).get(2);
            }
            amortizacion_anticipada_valor.setText(formato.format(amortizacion_ant) + "€");
        }else layout_amortizacion_anticipada.setVisibility(View.GONE);

        numero_cuota_actual.setText("Cuotas pagadas: " + numero_cuotas_pagadas + " / " + plazoTotalActual);

        cuotaFormateada = formato.format(cuota_mensual) + "€"; // Formatear el número
        cuota_mensual_seguimiento.setText(cuotaFormateada);

        double capitalPendiente = hip.getCapitalPendienteTotalActual(hip.getNumeroCuotaActual(amortizaciones_anticipadas), amortizaciones_anticipadas);
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
            default:
                logo_banco_seg.setImageResource(R.drawable.logo_bancodesconocido);
                break;
        }
    }
    private void eventos(){

        compruebaSiVinculacionAnual();

        btn_cuadro_amortizacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(VisualizarHipotecaSeguimiento.this, Cuadro_amortizacion.class);
                i.putExtra("hipoteca", hip);
                i.putExtra("tipo_hipoteca", hip.getTipo_hipoteca());
                i.putExtra("amortizaciones_anticipadas", (Serializable) amortizaciones_anticipadas);
                startActivity(i);
            }
        });

        btn_amortizar_antes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Comprobar si la siguiente cuota es la ultima que no deje amortizar
                if(hip.getNumeroCuotaActual(amortizaciones_anticipadas) < hip.getPlazoActual(amortizaciones_anticipadas) - 1){
                    //Comprobar si ya hay una amortización para la siguiente cuota
                    if(amortizaciones_anticipadas.containsKey(hip.getNumeroCuotaActual(amortizaciones_anticipadas) + 1)){
                        Toast.makeText(VisualizarHipotecaSeguimiento.this, getString(R.string.amortizacion_existente), Toast.LENGTH_LONG).show();
                    }
                    else{
                        Intent i = new Intent(VisualizarHipotecaSeguimiento.this, AmortizarAntes.class);
                        i.putExtra("cuota_actual", cuotaFormateada);
                        i.putExtra("porcentaje_aplicado", porcentaje_aplicado);
                        i.putExtra("cuotas_pendientes", numero_cuotas_restantes);
                        i.putExtra("cantidad_pendiente", cantidad_pendiente);
                        i.putExtra("amortizaciones_anticipadas", (Serializable) amortizaciones_anticipadas);
                        i.putExtra("hipoteca", hip);
                        i.putExtra("tipo_hipoteca", hip.getTipo_hipoteca());
                        startActivity(i);
                    }
                }else Toast.makeText(VisualizarHipotecaSeguimiento.this, getString(R.string.no_puede_amortizar), Toast.LENGTH_LONG).show();

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
                if(!btn_grafico_gastos_totales.isEnabled()){
                    CustomDialogoPremium dialogo = new CustomDialogoPremium();
                    dialogo.show(getSupportFragmentManager(), "dialogo");
                }else {
                    Intent i = new Intent(VisualizarHipotecaSeguimiento.this, grafico_gastos_totales.class);
                    i.putExtra("tipo_hipoteca", hip.getTipo_hipoteca());
                    i.putExtra("hipoteca", hip);
                    i.putExtra("amortizaciones_anticipadas", amortizaciones_anticipadas);
                    startActivity(i);
                }
            }
        });

        btn_grafico_intereses_capital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!btn_grafico_intereses_capital.isEnabled()){
                    CustomDialogoPremium dialogo = new CustomDialogoPremium();
                    dialogo.show(getSupportFragmentManager(), "dialogo");
                }else {
                    Intent i = new Intent(VisualizarHipotecaSeguimiento.this, grafico_intereses_capital.class);
                    i.putExtra("tipo_hipoteca", hip.getTipo_hipoteca());
                    i.putExtra("hipoteca", hip);
                    i.putExtra("amortizaciones_anticipadas", amortizaciones_anticipadas);
                    startActivity(i);
                }
            }
        });
     }


    public void construirGraficoAportadoVsAFinanciar(){
        DecimalFormat formato = new DecimalFormat("#.##"); // Establecer el formato a dos decimales

        titulo_grafico.setText("Aportado vs a financiar");
        double capitalPendiente  = hip.getCapitalPendienteTotalActual(hip.getNumeroCuotaActual(amortizaciones_anticipadas), amortizaciones_anticipadas);
        double capitalAmortizado = (hip.getPrecio_vivienda() - hip.getCantidad_abonada()) - capitalPendiente;

        double interesesTotales    = hip.getInteresesHastaNumPago(hip.getPlazo_anios() * 12, amortizaciones_anticipadas);
        double interesesPagados    = hip.getInteresesHastaNumPago(hip.getNumeroCuotaActual(amortizaciones_anticipadas), amortizaciones_anticipadas);
        double interesesPendientes = interesesTotales - interesesPagados;

        Pie pie = AnyChart.pie();
        List<DataEntry> data = new ArrayList<>();
        data.add(new ValueDataEntry("CAPITAL AMORTIZADO", Double.parseDouble(formato.format(capitalAmortizado))));
        data.add(new ValueDataEntry("CAPITAL PENDIENTE", Double.parseDouble(formato.format(capitalPendiente))));
        data.add(new ValueDataEntry("INTERESES PENDIENTES", Double.parseDouble(formato.format(interesesPendientes))));
        data.add(new ValueDataEntry("INTERESES PAGADOS", Double.parseDouble(formato.format(interesesPagados))));
        pie.data(data);
        pie.labels().fontSize(18);
        pie.labels().position("outside");
        pie.connectorLength(20);
        //pie.width(400);
        grafico.setChart(pie);
        grafico.invalidate();


        capital_amortizado.setText("" + formato.format(capitalAmortizado) + "€");
        capital_pendiente.setText("" + formato.format(capitalPendiente) + "€");
        intereses_pagados.setText("" + formato.format(interesesPagados) + "€");
        intereses_pendientes.setText("" + formato.format(interesesPendientes) + "€");

        //COMPRUEBA SI EL USUARIO ES NO PREMIUM
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userMail = user.getEmail();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Query query = db.collection("usuarios").whereEqualTo("correo", userMail);

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot querySnapshot = task.getResult();
                    DocumentSnapshot document = querySnapshot.getDocuments().get(0);
                    if(!document.getBoolean("premium")) {
                        //Al no ser premium no deja interactuar con los graficos
                        grafico.setAlpha(0.1f); //Hace que el grafico se vea menos
                        grafico.setEnabled(false);
                        btn_grafico_gastos_totales.setAlpha(0.8f);
                        btn_grafico_gastos_totales.setEnabled(false);
                        btn_grafico_intereses_capital.setAlpha(0.8f);
                        btn_grafico_intereses_capital.setEnabled(false);
                    }

                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });

    }

    @Override
    public void sendInput(double input) {

        Map<String, Object> nuevosDatos = new HashMap<>();
        hip.getArrayVinculacionesAnual().add(input);
        nuevosDatos.put("arrayVinculacionesAnual", hip.getArrayVinculacionesAnual());


        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // obtener una referencia a la colección
        CollectionReference hipotecasRef = db.collection("hipotecas_seguimiento");

        // crear una consulta para obtener el documento específico
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        Query query = hipotecasRef.whereEqualTo("nombre", hip.getNombre()).whereEqualTo("idUsuario", firebaseAuth.getCurrentUser().getUid());

        // ejecutar la consulta
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        // actualizar el documento con los nuevos valores
                        hipotecasRef.document(document.getId()).update(nuevosDatos);
                        Toast.makeText(VisualizarHipotecaSeguimiento.this, getString(R.string.vinculaciones_actualizdas), Toast.LENGTH_LONG).show();
                        /*Intent i = new Intent(VisualizarHipotecaSeguimiento.this, PaginaPrincipal.class);
                        startActivity(i);*/
                    }
                } else {
                    Log.e("ERROR", " getting documents");
                }
            }
        });

    }

    /** Muestra un dialogo en el que el usuario tiene que introducir la nueva cantidad de viculaciones anuales
        si es un nuevo año de hipoteca y el usuario no lo ha introducido aun **/
    public void compruebaSiVinculacionAnual(){
        Calendar fecha = Calendar.getInstance();
        fecha.setTime(hip.getFecha_inicio());
        int i = fecha.get(Calendar.DAY_OF_YEAR) + 1; //dia de inicio de la hipoteca + 1
        if(fecha.get(Calendar.YEAR)%4 == 0 && fecha.get(Calendar.DAY_OF_YEAR) > 59) i = i - 1; //comprueba si año bisiesto

        //calculo la variable aniosHastaAhora, que tiene el numero de años + 1  que llevamos de hipoteca
        int c = hip.getPlazo_anios() - getAniosMesesRestantes(hip.getPlazo_anios(), getNumeroCuotaActual(hip.getFecha_inicio(), hip.getPlazo_anios())).get(0);

        if(i <= Calendar.getInstance().get(Calendar.DAY_OF_YEAR) && hip.getArrayVinculacionesAnual().size() < c) {
            NuevaVinculacionAnualFragment fragment = new NuevaVinculacionAnualFragment();
            fragment.show(getSupportFragmentManager(), "NuevaVinculacionAnualFragment");
        }
    }

    /** Esta funcion devuelve los años y meses que quedan de hipoteca**/
    public ArrayList<Integer> getAniosMesesRestantes(int plazo, int cuota){

        ArrayList<Integer> anios_meses = new ArrayList<>();
        int cuotasRestantes = (plazo * 12) - cuota;
        int anios = cuotasRestantes / 12;
        int meses = cuotasRestantes % 12;
        anios_meses.add(anios);
        anios_meses.add(meses);
        return anios_meses;
    }

    /** Devuelve el numero de cuota por el que va actualmente la hipoteca [1 - plazo_anios * 12 ] **/
    public int getNumeroCuotaActual(Date fecha_inicio, int plazo){
        Calendar inicio = Calendar.getInstance();
        inicio.setTime(fecha_inicio);
        // Dia actual
        Calendar actual = Calendar.getInstance();
        // En caso de que todavia no haya empezado el seguimiento de la hipoteca
        if(actual.compareTo(inicio) < 0) return 0;
        int difA = actual.get(Calendar.YEAR) - inicio.get(Calendar.YEAR);
        int numeroPagoActual = difA * 12 + actual.get(Calendar.MONTH) - inicio.get(Calendar.MONTH);

        // Si el dia es el mismo que el de pago, devuelve como si ya ha pagado esa cuota
        if(actual.get(Calendar.DAY_OF_MONTH) >= inicio.get(Calendar.DAY_OF_MONTH)) numeroPagoActual = numeroPagoActual + 1; //Se le sumaria 1 debido a que ya ha pasado el dia de pago del mes correspondiente
        //else return numeroPagoActual + 1;
        // Fin de hipoteca
        if (numeroPagoActual >= plazo * 12) numeroPagoActual = plazo * 12;
        return numeroPagoActual;

    }
}

