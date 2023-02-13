package es.TFG.MiHipotecaApp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ActividadEjemplo extends AppCompatActivity {

    private EditText et_correo, et_pass;
    private TextView tv_texto;
    private Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_ejemplo);
        et_correo = findViewById(R.id.txt_correo);
        et_pass = findViewById(R.id.txt_correo);
        btn = findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    public void registro(View view){
        String correo = et_correo.getText().toString();
        String pass = et_pass.getText().toString();

        Toast.makeText(this,"Usuario Registrado", Toast.LENGTH_LONG).show();
    }
}