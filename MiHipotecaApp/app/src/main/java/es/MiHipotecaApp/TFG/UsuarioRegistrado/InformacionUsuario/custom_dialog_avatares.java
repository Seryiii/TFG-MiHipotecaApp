package es.MiHipotecaApp.TFG.UsuarioRegistrado.InformacionUsuario;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.DialogFragment;

import es.MiHipotecaApp.TFG.R;

public class custom_dialog_avatares  extends AppCompatDialogFragment {
    private customDialogInterface dialogoInterface;
    private RadioGroup rg_1, rg_2;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.custom_dialog_avateres,null);
        builder.setView(view)
                .setTitle("Seleccion avatar")
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        rg_1 = view.findViewById(R.id.grupo_avatar_registro_1);
                        rg_2 = view.findViewById(R.id.grupo_avatar_registro_2);
                        int avatar = 1;
                        if(rg_1.getCheckedRadioButtonId() != -1){
                            switch (rg_1.getCheckedRadioButtonId()) {
                                case R.id.avatar1:
                                    avatar = 1;
                                    break;
                                case R.id.avatar2:
                                    avatar = 2;
                                    break;
                                case R.id.avatar3:
                                    avatar = 3;
                                    break;
                            }
                        }else if(rg_2.getCheckedRadioButtonId() != -1){
                            switch (rg_2.getCheckedRadioButtonId()) {
                                case R.id.avatar4:
                                    avatar = 4;
                                    break;
                                case R.id.avatar5:
                                    avatar = 5;
                                    break;
                            }
                        }
                        dialogoInterface.setAvatares(avatar);
                    }
                });

        rg_1 = view.findViewById(R.id.grupo_avatar_registro_1);
        rg_2 = view.findViewById(R.id.grupo_avatar_registro_2);
        int avatar;


        rg_1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int checkedRadioButtonId = rg_2.getCheckedRadioButtonId();
                if (checkedRadioButtonId != -1) {
                    rg_2.clearCheck();
                }
            }
        });
        rg_2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                // Deseleccionar todos los RadioButtons que no sean el seleccionado actualmente
                int checkedRadioButtonId = rg_1.getCheckedRadioButtonId();
                if (checkedRadioButtonId != -1) {
                    rg_1.clearCheck();
                }
            }
        });
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        dialogoInterface = (customDialogInterface) context;

    }

    public interface customDialogInterface {
         void setAvatares(int avatar);
    }
}
