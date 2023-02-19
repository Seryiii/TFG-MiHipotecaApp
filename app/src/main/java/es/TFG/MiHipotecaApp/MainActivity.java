package es.TFG.MiHipotecaApp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button btn_simular_hipoteca;
    private Button btn_iniciar_sesion;
    private Button btn_registrarse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
    }

    private void initUI(){
        btn_simular_hipoteca = findViewById(R.id.btn_simular_pagina_inicio);
        btn_iniciar_sesion   = findViewById(R.id.btn_iniciar_sesion_inicio);
        btn_registrarse      = findViewById(R.id.btn_registrarse_pagina_inicio);

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

            }
        });
    }
}