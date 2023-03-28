package es.MiHipotecaApp.TFG.UsuarioRegistrado.HipotecasSeguimiento;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
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
    private HipotecaSeguimiento hip;
    private String[] meses;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuadro_amortizacion);

        meses = new String[]{"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};

        year_of_calendar = findViewById(R.id.year_of_calendar);

        before_year = findViewById(R.id.before_year);
        next_year = findViewById(R.id.next_year);
        choose_year = findViewById(R.id.choose_year);
        tabla_cuadro_amortizacion = findViewById(R.id.tabla_cuadro_amortizacion);

        if(getIntent().getStringExtra("tipo_hipoteca").equals("fija")) hip = (HipotecaSegFija) getIntent().getSerializableExtra("hipoteca");
        else if (getIntent().getStringExtra("tipo_hipoteca").equals("variable")) hip = (HipotecaSegVariable) getIntent().getSerializableExtra("hipoteca");
        else hip = (HipotecaSegMixta) getIntent().getSerializableExtra("hipoteca");


        Calendar calendar = Calendar.getInstance();
        calendar.setTime(hip.getFecha_inicio());

        if(calendar.get(Calendar.YEAR) > Calendar.getInstance().get(Calendar.YEAR)) year_of_calendar.setText(Integer.toString(calendar.get(Calendar.YEAR)));
        else year_of_calendar.setText(Integer.toString(Calendar.getInstance().get(Calendar.YEAR)));


        actualizarTablaMeses(Integer.parseInt((String) year_of_calendar.getText()));
        eventos();

    }


    public void actualizarTablaMeses(int año){

        Calendar aux = Calendar.getInstance();
        aux.setTime(hip.getFecha_inicio());
        int diferenciaEnMeses = (Calendar.getInstance().get(Calendar.YEAR) - año) * 12 + (12 - aux.get(Calendar.MONTH));
        if(diferenciaEnMeses < 0) diferenciaEnMeses = 0;

        for (int i = diferenciaEnMeses + 1; i <= diferenciaEnMeses + 12; i++) {
            // Crear una nueva fila y agregarla al TableLayout
            TableRow tableRow = new TableRow(this);
            tabla_cuadro_amortizacion.addView(tableRow);

            TextView numCuota = new TextView(this);
            numCuota.setText(Integer.toString(diferenciaEnMeses + i));
            numCuota.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            tableRow.addView(numCuota);

            TextView nombreMes = new TextView(this);
            nombreMes.setText(meses[(i - 1)%12]);
            nombreMes.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            tableRow.addView(nombreMes);

            TextView cuota = new TextView(this);
            cuota.setText(Double.toString(hip.getCuotaMensual(hip.getPorcentaje_fijo(), hip.getCapitalPendienteTotalActual(diferenciaEnMeses), hip.getPlazo_anios()*12 - diferenciaEnMeses)));
            cuota.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            tableRow.addView(cuota);

            TextView capital = new TextView(this);
            capital.setText("Fila " + i);
            capital.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            tableRow.addView(capital);

            TextView interes = new TextView(this);
            interes.setText("Fila " + i);
            interes.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            tableRow.addView(interes);

            TextView pendiente = new TextView(this);
            pendiente.setText("Fila " + i);
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
                if(Integer.parseInt(String.valueOf(year_of_calendar.getText())) - 1 >= calendar.get(Calendar.YEAR))
                    year_of_calendar.setText(Integer.toString(Integer.parseInt(String.valueOf(year_of_calendar.getText())) - 1));
            }
        });

        next_year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(hip.getFecha_inicio());
                if(Integer.parseInt(String.valueOf(year_of_calendar.getText())) + 1 <= calendar.get(Calendar.YEAR) + hip.getPlazo_anios())
                    year_of_calendar.setText(Integer.toString(Integer.parseInt(String.valueOf(year_of_calendar.getText())) + 1));
            }
        });
    }

    @Override
    public void setAnio(int anio) {
        year_of_calendar.setText(Integer.toString(anio));

    }
}