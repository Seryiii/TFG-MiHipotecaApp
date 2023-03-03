package es.MiHipotecaApp.TFG;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class Filtros extends AppCompatActivity {
    private Spinner sp_bancos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtros);
        sp_bancos = findViewById(R.id.sp_bancos);
        String [] bancos = {"ING","SANTANDER","BBVA","CAIXABANK","BANKINTER","EVO BANCO","SABADELL","UNICAJA","DEUTSCHE BANK","OPEN BANK","KUTXA BANK","IBERCAJA","ABANCA"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,bancos);
        sp_bancos.setAdapter(adapter);
    }
}