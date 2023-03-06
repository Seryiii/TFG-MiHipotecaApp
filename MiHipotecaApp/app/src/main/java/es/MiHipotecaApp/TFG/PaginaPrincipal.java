package es.MiHipotecaApp.TFG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import es.MiHipotecaApp.TFG.UsuarioRegistrado.HipotecasSeguimiento.NuevoSeguimiento;
import es.MiHipotecaApp.TFG.UsuarioRegistrado.HipotecasSeguimiento.TusHipotecas;
import es.MiHipotecaApp.TFG.UsuarioRegistrado.InformacionUsuario.InfoPerfilUsuario;

public class PaginaPrincipal extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private BottomNavigationView bottomNavigationView;
    private TusHipotecas tusHipotecasFragment           = new TusHipotecas();
    private InfoPerfilUsuario infoPerfilUsuarioFragment = new InfoPerfilUsuario();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagina_principal);
        initUI();
    }

    private void initUI(){
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.mis_hipotecas);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.mis_hipotecas:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, tusHipotecasFragment).commit();
                return true;

            case R.id.aniadir_hipoteca:
                Intent i = new Intent(PaginaPrincipal.this, NuevoSeguimiento.class);
                startActivity(i);
                return true;

            case R.id.simular_hipoteca:
                // Crear fragment formulario simular nueva hipoteca a formulario para simular hipoteca
                return true;

            case R.id.mi_perfil:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, infoPerfilUsuarioFragment).commit();
                return true;

        }
        return false;
    }


}