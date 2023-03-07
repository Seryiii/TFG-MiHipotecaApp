package es.MiHipotecaApp.TFG.UsuarioRegistrado.InformacionUsuario;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import es.MiHipotecaApp.TFG.R;

public class InformarDeUnProblema extends AppCompatActivity {

    private ImageButton agregar_imagen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        agregar_imagen = findViewById(R.id.agregar_imagen);
        setContentView(R.layout.activity_informar_de_un_problema);
    }
}