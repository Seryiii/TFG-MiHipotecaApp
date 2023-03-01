package es.MiHipotecaApp.TFG;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;


public class IniciarSesion extends AppCompatActivity {

    private Button btn_iniciar_sesion;
    private EditText correo;
    private EditText contra;
    private TextView link_no_tengo_cuenta_registrarse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciar_sesion);
        initUI();
    }

    private void initUI(){
        correo             = findViewById(R.id.edit_correo_iniciar_sesion);
        contra             = findViewById(R.id.edit_contrasenia_iniciar_sesion);
        btn_iniciar_sesion = findViewById(R.id.btn_iniciar_sesion);
        link_no_tengo_cuenta_registrarse = findViewById(R.id.link_no_tengo_cuenta_registrarse);
        btn_iniciar_sesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(TextUtils.isEmpty(correo.getText())) correo.setError(getString(R.string.correo_vacio));
                else if(TextUtils.isEmpty(contra.getText())) contra.setError(getString(R.string.contra_vacia));
                // else comprobarSiUsuarioCorrecto();
            }
        });
        link_no_tengo_cuenta_registrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(IniciarSesion.this, Registro.class);
                startActivity(i);
            }
        });

    }

    /*
    private void comprobarSiUsuarioCorrecto(){

        String newURL = Constantes.GET_BY_CORREO + "?correoUsuario=" + correo.getText().toString();

        // Realizar petición
        VolleySingleton.getInstance(this).addToRequestQueue(
                new JsonObjectRequest(
                        Request.Method.GET,
                        newURL,
                        null,
                        new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                // Procesar respuesta Json
                                procesarRespuesta(response);
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

    private void procesarRespuesta(JSONObject response) {

        try {
            // Obtener atributo "estado"
            String estado = response.getString("estado");
            switch (estado) {
                case "1":
                    //Parsear objeto
                    JSONObject object = response.getJSONObject("usuario");
                    //Parsear objeto
                    Usuario usuario = gson.fromJson(object.toString(), Usuario.class);
                    if(correo.getText().toString().equals(usuario.getCorreo())) {
                        if(usuario.getPassword().equals(contra.getText().toString())) {
                            // Primero, obtén una instancia del objeto SharedPreferences
                            SharedPreferences prefs = getSharedPreferences("credenciales", MODE_PRIVATE);
                            // Al iniciar sesión, guarda el id del usuario en las preferencias compartidas
                            String idUser = Integer.toString(usuario.getId());
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putString("idusuario", idUser);
                            editor.apply();
                            Intent i = new Intent(IniciarSesion.this, PaginaPrincipal.class);
                            startActivity(i);
                        }else contra.setError(getString(R.string.contra_incorrecta));
                    }else correo.setError(getString(R.string.correo_incorrecto));

                case "2":
                    //NO EXISTE EL USUARIO
                    correo.setError(getString(R.string.correo_no_existente));
                    break;
                case "3":
                    String mensaje3 = response.getString("mensaje");
                    Toast.makeText(this, mensaje3, Toast.LENGTH_LONG).show();
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }*/

}