package es.MiHipotecaApp.TFG.UsuarioRegistrado.HipotecasSeguimiento.Graficos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import es.MiHipotecaApp.TFG.R;
import es.MiHipotecaApp.TFG.Transfers.HipotecaSegFija;
import es.MiHipotecaApp.TFG.Transfers.HipotecaSegMixta;
import es.MiHipotecaApp.TFG.Transfers.HipotecaSegVariable;
import es.MiHipotecaApp.TFG.Transfers.HipotecaSeguimiento;

public class grafico_gastos_totales extends AppCompatActivity {

    private final String TAG = "GraficoGasTot";
    private AnyChartView grafico;
    private HipotecaSeguimiento hip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grafico_gastos_totales);
        initUI();
        construirGraficoGastosTotales();
    }

    private void initUI(){
        grafico = findViewById(R.id.grafico_gastos_totales);
        if(getIntent().getStringExtra("tipo_hipoteca").equals("fija")) hip = (HipotecaSegFija) getIntent().getSerializableExtra("hipoteca");
        else if (getIntent().getStringExtra("tipo_hipoteca").equals("variable")) hip = (HipotecaSegVariable) getIntent().getSerializableExtra("hipoteca");
        else hip = (HipotecaSegMixta) getIntent().getSerializableExtra("hipoteca");
    }


    public void construirGraficoGastosTotales(){

        Pie pie = AnyChart.pie();
        List<DataEntry> data = new ArrayList<>();
        data.add(new ValueDataEntry("VINCULACIONES", hip.getTotalVinculacionesAnual()));
        data.add(new ValueDataEntry("OTROS GASTOS (GESTORÍA, COMISIONES, ...)", hip.getTotalGastos()));
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("impuestos").document(hip.getComunidad_autonoma()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        double iva_itp;
                        double ajd;
                        String impuestos = "IVA";
                        if(hip.getAntiguedad_vivienda().equals("nueva")){
                            //Calcular IVA
                            if(hip.getTipo_vivienda().equals("proteccion_oficial")) iva_itp = document.getDouble("IVA");
                            else iva_itp = document.getDouble("IVA_PO");
                            ajd = document.getDouble("AJD_OBRA_NUEVA");

                        }else{
                            //Calcular ITP
                            iva_itp = document.getDouble("ITP");
                            ajd = document.getDouble("AJD_HIPOTECA");
                            impuestos = "ITP";
                        }

                        iva_itp = hip.getPrecio_vivienda() * (iva_itp / 100);
                        ajd     = hip.getPrecio_vivienda() * (ajd / 100);

                        data.add(new ValueDataEntry(impuestos, iva_itp));
                        data.add(new ValueDataEntry("AJD", ajd));
                        pie.data(data);
                        pie.labels().fontSize(22);
                        pie.labels().position("outside");
                        pie.connectorLength(30);
                        grafico.setChart(pie);
                        grafico.invalidate();

                    } else {
                        Log.e(TAG,"El documento no existe");
                    }
                }else Log.e(TAG,"Error al obtener datos del documento");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG,"Error al traer impuestos de bd");
            }
        });

    }
}