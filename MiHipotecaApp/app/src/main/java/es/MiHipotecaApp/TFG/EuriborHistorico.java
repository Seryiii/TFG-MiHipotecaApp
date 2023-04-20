package es.MiHipotecaApp.TFG;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.CountDownLatch;

import es.MiHipotecaApp.TFG.Transfers.Euribor;

public class EuriborHistorico extends AppCompatActivity {
    RequestQueue requestQueue;
    CountDownLatch latch = new CountDownLatch(1);
    Context context;

    private FirebaseFirestore db;

    private final String TAG = "EURIBOR ACTIVITY";

    public EuriborHistorico(Context context) {


        db = FirebaseFirestore.getInstance();
        this.context = context;
        requestQueue = Volley.newRequestQueue( this.context,new HurlStack());
    }
    protected void ActualizarEuribor() {
        String url = "http://10.0.2.2:5000/Euribor";
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        JSONArray mJsonArray = null;
                        try {
                            mJsonArray = response.getJSONArray("prueba");
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                        for (int i = 0; i < mJsonArray.length(); i++) {
                            try {
                                JSONObject jsonObject = mJsonArray.getJSONObject(i);
                                String anio = jsonObject.getString("anio");
                                String mes = jsonObject.getString("mes");
                                String valor_euribor = jsonObject.getString("valor");
                                Double valor=Double.parseDouble(valor_euribor);
                                Euribor eu=new Euribor(anio,mes,valor);
                                db.collection("euribor").add(eu).addOnSuccessListener(new OnSuccessListener<DocumentReference>(){


                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        Log.w(TAG,"euribor registrado con exito en Firestore: ");
                                        finish();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG,"Error al registrar euribor en Firestore: ");
                                    }
                                });

                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }

                        }
                    }
                }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {


                Log.d("PETICIONES", error.toString());
            }
        }
        );
        request.setRetryPolicy(new DefaultRetryPolicy(
                60000, // segundos
                0, // 1 reintentos
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //requestQueue.add(request);
    }
}

