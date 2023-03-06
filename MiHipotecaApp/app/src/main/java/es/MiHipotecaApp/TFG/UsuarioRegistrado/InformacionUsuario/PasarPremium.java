package es.MiHipotecaApp.TFG.UsuarioRegistrado.InformacionUsuario;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ViewFlipper;

import es.MiHipotecaApp.TFG.R;

public class PasarPremium extends AppCompatActivity {
    private ViewFlipper vf_1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pasar_premium);
        vf_1 = findViewById(R.id.view_flipper);
        vf_1.setFlipInterval(3000);
        vf_1.startFlipping();
    }
    public void previousView(View v){
        vf_1.showPrevious();
    }
    public void nextView(View v){
        vf_1.showNext();
    }
}