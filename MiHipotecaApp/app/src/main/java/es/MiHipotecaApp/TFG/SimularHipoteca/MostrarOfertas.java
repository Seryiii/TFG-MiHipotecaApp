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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;
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
    private  boolean detalles;

    private Spinner sp_bancos;
    private TextView txt_filtrarBancos;
    private Switch switchBusqueda;
    private boolean fija = true;


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
        detalles = datos.getBoolean("detalles");
        sp_bancos = findViewById(R.id.sp_bancos);
        txt_filtrarBancos = findViewById(R.id.txt_filtrarBancos);
        switchBusqueda = findViewById(R.id.switchBusqueda);

    }
    private void eventos(){
        btn_fijas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter = new RecyclerAdapter(lista_fija,"fija",detalles);
                rvLista.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                String[] bancosFija = bancosDiferentes(lista_fija);
                ArrayAdapter<String> adapterSpinner_fija = new ArrayAdapter(context, android.R.layout.simple_spinner_item, bancosFija);
                sp_bancos.setAdapter(adapterSpinner_fija);
                fija = true;
            }
        });

        btn_varMix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter = new RecyclerAdapter(lista_varMix,"varMix",detalles);

                String[] bancosVarMixt = bancosDiferentes(lista_varMix);
                ArrayAdapter<String> adapterSpinner_varMix = new ArrayAdapter(context, android.R.layout.simple_spinner_item, bancosVarMixt);
                sp_bancos.setAdapter(adapterSpinner_varMix);
                rvLista.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                fija = false;
            }
        });
        switchBusqueda.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //Aquí es donde puedes hacer algo con el estado del switch
                if (isChecked) {
                    //el switch está activado
                    if(fija){
                        String[] bancosFija = bancosDiferentes(lista_fija);
                        ArrayAdapter<String> adapterSpinner_fija = new ArrayAdapter(context, android.R.layout.simple_spinner_item, bancosFija);
                        sp_bancos.setAdapter(adapterSpinner_fija);
                    }
                    else{
                        String[] bancosVarMixt = bancosDiferentes(lista_varMix);
                        ArrayAdapter<String> adapterSpinner_varMix = new ArrayAdapter(context, android.R.layout.simple_spinner_item, bancosVarMixt);
                        sp_bancos.setAdapter(adapterSpinner_varMix);
                    }
                    sp_bancos.setVisibility(View.VISIBLE);

                } else {
                    sp_bancos.setVisibility(View.GONE);
                    if (fija){
                        adapter = new RecyclerAdapter(lista_fija,"fija",detalles);
                        rvLista.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }else{
                        adapter = new RecyclerAdapter(lista_varMix,"varMix",detalles);
                        rvLista.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        });
        sp_bancos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                List<Oferta> cuentasFiltradas = new ArrayList<>();
                String bancoSeleccionado = (String) adapterView.getItemAtPosition(i);
                if (fija) {
                    for(Oferta o : lista_fija){
                        if (o.getBanco().equals(bancoSeleccionado)) cuentasFiltradas.add(o);
                    }
                    adapter = new RecyclerAdapter(cuentasFiltradas,"fija",detalles);
                }
                else{
                    for(Oferta o : lista_varMix){
                        if (o.getBanco().equals(bancoSeleccionado)) cuentasFiltradas.add(o);
                    }
                    adapter = new RecyclerAdapter(cuentasFiltradas,"varMix",detalles);
                }
                rvLista.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }
    private void initValues() {
        LinearLayoutManager manager = new LinearLayoutManager(MostrarOfertas.this);
        rvLista.setLayoutManager(manager);
        try {
            obtenerDatosBancos();
        }catch (Exception e){}
        Log.d("DETALLES", String.valueOf(detalles));
        if(detalles) adapter = new RecyclerAdapter(lista_fija,"fija",detalles);
        else  adapter = new RecyclerAdapter(lista_fija,"fija",detalles);
        rvLista.setAdapter(adapter);


    }
    private String[] bancosDiferentes(List<Oferta> lista){
        List<String> bancos = new ArrayList<>();
        String[] bancosFinal;
        for (int i = 0; i < lista.size(); i++){
            if (bancos.isEmpty()) bancos.add(lista.get(i).getBanco());
            else{
                if (!bancos.contains(lista.get(i).getBanco()))bancos.add(lista.get(i).getBanco());
            }
        }
        bancosFinal = bancos.toArray(new String[bancos.size()]);
        return bancosFinal;
    }

    private void obtenerDatosBancos() {
        String url = "http://10.0.2.2:5000/pruebaArray";
        ProgressBar progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                datos,
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

                                if(detalles){
                                    String vinculaciones = jsonObject.getString("vinculaciones");
                                    Oferta oferta = new Oferta(banco,desc,tin,tae,cuota,vinculaciones);
                                    lista_fija.add(oferta);
                                }
                                else{
                                    Oferta oferta = new Oferta(banco,desc,tin,tae,cuota);
                                    lista_fija.add(oferta);
                                }

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
                                if(detalles){
                                    String vinculaciones = jsonObject.getString("vinculaciones");
                                    Oferta oferta = new Oferta(banco,desc,tin_x_anios,tin_resto,tae,cuota_x_anios,cuota_resto,vinculaciones);
                                    lista_varMix.add(oferta);
                                }
                                else{
                                    Oferta oferta = new Oferta(banco,desc,tin_x_anios,tin_resto,tae,cuota_x_anios,cuota_resto);
                                    lista_varMix.add(oferta);
                                }


                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }

                        }

                        adapter.notifyDataSetChanged();
                        //sp_bancos.setVisibility(View.VISIBLE);
                        txt_filtrarBancos.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        btn_fijas.setVisibility(View.VISIBLE);
                        btn_varMix.setVisibility(View.VISIBLE);
                        tvEspera.setVisibility(View.GONE);
                        switchBusqueda.setVisibility(View.VISIBLE);

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
                120000, // segundos
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



