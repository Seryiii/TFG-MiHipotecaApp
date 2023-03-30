package es.MiHipotecaApp.TFG.UsuarioRegistrado.HipotecasSeguimiento;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

import es.MiHipotecaApp.TFG.R;
import es.MiHipotecaApp.TFG.Transfers.HipotecaSegFija;
import es.MiHipotecaApp.TFG.Transfers.HipotecaSegMixta;
import es.MiHipotecaApp.TFG.Transfers.HipotecaSegVariable;
import es.MiHipotecaApp.TFG.Transfers.HipotecaSeguimiento;
import es.MiHipotecaApp.TFG.UsuarioRegistrado.InformacionUsuario.custom_dialog_avatares;

public class Cuadro_amortizacion extends AppCompatActivity implements custom_dialog_anios.customDialogInterface{

    private TextView year_of_calendar;
    private ImageButton before_year;
    private ImageButton next_year;
    private ImageButton choose_year;
    private TableLayout tabla_cuadro_amortizacion;
    private TableLayout tabla_cuadro_amortizacion_anual;
    private HipotecaSeguimiento hip;
    private String[] meses;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuadro_amortizacion);

        meses = new String[]{"Ene", "Feb", "Mar", "Abr", "May", "Jun", "Jul", "Ago", "Sep", "Oct", "Nov", "Dic"};

        year_of_calendar = findViewById(R.id.year_of_calendar);

        before_year = findViewById(R.id.before_year);
        next_year = findViewById(R.id.next_year);
        choose_year = findViewById(R.id.choose_year);
        tabla_cuadro_amortizacion = findViewById(R.id.tabla_cuadro_amortizacion);
        tabla_cuadro_amortizacion_anual = findViewById(R.id.tabla_cuadro_amortizacion_anual);

        //Obtenemos la hipoteca de la que vamos a sacar el cuadro de amortización
        if(getIntent().getStringExtra("tipo_hipoteca").equals("fija")) hip = (HipotecaSegFija) getIntent().getSerializableExtra("hipoteca");
        else if (getIntent().getStringExtra("tipo_hipoteca").equals("variable")) hip = (HipotecaSegVariable) getIntent().getSerializableExtra("hipoteca");
        else hip = (HipotecaSegMixta) getIntent().getSerializableExtra("hipoteca");


        Calendar calendar = Calendar.getInstance();
        calendar.setTime(hip.getFecha_inicio());
        /** Da valor al TextView del año mostrado en el calendario, si la hipoteca ya ha empezado, muestra el año actual
        si no ha empezado, muestra el primer año de hipoteca **/
        if(calendar.get(Calendar.YEAR) > Calendar.getInstance().get(Calendar.YEAR)) year_of_calendar.setText(Integer.toString(calendar.get(Calendar.YEAR)));
        else year_of_calendar.setText(Integer.toString(Calendar.getInstance().get(Calendar.YEAR)));


        actualizarTablaMeses(Integer.parseInt((String) year_of_calendar.getText()));
        actualizarTablaAnios();
        eventos();

    }

    /** Funcion a la que se llama cuando el textView que marca el año mostrado en el calendario cambia, ya sea
        por alguno de los botones laterales o por el uso de la seekBar **/
    public void actualizarTablaMeses(int anio){
        //Obtiene el numero de cuota de enero del año mostrado en el textView
        int numCuotaEnero = hip.getNumeroCuotaEnEnero(anio);
        //Elimina las filas de la tabla a excepcion de la primera
        tabla_cuadro_amortizacion.removeViews(1, tabla_cuadro_amortizacion.getChildCount() - 1);
        for (int i = 0; i < 12; i++) {

            // Crear una nueva fila y agregarla al TableLayout
            TableRow tableRow = new TableRow(this);
            tabla_cuadro_amortizacion.addView(tableRow);

            //Solo crea las filas si existe ese numero de cuota
            if(numCuotaEnero + i > 0 && numCuotaEnero + i <= hip.getPlazo_anios() * 12) {


                TextView numCuota = new TextView(this);
                numCuota.setText(Integer.toString(numCuotaEnero + i));
                numCuota.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                tableRow.addView(numCuota);

                TextView nombreMes = new TextView(this);
                nombreMes.setText(meses[i]);
                nombreMes.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                tableRow.addView(nombreMes);


                // CUOTA, CAPITAL, INTERESES, CAPITAL PDTE
                ArrayList<Double> valores = hip.getFilaCuadroAmortizacionMensual(numCuotaEnero + i);

                TextView cuota = new TextView(this);
                cuota.setText(Double.toString(valores.get(0)));
                cuota.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                tableRow.addView(cuota);

                TextView capital = new TextView(this);
                capital.setText(Double.toString(valores.get(1)));
                capital.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                tableRow.addView(capital);

                TextView interes = new TextView(this);
                interes.setText(Double.toString(valores.get(2)));
                interes.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                tableRow.addView(interes);

                TextView pendiente = new TextView(this);
                pendiente.setText(Double.toString(valores.get(3)));
                pendiente.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                tableRow.addView(pendiente);
            }

        }



    }

    public void actualizarTablaAnios(){
        for (int i = 1; i <= hip.getPlazo_anios(); i++) {

            // Crear una nueva fila y agregarla al TableLayout
            TableRow tableRow = new TableRow(this);
            tabla_cuadro_amortizacion_anual.addView(tableRow);

            TextView numAnio = new TextView(this);
            numAnio.setText(Integer.toString(i));
            numAnio.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            tableRow.addView(numAnio);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(hip.getFecha_inicio());
            TextView anio = new TextView(this);
            anio.setText(Integer.toString(calendar.get(Calendar.YEAR) + i - 1));
            anio.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            tableRow.addView(anio);


            // TOTAL_ANUAL, CAPITAL_ANUAL, INTERESES_ANUALES, CAPITAL PDTE
            ArrayList<Double> valores = hip.getFilaCuadroAmortizacionAnual(calendar.get(Calendar.YEAR) + i - 1, i);

            TextView totalAnual = new TextView(this);
            totalAnual.setText(Double.toString(valores.get(0)));
            totalAnual.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            tableRow.addView(totalAnual);

            TextView capitalAnual = new TextView(this);
            capitalAnual.setText(Double.toString(valores.get(1)));
            capitalAnual.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            tableRow.addView(capitalAnual);

            TextView interesAnual = new TextView(this);
            interesAnual.setText(Double.toString(valores.get(2)));
            interesAnual.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            tableRow.addView(interesAnual);

            TextView pendiente = new TextView(this);
            pendiente.setText(Double.toString(valores.get(3)));
            pendiente.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            tableRow.addView(pendiente);


        }
    }

    public void eventos(){
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
                if(Integer.parseInt(String.valueOf(year_of_calendar.getText())) + 1 <= calendar.get(Calendar.YEAR) + hip.getPlazo_anios()) {
                    year_of_calendar.setText(Integer.toString(Integer.parseInt(String.valueOf(year_of_calendar.getText())) + 1));
                    actualizarTablaMeses(Integer.parseInt((String) year_of_calendar.getText()));
                }
            }
        });
    }

    @Override
    public void setAnio(int anio) {
        year_of_calendar.setText(Integer.toString(anio));
        actualizarTablaMeses(anio);
    }
}