package es.MiHipotecaApp.TFG.UsuarioRegistrado.InformacionUsuario;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;

import es.MiHipotecaApp.TFG.R;

public class InformarDeUnProblema extends AppCompatActivity {

    private ImageButton agregar_imagen;
    private Button btn_enviar_problema;

    private EditText edit_texto_error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informar_de_un_problema);

        edit_texto_error    = findViewById(R.id.edit_texto_error);
        btn_enviar_problema = findViewById(R.id.btn_enviar_problema);

        btn_enviar_problema.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(edit_texto_error.getText())) edit_texto_error.setError("Debes rellenar este campo");
                else {
                    Intent emailIntent = new Intent(Intent.ACTION_SEND);
                    emailIntent.setType("plain/text");
                    emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"ejemplo@gmail.com"});
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Mejora propuesta por " + FirebaseAuth.getInstance().getCurrentUser().getEmail());
                    emailIntent.putExtra(Intent.EXTRA_TEXT, edit_texto_error.getText().toString());
                    startActivity(Intent.createChooser(emailIntent, "Enviar correo electrónico"));
                }
            }
        });
    }
}