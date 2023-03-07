package es.MiHipotecaApp.TFG.UsuarioRegistrado.InformacionUsuario;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;

import es.MiHipotecaApp.TFG.IniciarSesion;
import es.MiHipotecaApp.TFG.R;
import es.MiHipotecaApp.TFG.UsuarioRegistrado.HipotecasSeguimiento.TusHipotecas;

public class InfoPerfilUsuario extends Fragment {

    private Button eliminar_cuenta;
    private Button modificar_datos;
    private Button informar_problema;
    private Button pasar_a_premium;

    private Button cerrar_sesion;


    public InfoPerfilUsuario(){

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_info_perfil_usuario, container, false);
        initUI(view);
        return view;
    }

    private void initUI(View view) {
        eliminar_cuenta = view.findViewById(R.id.eliminar_cuenta);
        modificar_datos = view.findViewById(R.id.btn_modificar_datos);
        informar_problema = view.findViewById(R.id.btn_notificar_problema);
        pasar_a_premium = view.findViewById(R.id.btn_pasar_a_premium);
        cerrar_sesion = view.findViewById(R.id.cerrar_sesion);
        eliminar_cuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialogo = new AlertDialog.Builder(getActivity().getApplicationContext())
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
                Intent i = new Intent(getActivity().getApplicationContext(), ModificarDatosUsuario.class);
                startActivity(i);
            }
        });

        informar_problema.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity().getApplicationContext(), InformarDeUnProblema.class);
                startActivity(i);
            }
        });

        pasar_a_premium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity().getApplicationContext(), PasarPremium.class);
                startActivity(i);
            }
        });
        cerrar_sesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(getActivity().getApplicationContext(), getString(R.string.sesion_cerrada), Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getActivity().getApplicationContext(), IniciarSesion.class);
                startActivity(i);
                getActivity().finish();
            }
        });
    }
}