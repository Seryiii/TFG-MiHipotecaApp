package es.TFG.MiHipotecaApp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import es.TFG.MiHipotecaApp.Integracion.Constantes;
import es.TFG.MiHipotecaApp.Integracion.VolleySingleton;
import es.TFG.MiHipotecaApp.UsuarioRegistrado.PaginaPrincipal;

public class Registro extends AppCompatActivity {

    private EditText correo;
    private EditText nombre;
    private EditText contra;
    private EditText confir_contra;

    private CheckBox terminos;
    private TextView link_terminos;

    private Button registrar;

    private RadioGroup avatarRadio;

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
            if (TextUtils.isEmpty(correo.getText())) correo.setError(getString(R.string.correo_vacio));
            else{
                comprobarSiCorreoExiste(correo.getText().toString());
            }

        }else terminos.setError(getString(R.string.necesario_aceptar_terminos));


    }

    /** Esta funcion comprueba si el correo introducido existe en la base de datos,
     *  devuelve true si el correo no existe.**/
    private void comprobarSiCorreoExiste(String correo){
        // Añadir parámetro a la URL del web service
        String newURL = Constantes.GET_BY_CORREO + "?correoUsuario=" + correo;
        // Realizar petición GET_BY_CORREO
        VolleySingleton.getInstance(this).addToRequestQueue(
                new JsonObjectRequest(
                        Request.Method.GET,
                        newURL,
                        null,
                        new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                // Procesar respuesta Json
                                procesarRespuestaExisteUsu(response);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("Error", "Error Volley: " + error.getMessage());
                            }
                        }
                )
        );
    }

    /** Esta funcion procesa la respuesta enviada por el webservice:
     *  - Estado == 1 El usuario con correo  pasado a la peticion existe en bd
     *  - Estado == 2 El usuario con nombre de usuario pasado a la peticon no existe en bd
     *  - Estado == 3 Error en la peticion, no llega bien el nombre de usuario **/
    private void procesarRespuestaExisteUsu(JSONObject response) {

        try {
            // Obtener atributo "estado"
            String estado = response.getString("estado");

            switch (estado) {
                case "1":
                    correo.setError(getString(R.string.correo_en_bd));
                    break;

                case "2":
                    //NO EXISTE EL USUARIO POR LO QUE SE PASA A COMPROBAR CONTRASEÑA Y SI NOMBRE VACIO
                    if(!TextUtils.isEmpty(nombre.getText())){
                        comprobarContrasenia();
                    }else nombre.setError(getString(R.string.nombre_vacio));
                    break;

                case "3":
                    String mensaje3 = response.getString("mensaje");
                    Toast.makeText(
                            this,
                            mensaje3,
                            Toast.LENGTH_LONG).show();
                    break;
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /** Esta funcion comprueba que las dos contraseñas introducidas sean iguales y que siga el patron:
     *  Entre 8 y 15 caracteres
     *  Al menos una mayúscula
     *  Al menos una minúscula
     *  Al menos un  dígito
     *  NO  hace falta Caracteres especiales
     *  **/
    private void comprobarContrasenia(){

        if(TextUtils.isEmpty(contra.getText())) contra.setError(getString(R.string.contra_vacia));
        else{
            if(contra.getText().toString().equals(confir_contra.getText().toString())) {
                Pattern pat = Pattern.compile("^(?=\\w*\\d)(?=\\w*[A-Z])(?=\\w*[a-z])\\S{8,15}$");
                Matcher mat = pat.matcher(contra.getText().toString());
                if(mat.matches()) registarUsuarioBD();
            }else contra.setError(getString(R.string.contras_no_coinciden));
        }
    }

    /** Esta funcion registra un nuevo usuario en la bd**/
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

    }

    /** Esta funcion procesa la respuesta enviada por el webservice:
     *  Estado == 1 Usuario creado correctamente, se crea un toast informando
     *  Estado == 2 Usuario no se ha creado correctamente, se crea un toast informando **/
    private void procesarRespuestaCreacionUsuario(JSONObject response){
        try {
            // Obtener atributo "estado"
            String estado  = response.getString("estado");
            String mensaje = response.getString("mensaje");

            switch (estado) {
                case "1": // EXITO
                    Integer idUsuario = response.getInt("id");
                    String idUser = Integer.toString(idUsuario);
                    // Primero, obtén una instancia del objeto SharedPreferences
                    SharedPreferences prefs = getSharedPreferences("credenciales", MODE_PRIVATE);
                    // Al iniciar sesión, guarda el id del usuario en las preferencias compartidas
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("idusuario", idUser);
                    editor.apply();
                    Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();
                    Intent i = new Intent(Registro.this, PaginaPrincipal.class);
                    startActivity(i);
                    break;
                case "2": // FALLIDO
                    // Mostrar mensaje
                    Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();
                    break;
            }

        } catch (JSONException e) {
            Log.d("ERROR", e.getMessage());
        }

    }


}