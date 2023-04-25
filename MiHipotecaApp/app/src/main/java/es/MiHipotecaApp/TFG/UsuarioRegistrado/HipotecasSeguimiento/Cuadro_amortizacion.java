package es.MiHipotecaApp.TFG.UsuarioRegistrado.HipotecasSeguimiento;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import es.MiHipotecaApp.TFG.R;
import es.MiHipotecaApp.TFG.Transfers.HipotecaSegFija;
import es.MiHipotecaApp.TFG.Transfers.HipotecaSegMixta;
import es.MiHipotecaApp.TFG.Transfers.HipotecaSegVariable;
import es.MiHipotecaApp.TFG.Transfers.HipotecaSeguimiento;
import es.MiHipotecaApp.TFG.UsuarioRegistrado.InformacionUsuario.custom_dialog_avatares;

public class Cuadro_amortizacion extends AppCompatActivity implements custom_dialog_anios.customDialogInterface{

    private CircleImageView closeIcon;
    private TextView year_of_calendar;
    private ImageButton before_year;
    private ImageButton next_year;
    private ImageButton choose_year;
    private TableLayout tabla_cuadro_amortizacion;
    private TableLayout tabla_cuadro_amortizacion_anual;
    private HipotecaSeguimiento hip;
    private String[] meses;
    private ImageButton btn_fade_out_1;
    private ImageButton btn_fade_out_2;
    private View primeraColumna1;
    private View primeraColumna2;
    private boolean primeraTablaVisible;

    private HashMap<Integer, List<Object>> amortizaciones_hip;

    private List<Double> euribors;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuadro_amortizacion);
        closeIcon = findViewById(R.id.close_icon_cuadro);

        meses = new String[]{"Ene", "Feb", "Mar", "Abr", "May", "Jun", "Jul", "Ago", "Sep", "Oct", "Nov", "Dic"};

        year_of_calendar = findViewById(R.id.year_of_calendar);

        before_year = findViewById(R.id.before_year);
        next_year = findViewById(R.id.next_year);
        choose_year = findViewById(R.id.choose_year);
        tabla_cuadro_amortizacion = findViewById(R.id.tabla_cuadro_amortizacion);
        tabla_cuadro_amortizacion_anual = findViewById(R.id.tabla_cuadro_amortizacion_anual);
        btn_fade_out_1 = findViewById(R.id.btn_fade_out_1);
        btn_fade_out_1.setImageResource(R.drawable.drop_up);
        btn_fade_out_2 = findViewById(R.id.btn_fade_out_2);
        primeraColumna1 = null;
        primeraColumna2 = null;
        primeraTablaVisible = true;
        //Obtenemos la hipoteca de la que vamos a sacar el cuadro de amortización
        if(getIntent().getStringExtra("tipo_hipoteca").equals("fija")) hip = (HipotecaSegFija) getIntent().getSerializableExtra("hipoteca");
        else if (getIntent().getStringExtra("tipo_hipoteca").equals("variable")) hip = (HipotecaSegVariable) getIntent().getSerializableExtra("hipoteca");
        else hip = (HipotecaSegMixta) getIntent().getSerializableExtra("hipoteca");

        amortizaciones_hip = (HashMap<Integer, List<Object>>) getIntent().getSerializableExtra("amortizaciones_anticipadas");
        euribors = (List<Double>) getIntent().getSerializableExtra("euribors");

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(hip.getFecha_inicio());
        /** Da valor al TextView del año mostrado en el calendario, si la hipoteca ya ha empezado, muestra el año actual
        si no ha empezado, muestra el primer año de hipoteca **/
        if(calendar.get(Calendar.YEAR) > Calendar.getInstance().get(Calendar.YEAR)) year_of_calendar.setText(Integer.toString(calendar.get(Calendar.YEAR)));
        else year_of_calendar.setText(Integer.toString(Calendar.getInstance().get(Calendar.YEAR)));


        actualizarTablaMeses(Integer.parseInt((String) year_of_calendar.getText()));
        actualizarTablaAnios();
        eventos();

        contraeTabla2();
    }

    /** Funcion a la que se llama cuando el textView que marca el año mostrado en el calendario cambia, ya sea
        por alguno de los botones laterales o por el uso de la seekBar **/
    public void actualizarTablaMeses(int anio){

        // ADAPTARLO A QUE PARE CUANDO CAP PENDIENTE SEA 0

        DecimalFormat formato = new DecimalFormat("#.##"); // Establecer el formato a dos decimales

        //Obtiene el numero de cuota de enero del año mostrado en el textView
        int numCuotaEnero = hip.getNumeroCuotaEnEnero(anio);
        //Elimina las filas de la tabla a excepcion de la primera
        if(tabla_cuadro_amortizacion.getChildCount() > 0) tabla_cuadro_amortizacion.removeViews(1, tabla_cuadro_amortizacion.getChildCount() - 1);
        else tabla_cuadro_amortizacion.addView(primeraColumna1);
        for (int i = 0; i < 12; i++) {

            // Crear una nueva fila y agregarla al TableLayout
            TableRow tableRow = new TableRow(this);
            tabla_cuadro_amortizacion.addView(tableRow);

            //Solo crea las filas si existe ese numero de cuota
            double capPdteCuota = hip.getCapitalPendienteTotalActual(numCuotaEnero + i - 1, amortizaciones_hip, euribors);
            double cuota_actual = hip.cogerCuotaActual(numCuotaEnero + i, amortizaciones_hip);

            if(numCuotaEnero + i > 0) {

                TextView numCuota = new TextView(this);
                numCuota.setText(Integer.toString(numCuotaEnero + i));
                numCuota.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                tableRow.addView(numCuota);

                TextView nombreMes = new TextView(this);
                nombreMes.setText(meses[i]);
                nombreMes.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                tableRow.addView(nombreMes);

                ArrayList<Double> valores = hip.getFilaCuadroAmortizacionMensual(numCuotaEnero + i, amortizaciones_hip, euribors);

                TextView cuota = new TextView(this);
                cuota.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

                TextView capital = new TextView(this);
                capital.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

                TextView interes = new TextView(this);
                interes.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

                TextView pendiente = new TextView(this);
                pendiente.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

                //Cuota normal
                if (capPdteCuota > cuota_actual){
                    // CUOTA, CAPITAL, INTERESES, CAPITAL PDTE
                    cuota.setText(formato.format(valores.get(0)));
                    capital.setText(formato.format(valores.get(1)));
                    interes.setText(formato.format(valores.get(2)));
                    pendiente.setText(formato.format(valores.get(3)));
                    tableRow.addView(cuota);
                    tableRow.addView(capital);
                    tableRow.addView(interes);
                    tableRow.addView(pendiente);
                }
                //Ultima cuota
                else {
                    //TODO CAMBIAR PORCENTAJES Y ADAPTARLO PARA TODAS LAS HIPOTECAS
                    cuota.setText(formato.format(capPdteCuota));
                    capital.setText(formato.format(hip.getCapitalAmortizadoMensual(capPdteCuota, capPdteCuota, hip.getPorcentaje_fijo())));
                    interes.setText(formato.format(hip.getInteresMensual(capPdteCuota, hip.getPorcentaje_fijo())));
                    pendiente.setText("0");
                    tableRow.addView(cuota);
                    tableRow.addView(capital);
                    tableRow.addView(interes);
                    tableRow.addView(pendiente);
                    break;
                }

            }
        }

    }

    public void actualizarTablaAnios(){
        if(primeraColumna2 != null) tabla_cuadro_amortizacion_anual.addView(primeraColumna2);
        DecimalFormat formato = new DecimalFormat("#.##"); // Establecer el formato a dos decimales

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(hip.getFecha_inicio());

        for (int i = 1; i <= hip.getPlazo_anios(); i++) {

            // TOTAL_ANUAL, CAPITAL_ANUAL, INTERESES_ANUALES, CAPITAL PDTE
            ArrayList<Double> valores = hip.getFilaCuadroAmortizacionAnual(calendar.get(Calendar.YEAR) + i - 1, i, amortizaciones_hip, euribors);
            if(valores.get(3) <= 0) break;

            // Crear una nueva fila y agregarla al TableLayout
            TableRow tableRow = new TableRow(this);
            tabla_cuadro_amortizacion_anual.addView(tableRow);

            TextView numAnio = new TextView(this);
            numAnio.setText(Integer.toString(i));
            numAnio.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            tableRow.addView(numAnio);


            TextView anio = new TextView(this);
            anio.setText(Integer.toString(calendar.get(Calendar.YEAR) + i - 1));
            anio.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            tableRow.addView(anio);


            TextView totalAnual = new TextView(this);
            totalAnual.setText(formato.format(valores.get(0)));
            totalAnual.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            tableRow.addView(totalAnual);

            TextView capitalAnual = new TextView(this);
            capitalAnual.setText(formato.format(valores.get(1)));
            capitalAnual.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            tableRow.addView(capitalAnual);

            TextView interesAnual = new TextView(this);
            interesAnual.setText(formato.format(valores.get(2)));
            interesAnual.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            tableRow.addView(interesAnual);

            TextView pendiente = new TextView(this);
            pendiente.setText(formato.format(valores.get(3)));
            pendiente.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            tableRow.addView(pendiente);


        }
    }

    public void eventos(){

        closeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { finish(); }
        });
        choose_year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                custom_dialog_anios custom = new custom_dialog_anios(hip);
                custom.show(getSupportFragmentManager(),"Custom Dialog");
            }
        });

        before_year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(hip.getFecha_inicio());
                if (Integer.parseInt(String.valueOf(year_of_calendar.getText())) - 1 >= calendar.get(Calendar.YEAR)){
                    year_of_calendar.setText(Integer.toString(Integer.parseInt(String.valueOf(year_of_calendar.getText())) - 1));
                    actualizarTablaMeses(Integer.parseInt((String) year_of_calendar.getText()));
                }
            }
        });

        next_year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(hip.getFecha_inicio());
                int aniosActuales = hip.aniosActualesHipoteca(hip.getPlazoActual(amortizaciones_hip));
                if(Integer.parseInt(String.valueOf(year_of_calendar.getText())) + 1 < calendar.get(Calendar.YEAR) + aniosActuales) {
                    year_of_calendar.setText(Integer.toString(Integer.parseInt(String.valueOf(year_of_calendar.getText())) + 1));
                    actualizarTablaMeses(Integer.parseInt((String) year_of_calendar.getText()));
                }
            }
        });

        btn_fade_out_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!primeraTablaVisible) {
                    // Agregar filas al TableLayout
                    actualizarTablaMeses(Integer.parseInt((String) year_of_calendar.getText()));
                    contraeTabla2();
                    primeraTablaVisible = true;
                    btn_fade_out_1.setImageResource(R.drawable.drop_up);
                    btn_fade_out_2.setImageResource(R.drawable.drop_down);
                    next_year.setEnabled(true);
                    before_year.setEnabled(true);
                    choose_year.setEnabled(true);

                    // Animar el TableLayout
                    Animation animation = AnimationUtils.loadAnimation(Cuadro_amortizacion.this, android.R.anim.fade_in);
                    tabla_cuadro_amortizacion.startAnimation(animation);
                }
            }
        });

        btn_fade_out_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (primeraTablaVisible) {
                    // Agregar filas al TableLayout
                    actualizarTablaAnios();
                    contraeTabla1();
                    primeraTablaVisible = false;
                    btn_fade_out_2.setImageResource(R.drawable.drop_up);
                    btn_fade_out_1.setImageResource(R.drawable.drop_down);
                    next_year.setEnabled(false);
                    before_year.setEnabled(false);
                    choose_year.setEnabled(false);

                    // Animar el TableLayout
                    Animation animation = AnimationUtils.loadAnimation(Cuadro_amortizacion.this, android.R.anim.fade_in);
                    tabla_cuadro_amortizacion_anual.startAnimation(animation);
                }
            }
        });
    }

    public void contraeTabla1(){
        // Eliminar todas las filas del TableLayout
        primeraColumna1 = tabla_cuadro_amortizacion.getChildAt(0);
        tabla_cuadro_amortizacion.removeAllViews();
    }
    public void contraeTabla2(){
        // Eliminar todas las filas del TableLayout
        primeraColumna2 = tabla_cuadro_amortizacion_anual.getChildAt(0);
        tabla_cuadro_amortizacion_anual.removeAllViews();
    }


    @Override
    public void setAnio(int anio) {
        year_of_calendar.setText(Integer.toString(anio));
        actualizarTablaMeses(anio);
    }
}