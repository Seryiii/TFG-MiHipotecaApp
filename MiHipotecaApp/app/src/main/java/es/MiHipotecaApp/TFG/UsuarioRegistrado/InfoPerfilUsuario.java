package es.MiHipotecaApp.TFG.UsuarioRegistrado;

import static android.app.PendingIntent.getActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import es.MiHipotecaApp.TFG.IniciarSesion;
import es.MiHipotecaApp.TFG.R;
import es.MiHipotecaApp.TFG.RecuperarContra;

public class InfoPerfilUsuario extends AppCompatActivity {

    private Button eliminar_cuenta;
    private Button modificar_datos;
    private Button informar_problema;
    private Button pasar_a_premium;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_perfil_usuario);
        initUI();
    }

    private void initUI() {
        eliminar_cuenta = findViewById(R.id.eliminar_cuenta);
        modificar_datos = findViewById(R.id.btn_modificar_datos);
        informar_problema = findViewById(R.id.btn_notificar_problema);
        pasar_a_premium = findViewById(R.id.btn_pasar_a_premium);

        eliminar_cuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialogo = new AlertDialog.Builder(InfoPerfilUsuario.this)
                        .setPositiveButton(getString(R.string.si_eliminar_cuenta), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setNegativeButton(getString(R.string.no_eliminar_cuenta), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setTitle("ELIMINAR CUENTA").setMessage("¿Desea eliminar la cuenta?").create();
                dialogo.show();
            }
        });

        modificar_datos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(InfoPerfilUsuario.this, ModificarDatosUsuario.class);
                startActivity(i);
            }
        });

        informar_problema.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(InfoPerfilUsuario.this, InformarDeUnProblema.class);
                startActivity(i);
            }
        });

        pasar_a_premium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(InfoPerfilUsuario.this, PasarPremium.class);
                startActivity(i);
            }
        });
    }
}