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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import es.MiHipotecaApp.TFG.R;
import es.MiHipotecaApp.TFG.Transfers.HipotecaSeguimiento;
import es.MiHipotecaApp.TFG.UsuarioRegistrado.InformacionUsuario.ModificarDatosUsuario;

public class TusHipotecas extends Fragment {
    private CircleImageView foto_perfil;
    private RecyclerView recyclerHipotecas;
    private ArrayList<HipotecaSeguimiento> listaHipotecasSeg;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_tus_hipotecas, container, false);
        recyclerHipotecas = view.findViewById(R.id.recycler_hipotecas_seguimiento);
        recyclerHipotecas.addItemDecoration(new HorizontalSpaceItemDecoration(40));
        recyclerHipotecas.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.HORIZONTAL,false));
        listaHipotecasSeg = new ArrayList<>();
        HipotecaSeguimiento h1 = new HipotecaSeguimiento("Hipoteca 1");
        HipotecaSeguimiento h2 = new HipotecaSeguimiento("Hipoteca 2");
        HipotecaSeguimiento h3 = new HipotecaSeguimiento("Hipoteca 3");
        HipotecaSeguimiento h4 = new HipotecaSeguimiento("Nueva hipoteca");
        listaHipotecasSeg.add(h1);
        listaHipotecasSeg.add(h2);
        listaHipotecasSeg.add(h3);
        listaHipotecasSeg.add(h4);


        AdaptadorHipotecasSeguimiento adapter = new AdaptadorHipotecasSeguimiento(listaHipotecasSeg);
        recyclerHipotecas.setAdapter(adapter);

        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            }
        });
        // Coger las hipotecas de seguimiento del usuario de firebase
        foto_perfil = view.findViewById(R.id.foto_perfil_pag_principal);
        return view;
    }


}