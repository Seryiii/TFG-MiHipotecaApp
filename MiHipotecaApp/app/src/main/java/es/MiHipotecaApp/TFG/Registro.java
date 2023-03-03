package es.MiHipotecaApp.TFG;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
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

    private RadioGroup avatarRadio;

    private FirebaseAuth firebaseAuth;
    private AwesomeValidation awesomeValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        initUI();
    }

    private void initUI(){

        firebaseAuth = FirebaseAuth.getInstance();
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(this,R.id.edit_correo_registro, Patterns.EMAIL_ADDRESS,R.string.correo_incorrecto);

        correo = findViewById(R.id.edit_correo_registro);
        nombre = findViewById(R.id.edit_nombre_registro);
        contra = findViewById(R.id.edit_contra_registro);
        confir_contra = findViewById(R.id.edit_repetir_contra_registro);
        terminos = findViewById(R.id.terminos_registro);
        link_terminos = findViewById(R.id.link_abrir_terminos_condiciones);
        registrar = findViewById(R.id.btn_registrarse);
        avatarRadio = findViewById(R.id.grupo_avatar_registro);

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
            if(comprobarContrasenia()){
                if(awesomeValidation.validate()){
                    firebaseAuth.createUserWithEmailAndPassword(correo.getText().toString(), contra.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){

                                //AQUI FALTA METER USUARIO EN FIRESTORE EN COLLECCION USERS

                                Toast.makeText(Registro.this, getString(R.string.usuario_creado_exito), Toast.LENGTH_LONG).show();
                                finish();
                            }else{
                                String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();
                                dameToastdeerror(errorCode);
                            }
                        }
                    });
                }else Toast.makeText(Registro.this, getString(R.string.completar_campos), Toast.LENGTH_LONG).show();
            }

        }else terminos.setError(getString(R.string.necesario_aceptar_terminos));
    }


    private void dameToastdeerror(String error) {

        switch (error) {

            case "ERROR_INVALID_CUSTOM_TOKEN":
                Toast.makeText(Registro.this, "El formato del token personalizado es incorrecto. Por favor revise la documentación", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_CUSTOM_TOKEN_MISMATCH":
                Toast.makeText(Registro.this, "El token personalizado corresponde a una audiencia diferente.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_INVALID_CREDENTIAL":
                Toast.makeText(Registro.this, "La credencial de autenticación proporcionada tiene un formato incorrecto o ha caducado.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_INVALID_EMAIL":
                Toast.makeText(Registro.this, "La dirección de correo electrónico está mal formateada.", Toast.LENGTH_LONG).show();
                correo.setError("La dirección de correo electrónico está mal formateada.");
                correo.requestFocus();
                break;

            case "ERROR_WRONG_PASSWORD":
                Toast.makeText(Registro.this, "La contraseña no es válida o el usuario no tiene contraseña.", Toast.LENGTH_LONG).show();
                contra.setError(getString(R.string.contra_incorrecta));
                contra.requestFocus();
                contra.setText("");
                break;

            case "ERROR_USER_MISMATCH":
                Toast.makeText(Registro.this, "Las credenciales proporcionadas no corresponden al usuario que inició sesión anteriormente..", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_REQUIRES_RECENT_LOGIN":
                Toast.makeText(Registro.this,"Esta operación es sensible y requiere autenticación reciente. Inicie sesión nuevamente antes de volver a intentar esta solicitud.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL":
                Toast.makeText(Registro.this, "Ya existe una cuenta con la misma dirección de correo electrónico pero diferentes credenciales de inicio de sesión. Inicie sesión con un proveedor asociado a esta dirección de correo electrónico.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_EMAIL_ALREADY_IN_USE":
                Toast.makeText(Registro.this, "La dirección de correo electrónico ya está siendo utilizada por otra cuenta..   ", Toast.LENGTH_LONG).show();
                correo.setError(getString(R.string.correo_en_bd));
                correo.requestFocus();
                break;

            case "ERROR_CREDENTIAL_ALREADY_IN_USE":
                Toast.makeText(Registro.this, "Esta credencial ya está asociada con una cuenta de usuario diferente.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_USER_DISABLED":
                Toast.makeText(Registro.this, "La cuenta de usuario ha sido inhabilitada por un administrador..", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_USER_TOKEN_EXPIRED":
                Toast.makeText(Registro.this, "La credencial del usuario ya no es válida. El usuario debe iniciar sesión nuevamente.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_USER_NOT_FOUND":
                Toast.makeText(Registro.this, "No hay ningún registro de usuario que corresponda a este identificador. Es posible que se haya eliminado al usuario.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_INVALID_USER_TOKEN":
                Toast.makeText(Registro.this, "La credencial del usuario ya no es válida. El usuario debe iniciar sesión nuevamente.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_OPERATION_NOT_ALLOWED":
                Toast.makeText(Registro.this, "Esta operación no está permitida. Debes habilitar este servicio en la consola.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_WEAK_PASSWORD":
                Toast.makeText(Registro.this, "La contraseña proporcionada no es válida..", Toast.LENGTH_LONG).show();
                contra.setError("La contraseña no es válida, debe tener al menos 6 caracteres");
                contra.requestFocus();
                break;

        }

    }


    /** Esta funcion comprueba que las dos contraseñas introducidas sean iguales y que siga el patron:
     *  Entre 8 y 15 caracteres
     *  Al menos una mayúscula
     *  Al menos una minúscula
     *  Al menos un  dígito
     *  NO  hace falta Caracteres especiales
     *  **/
    private boolean comprobarContrasenia(){

        if(TextUtils.isEmpty(contra.getText())) contra.setError(getString(R.string.contra_vacia));
        else{
            if(contra.getText().toString().equals(confir_contra.getText().toString())) {
                Pattern pat = Pattern.compile("^(?=\\w*\\d)(?=\\w*[A-Z])(?=\\w*[a-z])\\S{8,15}$");
                Matcher mat = pat.matcher(contra.getText().toString());
                if(mat.matches()) return true;
            }else contra.setError(getString(R.string.contras_no_coinciden));
        }
        return false;
    }

    /*
    /** Esta funcion registra un nuevo usuario en la bd*
    private void registarUsuarioBD(){


        HashMap<String, String> map = new HashMap<>();// Mapeo previo

        map.put("nombre", nombre.getText().toString());
        map.put("correo", correo.getText().toString());
        map.put("contrasenia", contra.getText().toString());
        int avatar = 5;
        switch (avatarRadio.getCheckedRadioButtonId()){
            case R.id.avatar1:
                avatar = 1;
                break;
            case R.id.avatar2:
                avatar = 2;
                break;
            case R.id.avatar3:
                avatar = 3;
                break;
            case R.id.avatar4:
                avatar = 4;
                break;
            default:
                avatar = 5;
        }

        //map.put("avatar", Integer.toString(avatar));
        map.put("avatar", "1");

        // Crear nuevo objeto Json basado en el mapa
        JSONObject jobject = new JSONObject(map);

        VolleySingleton.getInstance(this).addToRequestQueue(
                new JsonObjectRequest(
                        Request.Method.POST,
                        Constantes.CREACION_USUARIO,
                        jobject,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                procesarRespuestaCreacionUsuario(response);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("ERROR","Error Volley: " + error.toString());
                            }
                        }
                ) {
                    @Override
                    public Map<String, String> getHeaders() {
                        Map<String, String> headers = new HashMap<String, String>();
                        headers.put("Content-Type", "application/json; charset=utf-8");
                        headers.put("Accept", "application/json");
                        return headers;
                    }

                    @Override
                    public String getBodyContentType() {
                        return "application/json; charset=utf-8" + getParamsEncoding();
                    }
                }
        );

    }*/



}