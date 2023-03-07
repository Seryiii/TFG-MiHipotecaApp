package es.MiHipotecaApp.TFG.UsuarioRegistrado.HipotecasSeguimiento;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import es.MiHipotecaApp.TFG.R;

public class NuevoSeguimiento extends AppCompatActivity {
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

    }
    private void Eventos() {
        check_vivienda_general.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) check_vivienda_poficial.setChecked(false);

            }
        });

        check_vivienda_poficial.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) check_vivienda_general.setChecked(false);

            }
        });
        check_vivienda_nueva.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) check_vivienda_smano.setChecked(false);

            }
        });

        check_vivienda_smano.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) check_vivienda_nueva.setChecked(false);

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
}