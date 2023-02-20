package es.TFG.MiHipotecaApp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import es.TFG.MiHipotecaApp.Transfers.Usuario;

public class IniciarSesion extends AppCompatActivity {

    private Button btn_iniciar_sesion;
    private EditText correo;
    private EditText contra;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciar_sesion);
        initUI();
    }

    private void initUI(){
        correo             = findViewById(R.id.edit_correo_iniciar_sesion);
        contra             = findViewById(R.id.edit_contrasenia_iniciar_sesion);
        btn_iniciar_sesion = findViewById(R.id.btn_iniciar_sesion);
        btn_iniciar_sesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Usuario usu = new Usuario();

                String c = correo.getText().toString();
                String cont = contra.getText().toString();
                usu.setCorreo(c);
                usu.setContra(cont);
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference();
                myRef.child("usuario").setValue(usu);
            }
        });
    }
}