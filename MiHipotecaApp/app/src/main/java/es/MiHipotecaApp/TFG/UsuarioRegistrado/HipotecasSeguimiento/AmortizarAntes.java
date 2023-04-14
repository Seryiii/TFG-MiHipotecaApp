package es.MiHipotecaApp.TFG.UsuarioRegistrado.HipotecasSeguimiento;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import es.MiHipotecaApp.TFG.R;
import es.MiHipotecaApp.TFG.Transfers.HipotecaSegFija;
import es.MiHipotecaApp.TFG.Transfers.HipotecaSegMixta;
import es.MiHipotecaApp.TFG.Transfers.HipotecaSegVariable;
import es.MiHipotecaApp.TFG.Transfers.HipotecaSeguimiento;

public class AmortizarAntes extends AppCompatActivity {

    private TextView titulo_hipoteca;
    private CheckBox check_comision;
    private EditText edit_comision;
    private CheckBox check_amort_total;
    private CheckBox check_amort_parcial;
    private TextView label_info_amort_parcial;
    private CheckBox check_reducir_cuota;
    private CheckBox check_reducir_plazo;
    private EditText edit_dinero_a_amortizar;
    private EditText edit_reduccion_plazo_meses;
    private ImageButton btn_info_dinero_amort;
    private ImageButton btn_info_reduccion_plazo;

    private Button amortizar_antes;


    private TextView cantidad_capital_amortizado;
    private TextView capital_pendiente_antiguo;
    private TextView capital_pendiente_nuevo;
    private TextView total_comision;
    private TextView cuota_plazo_antigua_valor;
    private TextView cuota_plazo_nueva_valor;
    private TextView cuota_plazo_antigua_tv;
    private TextView cuota_plazo_nueva_tv;

    //Utilizar este layout tambien para num cuotas anterior vs num_cuotas
    private LinearLayout layout_cuotaplazo_antigua_vs_nueva;
    private LinearLayout layout_amort_parcial;

    private LinearLayout layout_reducir_cuota;
    private LinearLayout layout_reducir_plazo;
    private CircleImageView close_icon;

    private HipotecaSeguimiento hip;
    private HashMap<Integer, List<Object>> amortizaciones_hip;

    private double capital_pendiente_actual;
    private String cuota_mensual_actual;
    private double plazo_actual;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amortizar_antes);
        if(getIntent().getStringExtra("tipo_hipoteca").equals("fija")) hip = (HipotecaSegFija) getIntent().getSerializableExtra("hipoteca");
        else if (getIntent().getStringExtra("tipo_hipoteca").equals("variable")) hip = (HipotecaSegVariable) getIntent().getSerializableExtra("hipoteca");
        else hip = (HipotecaSegMixta) getIntent().getSerializableExtra("hipoteca");
        initUI();

        capital_pendiente_actual = hip.getCapitalPendienteTotalActual(hip.getNumeroCuotaActual());
        cuota_mensual_actual = getIntent().getStringExtra("cuota_actual");
        Bundle bundle = getIntent().getExtras();
        amortizaciones_hip = (HashMap<Integer, List<Object>>) getIntent().getSerializableExtra("amortizaciones_anticipadas");
        plazo_actual = hip.getPlazoActual(amortizaciones_hip);

        //Como empieza marcada la casilla de amortizacion total, se pone en capital amortizado el capital pendiente actual

        cantidad_capital_amortizado.setText("Cantidad a amortizar: " +  capital_pendiente_actual + "€");
        capital_pendiente_antiguo.setText("Capital pdte anterior: " + capital_pendiente_actual + "€");
        capital_pendiente_nuevo.setText("Capital pdte nuevo:  0€");
        cuota_plazo_antigua_valor.setText(cuota_mensual_actual);
        cuota_plazo_nueva_valor.setText(cuota_mensual_actual);
        eventos();
    }

    private void initUI(){
        titulo_hipoteca = findViewById(R.id.nombre_amort_anticipada_hip);
        titulo_hipoteca.setText(hip.getNombre());
        check_comision  = findViewById(R.id.check_comision_anticipada);
        edit_comision = findViewById(R.id.edit_porcentaje_comision_ant);
        check_amort_total = findViewById(R.id.check_amort_total);
        check_amort_parcial = findViewById(R.id.check_amort_parcial);
        label_info_amort_parcial = findViewById(R.id.label_info_amort_parcial);
        check_reducir_cuota = findViewById(R.id.check_reducir_cuota);
        check_reducir_plazo = findViewById(R.id.check_reducir_plazo);
        edit_dinero_a_amortizar = findViewById(R.id.edit_dinero_amort);
        edit_reduccion_plazo_meses = findViewById(R.id.edit_reduccion_plazo);
        //todo Faltan los botones de informacion ponerle funcionalidad
        btn_info_dinero_amort = findViewById(R.id.btn_info_dinero_amort);
        btn_info_reduccion_plazo = findViewById(R.id.btn_info_reduccion_plazo);
        layout_cuotaplazo_antigua_vs_nueva = findViewById(R.id.layout_cuotavsnueva);
        layout_amort_parcial = findViewById(R.id.layout_amort_parcial);
        layout_reducir_cuota = findViewById(R.id.layout_reducir_cuota);
        layout_reducir_plazo = findViewById(R.id.layout_reducir_plazo);
        cantidad_capital_amortizado = findViewById(R.id.tv_cantidad_amortizado);
        capital_pendiente_antiguo = findViewById(R.id.tv_capital_pendiente_amort_antiguo);
        capital_pendiente_nuevo = findViewById(R.id.tv_capital_pendiente_amort_nuevo);
        total_comision = findViewById(R.id.tv_comision);
        cuota_plazo_antigua_valor = findViewById(R.id.tv_cuota_plazo_antigua_valor);
        cuota_plazo_nueva_valor = findViewById(R.id.tv_cuota_plazo_nueva_valor);
        cuota_plazo_antigua_tv = findViewById(R.id.tv_cuota_plazo_antigua);
        cuota_plazo_nueva_tv = findViewById(R.id.tv_cuota_plazo_nueva);
        amortizar_antes = findViewById(R.id.btn_realizar_amortizacion_anticipada);
        close_icon = findViewById(R.id.close_icon_amort_ant);

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

        edit_comision.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //COGER CAPITAL PEDIENTE * edit_comision.getText()

                if(s.toString().equals("")) total_comision.setText("Comisión: 0€");
                else{
                    double capPendiente = hip.getCapitalPendienteTotalActual(hip.getNumeroCuotaActual());
                    total_comision.setText("Comisión: " + capPendiente * Double.parseDouble(edit_comision.getText().toString()) + "€");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        check_amort_total.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    check_amort_parcial.setChecked(false);
                    edit_dinero_a_amortizar.setText("");
                    edit_reduccion_plazo_meses.setText("");
                    layout_cuotaplazo_antigua_vs_nueva.setVisibility(View.GONE);
                    layout_amort_parcial.setVisibility(View.GONE);
                    layout_reducir_plazo.setVisibility(View.GONE);
                    layout_reducir_cuota.setVisibility(View.GONE);
                    label_info_amort_parcial.setVisibility(View.GONE);
                    double cap_pendiente_actual =  hip.getCapitalPendienteTotalActual(hip.getNumeroCuotaActual());
                    cantidad_capital_amortizado.setText("Cantidad a amortizar: " + cap_pendiente_actual + "€");
                    capital_pendiente_antiguo.setText("Capital pdte anterior: " + cap_pendiente_actual + "€");
                    capital_pendiente_nuevo.setText("Capital pdte nuevo:  0€");
                }else{
                    if (!check_amort_parcial.isChecked()) check_amort_total.setChecked(true);
                }
            }
        });

        check_amort_parcial.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    check_amort_total.setChecked(false);
                    label_info_amort_parcial.setVisibility(View.VISIBLE);
                    layout_amort_parcial.setVisibility(View.VISIBLE);
                    layout_cuotaplazo_antigua_vs_nueva.setVisibility(View.VISIBLE);
                    cantidad_capital_amortizado.setText("Cantidad a amortizar: 0€");
                    double cap_pendiente_actual =  hip.getCapitalPendienteTotalActual(hip.getNumeroCuotaActual());
                    capital_pendiente_antiguo.setText("Capital pdte anterior: " + cap_pendiente_actual + "€");
                    capital_pendiente_nuevo.setText("Capital pdte nuevo: " + cap_pendiente_actual + "€");
                    if(check_reducir_cuota.isChecked()){
                        layout_reducir_cuota.setVisibility(View.VISIBLE);
                        layout_reducir_plazo.setVisibility(View.GONE);
                        cuota_plazo_antigua_tv.setText("Cuota Antigua");
                        cuota_plazo_nueva_tv.setText("Cuota Nueva");
                    }else{
                        layout_reducir_cuota.setVisibility(View.GONE);
                        layout_reducir_plazo.setVisibility(View.VISIBLE);
                        cuota_plazo_antigua_tv.setText("Plazo Antiguo");
                        cuota_plazo_nueva_tv.setText("Plazo Nuevo");
                    }
                }else{
                    if (!check_amort_total.isChecked()) check_amort_parcial.setChecked(true);
                }
            }
        });

        check_reducir_cuota.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    check_reducir_plazo.setChecked(false);
                    layout_reducir_plazo.setVisibility(View.GONE);
                    layout_reducir_cuota.setVisibility(View.VISIBLE);
                    cuota_plazo_antigua_tv.setText("Cuota Antigua");
                    cuota_plazo_antigua_valor.setText(cuota_mensual_actual);
                    cuota_plazo_nueva_tv.setText("Cuota Nueva");
                    cuota_plazo_antigua_valor.setText(getIntent().getStringExtra("cuota_actual"));
                    cuota_plazo_nueva_valor.setText(getIntent().getStringExtra("cuota_actual"));
                    edit_reduccion_plazo_meses.setText("");
                    edit_dinero_a_amortizar.setText("");
                    cantidad_capital_amortizado.setText("Cantidad a amortizar: 0€");
                    double cap_pendiente_actual =  hip.getCapitalPendienteTotalActual(hip.getNumeroCuotaActual());
                    capital_pendiente_antiguo.setText("Capital pdte anterior: " + cap_pendiente_actual + "€");
                    capital_pendiente_nuevo.setText("Capital pdte nuevo: " + cap_pendiente_actual + "€");
                }else{
                    if (!check_reducir_plazo.isChecked()) check_reducir_cuota.setChecked(true);
                }
            }
        });

        check_reducir_plazo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    check_reducir_cuota.setChecked(false);
                    layout_reducir_plazo.setVisibility(View.VISIBLE);
                    layout_reducir_cuota.setVisibility(View.GONE);
                    cuota_plazo_antigua_tv.setText("Plazo Antiguo");
                    cuota_plazo_nueva_tv.setText("Plazo Nuevo");

                    int plazo_antiguo = 1; //hip.getPlazoActual();
                    cuota_plazo_antigua_valor.setText(plazo_antiguo + "meses");
                    cuota_plazo_nueva_valor.setText(plazo_antiguo + "meses");
                    edit_dinero_a_amortizar.setText("");
                    edit_reduccion_plazo_meses.setText("");
                }else{
                    if (!check_reducir_cuota.isChecked()) check_reducir_plazo.setChecked(true);
                }
            }
        });

        edit_dinero_a_amortizar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(s.toString().equals("")){
                    cuota_plazo_nueva_valor.setText(getIntent().getStringExtra("cuota_actual"));
                }
                else{
                    double porcentaje_aplicado  = getIntent().getDoubleExtra("porcentaje_aplicado", -1);
                    int numero_cuotas_restantes = getIntent().getIntExtra("cuotas_pendientes", -1);
                    double cantidad_pendiente = getIntent().getDoubleExtra("cantidad_pendiente", -1);
                    double capital_a_amortizar = Double.parseDouble(s.toString());
                    double capitalPendienteTotalActual = hip.getCapitalPendienteTotalActual(hip.getNumeroCuotaActual());
                    if (capital_a_amortizar > capitalPendienteTotalActual){
                        edit_dinero_a_amortizar.setError("El capital a amortizar no puede ser mayor que el capital pendiente");
                        cantidad_capital_amortizado.setText("Cantidad no válida");
                        capital_pendiente_antiguo.setText("Capital pdte anterior: " + capitalPendienteTotalActual + "€");
                        capital_pendiente_nuevo.setText("Capital pdte nuevo: " + capitalPendienteTotalActual + "€");
                    }
                    else{
                        double cantidad_pendiente_con_amortizacion = cantidad_pendiente - capital_a_amortizar;
                        cuota_plazo_nueva_valor.setText(hip.getCuotaMensual(porcentaje_aplicado, cantidad_pendiente_con_amortizacion, numero_cuotas_restantes)+"€");
                        cantidad_capital_amortizado.setText("Cantidad a amortizar: " + edit_dinero_a_amortizar.getText().toString() + "€");
                        DecimalFormat formato = new DecimalFormat("#.##"); // Establecer el formato a dos decimales
                        String cap_formateado = "Capital pdte nuevo: " + formato.format(capitalPendienteTotalActual - capital_a_amortizar)  + "€";
                        capital_pendiente_nuevo.setText(cap_formateado);
                    }

                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edit_reduccion_plazo_meses.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // calcular Y poner en cuota_plazo_antigua_valor y cuota_plazo_nueva_valor el plazo antiguo y el nuevo calculado
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        close_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialogo = new AlertDialog.Builder(AmortizarAntes.this)
                        .setPositiveButton(getString(R.string.si_eliminar_cuenta), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .setNegativeButton(getString(R.string.no_eliminar_cuenta), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setTitle("CANCELAR AMORTIZACIÓN ANTICIPADA").setMessage("¿Desea dejar de hacer la amortización anticipada? Perderá todo su progreso").create();
                dialogo.show();
            }
        });

        amortizar_antes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amortizar();
            }
        });

    }

    private void amortizar(){

    }

}