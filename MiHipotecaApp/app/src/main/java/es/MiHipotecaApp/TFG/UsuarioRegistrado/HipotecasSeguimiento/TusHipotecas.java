package es.MiHipotecaApp.TFG.UsuarioRegistrado.HipotecasSeguimiento;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


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
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import es.MiHipotecaApp.TFG.R;
import es.MiHipotecaApp.TFG.Transfers.HipotecaSegFija;
import es.MiHipotecaApp.TFG.Transfers.HipotecaSegMixta;
import es.MiHipotecaApp.TFG.Transfers.HipotecaSegVariable;
import es.MiHipotecaApp.TFG.Transfers.HipotecaSeguimiento;
public class TusHipotecas extends Fragment {

    private RecyclerView recyclerHipotecas;
    private ArrayList<HipotecaSeguimiento> listaHipotecasSeg;

    private AdaptadorHipotecasSeguimiento adapter;

    private final String TAG = "Tus Hipotecas";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_tus_hipotecas, container, false);
        recyclerHipotecas = view.findViewById(R.id.recycler_hipotecas_seguimiento);
        recyclerHipotecas.addItemDecoration(new HorizontalSpaceItemDecoration(40));
        recyclerHipotecas.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.HORIZONTAL,false));
        listaHipotecasSeg = new ArrayList<>();
        adapter=new AdaptadorHipotecasSeguimiento(listaHipotecasSeg);
        cargarHipotecasUsuario();
        return view;

    }

    private void cargarHipotecasUsuario(){

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        CollectionReference hipotecasRef = db.collection("hipotecas_seguimiento");
        Query consultaHipotecasUsu = hipotecasRef.whereEqualTo("idUsuario",user.getUid());
        consultaHipotecasUsu.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()){
                    String nombre = documentSnapshot.getString("nombre");
                    String comunidad = documentSnapshot.getString("comunidad_autonoma");
                    String tipo = documentSnapshot.getString("tipo_hipoteca");
                    String antiguedad = documentSnapshot.getString("antiguedad_vivienda");
                    double precio_vivienda = documentSnapshot.getDouble("precio_vivienda");
                    double cantidad_abonada = documentSnapshot.getDouble("cantidad_abonada");
                    long plazo_l = documentSnapshot.getLong("plazo_anios");
                    int plazo = (int) plazo_l;
                    long anio_actual_l = documentSnapshot.getLong("anio_hipoteca_actual");
                    int anio_actual = (int) anio_actual_l;
                    double total_gastos = documentSnapshot.getDouble("totalGastos");
                    double total_vinculaciones = documentSnapshot.getDouble("totalVinculacionesAnual");
                    String banco_asociado = documentSnapshot.getString("banco_asociado");
                    HipotecaSeguimiento h;

                    if (tipo.equals("fija")){
                        double porcen_fijo = documentSnapshot.getDouble("porcentaje_fijo");
                        h = new HipotecaSegFija(nombre, comunidad, tipo, antiguedad, precio_vivienda, cantidad_abonada, plazo, anio_actual, total_gastos, total_vinculaciones, banco_asociado, porcen_fijo);
                    }else if(tipo.equals("variable")){
                        long duracion_primer_por_l = documentSnapshot.getLong("duracion_primer_porcentaje_variable");
                        int duracion_primer_por = (int) duracion_primer_por_l;
                        double primer_porcen = documentSnapshot.getDouble("primer_porcentaje_variable");
                        double porcentaje_diferen = documentSnapshot.getDouble("porcentaje_diferencial_variable");
                        boolean revision = documentSnapshot.getBoolean("revision_anual");
                        h = new HipotecaSegVariable(nombre, comunidad, tipo, antiguedad, precio_vivienda, cantidad_abonada, plazo, anio_actual, total_gastos, total_vinculaciones, banco_asociado, duracion_primer_por, primer_porcen, porcentaje_diferen, revision);
                    }else{
                        long anios_fija_mix_l =  documentSnapshot.getLong("anios_fija_mixta");
                        int anios_fija_mix = (int) anios_fija_mix_l;
                        double porcen_fijo_mix = documentSnapshot.getDouble("porcentaje_fijo_mixta");
                        double porcent_diferen_mix = documentSnapshot.getDouble("porcentaje_diferencial_mixta");
                        boolean revision = documentSnapshot.getBoolean("revision_anual");
                        h = new HipotecaSegMixta(nombre, comunidad, tipo, antiguedad, precio_vivienda, cantidad_abonada, plazo, anio_actual, total_gastos, total_vinculaciones, banco_asociado, anios_fija_mix, porcen_fijo_mix, porcent_diferen_mix, revision);
                    }
                    listaHipotecasSeg.add(h);
                }

                listaHipotecasSeg.add(new HipotecaSeguimiento("Nueva hipoteca"));
                adapter= new AdaptadorHipotecasSeguimiento(listaHipotecasSeg);
                recyclerHipotecas.setAdapter(adapter);
                adapter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (recyclerHipotecas.getChildAdapterPosition(v) != listaHipotecasSeg.size() - 1) {
                            PopupMenu popupMenu = new PopupMenu(getActivity(), v);
                            popupMenu.getMenuInflater().inflate(R.menu.menu_opciones, popupMenu.getMenu());
                            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem item) {
                                    switch (item.getItemId()) {
                                        case R.id.ver_hipoteca:
                                            //Redirigir a la vista de la hipoteca de seguimiento

                                            return true;
                                        case R.id.editar_hipoteca:
                                            //Redirigir a la vista de editar hipoteca seguimiento

                                            Intent i = new Intent(getActivity().getApplicationContext(), NuevoSeguimiento.class);
                                            startActivity(i);
                                            return true;
                                        case R.id.eliminar_hipoetca:
                                            AlertDialog dialogo = new AlertDialog.Builder(getActivity())
                                                    .setPositiveButton(getString(R.string.si_eliminar_cuenta), new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            //Eliminar hipoteca de la base de datos
                                                        }
                                                    })
                                                    .setNegativeButton(getString(R.string.no_eliminar_cuenta), new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {

                                                        }
                                                    })
                                                    .setTitle("ELIMINAR HIPOTECA").setMessage("¿Desea eliminar la hipoteca?").create();
                                            dialogo.show();

                                            return true;
                                        default:
                                            return false;
                                    }
                                }
                            });
                            popupMenu.show();
                        }else{
                            Intent i = new Intent(getActivity().getApplicationContext(), NuevoSeguimiento.class);
                            startActivity(i);
                        }
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG,"Error al traer hipotecas de bd");
            }
        });
    }

}