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
import es.MiHipotecaApp.TFG.UsuarioRegistrado.InformacionUsuario.custom_dialog_avatares;

public class Cuadro_amortizacion extends AppCompatActivity implements custom_dialog_anios.customDialogInterface{

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

        eventos();

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

    public void eventos(){
        choose_year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                custom_dialog_anios custom = new custom_dialog_anios();
                custom.show(getSupportFragmentManager(),"Custom Dialog");
            }
        });
    }

    @Override
    public void setAnio(int anio) {

    }
}