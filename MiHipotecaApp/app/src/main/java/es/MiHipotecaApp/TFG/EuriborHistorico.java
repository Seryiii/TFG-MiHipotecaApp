package es.MiHipotecaApp.TFG;

import android.content.Context;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



import es.MiHipotecaApp.TFG.Transfers.Euribor;

public class EuriborHistorico extends AppCompatActivity {
    RequestQueue requestQueue;

    Context context;

    private FirebaseFirestore db;

    private final String TAG = "EURIBOR ACTIVITY";

    public EuriborHistorico(Context context) {


        db = FirebaseFirestore.getInstance();
        this.context = context;
        requestQueue = Volley.newRequestQueue( this.context,new HurlStack());
    }
    protected void ActualizarEuribor() {
        //147.96.81.245:5000
        //http://10.0.2.2:5000
        String url = "http://147.96.81.245:5000/EuriborHistorico";
        System.out.println("Intenado obtener Euribor");
        Log.w(TAG,"Error al intentar acceder a Euribor : ");

        // Request a string response from the provided URL.
        final boolean[] euriborObtenido = {false};

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        System.out.println("Intenado obtener Euribor onResponse");
                        JSONArray mJsonArray = null;

                        try {
                            mJsonArray = response.getJSONArray("Eur_dos_ult_meses");
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
                                /* db.collection("euribor")
                                        .whereEqualTo("anio", anio)
                                        .whereEqualTo("mes", mes)
                                        .get()
                                        .addOnCompleteListener(task -> {
                                            if (task.isSuccessful()) {
                                                QuerySnapshot querySnapshot = task.getResult();
                                                if (querySnapshot != null && !querySnapshot.isEmpty()) {
                                                    // El documento ya existe en la base de datos
                                                    Log.d(TAG, "El documento ya existe en la base de datos");
                                                } else {
                                                    // El documento no existe en la base de datos, agrégalo
                                                    db.collection("euribor")
                                                            .add(eu)
                                                            .addOnSuccessListener(documentReference -> {
                                                                Log.d(TAG, "euribor registrado con exito en Firestore");
                                                            })
                                                            .addOnFailureListener(e -> {
                                                                Log.w(TAG, "Error al registrar euribor en Firestore", e);
                                                            });
                                                }
                                            } else {
                                                Log.w(TAG, "Error al consultar euribor en Firestore", task.getException());
                                            }
                                        });*/

                                db.collection("euribor").add(eu).addOnSuccessListener(new OnSuccessListener<DocumentReference>(){


                                    @Override
                                public void onSuccess(DocumentReference documentReference) {
                                        if (euriborObtenido[0]) {
                                            return;
                                        }

                                        euriborObtenido[0] = true;
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
                        finish();
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
        requestQueue.add(request);

    }
}

