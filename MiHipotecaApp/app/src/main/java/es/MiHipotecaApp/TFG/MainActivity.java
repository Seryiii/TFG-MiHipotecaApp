package es.MiHipotecaApp.TFG;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;

import es.MiHipotecaApp.TFG.SimularHipoteca.CompararNuevaHipoteca;
import es.MiHipotecaApp.TFG.EuriborHistorico;
import es.MiHipotecaApp.TFG.UsuarioRegistrado.HipotecasSeguimiento.TusHipotecas;
import es.MiHipotecaApp.TFG.UsuarioRegistrado.InformacionUsuario.PasarPremium;

public class MainActivity extends AppCompatActivity {

    private Button btn_simular_hipoteca;
    private Button btn_iniciar_sesion;
    private Button btn_registrarse;

    private FirebaseAuth firebaseAuth;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        comprobarSiSesionIniciada();
        db = FirebaseFirestore.getInstance();
        initUI();
        comprobarEuribor();
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
    private void comprobarEuribor(){
        db.collection("euribor").get().addOnCompleteListener(task-> {
            if(task.isSuccessful()){
                QuerySnapshot querySnapshot=task.getResult();
                if(querySnapshot==null || querySnapshot.isEmpty()){
                    EuriborHistorico hist=new EuriborHistorico();
                    hist.ActualizarEuribor();
                }
            }
        });
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


        btn_simular_hipoteca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, CompararNuevaHipoteca.class);
                startActivity(i);
            }
        });
    }
}