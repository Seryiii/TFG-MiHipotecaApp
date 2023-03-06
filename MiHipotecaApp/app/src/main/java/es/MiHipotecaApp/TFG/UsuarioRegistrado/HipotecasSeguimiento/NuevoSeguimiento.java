package es.MiHipotecaApp.TFG.UsuarioRegistrado.HipotecasSeguimiento;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import es.MiHipotecaApp.TFG.R;

public class NuevoSeguimiento extends AppCompatActivity {
    private Spinner sp_comunidad;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_seguimiento);
        sp_comunidad = findViewById(R.id.sp_comunidad);
        String [] comunidades = { "Andalucía" , "Aragón","Asturias", "Baleares", "Canarias", "Cantabria", "Castilla La Mancha", "Castilla León", "Cataluña", "Ceuta", "Comunidad de Madrid", "Comunidad Valenciana", "Extremadura", "Galicia", "La Rioja", "Melilla", "Murcia", "Navarra", "País Vasco"};
        ArrayAdapter<String>  adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item,comunidades);
        sp_comunidad.setAdapter(adapter);

    }
}