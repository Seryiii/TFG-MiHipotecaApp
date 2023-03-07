package es.MiHipotecaApp.TFG.UsuarioRegistrado.HipotecasSeguimiento;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import es.MiHipotecaApp.TFG.R;
import es.MiHipotecaApp.TFG.Transfers.HipotecaSeguimiento;

public class AdaptadorHipotecasSeguimiento extends RecyclerView.Adapter<AdaptadorHipotecasSeguimiento.ViewHolderHipotecasSeguimiento> implements View.OnClickListener{

    private ArrayList<HipotecaSeguimiento> hipotecasSeg;
    private View.OnClickListener listener;

    public AdaptadorHipotecasSeguimiento(ArrayList<HipotecaSeguimiento> hipotecasSeg) {
        this.hipotecasSeg = hipotecasSeg;
    }

    @NonNull
    @Override
    public AdaptadorHipotecasSeguimiento.ViewHolderHipotecasSeguimiento onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hipoteca_seguimiento, null, false);
        view.setOnClickListener(this);
        return new ViewHolderHipotecasSeguimiento(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorHipotecasSeguimiento.ViewHolderHipotecasSeguimiento holder, int position) {
        holder.tituloHipoteca.setText(hipotecasSeg.get(position).getNombre());
        if(position == hipotecasSeg.size() - 1) holder.fotoHipoteca.setImageResource(R.drawable.boton_anadir_hipoteca_seg);
        else holder.fotoHipoteca.setImageResource(R.drawable.hipoteca_icono);

    }

    @Override
    public int getItemCount() {
        return hipotecasSeg.size();
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        if(listener != null) {
            listener.onClick(view);
        }
    }

    public class ViewHolderHipotecasSeguimiento extends RecyclerView.ViewHolder{

        TextView tituloHipoteca;
        CircleImageView fotoHipoteca;

        public ViewHolderHipotecasSeguimiento(@NonNull View itemView) {
            super(itemView);
            tituloHipoteca = itemView.findViewById(R.id.titulo_tarjeta_hipoteca);
            fotoHipoteca   = itemView.findViewById(R.id.foto_tarjeta_hipoteca);
        }
    }
}
