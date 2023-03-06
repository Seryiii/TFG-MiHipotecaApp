package es.MiHipotecaApp.TFG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;

import es.MiHipotecaApp.TFG.UsuarioRegistrado.PaginaPrincipal;
import es.MiHipotecaApp.TFG.UsuarioRegistrado.PasarPremium;

public class MainActivity extends AppCompatActivity {

    private Button btn_simular_hipoteca;
    private Button btn_iniciar_sesion;
    private Button btn_registrarse;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        comprobarSiSesionIniciada();
        initUI();
    }

    private void comprobarSiSesionIniciada(){
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user != null){
            Intent i = new Intent(MainActivity.this, PaginaPrincipal.class);
            startActivity(i);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        comprobarSiSesionIniciada();
    }

    private void initUI() {
        btn_simular_hipoteca = findViewById(R.id.btn_simular_pagina_inicio);
        btn_iniciar_sesion = findViewById(R.id.btn_iniciar_sesion_inicio);
        btn_registrarse = findViewById(R.id.btn_registrarse_pagina_inicio);

        btn_iniciar_sesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, IniciarSesion.class);
                startActivity(i);
            }
        });

        btn_registrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, Registro.class);
                startActivity(i);
            }
        });


        //FALTA ESTE
        btn_simular_hipoteca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, PasarPremium.class);
                startActivity(i);
            }
        });
    }
}