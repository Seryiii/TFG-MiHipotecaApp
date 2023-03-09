package es.MiHipotecaApp.TFG.UsuarioRegistrado.InformacionUsuario;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import es.MiHipotecaApp.TFG.R;

public class ModificarDatosUsuario extends AppCompatActivity implements custom_dialog_avatares.customDialogInterface {
    private EditText et_nombre, et_pass, et_confPass;
    private ImageView iv_cambio_avatar;
    private ImageView imagenPerfil;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_datos_usuario);
        et_nombre = findViewById(R.id.edit_nuevo_nombre_registro);
        et_pass = findViewById(R.id.edit_contra_registro);
        et_confPass = findViewById(R.id.edit_repetir_contra_registro);
        iv_cambio_avatar = findViewById(R.id.btn_mod_image);
        imagenPerfil = findViewById(R.id.imagenPerfil);
        context = this;
        iv_cambio_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                custom_dialog_avatares custom = new custom_dialog_avatares();
                custom.show(getSupportFragmentManager(),"Custom Dialog");
            }
        });
    }


    @Override
    public void setAvatares(int avatar) {
        Log.e("Modificar datos usuario", ""+ avatar);
        switch (avatar){
            case 1:
                imagenPerfil.setImageResource(R.drawable.avatar1);
                break;
            case 2:
                imagenPerfil.setImageResource(R.drawable.avatar2);
                break;
            case 3:
                imagenPerfil.setImageResource(R.drawable.avatar3);
                break;
            case 4:
                imagenPerfil.setImageResource(R.drawable.avatar4);
                break;
            default:
                imagenPerfil.setImageResource(R.drawable.avatar5);
        }

    }
}