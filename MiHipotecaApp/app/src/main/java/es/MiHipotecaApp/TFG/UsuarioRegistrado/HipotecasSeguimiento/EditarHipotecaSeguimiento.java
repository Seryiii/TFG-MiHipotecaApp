package es.MiHipotecaApp.TFG.UsuarioRegistrado.HipotecasSeguimiento;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import es.MiHipotecaApp.TFG.PaginaPrincipal;
import es.MiHipotecaApp.TFG.R;
import es.MiHipotecaApp.TFG.Transfers.HipotecaSegFija;
import es.MiHipotecaApp.TFG.Transfers.HipotecaSegMixta;
import es.MiHipotecaApp.TFG.Transfers.HipotecaSegVariable;
import es.MiHipotecaApp.TFG.Transfers.HipotecaSeguimiento;

public class EditarHipotecaSeguimiento extends AppCompatActivity {
    private final String TAG = "SEGUIMIENTO ACTIVITY";

    private HipotecaSeguimiento hip;
    //Campos fijos
    private CircleImageView closeIcon;
    private Spinner sp_comunidad;
    private CheckBox check_vivienda_general;
    private CheckBox check_vivienda_poficial;
    private CheckBox check_vivienda_nueva;
    private CheckBox check_vivienda_smano;
    private EditText precio_vivienda;
    private EditText cantidad_abonada;
    private EditText plazo;
    private DatePicker inicio_hipoteca;
    private CheckBox check_fija;
    private CheckBox check_variable;
    private CheckBox check_mixta;
    private EditText gastos_totales;

    private EditText vinculaciones;
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
    private Button btn_editar_hipoteca;
    private Spinner sp_bancos;

    //Bases de datos

    private FirebaseFirestore db;

    private FirebaseAuth auth;
    private FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_hipoteca_seguimiento);
        sp_comunidad = findViewById(R.id.sp_comunidad);
        String[] comunidades = {"Andalucía", "Aragón", "Asturias", "Baleares", "Canarias", "Cantabria", "Castilla La Mancha", "Castilla León", "Cataluña", "Ceuta", "Comunidad de Madrid", "Comunidad Valenciana", "Extremadura", "Galicia", "La Rioja", "Melilla", "Murcia", "Navarra", "País Vasco"};
        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, comunidades);
        sp_comunidad.setAdapter(adapter);
        initUI();
        rellenarCampos();
        Eventos();
        db   = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
    }

    private void initUI() {
        //Campos fijos
        closeIcon = findViewById(R.id.close_icon_seg);
        check_vivienda_general = findViewById(R.id.checkBox_ViviendaRegGeneral);
        check_vivienda_poficial= findViewById(R.id.checkBox_ViviendaPOficial);
        check_vivienda_nueva= findViewById(R.id.checkBox_ViviendaNueva);
        check_vivienda_smano=findViewById(R.id.checkBox_ViviendaSegundaMano);
        precio_vivienda= findViewById(R.id.edit_precio_vivienda);
        cantidad_abonada=findViewById(R.id.edit_cant_abonada_comprador);
        plazo=findViewById(R.id.edit_plazo_pagar);
        inicio_hipoteca = findViewById(R.id.inicio_hipoteca);
        check_fija=findViewById(R.id.checkBoxFijaNuevoSeg);
        check_variable=findViewById(R.id.checkBoxVariableNuevoSeg);
        check_mixta=findViewById(R.id.checkBoxMixtaNuevoSeg);
        gastos_totales=findViewById(R.id.edit_gastos_totales);
        vinculaciones=findViewById(R.id.edit_gastos_vinculaciones);
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

        btn_editar_hipoteca = findViewById(R.id.btn_editar_hipoteca);

        sp_bancos = findViewById(R.id.sp_banco_nuevo_seg);
        String [] bancos = {"ING","SANTANDER","BBVA","CAIXABANK","BANKINTER","EVO BANCO","SABADELL","UNICAJA","DEUTSCHE BANK","OPEN BANK","KUTXA BANK","IBERCAJA","ABANCA"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,bancos);
        sp_bancos.setAdapter(adapter);

    }
    private void Eventos() {
        closeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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

        btn_editar_hipoteca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comprobarCampos();
            }
        });


    }
    private void ActivarCampos(String hipoteca){

        switch (hipoteca){
            case "fija" :
                ModificarCamposFija(View.VISIBLE);
                ModificarCamposVariable(View.GONE);
                ModificarCamposMixta(View.GONE);
                ModificarCamposMixtaVariable(View.GONE);

            break;
            case "variable" :
                ModificarCamposFija(View.GONE);
                ModificarCamposVariable(View.VISIBLE);
                ModificarCamposMixta(View.GONE);
                ModificarCamposMixtaVariable(View.VISIBLE);

            break;
            default:
                ModificarCamposFija(View.GONE);
                ModificarCamposVariable(View.GONE);
                ModificarCamposMixta(View.VISIBLE);
                ModificarCamposMixtaVariable(View.VISIBLE);

            break;

        }
    }
    private void ModificarCamposFija(int view) {
        edit_porcentaje_fijo.setVisibility(view);
        label_porcentaje_fijo.setVisibility(view);

    }
    private void ModificarCamposVariable(int view) {
        label_duracion_primer_porcentaje.setVisibility(view);
        edit_duracion_primer_porcentaje.setVisibility(view);
        label_primer_porcentaje.setVisibility(view);
        edit_primer_porcentaje.setVisibility(view);
        label_diferencial_variable.setVisibility(view);
        edit_diferencial_variable.setVisibility(view);

    }
    private void ModificarCamposMixta(int view) {
        edit_anios_fija.setVisibility(view);
        label_anios_fija.setVisibility(view);
        label_porcentaje_fijo_mix.setVisibility(view);
        edit_porcentaje_fijo_mix.setVisibility(view);
        label_diferencial_mixto.setVisibility(view);
        edit_diferencial_mixto.setVisibility(view);

    }
    private void ModificarCamposMixtaVariable(int view) {
        label_cuando_revision.setVisibility(view);
        check_seis_meses.setVisibility(view);
        check_un_anio.setVisibility(view);

    }
    private void comprobarCampos(){

        boolean camposCorrectos = true;
        //PRIMERO COMPROBAR CAMPOS COMUNES
        if(TextUtils.isEmpty(precio_vivienda.getText())){
            precio_vivienda.setError(getString(R.string.precio_vacio));
            camposCorrectos = false;
        }
        if(TextUtils.isEmpty(cantidad_abonada.getText())){
            cantidad_abonada.setError(getString(R.string.cantidad_abonada_vacio));
            camposCorrectos = false;
        }
        //COMPROBACION CANTIDAD APORTADA POR EL BANCO <= 80%
        double precio_viv = Double.parseDouble(precio_vivienda.getText().toString());
        double ahorro_aport = Double.parseDouble(cantidad_abonada.getText().toString());
        double dinero_aport_banco = precio_viv - ahorro_aport;
        if(dinero_aport_banco > precio_viv * 0.8){
            cantidad_abonada.setError(getString(R.string.ahorro_mayor_80_por_ciento));
            camposCorrectos = false;
        }

        if(TextUtils.isEmpty(plazo.getText())){
            plazo.setError(getString(R.string.plazo_vacio));
            camposCorrectos = false;
        }

        //CAMPOS FIJO
        if(check_fija.isChecked()){
            if(TextUtils.isEmpty(edit_porcentaje_fijo.getText())){
                edit_porcentaje_fijo.setError(getString(R.string.porcentaje_vacio));
                camposCorrectos = false;
            }
        }else if(check_variable.isChecked()){
            if(TextUtils.isEmpty(edit_duracion_primer_porcentaje.getText())){
                edit_duracion_primer_porcentaje.setError(getString(R.string.duracion_vacio));
                camposCorrectos = false;
            }
            if(TextUtils.isEmpty(edit_diferencial_variable.getText())){
                edit_diferencial_variable.setError(getString(R.string.porcentaje_vacio));
                camposCorrectos = false;
            }
        }else{
            if(TextUtils.isEmpty(edit_anios_fija.getText())){
                edit_anios_fija.setError(getString(R.string.duracion_vacio));
                camposCorrectos = false;
            }
            if(TextUtils.isEmpty(edit_porcentaje_fijo_mix.getText())){
                edit_porcentaje_fijo_mix.setError(getString(R.string.porcentaje_vacio));
                camposCorrectos = false;
            }
            if(TextUtils.isEmpty(edit_diferencial_mixto.getText())){
                edit_diferencial_mixto.setError(getString(R.string.porcentaje_vacio));
                camposCorrectos = false;
            }
        }
        if(TextUtils.isEmpty(nombre_hipoteca.getText())){
            nombre_hipoteca.setError(getString(R.string.nombre_hipoteca_vacio));
            camposCorrectos = false;
        }
        else {
            if(camposCorrectos){
                //Query hipotecas_usuario =  db.collection("hipotecas_seguimiento").whereEqualTo("idUsuario",auth.getCurrentUser().getUid()).whereEqualTo("nombre",nombre_hipoteca.getText().toString());
                //COMPROBACION DE QUE NO INTRODUCE UNA HIPOTECA CON EL MISMO NOMBRE EL MISMO USUARIO
                /*hipotecas_usuario.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            if(!task.getResult().isEmpty() && task.getResult() != null)nombre_hipoteca.setError("Una de sus hipotecas ya tiene este nombre");
                            else editarHipoteca();
                        }else Log.d(TAG, "Error en query hipotecas seg por nombre", task.getException());
                    }
                });*/
                editarHipoteca();
            }else return;
        }
    }

    private void editarHipoteca(){
        Map<String, Object> nuevosDatos = new HashMap<>();

        nuevosDatos.put("nombre", nombre_hipoteca.getText().toString());
        nuevosDatos.put("comunidad_autonoma", sp_comunidad.getSelectedItem().toString());

        String tipo_vivienda = "general";
        if(check_vivienda_poficial.isChecked()) tipo_vivienda = "proteccion_oficial";
        nuevosDatos.put("tipo_vivienda", tipo_vivienda);

        String antiguedad_vivienda = "nueva";
        if(check_vivienda_smano.isChecked()) antiguedad_vivienda = "segunda_mano";
        nuevosDatos.put("tipo_vivienda", tipo_vivienda);

        nuevosDatos.put("precio_vivienda", Double.parseDouble(precio_vivienda.getText().toString()));
        nuevosDatos.put("cantidad_abonada", Double.parseDouble(cantidad_abonada.getText().toString()));
        nuevosDatos.put("plazo_anios", Integer.parseInt(plazo.getText().toString()));

        int year = inicio_hipoteca.getYear();
        int month = inicio_hipoteca.getMonth();
        int dayOfMonth = inicio_hipoteca.getDayOfMonth();
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);
        nuevosDatos.put("fecha_inicio", calendar.getTime());

        double totalGastos, gastos_vin;
        if(TextUtils.isEmpty(gastos_totales.getText())) totalGastos = 0;
        else totalGastos = Double.parseDouble(gastos_totales.getText().toString());
        nuevosDatos.put("totalGastos", totalGastos);
        if(TextUtils.isEmpty(vinculaciones.getText())) gastos_vin = 0;
        else gastos_vin = Double.parseDouble(vinculaciones.getText().toString());
        nuevosDatos.put("totalViculacionesActual", gastos_vin);

        nuevosDatos.put("banco_asociado", sp_bancos.getSelectedItem().toString());
        HipotecaSeguimiento nuevaHip;
        //Hipoteca Fija
        String tipo_hipoteca = "fija";
        if(check_fija.isChecked()){
            nuevosDatos.put("porcentaje_fijo", Double.parseDouble(edit_porcentaje_fijo.getText().toString()));
            nuevosDatos.put("tipo_hipoteca", tipo_hipoteca);

        }else if (check_variable.isChecked()){
            tipo_hipoteca = "variable";
            nuevosDatos.put("duracion_primer_porcentaje_variable", Integer.parseInt(edit_duracion_primer_porcentaje.getText().toString()));
            nuevosDatos.put("primer_porcentaje_variable", Double.parseDouble(edit_primer_porcentaje.getText().toString()));
            nuevosDatos.put("porcentaje_diferencial_variable", Double.parseDouble(edit_diferencial_variable.getText().toString()));
            boolean revision_anual = true;
            if(check_seis_meses.isChecked()) revision_anual = false;
            nuevosDatos.put("revision_anual", revision_anual);
            //nuevaHip = new HipotecaSegVariable(nombre, comunidad, tipo_vivienda, antiguedad_vivienda, precio_viv, cant_abonada, plazo_hip, fecha_inicio, tipo_hipoteca, totalGastos, gastos_vin, banco_asociado, duracion_primer_porcentaje, primer_porc_variable, diferencial_variable, revision_anual);

            nuevosDatos.put("tipo_hipoteca", tipo_hipoteca);
        }else{
            tipo_hipoteca = "mixta";
            boolean revision_anual = true;
            if(check_seis_meses.isChecked()) revision_anual = false;
            nuevosDatos.put("revision_anual", revision_anual);

            nuevosDatos.put("anios_fija_mixta", Integer.parseInt(edit_anios_fija.getText().toString()));
            nuevosDatos.put("porcentaje_fijo_mixta", Double.parseDouble(edit_porcentaje_fijo_mix.getText().toString()));
            nuevosDatos.put("porcentaje_diferencial_mixta", Double.parseDouble(edit_diferencial_mixto.getText().toString()));

                nuevosDatos.put("tipo_hipoteca", tipo_hipoteca);
        }

        // obtener una referencia a la colección
        CollectionReference hipotecasRef = db.collection("hipotecas_seguimiento");

// crear una consulta para obtener el documento específico
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        Query query = hipotecasRef.whereEqualTo("nombre", hip.getNombre()).whereEqualTo("idUsuario", firebaseAuth.getCurrentUser().getUid());

// ejecutar la consulta
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        // actualizar el documento con los nuevos valores
                        hipotecasRef.document(document.getId()).update(nuevosDatos);
                        Toast.makeText(EditarHipotecaSeguimiento.this, getString(R.string.hipoteca_seguimiento_exito), Toast.LENGTH_LONG).show();
                        Intent i = new Intent(EditarHipotecaSeguimiento.this, PaginaPrincipal.class);
                        startActivity(i);
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }

    public void rellenarCampos(){
        if(getIntent().getStringExtra("tipo_hipoteca").equals("fija")) hip = (HipotecaSegFija) getIntent().getSerializableExtra("hipoteca");
        else if (getIntent().getStringExtra("tipo_hipoteca").equals("variable")) hip = (HipotecaSegVariable) getIntent().getSerializableExtra("hipoteca");
        else hip = (HipotecaSegMixta) getIntent().getSerializableExtra("hipoteca");

        String[] comunidades = {"Andalucía", "Aragón", "Asturias", "Baleares", "Canarias", "Cantabria", "Castilla La Mancha", "Castilla León", "Cataluña", "Ceuta", "Comunidad de Madrid", "Comunidad Valenciana", "Extremadura", "Galicia", "La Rioja", "Melilla", "Murcia", "Navarra", "País Vasco"};
        int i = 0;
        while(i < comunidades.length){
            if(hip.getComunidad_autonoma().equals(comunidades[i])) break;
            i++;
        }
        sp_comunidad.setSelection(i);

        String [] bancos = {"ING","SANTANDER","BBVA","CAIXABANK","BANKINTER","EVO BANCO","SABADELL","UNICAJA","DEUTSCHE BANK","OPEN BANK","KUTXA BANK","IBERCAJA","ABANCA"};
        int j = 0;
        while(j < bancos.length){
            if(hip.getBanco_asociado().equals(bancos[j])) break;
            j++;
        }
        sp_bancos.setSelection(j);

        if(hip.getTipo_vivienda().equals("general")) {
            check_vivienda_general.setChecked(false);
            check_vivienda_poficial.setChecked(true);
        }

        if(hip.getAntiguedad_vivienda().equals("nueva")){
            check_vivienda_nueva.setChecked(false);
            check_vivienda_smano.setChecked(true);
        }

        precio_vivienda.setText(Double.toString(hip.getPrecio_vivienda()));
        cantidad_abonada.setText(Double.toString(hip.getCantidad_abonada()));
        plazo.setText(Integer.toString(hip.getPlazo_anios()));

        //date

        if(hip.getTipo_hipoteca().equals("variable")){
            ActivarCampos("variable");
            check_fija.setChecked(false);
            check_variable.setChecked(true);

            edit_duracion_primer_porcentaje.setText(Integer.toString(hip.getDuracion_primer_porcentaje_variable()));
            edit_diferencial_variable.setText(Double.toString(hip.getPorcentaje_diferencial_variable()));
            if(!hip.isRevision_anual()){
                check_seis_meses.setChecked(true);
                check_un_anio.setChecked(false);
            }
        } else if(hip.getTipo_hipoteca().equals("mixta")){
            ActivarCampos("mixta");

            check_fija.setChecked(false);
            check_mixta.setChecked(true);
            if(!hip.isRevision_anual()){
                check_seis_meses.setChecked(true);
                check_un_anio.setChecked(false);
            }
            edit_anios_fija.setText(Integer.toString(hip.getAnios_fija_mixta()));
            edit_porcentaje_fijo_mix.setText(Double.toString(hip.getPorcentaje_fijo_mixta()));
            edit_diferencial_mixto.setText(Double.toString(hip.getPorcentaje_diferencial_mixta()));
        } else { //fija
            edit_porcentaje_fijo.setText(Double.toString(hip.getPorcentaje_fijo()));
        }

        gastos_totales.setText(Double.toString(hip.getTotalGastos()));
        vinculaciones.setText(Double.toString(hip.getTotalVinculacionesAnual()));
        nombre_hipoteca.setText(hip.getNombre());

    }

}