package es.MiHipotecaApp.TFG.UsuarioRegistrado.HipotecasSeguimiento;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import es.MiHipotecaApp.TFG.R;

public class AmortizarAntes extends AppCompatActivity {

    private TextView titulo_hipoteca;
    private CheckBox check_comision;
    private EditText edit_comision;
    private CheckBox check_amort_total;
    private CheckBox check_amort_parcial;
    private CheckBox check_reducir_cuota;
    private CheckBox check_reducir_plazo;
    private EditText edit_dinero_a_amortizar;
    private EditText edit_reduccion_plazo_meses;
    private ImageButton btn_info_dinero_amort;
    private ImageButton btn_info_reduccion_plazo;

    private Button amortizar_antes;


    private TextView capital_amortizado;
    private TextView total_comision;
    //Utilizar este layout tambien para num cuotas anterior vs num_cuotas
    private LinearLayout layout_cuotaplazo_antigua_vs_nueva;
    private LinearLayout layout_amort_parcial;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amortizar_antes);
        initUI();
        eventos();
    }

    private void initUI(){
        titulo_hipoteca = findViewById(R.id.nombre_amort_anticipada_hip);
        check_comision  = findViewById(R.id.checkBox_comision_amrotizacion_anticipada);
        edit_comision = findViewById(R.id.edit_porcentaje_comision_ant);
        check_amort_total = findViewById(R.id.checkBox_amort_total);
        check_amort_parcial = findViewById(R.id.checkBox_amort_parcial);
        check_reducir_cuota = findViewById(R.id.check_reducir_cuota);
        check_reducir_plazo = findViewById(R.id.check_reducir_plazo);
        edit_dinero_a_amortizar = findViewById(R.id.edit_dinero_amort);
        edit_reduccion_plazo_meses = findViewById(R.id.edit_reduccion_plazo);
        //todo Faltan los botones de informacion
        btn_info_dinero_amort = findViewById(R.id.btn_info_dinero_amort);
        btn_info_reduccion_plazo = findViewById(R.id.btn_info_reduccion_plazo);
        layout_cuotaplazo_antigua_vs_nueva = findViewById(R.id.layout_cuotavsnueva);
        layout_amort_parcial = findViewById(R.id.layout_amort_parcial);
        amortizar_antes = findViewById(R.id.btn_realizar_amortizacion_anticipada);
    }

    private void eventos(){
        check_comision.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    edit_comision.setEnabled(true);
                } else{
                    edit_comision.setEnabled(false);
                    edit_comision.setText("");
                }
            }
        });

        check_amort_total.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    check_amort_parcial.setChecked(false);
                    layout_cuotaplazo_antigua_vs_nueva.setVisibility(View.GONE);
                    layout_amort_parcial.setVisibility(View.GONE);
                    edit_dinero_a_amortizar.setText("PONER AQUI EL CAPITAL PENDIENTE");
                    //SI LA COMISION ESTA MARCADA PONER LA CANTIDAD EN EL EDIT
                } else{

                }
            }
        });

        check_amort_parcial.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    check_amort_total.setChecked(false);
                    layout_cuotaplazo_antigua_vs_nueva.setVisibility(View.VISIBLE);
                } else{

                }
            }
        });

    }
}