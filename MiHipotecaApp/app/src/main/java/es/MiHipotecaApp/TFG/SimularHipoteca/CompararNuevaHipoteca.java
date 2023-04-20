package es.MiHipotecaApp.TFG.SimularHipoteca;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;

import org.json.JSONException;
import org.json.JSONObject;

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
    private EditText ingresos;
    private Button btn_comparar_hipoteca;
    private Switch switch_detalles;

    private boolean detalles = false;

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
        String[] comunidades = {"Alava", "Albacete", "Alicante", "Almeria", "Avila", "Badajoz", " Illes Balears ", "Barcelona", "Burgos", "Caceres", "Cadiz", "Castellon", "Ciudad Real", "Cordoba", "A Coruña", "Cuenca", "Girona", "Granada", "Guadalajara", "Guipuzcoa", "Huelva", "Huesca", "Jaen", "Leon", "LLeida", "La Rioja", "Lugo", "Madrid", "Malaga", "Murcia", "Navarra", "Ourense", "Asturias", "Palencia", "Palmas", "Pontevedra", "Salamanca", "Santa Cruz De Tenerife", "Cantabria", "Segovia", "Sevilla", "Soria", "Tarragona", "Toledo", "Valencia", "Valladolid", "Vizcaya", "Zamora", "Zaragoza", "Ceuta", "Melilla"};
        ArrayAdapter<String> adapter2 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, comunidades);
        comunidadComp.setAdapter(adapter2);

        viviendaNuevaCheck    = findViewById(R.id.checkBox_vivienda_nueva);
        viviendaSegManoCheck  = findViewById(R.id.checkBox_vivienda_seg_mano);
        precioVivienda        = findViewById(R.id.edit_precio_vivienda_comp);
        cantidadAbonada       = findViewById(R.id.edit_cant_abonada_comp);
        plazo                 = findViewById(R.id.edit_plazo_pagar_comp);
        ingresos              = findViewById(R.id.edit_ingresos_mensuales);
        btn_comparar_hipoteca = findViewById(R.id.btn_comparar_hipoteca);
        switch_detalles       = findViewById(R.id.switch_detalle);

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
        switch_detalles.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //Aquí es donde puedes hacer algo con el estado del switch
                if (isChecked) {
                    //el switch está activado
                    detalles = true;
                } else {
                    detalles = false;
                }
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

        if(TextUtils.isEmpty(ingresos.getText())){
            plazo.setError(getString(R.string.ingresos_vacio));
            camposCorrectos = false;
        }

        if(camposCorrectos){
            Intent intent = new Intent(CompararNuevaHipoteca.this, MostrarOfertas.class);
            //Establezco los datos para pasarselos a la intent
            JSONObject datos = new JSONObject();
            try {

                datos.put("comunidad_autonoma", comunidadComp.getSelectedItem().toString());
                datos.put("tipo_vivienda" ,tipoVivienda.getSelectedItem().toString() );
                String antiguedad = "nueva";
                if(viviendaSegManoCheck.isChecked()) antiguedad = "segunda";
                datos.put("antiguedad_vivienda", antiguedad);
                datos.put("precio_vivienda", precioVivienda.getText().toString());
                datos.put("cantidad_abonada", cantidadAbonada.getText().toString());
                datos.put("plazo_anios", plazo.getText().toString());
                datos.put("ingresos", ingresos.getText().toString());
                datos.put("detalles", detalles);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            String jsonStr = datos.toString();
            intent.putExtra("datos", jsonStr);
            startActivity(intent);
        }
    }
}