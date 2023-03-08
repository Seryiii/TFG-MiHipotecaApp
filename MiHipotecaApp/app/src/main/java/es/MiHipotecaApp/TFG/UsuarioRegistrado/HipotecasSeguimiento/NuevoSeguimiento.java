package es.MiHipotecaApp.TFG.UsuarioRegistrado.HipotecasSeguimiento;

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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import es.MiHipotecaApp.TFG.R;
import es.MiHipotecaApp.TFG.Registro;
import es.MiHipotecaApp.TFG.Transfers.HipotecaSeguimiento;

public class NuevoSeguimiento extends AppCompatActivity {
    private final String TAG = "SEGUIMIENTO ACTIVITY";

    //Campos fijos
    private Spinner sp_comunidad;
    private CheckBox check_vivienda_general;
    private CheckBox check_vivienda_poficial;
    private CheckBox check_vivienda_nueva;
    private CheckBox check_vivienda_smano;
    private EditText precio_vivienda;
    private EditText cantidad_abonada;
    private EditText plazo;
    private EditText anio_actual;
    private CheckBox check_fija;
    private CheckBox check_variable;
    private CheckBox check_mixta;
    private EditText gastos_notaria;
    private EditText gastos_registro;
    private EditText gastos_gestoria;
    private EditText gastos_tasacion;

    private EditText gastos_vinculaciones;
    private EditText gastos_comisiones;
    private EditText nombre_hipoteca;

    //campos variables
    //Hipoteca Fija
    private TextView label_porcentaje_fijo;
    private EditText edit_porcentaje_fijo;
    //Hipoteca Variable
    private TextView label_duracion_primer_porcentaje;
    private EditText edit_duracion_primer_porcentaje;
    private TextView label_primer_porcentaje;
    private EditText edit_primer_porcentaje;
    private TextView label_diferencial_variable;
    private EditText edit_diferencial_variable;
    //Hipoteca mixta
    private TextView label_anios_fija;
    private EditText edit_anios_fija;
    private TextView label_porcentaje_fijo_mix;
    private EditText edit_porcentaje_fijo_mix;
    private TextView label_diferencial_mixto;
    private EditText edit_diferencial_mixto;
    //Campos comun a mixta y variable
    private TextView label_cuando_revision;
    private CheckBox check_seis_meses;
    private CheckBox check_un_anio;

    private Button btn_anadir_hipoteca;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_seguimiento);
        sp_comunidad = findViewById(R.id.sp_comunidad);
        String[] comunidades = {"Andalucía", "Aragón", "Asturias", "Baleares", "Canarias", "Cantabria", "Castilla La Mancha", "Castilla León", "Cataluña", "Ceuta", "Comunidad de Madrid", "Comunidad Valenciana", "Extremadura", "Galicia", "La Rioja", "Melilla", "Murcia", "Navarra", "País Vasco"};
        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, comunidades);
        sp_comunidad.setAdapter(adapter);
        initUI();
        Eventos();

    }

    private void initUI() {
        //Campos fijos
        check_vivienda_general = findViewById(R.id.checkBox_ViviendaRegGeneral);
        check_vivienda_poficial= findViewById(R.id.checkBox_ViviendaPOficial);
        check_vivienda_nueva= findViewById(R.id.checkBox_ViviendaNueva);
        check_vivienda_smano=findViewById(R.id.checkBox_ViviendaSegundaMano);
        precio_vivienda= findViewById(R.id.edit_precio_vivienda);
        cantidad_abonada=findViewById(R.id.edit_cant_abonada_comprador);
        plazo=findViewById(R.id.edit_plazo_pagar);
        anio_actual=findViewById(R.id.edit_anio_actual);
        check_fija=findViewById(R.id.checkBoxFijaNuevoSeg);
        check_variable=findViewById(R.id.checkBoxVariableNuevoSeg);
        check_mixta=findViewById(R.id.checkBoxMixtaNuevoSeg);
        gastos_notaria=findViewById(R.id.edit_gastos_notaria);
        gastos_registro=findViewById(R.id.edit_gastos_registro);
        gastos_gestoria=findViewById(R.id.edit_gastos_gestoria);
        gastos_tasacion=findViewById(R.id.edit_gastos_tasacion);
        nombre_hipoteca=findViewById(R.id.edit_nombre_hipoteca);

        //campos variables
        //Hipoteca Fija
        label_porcentaje_fijo=findViewById(R.id.label_porcentaje_fijo);
        edit_porcentaje_fijo=findViewById(R.id.edit_porcentaje_fijo);
        //Hipoteca Variable
        label_duracion_primer_porcentaje=findViewById(R.id.label_duracion_primer_porcentaje_variable);
        edit_duracion_primer_porcentaje=findViewById(R.id.edit_duracion_primer_porcentaje_variable);
        label_primer_porcentaje=findViewById(R.id.label_duracion_primer_porcentaje_variable);
        edit_primer_porcentaje=findViewById(R.id.edit_duracion_primer_porcentaje_variable);
        label_diferencial_variable=findViewById(R.id.label_diferencial_variable);
        edit_diferencial_variable=findViewById(R.id.edit_porcentaje_diferencial_variable);
        //Hipoteca mixta
        label_anios_fija=findViewById(R.id.label_cuantos_anios_fijo_mixta);
        edit_anios_fija=findViewById(R.id.edit_anios_fijos_mixta);
        label_porcentaje_fijo_mix=findViewById(R.id.label_porcentaje_fijo_mixta);
        edit_porcentaje_fijo_mix=findViewById(R.id.edit_porcentaje_fijo_mixta);
        label_diferencial_mixto=findViewById(R.id.label_diferencial_mixta);
        edit_diferencial_mixto=findViewById(R.id.edit_porcentaje_diferencial_mixta);
        //Campos comun a mixta y variable
        label_cuando_revision=findViewById(R.id.label_cada_cuanto_revision);
        check_seis_meses=findViewById(R.id.checkBox_revision_seis_meses);
        check_un_anio=findViewById(R.id.checkBox_revision_un_anio);

        btn_anadir_hipoteca = findViewById(R.id.btn_anadir_hipoteca);

    }
    private void Eventos() {
        check_vivienda_general.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) check_vivienda_poficial.setChecked(false);
                if(!check_vivienda_poficial.isChecked()) check_vivienda_general.setChecked(true);
            }
        });

        check_vivienda_poficial.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) check_vivienda_general.setChecked(false);
                if(!check_vivienda_general.isChecked()) check_vivienda_poficial.setChecked(true);
            }
        });
        check_vivienda_nueva.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) check_vivienda_smano.setChecked(false);
                if(!check_vivienda_smano.isChecked()) check_vivienda_nueva.setChecked(true);
            }
        });

        check_vivienda_smano.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) check_vivienda_nueva.setChecked(false);
                if(!check_vivienda_nueva.isChecked()) check_vivienda_smano.setChecked(true);
            }
        });
        check_fija.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    check_mixta.setChecked(false);
                    check_variable.setChecked(false);
                    ActivarCampos("fija");
                }
                if(!check_variable.isChecked() && !check_mixta.isChecked()) {
                    check_fija.setChecked(true);
                }
            }
        });

        check_variable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    check_mixta.setChecked(false);
                    check_fija.setChecked(false);
                    ActivarCampos("variable");
                }
                if(!check_fija.isChecked() && !check_mixta.isChecked()) {
                    check_variable.setChecked(true);
                }
            }
        });
        check_mixta.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    check_variable.setChecked(false);
                    check_fija.setChecked(false);
                    ActivarCampos("mixto");
                }
                if(!check_fija.isChecked() && !check_variable.isChecked()) {
                    check_mixta.setChecked(true);
                }
            }

        });

        check_seis_meses.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) check_un_anio.setChecked(false);
                if(!check_un_anio.isChecked()) check_seis_meses.setChecked(true);
            }
        });

        check_un_anio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) check_seis_meses.setChecked(false);
                if(!check_seis_meses.isChecked()) check_un_anio.setChecked(true);
            }
        });

        btn_anadir_hipoteca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(comprobarCampos()) registrarNuevaHipoteca();
            }
        });


    }
    private void ActivarCampos(String hipoteca){

        switch (hipoteca){
            case "fija" :
                //Fija
                edit_porcentaje_fijo.setVisibility(View.VISIBLE);
                label_porcentaje_fijo.setVisibility(View.VISIBLE);
                //Variable
                label_duracion_primer_porcentaje.setVisibility(View.GONE);
                edit_duracion_primer_porcentaje.setVisibility(View.GONE);
                label_primer_porcentaje.setVisibility(View.GONE);
                edit_primer_porcentaje.setVisibility(View.GONE);
                label_diferencial_variable.setVisibility(View.GONE);
                edit_diferencial_variable.setVisibility(View.GONE);
                //Mixto
                label_anios_fija.setVisibility(View.GONE);
                edit_anios_fija.setVisibility(View.GONE);
                label_porcentaje_fijo_mix.setVisibility(View.GONE);
                edit_porcentaje_fijo_mix.setVisibility(View.GONE);
                label_diferencial_mixto.setVisibility(View.GONE);
                edit_diferencial_mixto.setVisibility(View.GONE);
                //Mixto y variable
                label_cuando_revision.setVisibility(View.GONE);
                check_seis_meses.setVisibility(View.GONE);
                check_un_anio.setVisibility(View.GONE);


            break;
            case "variable" :
                //Fija
                edit_porcentaje_fijo.setVisibility(View.GONE);
                label_porcentaje_fijo.setVisibility(View.GONE);
                //Variable
                label_duracion_primer_porcentaje.setVisibility(View.VISIBLE);
                edit_duracion_primer_porcentaje.setVisibility(View.VISIBLE);
                label_primer_porcentaje.setVisibility(View.VISIBLE);
                edit_primer_porcentaje.setVisibility(View.VISIBLE);
                label_diferencial_variable.setVisibility(View.VISIBLE);
                edit_diferencial_variable.setVisibility(View.VISIBLE);
                //Mixto
                edit_anios_fija.setVisibility(View.GONE);
                label_anios_fija.setVisibility(View.GONE);
                label_porcentaje_fijo_mix.setVisibility(View.GONE);
                edit_porcentaje_fijo_mix.setVisibility(View.GONE);
                label_diferencial_mixto.setVisibility(View.GONE);
                edit_diferencial_mixto.setVisibility(View.GONE);
                //Mixto y variable
                label_cuando_revision.setVisibility(View.VISIBLE);
                check_seis_meses.setVisibility(View.VISIBLE);
                check_un_anio.setVisibility(View.VISIBLE);
            break;
            default:
                //Fija
                edit_porcentaje_fijo.setVisibility(View.GONE);
                label_porcentaje_fijo.setVisibility(View.GONE);
                //Variable
                label_duracion_primer_porcentaje.setVisibility(View.GONE);
                edit_duracion_primer_porcentaje.setVisibility(View.GONE);
                label_primer_porcentaje.setVisibility(View.GONE);
                edit_primer_porcentaje.setVisibility(View.GONE);
                label_diferencial_variable.setVisibility(View.GONE);
                edit_diferencial_variable.setVisibility(View.GONE);
                //Mixto
                edit_anios_fija.setVisibility(View.VISIBLE);
                label_anios_fija.setVisibility(View.VISIBLE);
                label_porcentaje_fijo_mix.setVisibility(View.VISIBLE);
                edit_porcentaje_fijo_mix.setVisibility(View.VISIBLE);
                label_diferencial_mixto.setVisibility(View.VISIBLE);
                edit_diferencial_mixto.setVisibility(View.VISIBLE);
                //Mixto y variable
                label_cuando_revision.setVisibility(View.VISIBLE);
                check_seis_meses.setVisibility(View.VISIBLE);
                check_un_anio.setVisibility(View.VISIBLE);
            break;

        }
    }

    private boolean comprobarCampos(){

        //PRIMERO COMPROBAR CAMPOS COMUNES
        if(TextUtils.isEmpty(precio_vivienda.getText())){
            precio_vivienda.setError(getString(R.string.precio_vacio));
            return false;
        }
        if(TextUtils.isEmpty(cantidad_abonada.getText())){
            cantidad_abonada.setError(getString(R.string.cantidad_abonada_vacio));
            return false;
        }
        if(TextUtils.isEmpty(plazo.getText())){
            plazo.setError(getString(R.string.plazo_vacio));
            return false;
        }
        if(TextUtils.isEmpty(anio_actual.getText())){
            anio_actual.setError(getString(R.string.anio_actual_vacio));
            return false;
        }
        if(TextUtils.isEmpty(nombre_hipoteca.getText())){
            nombre_hipoteca.setError(getString(R.string.nombre_hipoteca_vacio));
            return false;
        }

        //CAMPOS FIJO
        if(check_fija.isChecked()){
            if(TextUtils.isEmpty(edit_porcentaje_fijo.getText())){
                edit_porcentaje_fijo.setError(getString(R.string.porcentaje_vacio));
                return false;
            }
        }else if(check_variable.isChecked()){
            if(TextUtils.isEmpty(edit_duracion_primer_porcentaje.getText())){
                edit_duracion_primer_porcentaje.setError(getString(R.string.duracion_vacio));
                return false;
            }
            if(TextUtils.isEmpty(edit_diferencial_variable.getText())){
                edit_diferencial_variable.setError(getString(R.string.porcentaje_vacio));
                return false;
            }
        }else{
            if(TextUtils.isEmpty(edit_anios_fija.getText())){
                edit_anios_fija.setError(getString(R.string.duracion_vacio));
                return false;
            }
            if(TextUtils.isEmpty(edit_porcentaje_fijo_mix.getText())){
                edit_porcentaje_fijo_mix.setError(getString(R.string.porcentaje_vacio));
                return false;
            }
            if(TextUtils.isEmpty(edit_diferencial_mixto.getText())){
                edit_diferencial_mixto.setError(getString(R.string.porcentaje_vacio));
                return false;
            }
        }
        return true;
    }

    private void registrarNuevaHipoteca(){

        String nombre = nombre_hipoteca.getText().toString();
        String comunidad = sp_comunidad.getSelectedItem().toString();
        String tipo_vivienda = "general";
        if(check_vivienda_poficial.isChecked()) tipo_vivienda = "proteccion_oficial";
        String antiguedad_vivienda = "nueva";
        if(check_vivienda_smano.isChecked()) antiguedad_vivienda = "segunda_mano";
        float precio_viv = Float.parseFloat(precio_vivienda.getText().toString());
        float cant_abonada = Float.parseFloat(cantidad_abonada.getText().toString());
        int plazo_hip = Integer.parseInt(plazo.getText().toString());
        int anio_act = Integer.parseInt(anio_actual.getText().toString());

        float gastos_gest, gastos_not, gastos_reg, gastos_tas, gastos_vin, gastos_com;
        if(TextUtils.isEmpty(gastos_gestoria.getText())) gastos_gest = 0;
        else gastos_gest = Float.parseFloat(gastos_gestoria.getText().toString());
        if(TextUtils.isEmpty(gastos_notaria.getText())) gastos_not = 0;
        else gastos_not = Float.parseFloat(gastos_notaria.getText().toString());
        if(TextUtils.isEmpty(gastos_registro.getText())) gastos_reg = 0;
        else gastos_reg = Float.parseFloat(gastos_registro.getText().toString());
        if(TextUtils.isEmpty(gastos_tasacion.getText())) gastos_tas = 0;
        else gastos_tas = Float.parseFloat(gastos_tasacion.getText().toString());
        if(TextUtils.isEmpty(gastos_vinculaciones.getText())) gastos_vin = 0;
        else gastos_vin = Float.parseFloat(gastos_vinculaciones.getText().toString());
        if(TextUtils.isEmpty(gastos_comisiones.getText())) gastos_com = 0;
        else gastos_com = Float.parseFloat(gastos_comisiones.getText().toString());
        float totalGastos = gastos_gest + gastos_not + gastos_reg + gastos_tas + gastos_com;
        HipotecaSeguimiento nuevaHip = new HipotecaSeguimiento(nombre, comunidad, tipo_vivienda, antiguedad_vivienda, precio_viv, cant_abonada, plazo_hip, anio_act, totalGastos, gastos_vin);

        //Hipoteca Fija
        String tipo_hipoteca = "fija";
        if(check_fija.isChecked()){
            float porcentaje_fijo = Float.parseFloat(edit_porcentaje_fijo.getText().toString());

            nuevaHip.setTipo_hipoteca(tipo_hipoteca);
            nuevaHip.setPorcentaje_fijo(porcentaje_fijo);
        }else if (check_variable.isChecked()){
            tipo_hipoteca = "variable";
            int duracion_primer_porcentaje = Integer.parseInt(edit_duracion_primer_porcentaje.getText().toString());
            float diferencial_variable = Float.parseFloat(edit_diferencial_variable.getText().toString());
            boolean revision_anual = true;
            if(check_seis_meses.isChecked()) revision_anual = false;

            nuevaHip.setTipo_hipoteca(tipo_hipoteca);
            nuevaHip.setDuracion_primer_porcentaje_variable(duracion_primer_porcentaje);
            nuevaHip.setPorcentaje_diferencial_variable(diferencial_variable);
            nuevaHip.setRevision_anual(revision_anual);
        }else{
            tipo_hipoteca = "mixta";
            boolean revision_anual = true;
            if(check_seis_meses.isChecked()) revision_anual = false;
            int anios_fijo = Integer.parseInt(edit_anios_fija.getText().toString());
            float porc_fijo_mixta = Float.parseFloat(edit_porcentaje_fijo_mix.getText().toString());
            float porc_diferencial_mixta = Float.parseFloat(edit_diferencial_mixto.getText().toString());

            nuevaHip.setTipo_hipoteca(tipo_hipoteca);
            nuevaHip.setRevision_anual(revision_anual);
            nuevaHip.setAnios_fija_mixta(anios_fijo);
            nuevaHip.setPorcentaje_fijo_mixta(porc_fijo_mixta);
            nuevaHip.setGetPorcentaje_diferencial_mixta(porc_diferencial_mixta);
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        nuevaHip.setIdUsuario(user.getUid());

        db.collection("hipotecas_seguimiento").add(nuevaHip).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(NuevoSeguimiento.this, getString(R.string.hipoteca_seguimiento_exito), Toast.LENGTH_LONG).show();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG,"Error al registrar hipoteca de seguimiento en Firestore: ");
            }
        });
    }

}