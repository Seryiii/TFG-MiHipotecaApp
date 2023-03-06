package es.MiHipotecaApp.TFG.UsuarioRegistrado;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import es.MiHipotecaApp.TFG.IniciarSesion;
import es.MiHipotecaApp.TFG.R;
import es.MiHipotecaApp.TFG.Transfers.HipotecaSeguimiento;
import es.MiHipotecaApp.TFG.UsuarioRegistrado.HipotecasSeguimiento.AdaptadorHipotecasSeguimiento;

public class PaginaPrincipal extends AppCompatActivity {
    private Button btn_simular_hipoteca;
    private Button btn_config_usuario;
    private Button btn_cerrar_sesion;
    private CircleImageView foto_perfil;

    private RecyclerView recyclerHipotecas;
    private ArrayList<HipotecaSeguimiento> listaHipotecasSeg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagina_principal);
        btn_cerrar_sesion = findViewById(R.id.btn_cerrar_sesion);
        btn_simular_hipoteca = findViewById(R.id.btn_simular_hipoteca_pag_principal);
        recyclerHipotecas = findViewById(R.id.recycler_hipotecas_seguimiento);
        recyclerHipotecas.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));
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
        foto_perfil = findViewById(R.id.foto_perfil_pag_principal);
        btn_config_usuario = findViewById(R.id.btn_config_usuario);
        eventos();
    }

    public void eventos(){
        btn_config_usuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PaginaPrincipal.this, InfoPerfilUsuario.class);
                startActivity(i);
            }
        });

        btn_cerrar_sesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(PaginaPrincipal.this, getString(R.string.sesion_cerrada), Toast.LENGTH_SHORT).show();
                Intent i = new Intent(PaginaPrincipal.this, IniciarSesion.class);
                startActivity(i);
                finish();
            }
        });
    }


}