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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.MiHipotecaApp.TFG.R;
import es.MiHipotecaApp.TFG.Transfers.Oferta;
import es.MiHipotecaApp.TFG.UsuarioRegistrado.HipotecasSeguimiento.NuevaVinculacionAnualFragment;
import es.MiHipotecaApp.TFG.UsuarioRegistrado.custom_dialog_oferta;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerHolder> {
    private List<Oferta> lista;
    private String tipo;
    private Boolean detalles;
    public Boolean estado = false;

    private FirebaseFirestore db;

    private FirebaseAuth auth;
    private FirebaseUser user;
    private String tipoBtn;
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
    public RecyclerAdapter(List<Oferta> lista,String tipo) {
        this.lista = lista;
        this.tipo = tipo;
        tipoBtn = "eliminar";
        db   = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
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
        if(tipoBtn.equals("guardar"))holder.btn_eliminar.setVisibility(View.GONE);
        else holder.btn_guardar.setVisibility(View.GONE);
        Oferta oferta = lista.get(position);
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
        holder.tvBanco.setText(oferta.getBanco());
        holder.tvDesc.setText(oferta.getDesc());
        ponerLogoBanco(oferta.getBanco(), holder);
        if(tipo.equals("fija")){
            holder.tvTin.setText(oferta.getTin());
            holder.tvTae.setText("TAE " + oferta.getTae());
            holder.tvCuota.setText(oferta.getCuota());
        }else{
            holder.tvTin.setText("TIN " + oferta.getTin_x());
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
                //HACER ESTA NOCHE
                custom_dialog_oferta fragment = new custom_dialog_oferta(oferta);
                fragment.show(fragmentManager, "Nombre oferta fragment");

                String uid = user.getUid();
                Map<String, Object> o = new HashMap<>();
                o.put("idUser", uid);
                o.put("banco", oferta.getBanco());
                o.put("desc", oferta.getDesc());
                o.put("tae", oferta.getTae());
                o.put("nombreOferta" , nombreOferta);
                if(tipo.equals("fija")){
                    o.put("tin", oferta.getTin());
                    o.put("cuota", oferta.getCuota());
                    o.put("tipo","fija");
                }
                else{
                    o.put("tipo","varMixta");
                    o.put("tin_x_anios", oferta.getTin_x());
                    o.put("tin_resto", oferta.getTin_resto());
                    o.put("couta_x", oferta.getCuota_x());
                    o.put("cuota_resto", oferta.getCuota_resto());
                }
                if(detalles){
                    o.put("vinculaciones", oferta.getVinculaciones());
                }
                else o.put("vinculaciones" , "");
                db.collection("ofertas_guardadas").add(o).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        //Toast.makeText(MostrarOfertas., "Oferta guardada correctamente!", Toast.LENGTH_LONG).show();
                        Log.w("GUARDAR","Exito al guardar");
                        holder.btn_guardar.setVisibility(View.GONE);
                    }

                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("GUARDAR","Error al guardar oferta en Firestore: ");
                    }
                });

                // Guardar nombreOferta en la colección de ofertas_guardadas
                    }
                });
        holder.btn_eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return lista.size();
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

        private Button btn_guardar;
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
