package es.MiHipotecaApp.TFG.SimularHipoteca;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.List;

import es.MiHipotecaApp.TFG.R;
import es.MiHipotecaApp.TFG.Transfers.Oferta;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerHolder> {
    private List<Oferta> lista;
    private String tipo;
    public RecyclerAdapter(List<Oferta> lista,String tipo) {
        this.lista = lista;
        this.tipo = tipo;
    }

    @NonNull
    @Override
    public RecyclerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.ofertas_view, parent, false);
        return new RecyclerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerHolder holder, int position) {
        if(tipo.equals("fija")){
            Oferta oferta = lista.get(position);
            holder.tvBanco.setText(oferta.getBanco());
            holder.tvDesc.setText(oferta.getDesc());
            holder.tvTin.setText(oferta.getTin());
            holder.tvTae.setText("TAE " + oferta.getTae());
            holder.tvCuota.setText(oferta.getCuota());
        }else{
            Oferta oferta = lista.get(position);
            holder.tvBanco.setText(oferta.getBanco());
            holder.tvDesc.setText(oferta.getDesc());
            holder.tvTin.setText("TIN " + oferta.getTin_x());
            holder.tvTin_resto.setText("TIN Resto "+ oferta.getTin_resto());
            holder.tvTae.setText("TAE " + oferta.getTae());
            holder.tvCuota.setText(oferta.getCuota_x());
            holder.tvCuota_resto.setText(oferta.getCuota_resto());
        }


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


        public RecyclerHolder(@NonNull View itemView){
            super(itemView);
            img = itemView.findViewById(R.id.imgItem);
            tvBanco = itemView.findViewById(R.id.tvBanco);
            tvDesc = itemView.findViewById(R.id.tvDesc);
            tvTin = itemView.findViewById(R.id.tvTin);
            tvTin_resto = itemView.findViewById(R.id.tvTin_resto);
            tvTae = itemView.findViewById(R.id.tvTae);
            tvCuota = itemView.findViewById(R.id.tvCuota);
            tvCuota_resto = itemView.findViewById(R.id.tvCuota_resto);

        }
    }
}
