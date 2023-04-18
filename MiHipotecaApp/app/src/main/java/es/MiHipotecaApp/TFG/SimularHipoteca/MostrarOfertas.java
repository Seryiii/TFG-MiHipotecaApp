package es.MiHipotecaApp.TFG.SimularHipoteca;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import es.MiHipotecaApp.TFG.R;
import es.MiHipotecaApp.TFG.Transfers.Oferta;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.List;

public class MostrarOfertas extends AppCompatActivity {
    RequestQueue requestQueue;
    Context context;
    private RecyclerView rvLista;
    private RecyclerAdapter adapter;
    private List<Oferta> lista_fija = new ArrayList<>();
    private List<Oferta> lista_varMix = new ArrayList<>();
    private Button btn_fijas;
    private Button btn_varMix;
    private TextView tvEspera;
    private JSONObject datos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_ofertas);
        context = this;
        requestQueue = Volley.newRequestQueue(this,new HurlStack());
        try {
            initGUI();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        initValues();
        eventos();
    }


    private void initGUI() throws JSONException {
        rvLista = findViewById(R.id.recyclerOfertas);
        btn_fijas = findViewById(R.id.buttonFijas);
        btn_varMix = findViewById(R.id.buttonVariablesMixta);
        tvEspera = findViewById(R.id.tvEspera);
        //Pasar los datos de una intent a otra
        Intent intent = getIntent();
        String jsonStr = intent.getStringExtra("datos");
        datos = new JSONObject(jsonStr);
    }
    private void eventos(){
        btn_fijas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter = new RecyclerAdapter(lista_fija,"fija");
                rvLista.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });

        btn_varMix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter = new RecyclerAdapter(lista_varMix,"varMix");
                rvLista.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });
    }
    private void initValues(){
        LinearLayoutManager manager = new LinearLayoutManager(MostrarOfertas.this);
        rvLista.setLayoutManager(manager);
        try {
            obtenerDatosBancos();
        }catch (Exception e){

        }

        adapter = new RecyclerAdapter(lista_fija,"fija");
        rvLista.setAdapter(adapter);


    }

    private void obtenerDatosBancos() {
        String url = "http://10.0.2.2:5000/pruebaArray";
        ProgressBar progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        JSONArray mJsonArray = null;
                        try {
                            mJsonArray = response.getJSONArray("fija");
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                        for (int i = 0; i < mJsonArray.length(); i++) {
                            try {
                                JSONObject jsonObject = mJsonArray.getJSONObject(i);
                                String banco = jsonObject.getString("banco");
                                String desc = jsonObject.getString("desc");
                                String tin = jsonObject.getString("tin");
                                String tae = jsonObject.getString("tae");
                                String cuota = jsonObject.getString("cuota");
                                Oferta oferta = new Oferta(banco,desc,tin,tae,cuota);
                                lista_fija.add(oferta);
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }

                        }
                        try {
                            mJsonArray = response.getJSONArray("var_mixta");
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                        for (int i = 0; i < mJsonArray.length(); i++) {
                            try {
                                JSONObject jsonObject = mJsonArray.getJSONObject(i);
                                String banco = jsonObject.getString("banco");
                                String desc = jsonObject.getString("desc");
                                String tin_x_anios = jsonObject.getString("tin_x_anios");
                                String tin_resto = jsonObject.getString("tin_resto");
                                String tae = jsonObject.getString("tae");
                                String cuota_x_anios = jsonObject.getString("cuota_x_anios");
                                String cuota_resto = jsonObject.getString("cuota_resto");
                                Oferta oferta = new Oferta(banco,desc,tin_x_anios,tin_resto,tae,cuota_x_anios,cuota_resto);
                                lista_varMix.add(oferta);
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }

                        }
                        adapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
                        btn_fijas.setVisibility(View.VISIBLE);
                        btn_varMix.setVisibility(View.VISIBLE);
                        tvEspera.setVisibility(View.GONE);

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Intent intent = new Intent(MostrarOfertas.this, CompararNuevaHipoteca.class);
                startActivity(intent);
                Toast.makeText(MostrarOfertas.this,"Ha ocurrido un problema al conectar con el servidor", Toast.LENGTH_LONG).show();
                Log.d("PETICIONES", error.toString());
            }
        }
        );
        request.setRetryPolicy(new DefaultRetryPolicy(
                60000, // segundos
                0, // 1 reintentos
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(request);

    }

    private void postPrueba(){
        String url = "http://10.0.2.2:5000/postPrueba";
        JSONObject params = new JSONObject();
        try {
            params.put("precio", 12);
            params.put("edad", 10);
            params.put("fecha_nacimiento", "10/07/2000");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Manejar la respuesta de la API
                        try {
                            // Obtener los datos devueltos
                            int precio = response.getInt("precio");
                            int edad = response.getInt("edad");
                            String fechaNacimiento = response.getString("fecha_nacimiento");


                            // Hacer un log de los datos devueltos
                            Log.d("RESPONSE", "Precio: " + precio);
                            Log.d("RESPONSE", "Edad: " + edad);
                            Log.d("RESPONSE", "Fecha de Nacimiento: " + fechaNacimiento);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }




                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Manejar errores de la solicitud
                Log.e("ERROR", error.toString());
            }
        });

// Agregar solicitud a la RequestQueue
        requestQueue.add(request);
    }

}



/**
 private void obtenerDatosBancos() {
 String url = "http://10.0.2.2:5000/pruebaArray";
 Log.d("PETICION", "Botón pulsado");
 JsonObjectRequest request = new JsonObjectRequest(
 Request.Method.GET,
 url,
 null,
 new Response.Listener<JSONObject>() {
@Override
public void onResponse(JSONObject response) {
Log.d("PETICION_1", "Terminada");
latch.countDown();
}

}, new Response.ErrorListener() {
@Override
public void onErrorResponse(VolleyError error) {
latch.countDown();
}
}
 );
 requestQueue.add(request);
 }
 **/