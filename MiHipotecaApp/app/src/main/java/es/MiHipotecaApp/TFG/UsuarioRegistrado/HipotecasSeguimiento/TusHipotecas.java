package es.MiHipotecaApp.TFG.UsuarioRegistrado.HipotecasSeguimiento;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import es.MiHipotecaApp.TFG.IniciarSesion;
import es.MiHipotecaApp.TFG.R;
import es.MiHipotecaApp.TFG.Transfers.HipotecaSeguimiento;
import es.MiHipotecaApp.TFG.UsuarioRegistrado.InformacionUsuario.InfoPerfilUsuario;

public class TusHipotecas extends Fragment {
    private Button btn_config_usuario;
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
        // Coger las hipotecas de seguimiento del usuario de firebase
        foto_perfil = view.findViewById(R.id.foto_perfil_pag_principal);
        btn_config_usuario = view.findViewById(R.id.btn_config_usuario);
        eventos();
        return view;
    }

    public void eventos(){
        btn_config_usuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity().getApplicationContext(), InfoPerfilUsuario.class);
                startActivity(i);
            }
        });
    }





}