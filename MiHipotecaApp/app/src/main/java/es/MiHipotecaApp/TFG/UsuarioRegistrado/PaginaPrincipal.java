package es.MiHipotecaApp.TFG.UsuarioRegistrado;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import de.hdodenhof.circleimageview.CircleImageView;
import es.MiHipotecaApp.TFG.IniciarSesion;
import es.MiHipotecaApp.TFG.R;

public class PaginaPrincipal extends AppCompatActivity {
    private Button btn_simular_hipoteca;
    private Button btn_config_usuario;
    private Button btn_cerrar_sesion;
    private CircleImageView foto_perfil;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagina_principal);
        btn_cerrar_sesion = findViewById(R.id.btn_cerrar_sesion);
        btn_simular_hipoteca = findViewById(R.id.btn_simular_hipoteca_pag_principal);
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