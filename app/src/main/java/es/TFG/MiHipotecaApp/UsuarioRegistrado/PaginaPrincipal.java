package es.TFG.MiHipotecaApp.UsuarioRegistrado;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import de.hdodenhof.circleimageview.CircleImageView;
import es.TFG.MiHipotecaApp.R;

public class PaginaPrincipal extends AppCompatActivity {
    private Button btn_simular_hipoteca;
    private Button btn_config_usuario;
    private CircleImageView foto_perfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagina_principal);
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
    }


}