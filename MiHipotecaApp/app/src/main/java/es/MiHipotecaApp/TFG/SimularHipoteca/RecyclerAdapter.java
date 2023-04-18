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
