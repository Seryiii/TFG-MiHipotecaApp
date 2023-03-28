package es.MiHipotecaApp.TFG.UsuarioRegistrado.HipotecasSeguimiento;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import es.MiHipotecaApp.TFG.R;
import es.MiHipotecaApp.TFG.Transfers.HipotecaSegFija;
import es.MiHipotecaApp.TFG.Transfers.HipotecaSegMixta;
import es.MiHipotecaApp.TFG.Transfers.HipotecaSegVariable;
import es.MiHipotecaApp.TFG.Transfers.HipotecaSeguimiento;

public class Cuadro_amortizacion extends AppCompatActivity {

    private TextView year_of_calendar;
    private ImageButton before_year;
    private ImageButton next_year;
    private ImageButton choose_year;
    private TableLayout tabla_cuadro_amortizacion;
    private HipotecaSeguimiento hip;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuadro_amortizacion);

        year_of_calendar = findViewById(R.id.year_of_calendar);
        before_year = findViewById(R.id.before_year);
        next_year = findViewById(R.id.next_year);
        choose_year = findViewById(R.id.choose_year);
        tabla_cuadro_amortizacion = findViewById(R.id.tabla_cuadro_amortizacion);

        if(getIntent().getStringExtra("tipo_hipoteca").equals("fija")) hip = (HipotecaSegFija) getIntent().getSerializableExtra("hipoteca");
        else if (getIntent().getStringExtra("tipo_hipoteca").equals("variable")) hip = (HipotecaSegVariable) getIntent().getSerializableExtra("hipoteca");
        else hip = (HipotecaSegMixta) getIntent().getSerializableExtra("hipoteca");

        setTablaMeses();


/*
// Crear una nueva fila y agregarla al TableLayout
        TableRow tableRow = new TableRow(this);
        tabla_cuadro_amortizacion.addView(tableRow);

// Crear tres TextViews para las celdas y agregarlos a la fila
        TextView textView1 = new TextView(this);
        textView1.setText("1");
        tableRow.addView(textView1);

        TextView textView2 = new TextView(this);
        textView2.setText("Enero");
        tableRow.addView(textView2);

        TextView textView3 = new TextView(this);
        textView3.setText("599€");
        tableRow.addView(textView3);

        TextView textView4 = new TextView(this);
        textView3.setText("199€");
        tableRow.addView(textView4);

        TextView textView5 = new TextView(this);
        textView3.setText("400€");
        tableRow.addView(textView5);

        TextView textView6 = new TextView(this);
        textView3.setText("55151€");
        tableRow.addView(textView6);

// Crear otra fila y agregarla al TableLayout
        TableRow tableRow2 = new TableRow(this);
        tabla_cuadro_amortizacion.addView(tableRow2);

// Agregar más TextViews a la segunda fila
        TextView textView7 = new TextView(this);
        textView4.setText("2");
        tableRow2.addView(textView7);

        TextView textView8 = new TextView(this);
        textView5.setText("Febrero");
        tableRow2.addView(textView8);

        TextView textView9 = new TextView(this);
        textView6.setText("599€");
        tableRow2.addView(textView9);

        TextView textView10 = new TextView(this);
        textView4.setText("205€");
        tableRow2.addView(textView10);

        TextView textView11 = new TextView(this);
        textView5.setText("394€");
        tableRow2.addView(textView11);

        TextView textView12 = new TextView(this);
        textView6.setText("54948€");
        tableRow2.addView(textView12);


 */

    }


    public void setTablaMeses(){


        for (int i = 0; i < 12; i++) {
            // Crear una nueva fila y agregarla al TableLayout
            TableRow tableRow = new TableRow(this);
            tabla_cuadro_amortizacion.addView(tableRow);

            TextView numCuota = new TextView(this);
            numCuota.setText(Integer.toString(hip.getNumeroCuotaActual() + i));
            numCuota.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            tableRow.addView(numCuota);

            TextView nombreMes = new TextView(this);
            nombreMes.setText(hip.getNombreMesActual());
            nombreMes.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            tableRow.addView(nombreMes);

            TextView cuota = new TextView(this);
            cuota.setText(Double.toString(hip.getCuotaMensual(hip.getPorcentaje_fijo(), hip.getCapitalPendienteTotalActual(hip.getNumeroCuotaActual()), hip.getPlazo_anios()*12 - hip.getNumeroCuotaActual())));
            cuota.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            tableRow.addView(cuota);

            TextView capital = new TextView(this);
            capital.setText(Double.toString(hip.getCapitalAmortizadoMensual(hip.getNumeroCuotaActual(), hip.getCapitalPendienteTotalActual(hip.getNumeroCuotaActual()), hip.getPorcentaje_fijo())));
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
}