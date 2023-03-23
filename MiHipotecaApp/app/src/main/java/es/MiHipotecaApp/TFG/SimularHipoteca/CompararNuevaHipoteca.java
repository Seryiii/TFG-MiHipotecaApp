package es.MiHipotecaApp.TFG.SimularHipoteca;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;

import de.hdodenhof.circleimageview.CircleImageView;
import es.MiHipotecaApp.TFG.R;

public class CompararNuevaHipoteca extends AppCompatActivity {

    private CircleImageView closeIcon;
    private Spinner tipoVivienda;
    private Spinner comunidadComp;
    private CheckBox viviendaNuevaCheck;
    private CheckBox viviendaSegManoCheck;
    private EditText precioVivienda;
    private EditText cantidadAbonada;
    private EditText plazo;
    private Button btn_comparar_hipoteca;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comparar_nueva_hipoteca);
        initUI();
        eventos();
    }

    private void initUI(){
        closeIcon = findViewById(R.id.close_icon);
        tipoVivienda = findViewById(R.id.sp_tipo_vivienda);
        String[] tiposVivienda = {"Vivienda habitual", "Segunda vivienda", "No residente", "Otros"};
        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, tiposVivienda);
        tipoVivienda.setAdapter(adapter);

        comunidadComp = findViewById(R.id.sp_comunidad_comparador);
        String[] comunidades = {"Andalucía", "Aragón", "Asturias", "Baleares", "Canarias", "Cantabria", "Castilla La Mancha", "Castilla León", "Cataluña", "Ceuta", "Comunidad de Madrid", "Comunidad Valenciana", "Extremadura", "Galicia", "La Rioja", "Melilla", "Murcia", "Navarra", "País Vasco"};
        ArrayAdapter<String> adapter2 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, comunidades);
        comunidadComp.setAdapter(adapter2);

        viviendaNuevaCheck = findViewById(R.id.checkBox_vivienda_nueva);
        viviendaSegManoCheck = findViewById(R.id.checkBox_vivienda_seg_mano);
        precioVivienda = findViewById(R.id.edit_precio_vivienda_comp);
        cantidadAbonada = findViewById(R.id.edit_cant_abonada_comp);
        plazo = findViewById(R.id.edit_plazo_pagar_comp);
        btn_comparar_hipoteca = findViewById(R.id.btn_comparar_hipoteca);

    }

    private void eventos(){
        closeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { finish(); }
        });

        viviendaNuevaCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) viviendaSegManoCheck.setChecked(false);
                if(!viviendaSegManoCheck.isChecked()) viviendaNuevaCheck.setChecked(true);
            }
        });

        viviendaSegManoCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) viviendaNuevaCheck.setChecked(false);
                if(!viviendaNuevaCheck.isChecked()) viviendaSegManoCheck.setChecked(true);
            }
        });

        btn_comparar_hipoteca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comprobarCampos();

            }
        });
    }

    public void comprobarCampos(){
        boolean camposCorrectos = true;

        if(TextUtils.isEmpty(precioVivienda.getText())){
            precioVivienda.setError(getString(R.string.precio_vacio));
            camposCorrectos = false;
        }
        if(TextUtils.isEmpty(cantidadAbonada.getText())){
            cantidadAbonada.setError(getString(R.string.cantidad_abonada_vacio));
            camposCorrectos = false;
        }
        //COMPROBACION CANTIDAD APORTADA POR EL BANCO <= 80%
        double precio_viv = Double.parseDouble(precioVivienda.getText().toString());
        double ahorro_aport = Double.parseDouble(cantidadAbonada.getText().toString());
        double dinero_aport_banco = precio_viv - ahorro_aport;
        if(dinero_aport_banco > precio_viv * 0.8){
            cantidadAbonada.setError(getString(R.string.ahorro_mayor_80_por_ciento));
            camposCorrectos = false;
        }
        if(TextUtils.isEmpty(plazo.getText())){
            plazo.setError(getString(R.string.plazo_vacio));
            camposCorrectos = false;
        }

        if(camposCorrectos){
            Log.e("CompararNuevaHipoteca", "Pasar a la actividad que aparecen las ofertas de hipotecas");
        }
    }
}