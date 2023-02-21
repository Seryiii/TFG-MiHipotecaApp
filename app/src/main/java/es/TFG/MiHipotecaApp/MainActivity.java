package es.TFG.MiHipotecaApp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import es.TFG.MiHipotecaApp.Integracion.Constantes;
import es.TFG.MiHipotecaApp.Integracion.VolleySingleton;
import es.TFG.MiHipotecaApp.Transfers.Usuario;

public class MainActivity extends AppCompatActivity {

    private Button btn_simular_hipoteca;
    private Button btn_iniciar_sesion;
    private Button btn_registrarse;

    private Gson gson = new Gson();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
    }

    private void initUI() {
        btn_simular_hipoteca = findViewById(R.id.btn_simular_pagina_inicio);
        btn_iniciar_sesion = findViewById(R.id.btn_iniciar_sesion_inicio);
        btn_registrarse = findViewById(R.id.btn_registrarse_pagina_inicio);

        btn_iniciar_sesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, IniciarSesion.class);
                startActivity(i);
            }
        });

        btn_registrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, Registro.class);
                startActivity(i);
            }
        });


        //FALTA ESTE
        btn_simular_hipoteca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //PRUEBA BASE DE DATOS
                cogerTodosUsuarios();
            }
        });
    }

    private void cogerTodosUsuarios() {

        //Hacer una llamada a base de datos para coger todos los usuarios

        String newURL = Constantes.GET_ALL_USERS;

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
                    JSONArray usuariosJson = response.getJSONArray("usuarios");
                    Type listType = new TypeToken<List<Usuario>>() {}.getType();
                    ArrayList<Usuario> usuariosApp = gson.fromJson(usuariosJson.toString(), listType);
                    for (int i = 0; i < usuariosApp.size(); i++){
                        Log.i("USUARIO","Nombre usuario: " + usuariosApp.get(i).getNombre());
                    }

                case "2":
                    //NO HAY USUARIOS

                    break;

                case "3":
                    String mensaje3 = response.getString("mensaje");
                    Toast.makeText(this, mensaje3, Toast.LENGTH_LONG).show();
                    break;
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}