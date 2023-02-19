package es.TFG.MiHipotecaApp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Registro extends AppCompatActivity {

    private EditText correo;
    private EditText nombre;
    private EditText contra;
    private EditText confir_contra;

    private CheckBox terminos;
    private TextView link_terminos;

    private Button registrar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        initUI();
    }

    private void initUI(){
        correo = findViewById(R.id.edit_correo_registro);
        nombre = findViewById(R.id.edit_nombre_registro);
        contra = findViewById(R.id.edit_contra_registro);
        confir_contra = findViewById(R.id.edit_repetir_contra_registro);
        terminos = findViewById(R.id.terminos_registro);
        link_terminos = findViewById(R.id.link_abrir_terminos_condiciones);
        registrar = findViewById(R.id.btn_registrarse);

        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrarUsuario();
            }
        });

        link_terminos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Abrir un dialogo con la información de términos y condiciones

            }
        });
    }

    private void registrarUsuario(){

        if(terminos.isChecked()){
            if (TextUtils.isEmpty(correo.getText().toString())) correo.setError(getString(R.string.correo_vacio));
            else{
                if(comprobarSiCorreoExiste()){
                    if(!TextUtils.isEmpty(nombre.getText().toString())){
                        comprobarContrasenia();
                    }else nombre.setError(getString(R.string.nombre_vacio));
                }else correo.setError(getString(R.string.correo_en_bd));
            }

        }else terminos.setError(getString(R.string.necesario_aceptar_terminos));


    }

    /** Esta funcion comprueba si el correo introducido existe en la base de datos,
     *  devuelve true si el correo no existe.**/
    private boolean comprobarSiCorreoExiste(){

        boolean existe = true;

        //Llamar a base de datos


        return existe;

    }

    /** Esta funcion comprueba que las dos contraseñas introducidas sean iguales y que siga el patron:
     *  Entre 8 y 15 caracteres
     *  Al menos una mayúscula
     *  Al menos una minúscula
     *  Al menos un  dígito
     *  NO Caracteres especiales
     *  **/
    private void comprobarContrasenia(){

        if(TextUtils.isEmpty(contra.getText().toString())) contra.setError(getString(R.string.contra_vacia));
        else{
            if(contra.getText().toString().equals(confir_contra.getText().toString())) {
                Pattern pat = Pattern.compile("^(?=\\w*\\d)(?=\\w*[A-Z])(?=\\w*[a-z])\\S{8,15}$");
                Matcher mat = pat.matcher(contra.getText().toString());
                if(mat.matches()) registrarUsuarioBD();
            }else contra.setError(getString(R.string.contras_no_coinciden));
        }
    }

    /** Esta funcion registra un nuevo usuario en la bd**/
    private void registrarUsuarioBD() {

    }

}