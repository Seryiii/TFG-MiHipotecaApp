package es.MiHipotecaApp.TFG.SimularHipoteca;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import es.MiHipotecaApp.TFG.R;
import es.MiHipotecaApp.TFG.Transfers.Oferta;
import es.MiHipotecaApp.TFG.UsuarioRegistrado.HipotecasSeguimiento.NuevaVinculacionAnualFragment;
import es.MiHipotecaApp.TFG.UsuarioRegistrado.HipotecasSeguimiento.TusOfertas;
import es.MiHipotecaApp.TFG.UsuarioRegistrado.custom_dialog_oferta;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerHolder>{
    private List<Oferta> lista;
    private String tipo;
    private Boolean detalles;
    public Boolean estado = false;

    private FirebaseFirestore db;

    private FirebaseAuth auth;
    private FirebaseUser user;
    private String tipoBtn;
    private actualizarInter actualizarInter;
    private FragmentManager fragmentManager;
    private String nombreOferta;
    public RecyclerAdapter(List<Oferta> lista, String tipo, Boolean detalles, FragmentManager fragmentManager) {
        this.lista = lista;
        this.tipo = tipo;
        this.detalles = detalles;
        db   = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        tipoBtn = "guardar";
        this.fragmentManager = fragmentManager;

    }
    public RecyclerAdapter(List<Oferta> lista,String tipo,actualizarInter actualizarInter2) {
        this.lista = lista;
        this.tipo = tipo;
        tipoBtn = "eliminar";
        db   = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        this.actualizarInter = actualizarInter2;
    }


    @NonNull
    @Override
    public RecyclerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.ofertas_view, parent, false);
        return new RecyclerHolder(view);
    }
    private void ponerLogoBanco(String nombre_banco,@NonNull RecyclerHolder holder){
        switch (nombre_banco){
            case "ING":
                holder.img.setImageResource(R.drawable.logo_ing);
                break;
            case "SANTANDER":
                holder.img.setImageResource(R.drawable.logo_santander);
                break;
            case "BBVA":
                holder.img.setImageResource(R.drawable.logo_bbva);
                break;
            case "BANKIA":
                holder.img.setImageResource(R.drawable.logo_caixabank);
                break;
            case "CAIXABANK":
                holder.img.setImageResource(R.drawable.logo_caixabank);
                break;
            case "BANKINTER":
                holder.img.setImageResource(R.drawable.logo_bankinter);
                break;
            case "EVOBANCO":
                holder.img.setImageResource(R.drawable.logo_evo_banco);
                break;
            case "SABADELL":
                holder.img.setImageResource(R.drawable.logo_sabadell);
                break;
            case "UNICAJA":
                holder.img.setImageResource(R.drawable.logo_unicaja);
                break;
            case "DEUTSCHE BANK":
                holder.img.setImageResource(R.drawable.logo_deutsche_bank);
                break;
            case "OPENBANK":
                holder.img.setImageResource(R.drawable.logo_open_bank);
                break;
            case "KUTXA":
                holder.img.setImageResource(R.drawable.logo_kutxa_bank);
                break;
            case "IBERCAJA":
                holder.img.setImageResource(R.drawable.logo_ibercaja);
                break;
            case "ABANCA":
                holder.img.setImageResource(R.drawable.logo_abanca);
                break;
            case "GLOBALCAJA":
                holder.img.setImageResource(R.drawable.logo_global_caja);
                break;
            case "TARGOBANK":
                holder.img.setImageResource(R.drawable.logo_targo_bank);
                break;
            case "MYINVESTOR":
                holder.img.setImageResource(R.drawable.logo_myinvestor);
                break;
            case "BANCAMARCH":
                holder.img.setImageResource(R.drawable.logo_bancamarch);
                break;
            case "IMAGIN":
                holder.img.setImageResource(R.drawable.logo_imagin);
                break;
            default:
                holder.img.setImageResource(R.drawable.logo_bancodesconocido);
                break;
        }
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerHolder holder, int position) {
        Oferta oferta = lista.get(position);

        if(tipoBtn.equals("guardar")){
            holder.btn_eliminar.setVisibility(View.GONE);
            if(oferta.isGuardada())holder.btn_guardar.setVisibility(View.GONE);
            holder.tvBanco.setText(oferta.getBanco());
        }
        else {
            holder.btn_guardar.setVisibility(View.GONE);
            holder.tvBanco.setText(oferta.getNombre());
        }
        eventoBtn(holder,oferta);
        if(detalles == null){
            if(oferta.getVinculaciones().equals("")){
                holder.btn_details.setVisibility(View.GONE);
                holder.txt_detalles.setVisibility(View.GONE);
            }
        }else{
            if(!detalles){
                holder.btn_details.setVisibility(View.GONE);
                holder.txt_detalles.setVisibility(View.GONE);
            }

        }
        holder.tvDesc.setText(oferta.getDesc());
        ponerLogoBanco(oferta.getBanco(), holder);
        if(tipo.equals("fija")){
            holder.tvTin.setText(oferta.getTin());
            holder.tvTae.setText("TAE " + oferta.getTae());
            holder.tvCuota.setText(oferta.getCuota());
        }else{
            holder.tvTin.setText(oferta.getTin_x());
            holder.tvTin_resto.setText("TIN Resto "+ oferta.getTin_resto());
            holder.tvTae.setText("TAE " + oferta.getTae());
            holder.tvCuota.setText(oferta.getCuota_x());
            holder.tvCuota_resto.setText(oferta.getCuota_resto());
        }


    }
    public void eventoBtn(@NonNull RecyclerHolder holder, Oferta oferta){
        holder.btn_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!estado){
                    if(tipo.equals("fija")){
                        holder.tvTin.setVisibility(View.GONE);
                        holder.tvTae.setText("Vinculaciones");
                        holder.txtCuota.setVisibility(View.GONE);
                        holder.tvCuota.setText(oferta.getVinculaciones());
                        holder.tvCuota.setTextSize(10);
                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) holder.tvCuota.getLayoutParams();
                        params.setMargins(0, 14, 0, 0);
                        holder.tvCuota.setLayoutParams(params);
                    }else{
                        holder.tvTin.setVisibility(View.GONE);
                        holder.tvTin_resto.setVisibility(View.GONE);
                        holder.tvTae.setText("Vinculaciones");
                        holder.tvCuota.setText(oferta.getVinculaciones());
                        holder.tvCuota.setTextSize(10);
                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) holder.tvCuota.getLayoutParams();
                        params.setMargins(0, 14, 0, 0);
                        holder.tvCuota.setLayoutParams(params);
                        holder.txtCuota.setVisibility(View.GONE);
                        holder.tvCuota_resto.setVisibility(View.GONE);
                    }
                }else {
                    if(tipo.equals("fija")){
                        holder.tvTin.setVisibility(View.VISIBLE);
                        holder.txtCuota.setVisibility(View.VISIBLE);
                        holder.tvTae.setText("TAE " + oferta.getTae());
                        holder.tvCuota.setText(oferta.getCuota());
                        holder.tvCuota.setTextSize(12);
                    }else{
                        holder.tvTin.setVisibility(View.VISIBLE);
                        holder.tvTin_resto.setVisibility(View.VISIBLE);
                        holder.tvTae.setText("TAE " + oferta.getTae());
                        holder.tvCuota.setText(oferta.getCuota_x());
                        holder.tvCuota.setTextSize(12);
                        holder.txtCuota.setVisibility(View.VISIBLE);
                        holder.tvCuota_resto.setVisibility(View.VISIBLE);
                    }
                }

                if(estado) estado = false;
                else estado = true;
            }
        });
        holder.btn_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser currentUser = auth.getCurrentUser();
                if(currentUser != null) {
                    custom_dialog_oferta fragment = new custom_dialog_oferta(oferta, holder, tipo);
                    fragment.show(fragmentManager, "Nombre oferta fragment");
                    // Guardar nombreOferta en la colección de ofertas_guardadas
                }else Toast.makeText(view.getContext(), "Ha ocurrido un problema al conectar con el servidor", Toast.LENGTH_LONG).show();
            }
        });

        holder.btn_eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CollectionReference ofertasRef = db.collection("ofertas_guardadas");
                Query query = ofertasRef.whereEqualTo("nombreOferta" , oferta.getNombre());

                query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot querySnapshot) {
                        for (QueryDocumentSnapshot documentSnapshot : querySnapshot) {
                            String documentId = documentSnapshot.getId();
                            db.collection("ofertas_guardadas").document(documentId).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d("Eliminar", "Oferta eliminada correctamente");
                                            holder.btn_eliminar.setVisibility(View.GONE);
                                            actualizarInter.actualizar();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w("Eliminar", "Error deleting document", e);
                                        }
                                    });;
                        }
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }
    public interface actualizarInter{
        void actualizar();
    }


    public static class RecyclerHolder extends RecyclerView.ViewHolder{
        private ImageView img;
        private TextView tvBanco;
        private TextView tvDesc;
        private TextView tvTin;
        private TextView tvTin_resto;
        private TextView tvTae;
        private TextView tvCuota;
        private TextView tvCuota_resto;
        private TextView txtCuota;

        private Button btn_details;
        private TextView txt_detalles;

        public Button btn_guardar;
        private Button btn_eliminar;
        private Context contexto;


        public RecyclerHolder(@NonNull View itemView){
            super(itemView);
            contexto = itemView.getContext();
            img = itemView.findViewById(R.id.imgItem);
            tvBanco = itemView.findViewById(R.id.tvBanco);
            tvDesc = itemView.findViewById(R.id.tvDesc);
            tvTin = itemView.findViewById(R.id.tvTin);
            tvTin_resto = itemView.findViewById(R.id.tvTin_resto);
            tvTae = itemView.findViewById(R.id.tvTae);
            tvCuota = itemView.findViewById(R.id.tvCuota);
            txtCuota = itemView.findViewById(R.id.txt_cuota);
            tvCuota_resto = itemView.findViewById(R.id.tvCuota_resto);
            btn_details = itemView.findViewById(R.id.btnArrow);
            txt_detalles = itemView.findViewById(R.id.txt_detalles);
            btn_guardar = itemView.findViewById(R.id.btn_guardar);
            btn_eliminar = itemView.findViewById(R.id.btn_eliminar);
        }

    }
}
