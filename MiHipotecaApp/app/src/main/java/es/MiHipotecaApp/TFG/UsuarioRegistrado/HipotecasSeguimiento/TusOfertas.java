package es.MiHipotecaApp.TFG.UsuarioRegistrado.HipotecasSeguimiento;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import es.MiHipotecaApp.TFG.R;
import es.MiHipotecaApp.TFG.SimularHipoteca.MostrarOfertas;
import es.MiHipotecaApp.TFG.SimularHipoteca.RecyclerAdapter;
import es.MiHipotecaApp.TFG.Transfers.Oferta;

public class TusOfertas extends AppCompatActivity {
    private RecyclerView rvLista;
    private RecyclerAdapter adapter;
    private List<Oferta> ofertasFija = new ArrayList<>();
    private List<Oferta> ofertasVarMix = new ArrayList<>();
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tus_ofertas);
        rvLista = findViewById(R.id.recylcer_tusOfertas);
        db   = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        cargarOfertas();
    }
    private void cargarOfertas() {
        LinearLayoutManager manager = new LinearLayoutManager(TusOfertas.this);
        rvLista.setLayoutManager(manager);
        //Crear el adapter con los parametros vacios
        String uid = user.getUid();
        CollectionReference ofertasRef = db.collection("ofertas_guardadas");
        // Crear una consulta para obtener los documentos con el UID del usuario
        Query query = ofertasRef.whereEqualTo("uid", uid);
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()){
                    String banco = documentSnapshot.getString("banco");
                    String desc = documentSnapshot.getString("desc");
                    String tipo = documentSnapshot.getString("tipo");
                    String tae = documentSnapshot.getString("tae");
                    String vinculaciones = documentSnapshot.getString("vinculaciones");
                    Oferta o;
                    if(tipo.equals("fija")){
                        String cuota = documentSnapshot.getString("cuota");
                        String tin = documentSnapshot.getString("tin");
                        if (vinculaciones.equals("")){
                            o = new Oferta(banco,desc,tin,tae,cuota);
                        }else {
                            o = new Oferta(banco,desc,tin,tae,cuota,vinculaciones);
                        }
                        ofertasFija.add(o);
                    }
                    else{
                        String cuota_x = documentSnapshot.getString("cuota_x");
                        String cuota_resto = documentSnapshot.getString("cuota_resto");
                        String tin_x = documentSnapshot.getString("tin_x");
                        String tin_resto = documentSnapshot.getString("tin_resto");
                        if (vinculaciones.equals("")){
                            o = new Oferta(banco,desc,tin_x,tin_resto,tae,cuota_x,cuota_resto);
                        }else {
                            o = new Oferta(banco,desc,tin_x,tin_resto,tae,cuota_x,cuota_resto,vinculaciones);
                        }
                        ofertasVarMix.add(o);
                    }


                }
                adapter.notifyDataSetChanged();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }
}